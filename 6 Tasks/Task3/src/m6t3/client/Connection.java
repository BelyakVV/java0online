package m6t3.client;

import static m6t3.common.Const.INVALID_ID;
import static m6t3.common.Tranceiver.AUTH_ACKNOWLEDGEMENT;
import static m6t3.common.Tranceiver.AUTH_CHALLENGE;
import static m6t3.common.Tranceiver.receiveInt;
import static m6t3.server.ServerMain.DEFAULT_IP_PORT;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import org.eclipse.swt.widgets.Table;

import m6t3.common.AuthAcknowledgement;
import m6t3.common.AuthChallenge;
import m6t3.common.ChangePassRequest;
import m6t3.common.LoginRequest;


/**
 *  The client-side network connection controller. Upon creation, the controller 
 * automatically connects to a server and launches the Login Dialog.
 * 	If it is impossible to connect to the server using the default host name and
 * port number, it opens the Connection Settings Dialog.
 *  
 * @author aabyodj
 */
class Connection {

	static final String DEFAULT_SERVER_HOST = "localhost";
	static final int DEFAULT_TIMEOUT = 3000;
	static final int RETRIES_COUNT = 3;
	
	/** The main application window */
	final ClientMain client;
	
	/** A host name of a server */
	private String serverHost;
	
	/** A TCP port the server software is running on */
	private int serverPort;
		
	private Socket socket;
	
	/** The receiving thread of the connection */
	final ClientReceiver receiver;
	
	/** The transmitting thread of the connection */
	final ClientTransmitter transmitter;
	
	/** Data synchronizing background thread */
	final Synchronizer synchronizer;
		
	private String login = "";
	private char[] password = null;
	
	/** Last successful login challenge */
	private AuthChallenge lastChallenge = null;
	
	private boolean connected = false;
	private boolean terminated = false;

	/**
	 *  Create the client-side network connection controller. Upon creation, 
	 * the controller automatically connects to a server and launches the
	 * Login Dialog.
	 * @param client The main application window.
	 * @param table The table 
	 */
	public Connection(ClientMain client, Table table) {
		this.serverHost = DEFAULT_SERVER_HOST;
		this.serverPort = DEFAULT_IP_PORT;
		this.client = client;
		receiver = new ClientReceiver(this);
		receiver.start();	//This will open the socket
		transmitter = new ClientTransmitter(this);
		transmitter.start();
		synchronizer = new Synchronizer(client, table, this);
		synchronizer.start();
	}

	/** (Re)start the network connection */
	void reconnect() {
		while (!terminated) {
			int retries = RETRIES_COUNT;
			while ((retries-- > 0) && !terminated) {
				connected = false;
				try {
					socket.close();
				} catch (Exception e) {
					//Nothing to do here
				}
				socket = new Socket();
				try {
					socket.connect(new InetSocketAddress(serverHost, serverPort), 
														DEFAULT_TIMEOUT);
					InputStream in = socket.getInputStream();
					OutputStream out = socket.getOutputStream();					
					if (authenticate(in, out)) { //The Login Dialog may be opened from here
						receiver.in = in;
						transmitter.out = out;
						connected = true;
						return;						
					}
				} catch (IOException e) {
					//Nothing to do here
				} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//After a sequence of RETRIES_COUNT fails
			if (terminated) return;
			client.showConnectionDlg();
		}
	}
	
	/**
	 *  Try to log in. If there are no saved credentials, open the Login Dialog.
	 *  
	 * @param in
	 * @param out
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private boolean authenticate(InputStream in, OutputStream out) 
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		
		if (null == lastChallenge) {	//If there is no saved hash			
			client.showLoginDialog();
		}
		
		new LoginRequest(login).transmit(out);
		if (receiveInt(in) != AUTH_CHALLENGE) return false;
		AuthChallenge challenge = AuthChallenge.receive(in);
		
		if (null != password) {
			
			//Have got a clear password. Must use it and wipe out.
			challenge.createResponse(password).transmit(out);
			Arrays.fill(password, (char) 0);
			password = null;			
		} else {
			
			//There is no clear password. Gonna use saved hash.
			if (null == lastChallenge) return false; //XXX Impossible, but sometimes happens.
			challenge.createResponse(lastChallenge).transmit(out);
		}
		
		if (receiveInt(in) != AUTH_ACKNOWLEDGEMENT) return false;
		AuthAcknowledgement authAck = AuthAcknowledgement.receive(in);
		if (INVALID_ID == authAck.userId) {
			
			//Authentication failed
			lastChallenge = null;
			return false;
		}
		
		//Success
		client.setLogin(login);
		client.setAdmin(authAck.admin);
		lastChallenge = challenge;
		return true;
	}

	/**
	 *  Shut down the synchronizer, transmitter and receiver and close the
	 * network socket.
	 */
	public void terminate() {
		if (terminated) return;
		terminated = true;
		connected = false;
		synchronizer.interrupt();
		transmitter.terminate();
		try {
			socket.close();
		} catch (Exception e) {
			//Nothing to do here
		}
	}

	/**
	 * Acquire a host name of the server.
	 * @return
	 */
	public String getServerHost() {
		return serverHost;
	}

	/**
	 *  Set a new server host name and reestablish connection if necessary. 
	 * @param newHost
	 */
	public void setServerHost(String newHost) {
		if (newHost.isBlank()) return;
		newHost = newHost.trim();
		if (newHost.equalsIgnoreCase(serverHost)) return;
		serverHost = newHost;
		if (connected) reconnect();
	}

	/**
	 * Acquire an IP port number of the server.
	 * @return
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 *  Set a new server IP port number an reconnect if necessary.
	 * @param newPort
	 */
	public void setServerPort(int newPort) {
		if (newPort == serverPort) return;
		serverPort = newPort;
		if (connected) reconnect();
	}

	/**
	 * Acquire current login name.
	 * @return
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Set a new login name and restart connection if needed.
	 * @param newLogin
	 */
	public void setLogin(String newLogin) {
		if (newLogin.isBlank()) return;
		newLogin = newLogin.trim();
		if (newLogin.equalsIgnoreCase(login)) return;
		login = newLogin;
		if (connected) reconnect();
	}

	/**
	 * Set a password to be used while authenticating and reconnect if needed.
	 * @param newPass
	 */
	public void setPassword(char[] newPass) {
		if (newPass.length == 0) return;
		password = newPass;
		if (connected) reconnect();
	}

	/**
	 * Send a password change request to the server.
	 * @param oldPass
	 * @param newPass
	 * @return
	 */
	public boolean changePass(char[] oldPass, char[] newPass) {
		try {
			ChangePassRequest request = lastChallenge.createChangePassRequest(oldPass, newPass);
			if (request != null) {
				transmitter.send(request);
				return true;
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
