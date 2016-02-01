package server;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class Server {

	ServerSocket server;
	Socket socket;
	final int port = 27016;

	static Log log;

	static JFrame frame;
	static GridBagLayout layout;
	static JPanel panel;
	static JPanel footer;
	static JButton button, backup;

	static int clients = 16;

	static JPanel[] pnlClient = new JPanel[clients], pnlStatus = new JPanel[clients];
	static JLabel[] lblStatus = new JLabel[clients], lblName = new JLabel[clients];
	static JButton[] btnUnlock = new JButton[clients];

	static ClientThread[] client = new ClientThread[0];

	public Server() {

		File file = new File("C:\\Wahl");
		if (!file.exists())
			file.mkdir();

		file = new File("C:\\Wahl\\log.txt");
		log = new Log(file, true, true);

		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			log.error("BindException occurred! Shut down the server, which is running on port " + port
					+ " or change port. Terminating...");
			System.exit(1);
		}

		log.log("========================================================");
		log.log("ELECTION SOFTWARE server created! Waiting for clients...");
		log.log("========================================================\n");

		createGUI();

		while (true) {

			try {

				socket = server.accept();
				log.log("New client request from IPv4 Address: " + socket.getInetAddress());

				ClientThread clientThread = new ClientThread(socket);
				clientThread.start();

				ClientThread[] client2 = new ClientThread[client.length + 1];
				System.arraycopy(client, 0, client2, 0, client.length);
				client2[client.length] = clientThread;
				client = client2;

			} catch (IOException e) {
				log.error("IOException occurred while creating client thread!");
			}
		}
	}

	public static void createGUI() {

		layout = new GridBagLayout();

		frame = new JFrame("Wahlsoftware Server - Freigabeprogramm");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(700, 40 * clients + 80);
		frame.setResizable(false);
		frame.setLayout(layout);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.setExtendedState(Frame.ICONIFIED);
			}
		});

		panel = new JPanel();
		panel.setLayout(new GridLayout(clients, 1));
		addComponent(frame, layout, panel, 0, 0, 1, 1, 1, 1, new Insets(5, 5, 5, 5));

		for (int i = 0; i < clients; i++) {

			pnlClient[i] = new JPanel();
			if (i != 0)
				pnlClient[i].setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
			if (i == 0)
				pnlClient[i].setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.black));
			pnlClient[i].setLayout(layout);

			pnlStatus[i] = new JPanel();
			pnlStatus[i].setBackground(Color.gray);
			pnlStatus[i].setLayout(layout);
			addComponent(pnlClient[i], layout, pnlStatus[i], 0, 0, 1, 1, 0, 1, new Insets(0, 0, 0, 0));

			lblStatus[i] = new JLabel("nicht verbunden", SwingConstants.CENTER);
			lblStatus[i].setForeground(Color.white);
			lblStatus[i].setFont(new Font("Verdana", 0, 14));
			addComponent(pnlStatus[i], layout, lblStatus[i], 0, 0, 1, 1, 1, 1, new Insets(10, 30, 10, 30));

			lblName[i] = new JLabel("Wahlcomputer " + (i + 1), SwingConstants.LEFT);
			lblName[i].setFont(new Font("Verdana", 0, 14));
			addComponent(pnlClient[i], layout, lblName[i], 1, 0, 1, 1, 1, 0, new Insets(5, 15, 5, 5));

			btnUnlock[i] = new JButton("freischalten");
			btnUnlock[i].setEnabled(false);
			addComponent(pnlClient[i], layout, btnUnlock[i], 2, 0, 1, 1, 0, 0, new Insets(10, 5, 10, 5));

			panel.add(pnlClient[i]);
		}

		footer = new JPanel();
		footer.setLayout(layout);
		addComponent(frame, layout, footer, 0, 1, 1, 1, 1, 0, new Insets(0, 5, 5, 5));

		backup = new JButton("Backup erstellen");
		backup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Thread backupThread = new Thread(new Runnable() {

					@Override
					public void run() {

						File file = new File("C:\\Wahl\\Wahl_Ergebnisse.txt");
						if (file.exists()) {

							file = new File("C:\\Wahl\\Backups");
							if (!file.exists())
								file.mkdir();

							try {

								BufferedWriter writer;

								file = new File("C:\\Wahl\\Backup_Nummer.txt");
								if (!file.exists()) {

									file.createNewFile();

									writer = new BufferedWriter(new FileWriter(file));
									writer.write("0");
									writer.close();
								}

								BufferedReader reader = new BufferedReader(new FileReader(file));
								String number = reader.readLine();
								reader.close();

								writer = new BufferedWriter(new FileWriter(file));
								writer.write(String.valueOf(Integer.parseInt(number) + 1));
								writer.close();

								file = new File("C:\\Wahl\\Wahl_Ergebnisse.txt");
								reader = new BufferedReader(new FileReader(file));

								String content = "", s = reader.readLine();
								while (s != null) {

									content = content + s + "\n";
									s = reader.readLine();
								}
								reader.close();

								file = new File("C:\\Wahl\\Backups\\Backup" + number + ".txt");
								if (!file.exists())
									file.createNewFile();

								writer = new BufferedWriter(new FileWriter(file));
								writer.write(content);
								writer.close();

								log.info("Created backup. (" + file.getName() + ")");

								JOptionPane.showMessageDialog(null,
										"Backup erfolgreich erstellt. (" + file.getName() + ")", "Backup",
										JOptionPane.INFORMATION_MESSAGE);

							} catch (IOException e) {

								log.error("Error occured while copying data and creating backup!");

								JOptionPane.showMessageDialog(null,
										"Fehler beim erstellen des Backups! (" + file.getName() + ")", "Fehler",
										JOptionPane.ERROR_MESSAGE);
							}
						} else {

							log.warning("No voting results yet. Didn't created backup.");

							JOptionPane.showMessageDialog(null,
									"Noch keine Wahlergebnisse vorhanden. Kein Backup erstellt!", "Fehler",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				});
				backupThread.start();
			}
		});
		addComponent(footer, layout, backup, 0, 0, 1, 1, 0, 0, new Insets(5, 5, 5, 5));

		addComponent(footer, layout, new JPanel(), 1, 0, 1, 1, 1, 0, new Insets(0, 0, 0, 0));

		button = new JButton("alle freischalten");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				for (ClientThread clientThread : client) {

					clientThread.unlock();
				}
			}
		});
		addComponent(footer, layout, button, 2, 0, 1, 1, 0, 0, new Insets(5, 5, 5, 5));

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	static void addComponent(Container cont, GridBagLayout gbl, Component c, int x, int y, int width, int height,
			double weightx, double weighty, Insets insets) {

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.insets = insets;
		gbl.setConstraints(c, gbc);
		cont.add(c);
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Error while setting LookAndFeel! Default Java LookAndFeel will be used...");
		}

		new Server();
	}
}
