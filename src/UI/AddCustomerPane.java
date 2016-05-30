package UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Control.CustomerControl;

public class AddCustomerPane implements ActionListener {

	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel();

	private JLabel nameLabel = new JLabel("����");
	private JLabel birthLabel = new JLabel("����(4�ڸ�)");
	private JLabel phoneLabel = new JLabel("����ó");

	private JTextField nameInput = new JTextField();
	private JTextField birthInput = new JTextField();
	private JTextField phoneInput = new JTextField();

	private JButton addButton = new JButton("���Խ�û");
	private JButton cancelButton = new JButton("���");

	CustomerControl mCustomerControl;

	public AddCustomerPane(CustomerControl customerControl) {
		mCustomerControl = customerControl;
		panel.setLayout(null);

		nameLabel.setBounds(20, 10, 100, 30);
		birthLabel.setBounds(20, 50, 100, 30);
		phoneLabel.setBounds(20, 90, 100, 30);

		nameInput.setBounds(110, 10, 120, 30);
		birthInput.setBounds(110, 50, 120, 30);
		phoneInput.setBounds(110, 90, 120, 30);

		addButton.setBounds(10, 150, 100, 35);
		cancelButton.setBounds(130, 150, 100, 35);
		addButton.addActionListener(this);
		cancelButton.addActionListener(this);

		panel.add(nameLabel);
		panel.add(birthLabel);
		panel.add(phoneLabel);

		panel.add(nameInput);
		panel.add(birthInput);
		panel.add(phoneInput);

		panel.add(addButton);
		panel.add(cancelButton);

		frame.add(panel);

		frame.setTitle("ȸ�����");
		frame.setSize(280, 280);
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent act) {
		if (act.getSource() == addButton) {
			String name = nameInput.getText();
			String birth = birthInput.getText();
			String phone = phoneInput.getText();
			int res = mCustomerControl.addCustomer(name, birth, phone);

			if (res == 1) {
				JOptionPane.showMessageDialog(null, "��� �Ϸ�", "Info", 2);
				frame.dispose();
			} else {
				JOptionPane.showMessageDialog(null, mCustomerControl.mCurrentErrorMessage, "Error", 2);
			}
		} else if (act.getSource() == cancelButton) {
			frame.dispose();
		}
	}
}
