package m6t3.server;

import java.io.File;
import java.util.ArrayList;

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

class SrvSaver extends Thread {
	static final long SAVE_INTERVAL = 5000;

	final ServerThread server;
	private int checksum = 0;
	private final DocumentBuilder dBuilder;

	SrvSaver(ServerThread server) throws ParserConfigurationException {
			this.server = server;
			dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}

	@Override
	public void run() {
		while (server.running) {
			try {
				Thread.sleep(SAVE_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if ((server.syncSender.getChecksum() != checksum) || true) {
				checksum = 0;
				Iterable<Student> students = new ArrayList<>(server.students);
				Document xmlDoc = dBuilder.newDocument();
				Element root = xmlDoc.createElement("archive");
				xmlDoc.appendChild(root);
				Element xmlStudents = xmlDoc.createElement("students");
				root.appendChild(xmlStudents);
				for (var student: students) {
					xmlStudents.appendChild(createStudent(student, xmlDoc));					
					checksum += student.getSerial();
				}
				try {
					Transformer transformer = TransformerFactory.newInstance().newTransformer();
					DOMSource source = new DOMSource(xmlDoc);
					StreamResult result = new StreamResult(new File("data/archive.xml"));
					transformer.transform(source, result);
				} catch (TransformerFactoryConfigurationError | TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	static Element createStudent(Student student, Document xmlDoc) {
		Element result = xmlDoc.createElement("student");
		result.setAttribute("id", Integer.toString(student.id));
		result.setAttribute("serial", Integer.toString(student.getSerial()));
		result.setAttribute("number", student.getNumber());
		result.setAttribute("surname", student.getSurname());
		result.setAttribute("name", student.getName());
		result.setAttribute("patronymic", student.getPatronymic());
		return result;
	}
}
