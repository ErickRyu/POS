package UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Control.StaffControl;

public class AddStaffPane implements ActionListener {

	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel();

	private JLabel nameLabel = new JLabel("직원명");
	private JLabel positionLabel = new JLabel("직급");

	private JTextField nameInput = new JTextField();
	private JComboBox<String> positionInput = new JComboBox<>();

	private JButton addButton = new JButton("등록");
	private JButton cancelButton = new JButton("취소");

	StaffControl mStaffControl;

	public AddStaffPane(StaffControl staffControl) {
		mStaffControl = staffControl;
		panel.setLayout(null);

		positionInput.addItem("Staff");
		positionInput.addItem("Supervisor");
		nameLabel.setBounds(20, 10, 100, 30);
		positionLabel.setBounds(20, 50, 100, 30);

		nameInput.setBounds(110, 10, 120, 30);
		positionInput.setBounds(110, 50, 120, 30);

		addButton.setBounds(10, 100, 100, 35);
		cancelButton.setBounds(130, 100, 100, 35);
		addButton.addActionListener(this);
		cancelButton.addActionListener(this);

		panel.add(nameLabel);
		panel.add(positionLabel);

		panel.add(nameInput);
		panel.add(positionInput);

		panel.add(addButton);
		panel.add(cancelButton);

		frame.add(panel);

		frame.setTitle("직원등록");
		frame.setSize(280, 200);
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			String name = nameInput.getText();
			String position = positionInput.getSelectedItem().toString();
			System.out.println("selected position name is : " + position);
			int res = mStaffControl.addStaffDB(name, position);
			if (res == 1) {
				JOptionPane.showMessageDialog(null, (String) "등록완료", "메시지", 2);
				frame.dispose();
			} else {
				JOptionPane.showMessageDialog(null, (String) mStaffControl.mCurrentErrorMessage, "메시지", 2);
			}
		} else if (e.getSource() == cancelButton) {
			frame.setVisible(false);
		}
	}

}
