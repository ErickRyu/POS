package UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Control.MenuControl;

public class AddMenuPane implements ActionListener {

	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel();

	private JLabel nameLabel = new JLabel("메뉴명");
	private JLabel priceLabel = new JLabel("가격");

	private JTextField nameInput = new JTextField();
	private JTextField priceInput = new JTextField();

	private JButton addButton = new JButton("등록");
	private JButton cancelButton = new JButton("취소");

	MenuControl mMenuControl;

	public AddMenuPane(MenuControl menuControl) {
		mMenuControl = menuControl;

		panel.setLayout(null);

		nameLabel.setBounds(20, 10, 100, 30);
		priceLabel.setBounds(20, 50, 100, 30);

		nameInput.setBounds(110, 10, 120, 30);
		priceInput.setBounds(110, 50, 120, 30);

		addButton.setBounds(10, 100, 100, 35);
		cancelButton.setBounds(130, 100, 100, 35);
		addButton.addActionListener(this);
		cancelButton.addActionListener(this);

		panel.add(nameLabel);
		panel.add(priceLabel);

		panel.add(nameInput);
		panel.add(priceInput);

		panel.add(addButton);
		panel.add(cancelButton);

		frame.add(panel);

		frame.setTitle("메뉴등록");
		frame.setSize(280, 200);
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			try {
				String name = nameInput.getText();
				int price = Integer.parseInt(priceInput.getText());

				int res = mMenuControl.addMenuDB(name, price);
				if (res == 1) {
					JOptionPane.showMessageDialog(null, (String) "등록완료", "메시지", 2);
					frame.dispose();
				} else {
					JOptionPane.showMessageDialog(null, (String) mMenuControl.mCurrentErrorMessage, "메시지", 2);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, (String) "다시 입력하세요.", "메시지", 2);
			}
		} else if (e.getSource() == cancelButton) {
			frame.setVisible(false);
		}
	}
}
