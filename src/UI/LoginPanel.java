package UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel implements ActionListener {

	private static Connection dbTest;

	private String username;
	private String password ;
	private String loginName;
	private String loginPw;
	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel();
	private JLabel idLabel = new JLabel("아이디");
	private JLabel pwdLabel = new JLabel("비밀번호");
	private JTextField idInput = new JTextField();
	private JPasswordField pwdInput = new JPasswordField();
	private JButton loginButton = new JButton("로그인");

	public static void main(String[] args) {
		new LoginPanel();
	}

	public LoginPanel() {

		panel.setLayout(null);

		idLabel.setBounds(20, 10, 60, 30);
		pwdLabel.setBounds(20, 50, 60, 30);
		idInput.setBounds(100, 10, 80, 30);
		pwdInput.setBounds(100, 50, 80, 30);
		loginButton.setBounds(200, 25, 80, 35);

		loginButton.addActionListener(this);

		panel.add(idLabel);
		panel.add(pwdLabel);
		panel.add(idInput);
		panel.add(pwdInput);
		panel.add(loginButton);

		frame.add(panel);

		frame.setTitle("Login");
		frame.setSize(320, 130);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			username = idInput.getText();
			password = new String(pwdInput.getPassword());
			connectDB();
		}
	}

	private void connectDB() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			dbTest = DriverManager.getConnection("jdbc:oracle:thin:" + "@localhost:1521:XE", username, password);
            JOptionPane.showMessageDialog(null, (String)"로그인 성공","메시지",2);
            frame.dispose();

		} catch (SQLException e) {
            JOptionPane.showMessageDialog(null, (String)"ID, PW를 확인해주세요 ","메시지",2);
		} catch (Exception e) {
			System.out.println("Exception:" + e);
		}
	}

	public void executeSQL() {
		try {
			String sqlStr = "select id from userdatabase " + "where id='" + loginName + "' and pw='" + loginPw + "'";
			PreparedStatement stmt = dbTest.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				System.out.println("Login");
			} else {
				System.out.println("Fail");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("SQLException:" + e);
			e.printStackTrace();
		}
	}

}
