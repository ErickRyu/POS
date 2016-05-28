package UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Control.POS;
import Model.Staff;

public class LoginPanel implements ActionListener {

	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel();
	private JLabel nameLabel = new JLabel("이름");
	private JLabel idLabel = new JLabel("사원번호");
	private JTextField nameInput = new JTextField();
	private JTextField idInput = new JTextField();
	private JButton loginButton = new JButton("로그인");

	public POS mPos;

	public LoginPanel(POS pos) {

		panel.setLayout(null);
		mPos = pos;
		nameLabel.setBounds(20, 10, 60, 30);
		idLabel.setBounds(20, 50, 60, 30);
		nameInput.setBounds(100, 10, 80, 30);
		idInput.setBounds(100, 50, 80, 30);
		loginButton.setBounds(200, 25, 80, 35);

		loginButton.addActionListener(this);

		panel.add(nameLabel);
		panel.add(idLabel);
		panel.add(nameInput);
		panel.add(idInput);
		panel.add(loginButton);

		frame.add(panel);

		frame.setTitle("Login");
		frame.setSize(320, 130);
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String name = nameInput.getText();
		int id = Integer.parseInt(idInput.getText());
		login(name, id);
	}

	public void login(String name, int id) {
		Staff staff = mPos.mStaffMap.get(name);

		if (staff != null) {
			if (staff.getName().equals(name)) {
				if (staff.getId() == id) {
					mPos.mLoginStaffName = name;
					frame.setVisible(false);
					return;
				}
			}
		}
		mPos.mCurrentErrorMessage = "Fail to login";
		JOptionPane.showMessageDialog(null, (String) "아이디/사원번호 를 확인하세요.", "메시지", 2);
	}
}
