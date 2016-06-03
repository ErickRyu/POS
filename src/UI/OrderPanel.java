package UI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import Control.POS;

public class OrderPanel implements ActionListener {
	private static JTextArea orderListArea = new JTextArea();
	private static JComboBox<Integer> table_box = new JComboBox<>();
	private static JTextField customerNameField = new JTextField();
	private JButton orderButton = new JButton("주문");
	private JButton cancelButton = new JButton("취소");
	private JButton purchaseButton = new JButton("결제");

	POS mPos;

	public OrderPanel(POS pos) {
		mPos = pos;
	}

	public JPanel orderPane() {
		JPanel panel = POSFrame.getDefaultPanel("주문내역");

		for (int i = 1; i <= 20; i++)
			table_box.addItem(i);

		orderListArea.setBorder(new LineBorder(Color.gray, 2));
		orderListArea.setEditable(false);
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(orderListArea);
		JLabel nameLabel = new JLabel("고객명");
		JLabel tableNumLabel = new JLabel("테이블명");

		nameLabel.setBounds(240, 30, 80, 20);
		customerNameField.setBounds(240, 50, 80, 30);
		
		tableNumLabel.setBounds(240, 85, 80, 20);
		table_box.setBounds(270, 110, 50, 25);
		
		orderButton.setBounds(260, 140, 60, 25);
		cancelButton.setBounds(260, 170, 60, 25);
		purchaseButton.setBounds(260, 200, 60, 25);
		scroll.setBounds(10, 30, 200, 205);

		table_box.addActionListener(this);
		orderButton.addActionListener(this);
		cancelButton.addActionListener(this);
		purchaseButton.addActionListener(this);

		panel.add(nameLabel);
		panel.add(tableNumLabel);
		panel.add(customerNameField);
		panel.add(table_box);
		panel.add(orderButton);
		panel.add(cancelButton);
		panel.add(purchaseButton);
		panel.add(scroll);
		return panel;
	}

	public void actionPerformed(ActionEvent e) {
		int tableNum = (int) table_box.getSelectedItem();
		if (e.getSource() == table_box) {
			mPos.saleControl.rollBack();
			setResultArea(null);
			mPos.saleControl.showOrder(tableNum);
		} else if (e.getSource() == orderButton) {
			if (isLogin()) {
				mPos.saleControl.commit();
				TablePanel.setTableColorYellow(tableNum);
			}
		} else if (e.getSource() == cancelButton) {
			if (isLogin()) {
				TablePanel.setTableColorWhite(tableNum);
				mPos.saleControl.deleteOrder(tableNum);
				setResultArea(null);
			}
		} else if (e.getSource() == purchaseButton) {
			if (isLogin()) {
				if (isOrdered(tableNum)) {
					String customerName = customerNameField.getText();
					mPos.saleControl.purchase(tableNum, customerName);
				}
			}
		}

	}

	public boolean isOrdered(int tableNum) {
		boolean isOrdered = TablePanel.getTableColor(tableNum) == Color.yellow;
		if (!isOrdered) {
			JOptionPane.showMessageDialog(null, (String) "주문을 확인하세요.", "메시지", 2);
		}
		return isOrdered;
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

	public static int getSelectedTableBoxItem() {
		return (int) table_box.getSelectedItem();
	}

	public static void setTableBoxItem(int index) {
		table_box.setSelectedIndex(index - 1);
	}

	public static void setResultArea(String result) {
		orderListArea.setText(result);
	}

	public static String getCustomerName() {
		return customerNameField.getText();
	}

	public static void setCustomerName(String customerName) {
		customerNameField.setText(customerName);
	}

}