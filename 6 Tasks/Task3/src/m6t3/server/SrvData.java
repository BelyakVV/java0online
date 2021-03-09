package m6t3.server;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import m6t3.common.Student;

class SrvData extends Thread {
	static final long SAVE_INTERVAL = 5000;	

	final List<Student> students = new LinkedList<>();
	private int nextId = 0;
	private volatile boolean changed;

	private final SrvListener server;
	private int checksum = 0;
	private final DocumentBuilder dBuilder;

	SrvData(SrvListener server) throws ParserConfigurationException {
			this.server = server;
			dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(SAVE_INTERVAL);
				save();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		save();
	}
	
	public int getChecksum() {
		return checksum;
	}

	public void updateStudent(Student upd) {
		synchronized (students) {
			if (upd.id < 0) {
				if (upd.getSerial() > 0) {
					Student student = new Student(nextId++, upd);
					if (students.add(student)) {
						changed = true;
						checksum += student.hashCode();
						server.broadcast(student);
					}
				} else {
					System.err.println("Ошибка: студент id < 0 и serial < 1");
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

	Element toXML(Student student, Document xmlDoc) {
		Element result = xmlDoc.createElement("student");
		result.setAttribute("id", Integer.toString(student.id));
		result.setAttribute("serial", Integer.toString(student.getSerial()));
		result.setAttribute("number", student.getNumber());
		result.setAttribute("surname", student.getSurname());
		result.setAttribute("name", student.getName());
		result.setAttribute("patronymic", student.getPatronymic());
		return result;
	}
	
	Student toStudent(Element elem) {
		int id = Integer.parseInt(elem.getAttribute("id"));
		int serial = Integer.parseInt(elem.getAttribute("serial"));
		String number = elem.getAttribute("number");
		String surname = elem.getAttribute("surname");
		String name = elem.getAttribute("name");
		String patronymic = elem.getAttribute("patronymic");
		return new Student(id, serial, number, surname, name, patronymic);
	}
	
	void save() {
		if (!changed) return;
		Iterable<Student> outStudents;
		synchronized (students) {
			outStudents = new ArrayList<>(students);
			changed = false;
		}
		Document xmlDoc = dBuilder.newDocument();
		Element root = xmlDoc.createElement("archive");
		xmlDoc.appendChild(root);
		Element xmlStudents = xmlDoc.createElement("students");
		root.appendChild(xmlStudents);
		for (var student: outStudents) {
			xmlStudents.appendChild(toXML(student, xmlDoc));
		}
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(xmlDoc);
			StreamResult result = new StreamResult(new File("data/archive.xml"));
			transformer.transform(source, result);
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			changed = true;
		}
	}
}
