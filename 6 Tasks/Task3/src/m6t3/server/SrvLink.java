package m6t3.server;

import static m6t3.common.Tranceiver.AUTH_RESPONSE;
import static m6t3.common.Tranceiver.LOGIN_REQUEST;
import static m6t3.common.Tranceiver.receiveInt;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import m6t3.common.AuthChallenge;
import m6t3.common.AuthResponse;
import m6t3.common.LoginRequest;
import m6t3.common.User;

class SrvLink {
	
	final SrvListener server;
	final Socket socket;
	final SrvReceiver reciever;
	final SrvTransmitter transmitter;
	final BlockingQueue<Object> outQueue;
	User user;
	volatile boolean running = true;

	SrvLink(SrvListener server, Socket socket) throws IOException {
		this.server = server;
		this.socket = socket;
		if (!authenticate()) user = null;
		outQueue = new LinkedBlockingQueue<>();
		reciever = new SrvReceiver(this);
		reciever.start();	
		transmitter = new SrvTransmitter(this);
		transmitter.start();
	}

	private boolean authenticate() throws IOException {
		InputStream in = socket.getInputStream();
		if (receiveInt(in) != LOGIN_REQUEST) return false;
		LoginRequest request = LoginRequest.receive(in);
		user = server.data.getUser(request.login);
		if (null == user) return false;
		AuthChallenge challenge = user.createChallenge();
		challenge.transmit(socket.getOutputStream());
		if (receiveInt(in) != AUTH_RESPONSE) return false;
		AuthResponse response = AuthResponse.receive(in);
		try {
			return user.isValidResponse(challenge, response);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	void close() {
		if (!running) return;
		running = false;
		try {
			if (socket.getInputStream().available() < 1) {
				socket.getInputStream().close();
			}
		} catch (IOException e) {
			//Nothing to do here
		}
		try {
			if (Thread.currentThread() != reciever) reciever.join();
		} catch (InterruptedException e) {
			//Nothing to do here
		}
		transmitter.interrupt();
		try {
			if (Thread.currentThread() != transmitter) transmitter.join();
		} catch (InterruptedException e) {
			//Nothing to do here
		}
		try {
			socket.close();
		} catch (IOException e) {
			//Nothing to do here
		}
		server.links.remove(this);
	}	
}
