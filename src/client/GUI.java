package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;

/*
 * @author Johannes Groß
 * @date 29.01.2016 16:35:59
 */

public class GUI implements ActionListener, MouseListener {

	private static JFrame frame;
	private static JLabel lbl_wait, lbl_header, lbl_subheader, ev_president, ev_party1, ev_party2, ev_emblem, ev_flag;
	private static JPanel panel, pnl_president, pnl_party, pnl_emblem, pnl_flag, pnl_evaluation, pnl_south, pnl_north;
	private static JPanel[] ip_president, ip_party, ip_emblem, ip_flag;
	private static JButton btn_next;
	private static GridBagLayout gridbaglayout = new GridBagLayout();
	private static JScrollPane sp_president, sp_party, sp_emblem, sp_flag, sp_evaluation;
	private int width;
	private static Border border;
	private static boolean[] boo_presidentBorder, boo_partyBorder, boo_emblemBoder, boo_flagBorder;
	private static JLabel[] il_president, il_party, il_emblem, il_flag;
	private static int selectedPresident, selectedEmblem, selectedFlag, selectedPartiesCounter;
	private static boolean PresidentIsSelected = false;
	private static boolean EmblemIsSelected = false;
	private static boolean FlagIsSelected = false;
	private static int[] SelectedParties = new int[2];

	private static ImageIcon img_empty_president, img_empty_party, img_empty_emblem, img_empty_flag;

	private static String[] sa_presidentnames, sa_partynames;
	private static ImageIcon[] iia_presidentimages, iia_partyimages, iia_emblemimages, iia_flagimages;

	public GUI(String[] presidentnames, ImageIcon[] presidentimages, String[] partynames, ImageIcon[] partyimages,
			ImageIcon[] emblemimages, ImageIcon[] flagimages, ImageIcon emty_president, ImageIcon empty_party,
			ImageIcon empty_emblem, ImageIcon empty_flag) {

		sa_presidentnames = presidentnames.clone();
		sa_partynames = partynames.clone();

		img_empty_president = new ImageIcon(emty_president.getImage());
		img_empty_party = new ImageIcon(empty_party.getImage());
		img_empty_emblem = new ImageIcon(empty_emblem.getImage());
		img_empty_flag = new ImageIcon(empty_flag.getImage());

		iia_presidentimages = presidentimages.clone();
		iia_partyimages = partyimages.clone();
		iia_emblemimages = emblemimages.clone();
		iia_flagimages = flagimages.clone();
		
		init();
	}
	
	void reload() {
		
		selectedPresident = -1;
		selectedEmblem = -1;
		selectedFlag = -1;
		selectedPartiesCounter = 0;
		
		il_president = new JLabel[iia_presidentimages.length];
		il_party = new JLabel[iia_presidentimages.length];
		il_emblem = new JLabel[iia_presidentimages.length];
		il_flag = new JLabel[iia_presidentimages.length];
		
		SelectedParties[0] = -1;
		SelectedParties[1] = -1;
		
		boo_presidentBorder = new boolean[iia_presidentimages.length];
		for (int i = 0; i > iia_presidentimages.length; i++) {
			boo_presidentBorder[i] = false;
		}
		boo_partyBorder = new boolean[iia_partyimages.length];
		for (int i = 0; i > iia_partyimages.length; i++) {
			boo_partyBorder[i] = false;
		}
		boo_emblemBoder = new boolean[iia_emblemimages.length];
		for (int i = 0; i > iia_emblemimages.length; i++) {
			boo_emblemBoder[i] = false;
		}
		boo_flagBorder = new boolean[iia_flagimages.length];
		for (int i = 0; i > iia_flagimages.length; i++) {
			boo_flagBorder[i] = false;
		}
		
		init_pnl_president(presidentimages, presidentnames);
		init_pnl_party(partyimages, partynames);
		init_pnl_emblem(emblemimages);
		init_pnl_flag(flagimages);
		init_pnl_evaluation();
		init_panel();
	}

	private void init_panel() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		sp_president = new JScrollPane(pnl_president, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sp_president.getVerticalScrollBar().setUnitIncrement(25);

		panel.add(sp_president, BorderLayout.CENTER);
		panel.add(pnl_south, BorderLayout.SOUTH);

		panel.setVisible(false);

		frame.add(panel);
	}

	private void init() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Error while setting LookAndFeel!");
		}

		border = BorderFactory.createLineBorder(Color.black, 5);

		lbl_wait = new JLabel("Auf Freischaltung durch Wahlhelfer warten...");
		lbl_wait.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_wait.setFont(new Font("Arial", Font.BOLD, 40));

		frame = new JFrame();
		frame.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		frame.setAlwaysOnTop(true);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(lbl_wait);
		frame.setVisible(true);

		width = frame.getWidth();

		lbl_wait.setVisible(true);

		btn_next = new JButton("Weiter");
		btn_next.addActionListener(this);
		btn_next.setHorizontalAlignment(SwingConstants.CENTER);
		btn_next.setFont(new Font("Arial", Font.BOLD, 30));
		btn_next.setVisible(true);

		pnl_south = new JPanel();
		pnl_south.setLayout(gridbaglayout);
		addComponent(pnl_south, gridbaglayout, new JPanel(), 0, 0, 1, 1, 0, 0, new Insets(0, 0, 0, 0));
		addComponent(pnl_south, gridbaglayout, new JPanel(), 2, 0, 1, 1, 0, 0, new Insets(0, 0, 0, 0));
		addComponent(pnl_south, gridbaglayout, btn_next, 1, 0, 1, 1, 0, 0, new Insets(5, 5, 5, 5));

		lbl_header = new JLabel("Schule als Staat - Wahlen");
		lbl_header.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_header.setFont(new Font("Arial", Font.BOLD, 50));
		lbl_header.setVisible(true);

		lbl_subheader = new JLabel();
		lbl_subheader.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_subheader.setFont(new Font("Arial", Font.BOLD, 35));
		lbl_subheader.setVisible(true);

		pnl_north = new JPanel();
		pnl_north.setLayout(gridbaglayout);

		addComponent(pnl_north, gridbaglayout, lbl_header, 0, 0, 1, 1, 0, 0, new Insets(5, 5, 5, 5));
		addComponent(pnl_north, gridbaglayout, lbl_subheader, 0, 1, 1, 1, 0, 0, new Insets(0, 0, 0, 0));

		frame.add(pnl_north, BorderLayout.NORTH);
	}

	private void init_pnl_president(ImageIcon[] presidentimages, String[] presidentname) {
		int add = 0;
		if (presidentimages.length % 4 != 0) {
			add = 4 - presidentimages.length % 4;
		}
		GridLayout gridlayout = new GridLayout((presidentimages.length / 4) + add, 4);
		ip_president = new JPanel[presidentimages.length];

		pnl_president = new JPanel();
		pnl_president.setLayout(gridlayout);
		pnl_president.setVisible(false);

		for (int i = 0; i < presidentimages.length; i++) {
			ip_president[i] = new JPanel();
			ip_president[i].setLayout(gridbaglayout);
			ip_president[i].addMouseListener(this);

			il_president[i] = new JLabel(new ImageIcon(presidentimages[i].getImage()
					.getScaledInstance((int) (width * 0.9) / 4, (int) (width * 0.9) / 4, Image.SCALE_FAST)));

			ip_president[i].setSize((width * 9) / 4, (int) ((width * 0.9)));

			il_president[i].setBorder(BorderFactory.createEmptyBorder());
			addComponent(ip_president[i], gridbaglayout, il_president[i], 0, 0, 1, 1, 1, 1, new Insets(10, 10, 10, 10));
			pnl_president.add(ip_president[i]);
		}

	}

	private void init_pnl_party(ImageIcon[] partyimages, String[] partynames) {

		int add = 0;
		if (partyimages.length % 4 != 0) {
			add = 4 - partyimages.length % 4;
		}
		GridLayout gridlayout = new GridLayout((partyimages.length / 4) + add, 4);
		ip_party = new JPanel[partyimages.length];
		pnl_party = new JPanel();
		pnl_party.setLayout(gridlayout);
		pnl_party.setVisible(false);

		for (int i = 0; i < partyimages.length; i++) {
			ip_party[i] = new JPanel();
			ip_party[i].setLayout(gridbaglayout);
			ip_party[i].addMouseListener(this);

			il_party[i] = new JLabel(new ImageIcon(partyimages[i].getImage().getScaledInstance((int) (width * 0.9) / 4,
					(int) (width * 0.9) / 4, Image.SCALE_FAST)));
			ip_party[i].setSize((width * 9) / 4, (int) ((width * 0.9)));

			il_party[i].setBorder(BorderFactory.createEmptyBorder());
			addComponent(ip_party[i], gridbaglayout, il_party[i], 0, 0, 1, 1, 1, 1, new Insets(10, 10, 10, 10));

			pnl_party.add(ip_party[i]);
		}
		sp_party = new JScrollPane(pnl_party, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sp_party.getVerticalScrollBar().setUnitIncrement(25);

	}

	private void init_pnl_emblem(ImageIcon[] emblemimages) {
		int add = 0;
		if (emblemimages.length % 4 != 0) {
			add = 4 - emblemimages.length % 4;
		}
		GridLayout gridlayout = new GridLayout((emblemimages.length / 4) + add, 4);
		ip_emblem = new JPanel[emblemimages.length];
		pnl_emblem = new JPanel();
		pnl_emblem.setLayout(gridlayout);
		pnl_emblem.setVisible(false);

		for (int i = 0; i < emblemimages.length; i++) {
			ip_emblem[i] = new JPanel();
			ip_emblem[i].setLayout(gridbaglayout);
			ip_emblem[i].addMouseListener(this);

			il_emblem[i] = new JLabel(new ImageIcon(emblemimages[i].getImage()
					.getScaledInstance((int) (width * 0.9) / 4, (int) (width * 0.9) / 4, Image.SCALE_FAST)));
			ip_emblem[i].setSize((width * 9) / 4, (int) ((width * 0.9)));

			il_emblem[i].setBorder(BorderFactory.createEmptyBorder());
			addComponent(ip_emblem[i], gridbaglayout, il_emblem[i], 0, 0, 1, 1, 1, 1, new Insets(10, 10, 10, 10));

			pnl_emblem.add(ip_emblem[i]);
		}

		sp_emblem = new JScrollPane(pnl_emblem, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sp_emblem.getVerticalScrollBar().setUnitIncrement(25);

	}

	private void init_pnl_flag(ImageIcon[] flagimages) {
		int add = 0;
		if (flagimages.length % 4 != 0) {
			add = 4 - flagimages.length % 4;
		}
		GridLayout gridlayout = new GridLayout((flagimages.length / 4) + add, 4);

		ip_flag = new JPanel[flagimages.length];

		pnl_flag = new JPanel();
		pnl_flag.setLayout(gridlayout);
		pnl_flag.setVisible(false);

		for (int i = 0; i < flagimages.length; i++) {
			ip_flag[i] = new JPanel();
			ip_flag[i].setLayout(gridbaglayout);
			ip_flag[i].addMouseListener(this);

			il_flag[i] = new JLabel(new ImageIcon(flagimages[i].getImage().getScaledInstance((int) (width * 0.9) / 4,
					(int) (width * 0.9) / 4, Image.SCALE_FAST)));
			ip_flag[i].setSize((width * 9) / 4, (int) ((width * 0.9)));

			il_flag[i].setBorder(BorderFactory.createEmptyBorder());
			addComponent(ip_flag[i], gridbaglayout, il_flag[i], 0, 0, 1, 1, 1, 1, new Insets(10, 10, 10, 10));
			pnl_flag.add(ip_flag[i]);
		}

		sp_flag = new JScrollPane(pnl_flag, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sp_flag.getVerticalScrollBar().setUnitIncrement(25);
	}

	private void init_pnl_evaluation() {

		GridLayout gridlayout = new GridLayout(2, 4);
		pnl_evaluation = new JPanel();
		pnl_evaluation.setLayout(gridlayout);
		pnl_evaluation.setVisible(false);

		sp_evaluation = new JScrollPane(pnl_evaluation, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sp_evaluation.getVerticalScrollBar().setUnitIncrement(25);

	}

	public static void unlock() {

		panel.setVisible(true);
		lbl_wait.setVisible(false);
		panel.add(sp_president, BorderLayout.CENTER);

		pnl_president.setVisible(true);
		sp_president.setVisible(true);

		lbl_subheader.setText("Präsident wählen");
		panel.setVisible(true);
		pnl_evaluation.setVisible(false);
		pnl_north.setVisible(true);
		panel.repaint();

	}

	private void lock() throws InterruptedException {

		pnl_flag.setVisible(false);
		sp_flag.setVisible(false);
		pnl_emblem.setVisible(false);
		sp_emblem.setVisible(false);
		pnl_party.setVisible(false);
		sp_party.setVisible(false);
		pnl_president.setVisible(false);
		sp_president.setVisible(false);
		pnl_north.setVisible(false);
		panel.setVisible(false);
		frame.repaint();
		btn_next.setEnabled(true);

		lbl_wait.setText("Wahl abgeschlossen, verlassen sie nun bitte den Raum.");
		lbl_wait.setVisible(true);
		frame.add(lbl_wait);

		reOpen();
	}

	private void reOpen() throws InterruptedException {

		Thread.sleep(5000);
		
		Client.SendData(selectedPresident, SelectedParties[0], SelectedParties[1], selectedEmblem, selectedFlag);

		frame.dispose();
		frame = null;
		System.gc();

		reload();

		pnl_north.setVisible(false);
		frame.repaint();


	}

	private void addEvaluationSelection() {
		pnl_evaluation.removeAll();
		// TODO Bilder
		ev_emblem = new JLabel("Wappen nicht gewählt");
		ev_emblem.setText("");
		ev_emblem.setIcon(new ImageIcon(img_empty_emblem.getImage().getScaledInstance((int) (width * 0.9) / 4,
				(int) (width * 0.9) / 4, Image.SCALE_FAST)));
		ev_emblem.setSize((width * 9) / 4, (int) ((width * 0.9)));
		ev_emblem.addMouseListener(this);

		ev_flag = new JLabel("Flagge nicht gewählt");
		ev_flag.setText("");
		ev_flag.setIcon(new ImageIcon(img_empty_flag.getImage().getScaledInstance((int) (width * 0.9) / 4,
				(int) (width * 0.9) / 4, Image.SCALE_FAST)));
		ev_flag.setSize((width * 9) / 4, (int) ((width * 0.9)));
		ev_flag.addMouseListener(this);

		ev_party1 = new JLabel("Partei nicht gewählt");
		ev_party1.setText("");
		ev_party1.setIcon(new ImageIcon(img_empty_party.getImage().getScaledInstance((int) (width * 0.9) / 4,
				(int) (width * 0.9) / 4, Image.SCALE_FAST)));
		ev_party1.setSize((width * 9) / 4, (int) ((width * 0.9)));
		ev_party1.addMouseListener(this);

		ev_president = new JLabel("Präsident nicht gewählt");
		ev_president.setText("");
		ev_president.setIcon(new ImageIcon(img_empty_president.getImage().getScaledInstance((int) (width * 0.9) / 4,
				(int) (width * 0.9) / 4, Image.SCALE_FAST)));
		ev_president.setSize((width * 9) / 4, (int) ((width * 0.9)));
		ev_president.addMouseListener(this);

		ev_party2 = new JLabel("Partei nicht gewählt");
		ev_party2.setText("");
		ev_party2.setIcon(new ImageIcon(img_empty_party.getImage().getScaledInstance((int) (width * 0.9) / 4,
				(int) (width * 0.9) / 4, Image.SCALE_FAST)));
		ev_party2.setSize((width * 9) / 4, (int) ((width * 0.9)));
		ev_party2.addMouseListener(this);

		int id = 1;
		emblem: for (int i = 0; i < boo_emblemBoder.length; i++) {
			if (boo_emblemBoder[i] == true) {
				ev_emblem = new JLabel(il_emblem[i].getIcon());
				ev_emblem.addMouseListener(this);
				ev_emblem.setBorder(BorderFactory.createEmptyBorder());
				ev_emblem.repaint();
				// pnl_evaluation.add(ev_emblem);
				break emblem;
			} else {
				// pnl_evaluation.add(ev_emblem);
			}
		}
		flag: for (int i = 0; i < boo_flagBorder.length; i++) {
			if (boo_flagBorder[i] == true) {
				ev_flag = new JLabel(il_flag[i].getIcon());
				ev_flag.addMouseListener(this);
				ev_flag.setBorder(BorderFactory.createEmptyBorder());
				ev_flag.repaint();
				// pnl_evaluation.add(ev_flag);
				break flag;
			} else {
				// pnl_evaluation.add(ev_flag);
			}
		}

		president: for (int i = 0; i < boo_presidentBorder.length; i++) {
			if (boo_presidentBorder[i] == true) {
				ev_president = new JLabel(il_president[i].getIcon());
				ev_president.addMouseListener(this);
				ev_president.setBorder(BorderFactory.createEmptyBorder());
				ev_president.repaint();
				// pnl_evaluation.add(ev_president);
				break president;
			} else {
				// pnl_evaluation.add(ev_president);
			}
		}

		for (int i = 0; i < boo_partyBorder.length; i++) {
			if (boo_partyBorder[i] == true && id == 1) {
				ev_party1 = new JLabel(il_party[i].getIcon());
				ev_party1.addMouseListener(this);
				ev_party1.setBorder(BorderFactory.createEmptyBorder());
				ev_party1.repaint();

				id++;
			} else if (boo_partyBorder[i] == true && id == 2) {

				ev_party2 = new JLabel(il_party[i].getIcon());
				ev_party2.addMouseListener(this);
				ev_party2.setBorder(BorderFactory.createEmptyBorder());
				ev_party2.repaint();

				// pnl_evaluation.remove(ev_party1);
				// pnl_evaluation.remove(ev_party2);
				// pnl_evaluation.add(ev_party1);
				// pnl_evaluation.add(ev_party2);
				id++;
			} else {

			}
		}

		pnl_evaluation.add(ev_president);
		pnl_evaluation.add(ev_flag);
		pnl_evaluation.add(ev_emblem);
		pnl_evaluation.add(ev_party1);
		pnl_evaluation.add(ev_party2);

		ev_president.repaint();
		ev_flag.repaint();
		ev_emblem.repaint();
		ev_party1.repaint();
		ev_party2.repaint();

		pnl_evaluation.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btn_next) {
			if (pnl_president.isVisible() == true) {
				pnl_president.setVisible(false);
				sp_president.setVisible(false);
				panel.add(sp_party, BorderLayout.CENTER);
				lbl_subheader.setText("Parteien wählen (2 Stimmen)");
				panel.repaint();
				pnl_party.setVisible(true);
				pnl_president.setVisible(false);
			} else if (pnl_party.isVisible() == true) {

				pnl_party.setVisible(false);
				sp_party.setVisible(false);

				panel.add(sp_emblem, BorderLayout.CENTER);
				lbl_subheader.setText("Wappen wählen");
				panel.repaint();
				pnl_emblem.setVisible(true);
				pnl_party.setVisible(false);

			} else if (pnl_emblem.isVisible() == true) {

				pnl_emblem.setVisible(false);
				sp_emblem.setVisible(false);

				panel.add(sp_flag, BorderLayout.CENTER);
				lbl_subheader.setText("Flagge wählen");
				panel.repaint();
				pnl_flag.setVisible(true);
				pnl_emblem.setVisible(false);

			} else if (pnl_flag.isVisible() == true) {

				pnl_flag.setVisible(false);
				sp_flag.setVisible(false);

				lbl_subheader.setText("Ihre Wahl");
				btn_next.setText("Abgeben");

				panel.add(sp_evaluation, BorderLayout.CENTER);

				panel.repaint();
				sp_evaluation.setVisible(true);
				pnl_evaluation.setVisible(true);
				addEvaluationSelection();
				pnl_flag.setVisible(false);
			} else if (btn_next.getText() == "Abgeben") {

				System.out.println("Präsident " + selectedPresident);
				System.out.println("Flagge " + selectedFlag);
				System.out.println("Wappen " + selectedEmblem);
				System.out.println("Partei 1 " + SelectedParties[0]);
				System.out.println("Partei 2 " + SelectedParties[1]);

				btn_next.setEnabled(false);
				btn_next.setText("Weiter");
				try {
					lock();
					System.out.println("System locked....");
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

			}

		}
		if (e.getSource() == btn_next && btn_next.getText().contains("Zurück")) {

			pnl_flag.setVisible(false);
			sp_flag.setVisible(false);
			pnl_emblem.setVisible(false);
			sp_emblem.setVisible(false);
			pnl_party.setVisible(false);
			sp_party.setVisible(false);
			pnl_president.setVisible(false);
			sp_president.setVisible(false);

			lbl_subheader.setText("Ihre Wahl");
			btn_next.setText("Abgeben");

			addEvaluationSelection();

			panel.add(sp_evaluation, BorderLayout.CENTER);

			pnl_evaluation.repaint();
			sp_evaluation.setVisible(true);
			pnl_evaluation.setVisible(true);
			panel.repaint();
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (pnl_president.isVisible()) {

			for (int i = 0; i < ip_president.length; i++) {
				if (e.getSource() == ip_president[i]) {

					if (PresidentIsSelected) {
						if (selectedPresident != -1) {
							il_president[selectedPresident].setBorder(BorderFactory.createEmptyBorder());
							boo_presidentBorder[selectedPresident] = false;
						}

						if (selectedPresident == i) {
							selectedPresident = -1;
							PresidentIsSelected = false;

						} else {
							selectedPresident = i;
							il_president[i].setBorder(border);
							boo_presidentBorder[i] = true;

							PresidentIsSelected = true;
						}
					} else {

						selectedPresident = i;
						il_president[i].setBorder(border);
						boo_presidentBorder[i] = true;
						PresidentIsSelected = true;
					}

				}

			}

		}
		if (pnl_party.isVisible()) {

			for (int i = 0; i < ip_party.length; i++) {

				if (e.getSource() == ip_party[i]) {

					if (selectedPartiesCounter == 0) {
						SelectedParties[selectedPartiesCounter] = i;
						il_party[i].setBorder(border);
						boo_partyBorder[i] = true;

						for (int k = 0; k < ip_party.length; k++) {
							if (k != i) {
								boo_partyBorder[k] = false;
								il_party[k].setBorder(BorderFactory.createEmptyBorder());
							}
						}
						selectedPartiesCounter = 1;
						SelectedParties[1] = -1;

					} else if (selectedPartiesCounter == 1) {

						if (SelectedParties[0] == i) {
							il_party[i].setBorder(BorderFactory.createEmptyBorder());
							boo_partyBorder[i] = false;
							selectedPartiesCounter--;
							SelectedParties[0] = -1;
						} else {
							SelectedParties[1] = i;
							selectedPartiesCounter = 2;
							il_party[i].setBorder(border);
							boo_partyBorder[i] = true;
						}

					} else {

						if (SelectedParties[0] == i) {
							il_party[i].setBorder(BorderFactory.createEmptyBorder());
							selectedPartiesCounter--;
							SelectedParties[0] = SelectedParties[1];
							boo_partyBorder[i] = false;

							SelectedParties[1] = -1;
						} else if (SelectedParties[1] == i) {
							il_party[i].setBorder(BorderFactory.createEmptyBorder());
							selectedPartiesCounter--;
							SelectedParties[1] = -1;
							boo_partyBorder[i] = false;
						}

					}

				}
			}
		}
		if (pnl_emblem.isVisible())

		{

			for (int i = 0; i < ip_emblem.length; i++) {
				if (e.getSource() == ip_emblem[i]) {

					if (EmblemIsSelected) {
						if (selectedEmblem != -1) {
							il_emblem[selectedEmblem].setBorder(BorderFactory.createEmptyBorder());
							boo_emblemBoder[selectedEmblem] = false;
						}

						if (selectedEmblem == i) {
							selectedEmblem = -1;
							EmblemIsSelected = false;

						} else {
							selectedEmblem = i;
							il_emblem[i].setBorder(border);
							boo_emblemBoder[i] = true;

							EmblemIsSelected = true;
						}
					} else {

						selectedEmblem = i;
						il_emblem[i].setBorder(border);
						boo_emblemBoder[i] = true;
						EmblemIsSelected = true;
					}

				}

			}
		}

		if (pnl_flag.isVisible())

		{

			for (int i = 0; i < ip_flag.length; i++) {
				if (e.getSource() == ip_flag[i]) {

					if (FlagIsSelected) {
						if (selectedFlag != -1) {
							il_flag[selectedFlag].setBorder(BorderFactory.createEmptyBorder());
							boo_flagBorder[selectedFlag] = false;
						}

						if (selectedFlag == i) {
							selectedFlag = -1;
							FlagIsSelected = false;

						} else {
							selectedFlag = i;
							il_flag[i].setBorder(border);
							boo_flagBorder[i] = true;

							FlagIsSelected = true;
						}
					} else {

						selectedFlag = i;
						il_flag[i].setBorder(border);
						boo_flagBorder[i] = true;
						FlagIsSelected = true;
					}

				}

			}
		}

		if (pnl_evaluation.isVisible()) {

			if (e.getSource() == ev_emblem) {

				panel.add(sp_emblem, BorderLayout.CENTER);
				pnl_evaluation.setVisible(false);
				sp_evaluation.setVisible(false);

				sp_emblem.setVisible(true);
				pnl_emblem.setVisible(true);

			} else if (e.getSource() == ev_flag) {
				panel.add(sp_flag, BorderLayout.CENTER);
				pnl_evaluation.setVisible(false);
				sp_evaluation.setVisible(false);
				sp_flag.setVisible(true);
				pnl_flag.setVisible(true);

			} else if (e.getSource() == ev_president) {
				panel.add(sp_president, BorderLayout.CENTER);
				pnl_evaluation.setVisible(false);
				sp_evaluation.setVisible(false);
				sp_president.setVisible(true);
				pnl_president.setVisible(true);

			} else if (e.getSource() == ev_party2 || e.getSource() == ev_party1) {
				panel.add(sp_party, BorderLayout.CENTER);
				sp_evaluation.setVisible(false);
				pnl_evaluation.setVisible(false);
				sp_party.setVisible(true);
				pnl_party.setVisible(true);

			}
			btn_next.setText("Zurück zur Abgabe");
			panel.repaint();
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

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

}