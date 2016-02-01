/**
 * 
 */
package client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * @author Maximilian von Gaisberg
 *
 */
public class Client {

	Socket socket;
	String ip;

	Properties props;

	int presidents;
	int parties;
	int emblems;
	int flags;

	static Integer[] presi_sort;
	static Integer[] flag_sort;
	static Integer[] emblem_sort;
	static Integer[] party_sort;

	File president_file[];
	File party_file[];
	File emblem_file[];
	File flag_file[];

	static ImageIcon president_image[];
	static ImageIcon party_image[];
	static ImageIcon emblem_image[];
	static ImageIcon flag_image[];
	static ImageIcon noParty;
	static ImageIcon noPresi;
	static ImageIcon noFlag;
	static ImageIcon noEmblem;

	static String president[];
	static String party[];

	String president_path;
	String parties_path;
	String flags_path;
	String emblems_path;
	String enthaltung_path;

	BufferedReader r;
	static DataOutputStream send;
	static GUI gui;

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		new Client();
	}

	private void readData(File propFile) throws FileNotFoundException,
			IOException {

		props = new Properties();
		props.load(new FileInputStream(propFile));

		ip = props.getProperty("ip");

		int presidents = Integer.parseInt(props.getProperty("presidents"));
		int parties = Integer.parseInt(props.getProperty("parties"));
		int emblems = Integer.parseInt(props.getProperty("emblems"));
		int flags = Integer.parseInt(props.getProperty("flags"));

		this.president_file = new File[presidents];
		this.party_file = new File[parties];
		this.emblem_file = new File[emblems];
		this.flag_file = new File[flags];

		this.president_image = new ImageIcon[presidents];
		this.party_image = new ImageIcon[parties];
		this.emblem_image = new ImageIcon[emblems];
		this.flag_image = new ImageIcon[flags];

		this.president = new String[presidents];
		this.party = new String[parties];

		this.president_path = props.getProperty("presidentsPath");
		this.parties_path = props.getProperty("partiesPath");
		this.flags_path = props.getProperty("flagsPath");
		this.emblems_path = props.getProperty("emblemsPath");
		this.enthaltung_path = props.getProperty("enthaltungPath");

		for (int i = 0; i < presidents; i++) {
			president_file[i] = new File(president_path + i + ".jpg");
			president[i] = props.getProperty("PresidentText" + i);
			System.out.println(president[i]);
		}

		for (int i = 0; i < parties; i++) {
			party_file[i] = new File(parties_path + i + ".jpg");
			party[i] = props.getProperty("PartieText" + i);
		}

		for (int i = 0; i < flags; i++) {
			flag_file[i] = new File(flags_path + i + ".jpg");
		}

		for (int i = 0; i < emblems; i++) {
			emblem_file[i] = new File(emblems_path + i + ".jpg");
		}

		president_image = readImages(president_file);
		party_image = readImages(party_file);
		emblem_image = readImages(emblem_file);
		flag_image = readImages(flag_file);

		presi_sort = shuffleDaCruffleDuffle(president_image, president);
		party_sort = shuffleDaCruffleDuffle(party_image, party);
		emblem_sort = shuffleDaCruffleDuffle(emblem_image);
		flag_sort = shuffleDaCruffleDuffle(flag_image);

		noParty = new ImageIcon(ImageIO.read(new File(enthaltung_path
				+ "keinePartei.jpg")));
		noFlag = new ImageIcon(ImageIO.read(new File(enthaltung_path
				+ "keineFlagge.jpg")));
		noPresi = new ImageIcon(ImageIO.read(new File(enthaltung_path
				+ "keinPresi.jpg")));
		noEmblem = new ImageIcon(ImageIO.read(new File(enthaltung_path
				+ "keinWappen.jpg")));

	}

	private ImageIcon[] readImages(File[] files) {

		ImageIcon[] buff = new ImageIcon[files.length];
		for (int i = 0; i < files.length; i++) {
			System.out.println("Reading " + files[i].getPath());
			try {
				buff[i] = new ImageIcon(ImageIO.read(files[i]));
			} catch (IOException e) {
				System.out.println("Can't read images!");

				e.printStackTrace();
			}
		}
		return buff;
	}

	public Client() {
		try {
			readData(new File("C://wahl//wahl.properties"));
		} catch (IOException e) {
			System.out.println("Can't read wahl.properties on C:/wahl/");
			e.printStackTrace();
		}
		gui = new GUI(president, president_image, party, party_image,
				emblem_image, flag_image, noPresi, noParty, noEmblem, noFlag);
		connect();
	}

	private void connect() {
		try {
			System.out.println("Connecting...");
			socket = new Socket(ip, 27016);
			System.out.println("Connected!");
			r = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			send = new DataOutputStream(socket.getOutputStream());
			send.writeBytes(props.getProperty("id") + "\n");
			send.writeBytes("#-+W#nHk=ToF{/E31;[A`l9!UnhB%[/<\n");

			while (true) {
				if (r.readLine().startsWith("unlock")) {

					System.out.println("Client has been unlocked");
					gui.unlock();
				}
			}

		} catch (IOException e) {
			System.out.println("Could't connect to server");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void SendData(int presidentindex, int partynameindex,
			int partynameindex2, int emblemindex, int flagindex) {

		try {
			send.writeBytes("pres"
					+ (presidentindex >= 0 ? presi_sort[presidentindex] : "-1")
					+ "\nparty"
					+ (partynameindex >= 0 ? party_sort[partynameindex] : "-1")
					+ "\nparty"
					+ (partynameindex2 >= 0 ? party_sort[partynameindex2]
							: "-1") + "\nemblem"
					+ (emblemindex >= 0 ? emblem_sort[emblemindex] : "-1")
					+ "\nflag" + (flagindex >= 0 ? flag_sort[flagindex] : "-1")
					+ "\n");
		} catch (IOException e) {
			System.out.println("Couldn't send Results");
			e.printStackTrace();
		}

	}

	private Integer[] shuffleDaCruffleDuffle(Object[] array) {
		Integer[] array2 = new Integer[array.length];

		for (int i = 0; i < array.length; i++) {
			array2[i] = i;
		}

		int index;
		Object temp;
		int temp2;
		Random random = new Random();
		for (int i = array.length - 1; i > 0; i--) {
			index = random.nextInt(i + 1);
			temp = array[index];
			temp2 = array2[index];
			array[index] = array[i];
			array2[index] = array2[i];
			array[i] = temp;
			array2[i] = temp2;
		}
		return array2;
	}

	private Integer[] shuffleDaCruffleDuffle(Object[] array, Object[] array3) {
		Integer[] array2 = new Integer[array.length];
		for (int i = 0; i < array.length; i++) {
			array2[i] = i;
		}

		int index;
		Object temp;
		int temp2;
		Object temp3;
		Random random = new Random();
		for (int i = array.length - 1; i > 0; i--) {
			index = random.nextInt(i + 1);
			temp = array[index];
			temp2 = array2[index];
			temp3 = array3[index];
			array[index] = array[i];
			array2[index] = array2[i];
			array3[index] = array3[i];
			array[i] = temp;
			array2[i] = temp2;
			array3[i] = temp3;
		}
		return array2;
	}

}
