package Control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DB.JDBC;
import UI.POSFrame;

public class POS {
	// member ���� �տ��� m�� ���̴� ��.

	/* Save name who login */
	public String mLoginStaffName;
	public String mLoginStaffPosition;
	public String mCurrentErrorMessage = "";

	public CustomerControl customerControl;
	public MenuControl menuControl;
	public SaleControl saleControl;
	public StaffControl staffControl;

	public JDBC jdbc;

	Connection db;

	public POS() {
		jdbc = new JDBC(this);

		customerControl = new CustomerControl(this);
		menuControl = new MenuControl(this);
		saleControl = new SaleControl(this);
		staffControl = new StaffControl(this);

		db = jdbc.db;
		new POSFrame(this);

	}

	public int login(String name, int id) {
		int res = -1;
		String sqlStr = "select name, id, position from staff where name = '" + name + "' and id='" + id + "'";
		try {
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			mLoginStaffName = name;
			mLoginStaffPosition = rs.getString("position");
			POSFrame.changeLoginButton();
			rs.close();
			stmt.close();

			res = 1;
		} catch (SQLException e) {
			mCurrentErrorMessage = "���̵�/�����ȣ �� Ȯ���ϼ���.";
		}
		return res;
	}

	public void logout() {
		mLoginStaffName = null;
		mLoginStaffPosition = null;
	}

	public boolean isLogin() {
		return mLoginStaffName != null;
	}

	public boolean isSupervisor() {
		return mLoginStaffPosition != null && mLoginStaffPosition.equals("Supervisor");
	}

	public static int GetStringLength(String values) {
		char[] obj = values.toCharArray(); // �Է� string�� char[]�� ����
		int maxB = 0; // ����Ʈ ���̸� ����� ����

		for (int i = 0; i < obj.length; i++) // ������ ���鼭
		{
			// ���� 1����Ʈ�� �����´�.
			byte oF = (byte) ((obj[i] & 0xff00) >> 8);
			// ���� 1����Ʈ�� �����´�.
			// byte oB = (byte) (obj[i] & 0x00ff);

			if (oF == 0) // ���� 1����Ʈ�� 0�̸� ���ĺ��̴�.
				maxB++;
			else
				maxB += 3;
		}
		return maxB;
	}

}
