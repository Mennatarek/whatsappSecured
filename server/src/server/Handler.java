package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;

public class Handler {

	private ServerSocket serverSocket;
	private File contactsInfo;
	private Hashtable<String, String> htblContactsPorts;
	private Hashtable<String, String> htblContactsKeys;
	private Hashtable<String, Boolean> htblContactsStatus;

	public Handler() {
		contactsInfo = new File("contactsInfo.csv");
		htblContactsPorts = new Hashtable<String, String>();
		htblContactsKeys = new Hashtable<String, String>();
		htblContactsStatus = new Hashtable<String, Boolean>();
		if (contactsInfo.exists()) {

			try {
				BufferedReader br = new BufferedReader(new FileReader(
						contactsInfo.getName()));
				String line;

				line = br.readLine();
				while (line != null) {

					String[] info = line.split(",");

					htblContactsKeys.put(info[0], info[1]);
					htblContactsStatus.put(info[0], false);

					line = br.readLine();
				}
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getContactKey(String phoneNbr) {
		return htblContactsKeys.get(phoneNbr);
	}

	public boolean getContactStatus(String phoneNbr) {
		return htblContactsStatus.get(phoneNbr);
	}

	public void updateContactStatus(String phoneNbr, boolean online) {
		htblContactsStatus.put(phoneNbr, online);
	}

	public String getContactPort(String phoneNbr) {
		return htblContactsPorts.get(phoneNbr);
	}

	public void updateContactPort(String phoneNbr, String portNbr) {
		htblContactsPorts.put(phoneNbr, portNbr);
	}

	public boolean isRegistered(String phoneNbr) {
		return htblContactsKeys.containsKey(phoneNbr);
	}

	public void register(String phoneNbr, String portNbr, String key) {
		htblContactsPorts.put(phoneNbr, portNbr);
		htblContactsKeys.put(phoneNbr, key);
		htblContactsStatus.put(phoneNbr, true);
		
		// update contacts info on disk
		try {
			String lines = "";
			BufferedReader br = new BufferedReader(new FileReader(
					contactsInfo.getName()));
			String nextLine;
			nextLine = br.readLine();
			while(nextLine != null){
				lines += nextLine + "\n";
				nextLine = br.readLine();
			}
			FileWriter fr;
			fr = new FileWriter(contactsInfo);
			fr.write(lines + phoneNbr + "," + key + "\n");
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {
		try {

			serverSocket = new ServerSocket(5420);

			if (!contactsInfo.exists()) {
				contactsInfo.createNewFile();
			}

			while (true) {
				Socket clientSocket = serverSocket.accept();

				System.out.println("Connected" + clientSocket.getPort());

				User newUser = new User(clientSocket, this);

				newUser.start();

			}

		} catch (IOException e) {
			e.printStackTrace();
			try {
				serverSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Handler server = new Handler();
		server.start();

	}

}
