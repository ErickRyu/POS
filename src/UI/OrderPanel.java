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
	private static Connection dbTest;
	private String username;
	private String password;
	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel();

	private JTextField idInput = new JTextField();
	private JPasswordField pwdInput = new JPasswordField();
	private JButton loginButton = new JButton("로그인");
	private JTextArea check_area = new JTextArea();
	private JComboBox<String> check_box = new JComboBox<String>();
	private JComboBox<String> table_box = new JComboBox<String>();
	private JTextField model_field = new JTextField();
	private JButton orderButton = new JButton("주문");
	private JButton cancelButton = new JButton("취소");
	private JButton purchaseButton = new JButton("결제");
	

	private int primary_key = 0;

	public OrderPanel() {
		frame.setVisible(false);
		frame = new JFrame();
		panel = new JPanel();

		panel.setFont(new Font("필기체", 1, 12));
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		TitledBorder title = BorderFactory.createTitledBorder(loweredbevel, "주문내역");
		title.setTitlePosition(TitledBorder.ABOVE_TOP);
		panel.setBorder(title);
//		panel.setBounds(380, 80, 490, 280);
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
		nameLabel.setBounds(280,30,80,20);
		model_field.setBounds(280, 50, 80, 30);
		
		JLabel tableNumLabel = new JLabel("테이블명");
		tableNumLabel.setBounds(280,100,80,20);
		table_box.setBounds(310, 130, 50, 30);
		
		orderButton.setBounds(300, 200, 60, 25);
		cancelButton.setBounds(300, 230, 60, 25);
		purchaseButton.setBounds(300, 260, 60, 25);
		scroll.setBounds(10, 30, 260, 260);

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

		frame.add(panel);

		frame.setTitle("PC Store");
		frame.setSize(400, 350);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		// 구매버튼 클릭 시 구매작업 수행
	}

	private void insertTransaction() throws SQLException {
		String specification = "";
		String modelname = model_field.getText();
		String count = (String) table_box.getSelectedItem();
		String Tablename = ((String) check_box.getSelectedItem()).toUpperCase();
		int price;

		try {
			String sqlStr = "select count(tnumber) from transaction";
			PreparedStatement stmt = dbTest.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			primary_key = rs.getInt("count(tnumber)");
			primary_key++;
			int tmodel = Integer.parseInt(modelname);
			int tcount = Integer.parseInt(count);

			sqlStr = "select price from " + Tablename.toUpperCase() + " where model = " + modelname;
			stmt = dbTest.prepareStatement(sqlStr);
			rs = stmt.executeQuery();
			rs.next();
			price = rs.getInt("price");
			int tprice = price * tcount;

			sqlStr = "insert into transaction values(" + primary_key + ", " + tmodel + ", " + tcount + ", " + tprice
					+ ")";
			stmt = dbTest.prepareStatement(sqlStr);
			stmt.executeQuery();

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, (String) "구매를 할 수 없습니다", "메시지", 2);
		}

	}

	private void showTable() throws SQLException {
		String specification = "";

		String sqlStr = "select count(column_name) num from cols where table_name = '"
				+ ((String) check_box.getSelectedItem()).toUpperCase() + "'";
		PreparedStatement stmt = dbTest.prepareStatement(sqlStr);
		ResultSet rs = stmt.executeQuery();

		rs.next();
		int number = rs.getInt("num");
		String[] tables = new String[number];

		sqlStr = "select column_name from cols where table_name = '"
				+ ((String) check_box.getSelectedItem()).toUpperCase() + "'";

		stmt = dbTest.prepareStatement(sqlStr);
		rs = stmt.executeQuery();

		for (number = 0; rs.next(); number++) {
			tables[number] = rs.getString("column_name");
			specification += tables[number] + '\t';
		}
		for (specification += "\n"; number > 0; number--) {
			specification += "--------------------";
		}
		specification += "\n";

		sqlStr = "select * from " + (String) check_box.getSelectedItem();
		stmt = dbTest.prepareStatement(sqlStr);
		rs = stmt.executeQuery();

		for (number = 0; rs.next(); number++) {
			for (int i = 0; i < tables.length; i++) {
				specification += rs.getString(tables[i]) + '\t';
			}
			specification += "\n";
		}
		check_area.setText(specification);
		rs.close();
		stmt.close();
	}

	public static void main(String[] argv) {
		new OrderPanel();
	}
}