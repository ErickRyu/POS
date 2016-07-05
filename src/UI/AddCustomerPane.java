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

	private JLabel nameLabel = new JLabel("고객명");
	private JLabel birthLabel = new JLabel("생일(4자리)");
	private JLabel phoneLabel = new JLabel("연락처");

	private JTextField nameInput = new JTextField();
	private JTextField birthInput = new JTextField();
	private JTextField phoneInput = new JTextField();

	private JButton addButton = new JButton("가입신청");
	private JButton cancelButton = new JButton("취소");

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

		frame.setTitle("회원등록");
		frame.setSize(280, 280);
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent act) {
		if (act.getSource() == addButton) {
			String name = nameInput.getText();
			String birth = birthInput.getText();
			String phone = phoneInput.getText();
			String errorMessage = "숫자를 입력하세요.";
			try{
				Integer.parseInt(birth);
				Integer.parseInt(phone);
				if(birth.length() != 4){
					errorMessage = "생일은 4자리를 입력하세요.\n ex)5월 7일  -> 0507";		
					throw new Exception();
				}
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, (String) errorMessage, "메시지", 2);
				return;
			}
			int res = mCustomerControl.addCustomerDB(name, birth, phone);

			if (res == 1) {
				JOptionPane.showMessageDialog(null, "등록 완료", "메시지", 2);
				frame.dispose();
			} else {
				JOptionPane.showMessageDialog(null, mCustomerControl.mCurrentErrorMessage, "메시지", 2);
			}
		} else if (act.getSource() == cancelButton) {
			frame.dispose();
		}
	}
}
