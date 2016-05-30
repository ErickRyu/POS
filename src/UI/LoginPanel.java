package UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Control.POS;

public class LoginPanel implements ActionListener {

	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel();
	private JLabel nameLabel = new JLabel("이름");
	private JLabel idLabel = new JLabel("사원번호");
	private JTextField nameInput = new JTextField();
	private JTextField idInput = new JTextField();
	private JButton loginButton = new JButton("로그인");
	public static String mCurrentErrorMessage = "";
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

	public void actionPerformed(ActionEvent act) {
		try {
			String name = nameInput.getText();
			int id = Integer.parseInt(idInput.getText());
			int res = mPos.login(name, id);
			if (res == 1) {
				JOptionPane.showMessageDialog(null, "로그인 성공", "Info", 2);
				frame.dispose();
			} else {
				JOptionPane.showMessageDialog(null, (String) mPos.mCurrentErrorMessage, "메시지", 2);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, (String) "사원번호를 확인하세요 ", "Error", 2);
		}
	}
}
