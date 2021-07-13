package m6t3.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import m6t3.common.Student;
import m6t3.common.User;

class SrvData extends Thread {
	static final long SAVE_INTERVAL = 5000;	

	final List<Student> students = new LinkedList<>();
	private int nextStudentId;
	
	final List<User> users = new LinkedList<>();
	private int nextUserId = 0;
	
	private volatile boolean changed;

	private final SrvListener server;
	private int checksum = 0;
	private final DocumentBuilder dBuilder;
	private final String fileName;

	SrvData(SrvListener server, String fileName) throws ParserConfigurationException {
			this.server = server;
			this.fileName = fileName;
			dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			try {
				load();
			} catch (SAXException | IOException e) {
				// Error reading data from XML file. 
				// Nothing to do as it will be recreated later.
			}
		}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(SAVE_INTERVAL);
				save();
			}
		} catch (InterruptedException e) {
			// It's OK. Nothing to do here
		}
		save();
	}
	
	public int getChecksum() {
		return checksum;
	}

	public void touch() {
		changed = true;
	}

	public void updateStudent(Student upd) {
		synchronized (students) {
			if (upd.id < 0) {
				if (upd.getSerial() > 0) {
					Student student = new Student(nextStudentId++, upd);
					if (students.add(student)) {
						changed = true;
						checksum += student.hashCode();
						server.broadcast(student);
					}
				} else {
					System.err.println("Error: student id < 0 and serial < 1");
				}
				return;
			}
			for (var student: students) {
				if (student.id == upd.id) {
					if (upd.getSerial() < 0) {
						students.remove(student);
						changed = true;
						checksum -= student.hashCode();
						server.broadcast(upd);
					} else {
						int oldHash = student.hashCode();
						upd.incSerial();
						if (student.update(upd)) {
							changed = true;
							checksum += student.hashCode() - oldHash;
							server.broadcast(student);
						}
					}
					return;
				}
			}
		}
	}

	public boolean updateUser(User upd) {
		synchronized (users) {
			if (upd.id < 0) {
				if (upd.getSerial() > 0) {
					User user = new User(nextUserId++, upd);
					if (users.add(user)) {
						changed = true;
						server.broadcast(user);
					}
				} else {
					System.err.println("Error: user id < 0 && serial < 1");
					return false;
				}
				return true;
			}
			for (var user: users) {
				if (user.id == upd.id) {
					if (upd.getSerial() < 0) {
						users.remove(user);
						changed = true;
						server.disconnect(upd);
						server.broadcast(upd);
					} else {
						//TODO: check for duplicate login name
//						int oldHash = student.hashCode();
//						upd.incSerial();
						if (user.update(upd)) {
							changed = true;
//							checksum += student.hashCode() - oldHash;
							server.broadcast(user);
						}
					}
					return true;
				}
			}
			return true;
		}
	}
	
	void load() throws SAXException, IOException {
		Document xmlDoc = dBuilder.parse(new File(fileName));
		xmlDoc.getDocumentElement().normalize();	//TODO: проверить, надо ли это
		
		students.clear();
		nextStudentId = 0;
		checksum = 0;
		NodeList xmlStudents = xmlDoc.getElementsByTagName("student");
		for (int i = 0; i < xmlStudents.getLength(); i++) {
			Node node = xmlStudents.item(i);
			Student student = Student.fromXML((Element) node);
			students.add(student);
			if (student.id >= nextStudentId) {
				nextStudentId = student.id + 1;
			}
			checksum += student.hashCode();
		}
		
		users.clear();
		nextUserId = 0;
		NodeList xmlUsers = xmlDoc.getElementsByTagName("user");
		for (int i = 0; i < xmlUsers.getLength(); i++) {
			Node node = xmlUsers.item(i);
			User user = User.fromXML((Element) node);
			users.add(user);
			if (user.id >= nextUserId) {
				nextUserId = user.id + 1;
			}
		}
		changed = false;
	}
	
	void save() {
		if (!changed) return;
		Iterable<Student> outStudents;
		Iterable<User> outUsers;
		synchronized (students) {
			synchronized (users) {
				outStudents = new ArrayList<>(students);
				outUsers = new ArrayList<>(users);
				changed = false;
			}
		}		
		Document xmlDoc = dBuilder.newDocument();
		Element root = xmlDoc.createElement("archive");
		xmlDoc.appendChild(root);
		
		Element xmlStudents = xmlDoc.createElement("students");
		root.appendChild(xmlStudents);
		for (var student: outStudents) {
			xmlStudents.appendChild(student.toXML(xmlDoc));
		}
		
		Element xmlUsers = xmlDoc.createElement("users");
		root.appendChild(xmlUsers);
		for (var user: outUsers) {
			xmlUsers.appendChild(user.toXML(xmlDoc));
		}

		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		DOMSource source = new DOMSource(xmlDoc);
		File file = new File(fileName);
		File parent = file.getParentFile();
		if (!parent.exists()) parent.mkdirs();
		StreamResult result = new StreamResult(file);
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			changed = true;
			System.err.println("Не удалось записать файл \"" 
					+ file.getAbsolutePath() + "\"");
		}
	}

	public void printStudents() {
		for (var student: students) {
			System.out.println(student);
		}
	}

	public void printUsers() {
		for (var user: users) {
			System.out.println(user);
		}
	}

	public boolean noOtherAdmins(int excludedId) {
		for (var user: users) {
			if (user.id == excludedId) continue;
			if (user.isAdmin()) return false;
		}
		return true;
	}

	public User getUser(String login) {
		for (var user: users) {
			if (user.getLogin().equalsIgnoreCase(login)) {
				return user;
			}
		}
		return null;
	}
}
