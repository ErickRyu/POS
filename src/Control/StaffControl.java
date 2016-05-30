package Control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import UI.TabbedPane;

public class StaffControl {
	POS mPos;
	String mName;
	String mPosition;
	String mSales;
	Connection db;
	public String mCurrentErrorMessage = "";
	static int mStaffId = 1000;

	public StaffControl(POS pos) {
		mPos = pos;
		db = mPos.jdbc.db;
	}

	public int searchStaff(String searchName) {

		int res = -1;
		try {
			String sqlStr = "select * from staff where name = '" + searchName + "'";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			mName = rs.getString("name");
			mPosition = rs.getString("position");
			mSales = rs.getString("sales");
			res = 1;

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			mCurrentErrorMessage = "�˻� ��� ����.";
		}
		return res;
	}

	public void searchAndShowStaff(String searchName) {
		int res = searchStaff(searchName);
		if (res == 1) {
			String result = "";
			result += "������ : " + mName + "\n";
			result += "��    �� : " + mPosition + "\n";
			result += "�ѽ��� : " + mSales + "\n";
			TabbedPane.setStaffResultArea(result);
		} else {
			TabbedPane.setStaffResultArea(mCurrentErrorMessage);
		}
	}

	public int addStaffDB(String name, String position) {
		int res = searchStaff(name);
		if (searchStaff(name) == 1) {
			res = -1;
			mCurrentErrorMessage = "�̹� �����ϴ� �̸��Դϴ�.";
			return res;
		}

		int strLength = POS.GetStringLength(name);
		if (strLength > 20) {
			mCurrentErrorMessage = "���� �� ����";
			return res;
		}

		try {
			res = -1;
			String sqlStr = "insert into staff (id, name, position) values(" + (getNextCustomerId()) + ", '" + name
					+ "','" + position + "')";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			rs.close();
			stmt.close();
			db.commit();
			res = 1;

		} catch (SQLException e) {
			mCurrentErrorMessage = "�ٽ� �Է��ϼ���.";
		}
		return res;
	}

	public int getNextCustomerId() {
		return mStaffId++;
	}
}
