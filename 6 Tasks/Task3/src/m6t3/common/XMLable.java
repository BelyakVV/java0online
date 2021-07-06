package m6t3.common;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *	This interface must be implemented by objects which can be written into an
 * XML file.
 * 
 * @author aabyodj
 */
public interface XMLable {
	
	/**
	 * Encode the object into an element of an XML document
	 * @param xmlDoc
	 * @return Element created
	 */
	public Element toXML(Document xmlDoc);
	
	/**
	 * Decode an object from an XML element
	 * @param elem Element to be decoded
	 * @return Object decoded
	 */
	public static Object fromXML(Element elem) {
		return null;
	}
}
