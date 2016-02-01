package server;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ClientThread extends Thread {

	Socket socket;
	OutputStream out;
	InputStream in;
	BufferedReader reader;
	DataOutputStream writer;

	String password = "#-+W#nHk=ToF{/E31;[A`l9!UnhB%[/<";
	int id = -1;
	boolean unlocked = false;

	public void run() {

		try {

			out = socket.getOutputStream();
			in = socket.getInputStream();

			reader = new BufferedReader(new InputStreamReader(in));
			writer = new DataOutputStream(out);

			id = Integer.parseInt(reader.readLine());

			String passwd = reader.readLine();
			if (passwd.equals(password)) {

				Server.log.info("Successful authentication from client " + id);

				Server.pnlStatus[id].setBackground(Color.red);
				Server.lblStatus[id].setText("nicht freigeschaltet");
				Server.lblName[id].setText("Wahlcomputer " + (id + 1) + " (IP: " + socket.getInetAddress() + ")");

				Server.btnUnlock[id].addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						unlock();
					}
				});

				Server.btnUnlock[id].setEnabled(true);

			} else {

				Server.log.warning("Authentication from client " + id + " wasn't successful! Closing socket...");
				socket.close();
			}
		} catch (Exception e) {

			try {
				socket.close();
			} catch (IOException e1) {
				Server.log.error("Error while closing socket!");
			}
			
			try {
				Server.pnlStatus[id].setBackground(Color.gray);
				Server.lblStatus[id].setText("nicht verbunden");
				Server.lblName[id].setText("Wahlcomputer " + (id + 1));
			} catch (ArrayIndexOutOfBoundsException e2) {
				Server.log.error("Error while reseting status panel!");
			}
		}
	}

	void unlock() {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {

				try {

					Server.btnUnlock[id].setEnabled(false);

					writer.writeBytes("unlock\n");
					Server.pnlStatus[id].setBackground(new Color(40, 200, 75));
					Server.lblStatus[id].setText("freigeschaltet");

					unlocked = true;

					Server.log.log("Unlocked client with id " + id);

					getChoice();

				} catch (IOException e1) {

					try {
						socket.close();
					} catch (IOException e2) {
						Server.log.error("Error while closing socket!");
					}

					Server.pnlStatus[id].setBackground(Color.gray);
					Server.lblStatus[id].setText("nicht verbunden");
					Server.lblName[id].setText("Wahlcomputer " + (id + 1));

					unlocked = false;
				}
			}
		});

		if (!unlocked)
			thread.start();
	}

	void getChoice() throws IOException {

		File file = new File("C:\\Wahl");
		if (!file.exists())
			file.mkdir();

		file = new File("C:\\Wahl\\Wahl_Ergebnisse.txt");
		if (!file.exists())
			file.createNewFile();

		BufferedWriter bWriter = new BufferedWriter(new FileWriter(file, true));

		String s = "Client " + id + " voted: ";

		for (int i = 0; i < 5; i++) {

			String line = reader.readLine();
			bWriter.append(line + "\n");

			if (i < 3)
				s = s + line + ", ";
			else if (i == 3)
				s = s + line + " and ";
			else
				s = s + line;
		}
		Server.log.info(s);

		bWriter.close();

		Server.pnlStatus[id].setBackground(Color.red);
		Server.lblStatus[id].setText("nicht freigeschaltet");
		Server.btnUnlock[id].setEnabled(true);

		unlocked = false;
	}

	public ClientThread(Socket s) {

		socket = s;
	}
}
