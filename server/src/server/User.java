package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class User extends Thread {

	private Handler server;
	private Socket socket;
	private InputStream is;
	private DataInputStream dis;
	private OutputStream os;
	private DataOutputStream dos;
	private String phoneNbr;

	public User(Socket socket, Handler server) {
		this.socket = socket;
		this.server = server;
		try {
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				String response = dis.readUTF();
				System.out.println("Response :: " + response);
				String[] filteredResponse = response.split(";");
				String phoneNbr = filteredResponse[1];
				boolean isRegistered = server.isRegistered(phoneNbr);
				switch (filteredResponse[0]) {
				case "logIn":
					System.out.println("AA");
					this.phoneNbr = phoneNbr;
					String portNbr = filteredResponse[2];
					if (isRegistered) {
						System.out.println("online");
						server.updateContactPort(phoneNbr, portNbr);
						server.updateContactStatus(phoneNbr, true);
						dos.writeUTF("loggedIn");
					} else {
						String key = filteredResponse[3];
						// register in server
						server.register(phoneNbr, portNbr, key);
						dos.writeUTF("registered");
						System.out.println("registered");
					}
					break;
				case "getContactInfo":
					if (isRegistered) {
						boolean online = server.getContactStatus(phoneNbr);
						if (online) {
							dos.writeUTF("registeredonline;"
									+ server.getContactKey(phoneNbr) + ";"
									+ server.getContactPort(phoneNbr));
						} else {
							dos.writeUTF("registerednotonline");
						}
					} else {
						dos.writeUTF("notregistered");
					}
					break;
				case "logOut":
					server.updateContactStatus(phoneNbr, false);
					break;
				}
			} catch (EOFException e) {
				// TODO Auto-generated catch block
				try {
					server.updateContactStatus(phoneNbr, false);
					is.close();
					dis.close();
					os.close();
					dos.close();
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
}
