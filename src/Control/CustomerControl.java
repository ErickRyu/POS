package Control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	public String mCurrentErrorMessage = "";

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
			result += "고    객     명 : " + mName + "\n";
			result += "고    객    I D : " + mId + "\n";
			result += "생             일 : " + mBirth + "\n";
			result += "전  화 번  호 : " + mPhone + "\n";
			result += "고  객 등  급 : " + mGrade + "\n";
			result += "총 구매금액 : " + mTotalPurchase + "\n";
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

		int strLength = POS.GetStringLength(name);
		if (strLength > 20 || phone.length() > 20) {
			mCurrentErrorMessage = "글자 수 제한";
			return res;
		}
		try {
			Integer.parseInt(birth);
			Integer.parseInt(phone);
			if (birth.length() != 4) {
				throw new Exception();
			}
		} catch (NumberFormatException e) {
			mCurrentErrorMessage = "숫자를 입력하세요.";
			return res;
		} catch (Exception e) {
			mCurrentErrorMessage = "생일은 4자리를 입력하세요.\n ex)5월 7일  -> 0507";
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

	public int updateCustomerGrade(String customerName) {
		int res = -1;
		try {
			String sqlStr = "select total_purchase from customer where name='" + customerName + "'";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			int totalPurchase = rs.getInt("total_purchase");
			upgrade(customerName, totalPurchase);
			res = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public int upgrade(String customerName, int totalPurchase) {
		int res = -1;
		try {
			String sqlStr = "select * from grade";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			/* */
			Map<String, Integer> gradeMap = new HashMap<>();
			while (rs.next()) {
				String gradeName = rs.getString("grade_name");
				int threshold = rs.getInt("threshold");
				gradeMap.put(gradeName, threshold);
			}
			gradeMap = MapUtil.sortByValue(gradeMap);
			for (Entry<String, Integer> entry : gradeMap.entrySet()) {
				String checkGrade = entry.getKey();
				int checkThreshold = entry.getValue();
				if (checkThreshold < totalPurchase) {
					// update and return;
					sqlStr = "update customer set grade='" + checkGrade + "' where name='" + customerName + "'";
					stmt = db.prepareStatement(sqlStr);
					stmt.executeQuery();
					return 1;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	public static class MapUtil {
		public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
			List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
				public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
					return (o2.getValue()).compareTo(o1.getValue());
				}
			});

			Map<K, V> result = new LinkedHashMap<K, V>();
			for (Map.Entry<K, V> entry : list) {
				result.put(entry.getKey(), entry.getValue());
			}
			return result;
		}
	}

	public int getNextCustomerId() {
		return mCustomerId++;
	}
}
