package UI;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import Control.POS;

public class POSFrame implements ActionListener {
	JFrame frame;
	JPanel posPanel;
	POS mPos;
	TablePanel tablePanel;
	OrderPanel orderPanel;
	MenuPanel menuPanel;
	TabbedPane tabbed;
	JMenuBar menuBar = new JMenuBar();
	JMenu menu;
	JMenuItem openItem;
	
	final JFileChooser fc = new JFileChooser();
	public static JMenuItem loginItem;

	public POSFrame(POS pos) {
		mPos = pos;

		/* Set frame */
		frame = new JFrame("식당 관리 시스템");
		frame.setLayout(null);
		
		/* Set main panel */
		posPanel = new JPanel(null);
		posPanel.setBounds(10, 50, 700, 640);
		
		/* Set table panel */
		tablePanel = new TablePanel(mPos);
		JPanel tablePane = tablePanel.tablePane();
		tablePane.setSize(300, 300);
		tablePane.setBounds(10, 10, 310, 245);

		/* Set order panel */
		orderPanel = new OrderPanel(mPos);
		JPanel orderPane = orderPanel.orderPane();
		orderPane.setBounds(330, 10, 350, 245);

		/* Reduce 25 */
		/* Set menu panel */
		menuPanel = new MenuPanel(mPos);
		JPanel menuPane = menuPanel.menuPane();
		menuPane.setBounds(10, 260, 310, 390);

		/* Set tabbed panel that contains Add/Search components */
		tabbed = new TabbedPane(mPos);
		JTabbedPane tabbedPane = tabbed.createAndShowGUI();
		tabbedPane.setBounds(330, 260, 350, 390);

		/* Add main panel's components */
		posPanel.add(tablePane);
		posPanel.add(orderPane);
		posPanel.add(menuPane);
		posPanel.add(tabbedPane);
		
		/* Set top most text */
		JPanel tmpPanel = getDefaultPanel(null);
		JLabel label = new JLabel("식당 주문 관리", SwingConstants.CENTER);
		label.setFont(new Font("필기체", 1, 30));
		label.setBounds(0,10,660,30);
		tmpPanel.add(label);
		tmpPanel.setBounds(20,10,667,50);
		
		/* Menu bar setting */
		setMenuBar();
		
		/* Add all components */
		frame.add(tmpPanel);
		frame.setJMenuBar(menuBar);
		frame.add(posPanel);
		frame.setSize(710, 750);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static JPanel getDefaultPanel(String titleStr) {
		JPanel panel = new JPanel();

		panel.setFont(new Font("필기체", 1, 12));
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		TitledBorder title = BorderFactory.createTitledBorder(loweredbevel, titleStr);
		title.setTitlePosition(TitledBorder.ABOVE_TOP);

		panel.setBorder(title);
		panel.setLayout(null);
		return panel;
	}

	private void setMenuBar() {
		menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_M);
		menuBar.add(menu);

		openItem = new JMenuItem("Open", KeyEvent.VK_O);
		menu.add(openItem);
		openItem.addActionListener(this);

		loginItem = new JMenuItem("Log in", KeyEvent.VK_L);
		menu.add(loginItem);
		loginItem.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("Open")) {
			if (e.getSource() == openItem) {
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        int returnVal = fc.showOpenDialog(this.posPanel);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            mPos.jdbc.openData(file);
		        } else {
		        }
		   } 
			
			
		} else if (e.getActionCommand().equals("Log in")) {
			new LoginPanel(mPos);
		} else if (e.getActionCommand().equals("Log out")) {
			mPos.logout();
			changeLoginButton();
		}
	}

	public static void changeLoginButton() {
		if (loginItem.getText().equals("Log in")) {
			loginItem.setText("Log out");
		} else {
			loginItem.setText("Log in");
		}
	}

}
