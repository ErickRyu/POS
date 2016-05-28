package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class TabbedPane extends JPanel implements ActionListener {
	JButton customerSearchButton;
	JButton staffSearchButton;
	JButton menuSearchButton;
	
	JButton customerAddButton;
	JButton staffAddButton;
	JButton menuAddButton;
	JFrame frame;
	public TabbedPane() {
//		super(new GridLayout(1, 1));
//		
//		frame	= new JFrame("TabbedPane");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//		// Add content to the window.
//		frame.add(this, BorderLayout.CENTER);
//
//		// Display the window.
//		frame.pack();
//		frame.setSize(400, 400);
//		frame.setVisible(true);
//		createAndShowGUI();
		
	}

	protected JComponent customerPane() {
		JPanel panel = new JPanel();

		panel.setFont(new Font("필기체", 1, 12));
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		TitledBorder title = BorderFactory.createTitledBorder(loweredbevel);
		title.setTitlePosition(TitledBorder.ABOVE_TOP);
		panel.setBorder(title);
		panel.setLayout(null);
//		panel.setBounds(10,10,,280);
//		panel.setSize(200,300);
		JTextArea resul_area = new JTextArea();
		JTextField name_field = new JTextField();
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(resul_area);
		
		customerAddButton = new JButton("가입");
		customerSearchButton = new JButton("조회");
		JLabel name_label = new JLabel("고객명");
		
		customerAddButton.addActionListener(this);
		customerSearchButton.addActionListener(this);
		
		name_label.setBounds(10,10,80,30);
		name_field.setBounds(10,40,80,30);
        customerAddButton.setBounds(180,40,65,30);
        customerSearchButton.setBounds(270,40,65,30);
        scroll.setBounds(10,80,330,240);

        panel.add(name_label);
		panel.add(name_field);
		panel.add(customerAddButton);
		panel.add(customerSearchButton);
		panel.add(scroll);

		return panel;
	}

	protected JComponent salesPane() {
		JPanel panel = new JPanel();

		panel.setFont(new Font("필기체", 1, 12));
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		TitledBorder title = BorderFactory.createTitledBorder(loweredbevel);
		title.setTitlePosition(TitledBorder.ABOVE_TOP);
		panel.setBorder(title);
		panel.setLayout(null);
		panel.setBounds(380,80,490,280);
		JComboBox<String> check_box = new JComboBox<String>();
		check_box.addItem("2016-05-26");
		
		JTextArea resul_area = new JTextArea();
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(resul_area);
		
		JLabel name_label = new JLabel("기간");
		name_label.setBounds(10,40,80,30);
		check_box.setBounds(60,40,110,30);
        scroll.setBounds(10,80,330,240);

        panel.add(name_label);
		panel.add(check_box);
		panel.add(scroll);

		return panel;
	}

	protected JComponent staffPane() {
		JPanel panel = new JPanel();

		panel.setFont(new Font("필기체", 1, 12));
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		TitledBorder title = BorderFactory.createTitledBorder(loweredbevel);
		title.setTitlePosition(TitledBorder.ABOVE_TOP);
		panel.setBorder(title);
		panel.setLayout(null);
		panel.setBounds(380,80,490,280);
		
		JTextArea resul_area = new JTextArea();
		JTextField name_field = new JTextField();
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(resul_area);
		
		staffAddButton = new JButton("직원등록");
		staffAddButton.setMargin(new Insets(0, 0, 0, 0));
		staffSearchButton = new JButton("조회");
		staffSearchButton.setMargin(new Insets(0, 0, 0, 0));
		
		staffAddButton.addActionListener(this);
		staffSearchButton.addActionListener(this);
		
		JLabel name_label = new JLabel("직원명");
		name_label.setBounds(10,10,80,30);
		name_field.setBounds(10,40,80,30);
		staffAddButton.setBounds(180,40,85,30);
		staffSearchButton.setBounds(270,40,65,30);
        scroll.setBounds(10,80,330,240);

        panel.add(name_label);
		panel.add(name_field);
		panel.add(staffAddButton);
		panel.add(staffSearchButton);
		panel.add(scroll);

		return panel;
	}

	protected JComponent menuPane() {
		JPanel panel = new JPanel();

		panel.setFont(new Font("필기체", 1, 12));
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		TitledBorder title = BorderFactory.createTitledBorder(loweredbevel);
		title.setTitlePosition(TitledBorder.ABOVE_TOP);
		panel.setBorder(title);
		panel.setLayout(null);
		panel.setBounds(380,80,490,280);
		
		JTextArea resul_area = new JTextArea();
		JTextField name_field = new JTextField();
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(resul_area);
		
		menuAddButton = new JButton("메뉴등록");
		menuAddButton.setMargin(new Insets(0, 0, 0, 0));
		menuSearchButton = new JButton("조회");
		menuSearchButton.setMargin(new Insets(0, 0, 0, 0));
		JLabel name_label = new JLabel("메뉴명");
		
		
		menuAddButton.addActionListener(this);
		menuSearchButton.addActionListener(this);
		
		name_label.setBounds(10,10,80,30);
		name_field.setBounds(10,40,150,30);
        menuAddButton.setBounds(180,40,85,30);
        menuSearchButton.setBounds(270,40,65,30);
        scroll.setBounds(10,80,330,240);

        panel.add(name_label);
		panel.add(name_field);
		panel.add(menuAddButton);
		panel.add(menuSearchButton);
		panel.add(scroll);

		return panel;
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()== customerAddButton){
			new AddCustomerPane();
		}else if(e.getSource() == staffAddButton){
			new AddStaffPane();
		}else if(e.getSource() == menuAddButton){
			new AddMenuPane();
		}
	}
	public JTabbedPane createAndShowGUI() {
		// Create and set up the window.

		JTabbedPane tabbedPane = new JTabbedPane();

		JComponent panel1 = customerPane();
		tabbedPane.addTab("고객", panel1);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JComponent panel2 = salesPane();
		tabbedPane.addTab("매출", panel2);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		JComponent panel3 = staffPane();
		tabbedPane.addTab("직원", panel3);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		JComponent panel4 = menuPane();
		panel4.setPreferredSize(new Dimension(410, 50));
		tabbedPane.addTab("메뉴", panel4);
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

		// Add the tabbed pane to this panel.
		add(tabbedPane);

		// The following line enables to use scrolling tabs.
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		return tabbedPane;
	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				// Turn off metal's use of bold fonts
//				UIManager.put("swing.boldMetal", Boolean.FALSE);
////				createAndShowGUI();
//			}
//		});
		new TabbedPane();
	}
}