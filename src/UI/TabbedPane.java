package UI;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Control.CustomerControl;
import Control.MenuControl;
import Control.POS;
import Control.SaleControl;
import Control.StaffControl;

public class TabbedPane extends JPanel implements ActionListener {
	JButton customerSearchButton;
	JButton staffSearchButton;
	JButton menuSearchButton;

	JButton customerAddButton;
	JButton staffAddButton;
	JButton menuAddButton;
	JFrame frame;

	/* Refactoring after */
	public static JTextArea customerResultArea = new JTextArea();
	public static JTextField customerNameField;

	public static JTextArea mMenuResultArea = new JTextArea();
	public static JTextField mMenuNameField = new JTextField();

	public static JTextArea mStaffResultArea = new JTextArea();
	public static JTextField mStaffNameField = new JTextField();

	public static JComboBox<String> check_box;

	public static JTextArea mSaleResultArea = new JTextArea();

	static POS mPos;
	CustomerControl customerControl;
	MenuControl menuControl;
	StaffControl staffControl;
	SaleControl saleControl;
	static Connection db;

	public TabbedPane(POS pos) {
		mPos = pos;
		customerControl = mPos.customerControl;
		menuControl = mPos.menuControl;
		staffControl = mPos.staffControl;
		saleControl = mPos.saleControl;
		db = mPos.jdbc.db;
	}

	public JTabbedPane createAndShowGUI() {
		// Create and set up the window.

		JTabbedPane tabbedPane = new JTabbedPane();

		JComponent panel1 = customerPane();
		tabbedPane.addTab("고객", panel1);

		JComponent panel2 = salesPane();
		tabbedPane.addTab("매출", panel2);

		JComponent panel3 = staffPane();
		tabbedPane.addTab("직원", panel3);

		JComponent panel4 = menuPane();
		tabbedPane.addTab("메뉴", panel4);

		add(tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		ChangeListener changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				setCustomerResultArea(null);
				setMenuResultArea(null);
				setStaffResultArea(null);
				setSaleResultArea(null);
				customerNameField.setText(null);
				mMenuNameField.setText(null);
				mStaffNameField.setText(null);
				
			}
		};
		tabbedPane.addChangeListener(changeListener);
		return tabbedPane;
	}

	public static void setCustomerResultArea(String result) {
		customerResultArea.setText(result);
	}

	public static void setMenuResultArea(String result) {
		mMenuResultArea.setText(result);
	}

	public static void setStaffResultArea(String result) {
		mStaffResultArea.setText(result);
	}

	public static void setSaleResultArea(String result) {
		mSaleResultArea.setText(result);
	}

	protected JComponent customerPane() {
		JPanel panel = POSFrame.getDefaultPanel();

		/* Have to refactoring this area */

		customerNameField = new JTextField();
		/* End of must refactoring area */

		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(customerResultArea);

		customerAddButton = new JButton("가입");
		customerSearchButton = new JButton("조회");
		JLabel name_label = new JLabel("고객명");

		customerAddButton.addActionListener(this);
		customerSearchButton.addActionListener(this);

		name_label.setBounds(10, 10, 80, 30);
		customerNameField.setBounds(10, 40, 80, 30);
		customerAddButton.setBounds(180, 40, 65, 30);
		customerSearchButton.setBounds(270, 40, 65, 30);
		scroll.setBounds(10, 80, 330, 240);

		panel.add(name_label);
		panel.add(customerNameField);
		panel.add(customerAddButton);
		panel.add(customerSearchButton);
		panel.add(scroll);

		return panel;
	}

	protected JComponent salesPane() {
		JPanel panel = POSFrame.getDefaultPanel();

		panel.setBounds(380, 80, 490, 280);
		check_box = new JComboBox<String>();
		updateSaleTabBox();

		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(mSaleResultArea);

		JLabel name_label = new JLabel("기간");
		name_label.setBounds(10, 40, 80, 30);
		check_box.setBounds(60, 40, 110, 30);
		check_box.addActionListener(this);
		scroll.setBounds(10, 80, 330, 240);

		panel.add(name_label);
		panel.add(check_box);
		panel.add(scroll);

		return panel;
	}

	public static void updateSaleTabBox() {
		try {
			check_box.removeAllItems();
			String sqlStr = "select day from sale";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Date date = rs.getDate("day");
				check_box.addItem(date.toString());
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected JComponent staffPane() {
		JPanel panel = POSFrame.getDefaultPanel();

		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(mStaffResultArea);

		staffAddButton = new JButton("직원등록");
		staffAddButton.setMargin(new Insets(0, 0, 0, 0));
		staffSearchButton = new JButton("조회");
		staffSearchButton.setMargin(new Insets(0, 0, 0, 0));

		staffAddButton.addActionListener(this);
		staffSearchButton.addActionListener(this);

		JLabel name_label = new JLabel("직원명");
		name_label.setBounds(10, 10, 80, 30);
		mStaffNameField.setBounds(10, 40, 80, 30);
		staffAddButton.setBounds(180, 40, 85, 30);
		staffSearchButton.setBounds(270, 40, 65, 30);
		scroll.setBounds(10, 80, 330, 240);

		panel.add(name_label);
		panel.add(mStaffNameField);
		panel.add(staffAddButton);
		panel.add(staffSearchButton);
		panel.add(scroll);

		return panel;
	}

	protected JComponent menuPane() {
		JPanel panel = POSFrame.getDefaultPanel();

		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(mMenuResultArea);

		menuAddButton = new JButton("메뉴등록");
		menuAddButton.setMargin(new Insets(0, 0, 0, 0));
		menuSearchButton = new JButton("조회");
		menuSearchButton.setMargin(new Insets(0, 0, 0, 0));
		JLabel name_label = new JLabel("메뉴명");

		menuAddButton.addActionListener(this);
		menuSearchButton.addActionListener(this);

		name_label.setBounds(10, 10, 80, 30);
		mMenuNameField.setBounds(10, 40, 150, 30);
		menuAddButton.setBounds(180, 40, 85, 30);
		menuSearchButton.setBounds(270, 40, 65, 30);
		scroll.setBounds(10, 80, 330, 240);

		panel.add(name_label);
		panel.add(mMenuNameField);
		panel.add(menuAddButton);
		panel.add(menuSearchButton);
		panel.add(scroll);

		return panel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == customerAddButton) {
			if (isSupervisor())
				new AddCustomerPane(customerControl);
		} else if (e.getSource() == staffAddButton) {
			if (isSupervisor())
				new AddStaffPane(staffControl);
		} else if (e.getSource() == menuAddButton) {
			if (isSupervisor())
				new AddMenuPane(menuControl);
		} else if (e.getSource() == customerSearchButton) {
			String name = customerNameField.getText();
			if (isLogin())
				customerControl.searchAndShowCustomer(name);
		} else if (e.getSource() == staffSearchButton) {
			String name = mStaffNameField.getText();
			if (isLogin())
				staffControl.searchAndShowStaff(name);
		} else if (e.getSource() == menuSearchButton) {
			String name = mMenuNameField.getText();
			if (isLogin())
				menuControl.searchAndShowMenu(name);
		} else if (e.getSource() == check_box) {
			setSaleResultArea(null);
			String date = (String) check_box.getSelectedItem();
			if (date != null)
				saleControl.searchSales(date);
		}
	}

	public boolean isLogin() {
		boolean res = mPos.isLogin();
		if (!res)
			JOptionPane.showMessageDialog(null, (String) "로그인 하세요.", "메시지", 2);
		return res;
	}

	public boolean isSupervisor() {
		boolean res = isLogin();
		if (!res)
			return res;
		res = mPos.isSupervisor();
		if (!res)
			JOptionPane.showMessageDialog(null, (String) "Supervisor만 등록이 가능합니다.", "메시지", 2);
		return res;
	}
}