package UI;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import Control.POS;

public class POSFrame implements ActionListener{
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
	public static JMenuItem loginItem;
	
	public POSFrame(POS pos){
		mPos = pos;
		
		frame = new JFrame();
		
		posPanel = new JPanel(null);
		posPanel.setBounds(10, 10, 700, 700);
		
		tablePanel = new TablePanel(mPos);
		JPanel tablePane = tablePanel.tablePane();
		tablePane.setSize(300, 300);
		tablePane.setBounds(10, 10, 310, 280);

		orderPanel = new OrderPanel(mPos);
		JPanel orderPane = orderPanel.orderPane();
		orderPane.setBounds(330, 10, 350, 280);

		menuPanel = new MenuPanel(mPos);
		JPanel menuPane = menuPanel.menuPane();
		menuPane.setBounds(10, 300, 310, 390);

		tabbed = new TabbedPane(mPos);
		JTabbedPane tabbedPane = tabbed.createAndShowGUI();
		tabbedPane.setBounds(330, 300, 350, 390);

		posPanel.add(tablePane);
		posPanel.add(orderPane);
		posPanel.add(menuPane);
		posPanel.add(tabbedPane);
		
		setMenuBar();
		
		frame.setJMenuBar(menuBar);
		frame.add(posPanel);
		frame.setTitle("POS");
		frame.setSize(710, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static JPanel getDefaultPanel() {
		JPanel panel = new JPanel();

		panel.setFont(new Font("« ±‚√º", 1, 12));
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		TitledBorder title = BorderFactory.createTitledBorder(loweredbevel);
		title.setTitlePosition(TitledBorder.ABOVE_TOP);

		panel.setBorder(title);
		panel.setLayout(null);
		return panel;
	}
	
	private void setMenuBar(){
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
		if(e.getActionCommand().equals("Open")){
			mPos.jdbc.openData();
		}else if(e.getActionCommand().equals("Log in")){
			new LoginPanel(mPos);
		}else if(e.getActionCommand().equals("Log out")){
			mPos.logout();
			changeLoginButton();
		}
	}
	public static void changeLoginButton(){
		if(loginItem.getText().equals("Log in")){
			loginItem.setText("Log out");
		}else{
			loginItem.setText("Log in");
		}
	}
}
