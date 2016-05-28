package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class OrderPanel implements ActionListener {
	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel();

	private JTextArea check_area = new JTextArea();
	private JComboBox<String> check_box = new JComboBox<String>();
	private JComboBox<String> table_box = new JComboBox<String>();
	private JTextField model_field = new JTextField();
	private JButton orderButton = new JButton("주문");
	private JButton cancelButton = new JButton("취소");
	private JButton purchaseButton = new JButton("결제");
	

	public OrderPanel() {
//		frame.setVisible(false);
//		frame = new JFrame();
//		
//
//		frame.add(orderPane());
//
//		frame.setTitle("PC Store");
//		frame.setSize(400, 350);
//		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public JPanel orderPane(){
		panel = new JPanel();
		panel.setFont(new Font("필기체", 1, 12));
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		TitledBorder title = BorderFactory.createTitledBorder(loweredbevel, "주문내역");
		title.setTitlePosition(TitledBorder.ABOVE_TOP);
		panel.setBorder(title);
		panel.setLayout(null);

		check_box.addItem("PC");
		check_box.addItem("Laptop");
		check_box.addItem("Printer");

		for (int i = 1; i <= 10; i++)
			table_box.addItem(String.valueOf(i));

		check_area.setBorder(new LineBorder(Color.gray, 2));
		check_area.setEditable(false);

		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(check_area);
		
		JLabel nameLabel = new JLabel("고객명");
		nameLabel.setBounds(240,30,80,20);
		model_field.setBounds(240, 50, 80, 30);
		
		JLabel tableNumLabel = new JLabel("테이블명");
		
		tableNumLabel.setBounds(240,90,80,20);
		table_box.setBounds(270, 120, 50, 30);
		
		orderButton.setBounds(260, 170, 60, 25);
		cancelButton.setBounds(260, 200, 60, 25);
		purchaseButton.setBounds(260, 230, 60, 25);
		scroll.setBounds(10, 30, 200, 230);

		check_area.setText("안심스테이크\t21000");
		// Checkbox로 테이블명 선택 시 테이블 값 출력
		check_box.addActionListener(this);

		// 구매버튼 클릭 시 구매작업 수행
//		buyButton.addActionListener(this);
		
		panel.add(nameLabel);
		panel.add(tableNumLabel);
		panel.add(check_box);
		panel.add(model_field);
		panel.add(table_box);
		panel.add(orderButton);
		panel.add(cancelButton);
		panel.add(purchaseButton);
		panel.add(scroll);
		return panel;
	}
	public void actionPerformed(ActionEvent e) {
		// 구매버튼 클릭 시 구매작업 수행
	}

	public static void main(String[] argv) {
		new OrderPanel();
	}
}