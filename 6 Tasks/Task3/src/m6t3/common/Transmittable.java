package m6t3.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *	This interface must be implemented by objects which are transmittable over 
 * OutputStream. It makes sense using this interface because Serializable
 * requires ObjectOutputStream and ObjectInputStream which are hardly compatible
 * with languages other than Java.
 * 
 * @author aabyodj
 */
public interface Transmittable {
	
	/**
	 * Transmit the object preceded by its signature into an OutputStream
	 * @param out Stream into which the object is transmitted
	 * @throws IOException
	 */
	public void transmit(OutputStream out) throws IOException;
	
	/**
	 * 	Receive a new object from an InputStream. The signature of the object 
	 * being received must be pulled out of the stream before using this method.
	 * @param in Stream from which an object is received.
	 * @return An object received
	 */
	public static Object receive(InputStream in) {
		return null;
	}
}
