package Control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import UI.TabbedPane;

public class CustomerControl {
	POS mPos;
	String mName;
	String mId;
	String mBirth;
	String mPhone;
	String mGrade;
	String mTotalPurchase;
	Connection db;

	private static int mCustomerId = 1000;
	public static String mCurrentErrorMessage = "";

	public CustomerControl(POS pos) {
		mPos = pos;
		db = mPos.jdbc.db;
	}

	public int searchCustomer(String searchName) {
		int res = -1;
		try {
			String sqlStr = "select * from customer where name = '" + searchName + "'";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			
			
			rs.next();
			mName = rs.getString("name");
			mId = rs.getString("id");
			mBirth = rs.getString("birth");
			mPhone = rs.getString("phone");
			mGrade = rs.getString("grade");
			mTotalPurchase = rs.getString("total_purchase");
			
			rs.close();
			stmt.close();
			res = 1;
		} catch (SQLException e) {
			mCurrentErrorMessage = "검색 결과 없음.";
		}
		return res;
	}

	public void searchAndShowCustomer(String searchName) {
		if (searchCustomer(searchName) == 1) {
			String result = "";
			result +=  "고    객     명 : " + mName + "\n";
			result +=  "고    객    I D : "+ mId + "\n";
			result +=  "생             일 : "+ mBirth + "\n";
			result +=  "전  화 번  호 : "+ mPhone + "\n";
			result +=  "고  객 등  급 : "+ mGrade + "\n";
			result +=  "총 구매금액 : "+ mTotalPurchase + "\n";
			TabbedPane.setCustomerResultArea(result);
		} else {
			TabbedPane.setCustomerResultArea(mCurrentErrorMessage);
		}
	}
	public String getCustomerName(String customerName) {
		try {
			if (customerName.equals("")) {
				return "비회원";
			}
			String sqlStr = "select name from customer where name = '" + customerName + "'";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			String eq = rs.getString("name");

			if (!eq.equals(customerName)) {
				customerName = "비회원";
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			customerName = "비회원";
		}
		return customerName;
	}
	public int addCustomer(String name, String birth, String phone) {
		int res = -1;
		if (searchCustomer(name) == 1) {
			mCurrentErrorMessage = "이미 존재하는 고객입니다.";
			return res;
		}
		try {
			String sqlStr = "insert into customer (id, name, birth, phone) values(" + (getNextCustomerId()) + ", '"
					+ name + "','" + birth + "', '" + phone + "')";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			rs.close();
			stmt.close();

			db.commit();
			res = 1;
		} catch (SQLException e) {
			mCurrentErrorMessage = "다시 입력하세요.";
		}
		return res;
	}

	public int addCustomer(String name, String birth, String phone, String grade) throws SQLException {
		int res = -1;
		try {
			String sqlStr = "select threshold from grade where grade_name = '" + grade + "'";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			int totalPurchase = rs.getInt("threshold");
			rs.close();
			
			sqlStr = "insert into customer values(" + (getNextCustomerId()) + ", '" + name + "', '" + birth + "', '"
					+ phone + "', '" + grade + "', " + totalPurchase + ")";
			stmt = db.prepareStatement(sqlStr);
			stmt.executeQuery();
			stmt.close();
			db.commit();
		} catch (SQLException e) {

		}
		return res;
	}
	public int getNextCustomerId(){return mCustomerId++;}
}
