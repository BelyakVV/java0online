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

class Connection {

	static final String DEFAULT_SERVER_HOST = "localhost";
	static final int DEFAULT_TIMEOUT = 3000;
	static final int RETRIES_COUNT = 3;
	
	final ClientMain client;
	private String serverHost;
	private int serverPort;
	private Socket socket;
	final ClientReceiver receiver;
	final ClientTransmitter transmitter;
	final Synchronizer synchronizer;
	
	private String login = "";
	private char[] password = null;
	private AuthChallenge lastChallenge = null;
	
	private boolean connected = false;
	private boolean terminated = false;

	public Connection(ClientMain client, Table table) {
		this.serverHost = DEFAULT_SERVER_HOST;
		this.serverPort = DEFAULT_IP_PORT;
		this.client = client;
		receiver = new ClientReceiver(this);
		receiver.start();
		transmitter = new ClientTransmitter(this);
		transmitter.start();
		synchronizer = new Synchronizer(client, table, this);
		synchronizer.start();
	}

	void reconnect() {
		while (!terminated) {
			int retries = RETRIES_COUNT;
			while (retries-- > 0) {
				connected = false;
				try {
					socket.close();
				} catch (Exception e) {
					//Nothing to do here
				}
				socket = new Socket();
				try {
					socket.connect(new InetSocketAddress(serverHost, serverPort), DEFAULT_TIMEOUT);
					InputStream in = socket.getInputStream();
					OutputStream out = socket.getOutputStream();
					if (authenticate(in, out)) {
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
				if (terminated) return;
			}
			if (terminated) return;
			client.showReconnDlg();
		}
		terminate();
	}
	
	private boolean authenticate(InputStream in, OutputStream out) 
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		if (null == lastChallenge) {
			client.showLoginDialog();
		}
		new LoginRequest(login).transmit(out);
		if (receiveInt(in) != AUTH_CHALLENGE) return false;
		AuthChallenge challenge = AuthChallenge.receive(in);
		if (null != password) {
			challenge.createResponse(password).transmit(out);
			Arrays.fill(password, (char) 0);
			password = null;			
		} else {
			challenge.createResponse(lastChallenge).transmit(out);
		}
		if (receiveInt(in) != AUTH_ACKNOWLEDGEMENT) return false;
		AuthAcknowledgement authAck = AuthAcknowledgement.receive(in);
		if (INVALID_ID == authAck.userId) {
			lastChallenge = null;
			return false;
		}
		client.setLogin(login);
		client.setAdmin(authAck.admin);
		lastChallenge = challenge;
		return true;
	}

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

	public String getServerHost() {
		return serverHost;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerHost(String newHost) {
		if (newHost.isBlank()) return;
		newHost = newHost.trim();
		if (newHost.equalsIgnoreCase(serverHost)) return;
		serverHost = newHost;
		if (connected) reconnect();
	}

	public void setServerPort(int newPort) {
		if (newPort == serverPort) return;
		serverPort = newPort;
		if (connected) reconnect();
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String newLogin) {
		if (newLogin.isBlank()) return;
		newLogin = newLogin.trim();
		if (newLogin.equalsIgnoreCase(login)) return;
		login = newLogin;
		if (connected) reconnect();
	}

	public void setPassword(char[] newPass) {
		if (newPass.length == 0) return;
		password = newPass;
		if (connected) reconnect();
	}

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
