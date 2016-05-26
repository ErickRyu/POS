package Control;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import Model.Customer;

public class CustomerControl {
	POS mPos;

	public CustomerControl(POS pos) {
		mPos = pos;
	}

	/* Search from JDBC */
	public int searchCustomerDB(String searchName) {

		int res = -1;
		try {
			String sqlStr = "select * from customer where name = '" + searchName + "'";
			ResultSet rs = mPos.jdbc.executeQueryAndGetResultSet(sqlStr);
			rs.next();
			String name = rs.getString("name");
			String birth = rs.getString("birth");
			String phone = rs.getString("phone");
			String grade = rs.getString("grade");
			String totalPurchase = rs.getString("total_purchase");

			System.out.println("Customer Info");
			System.out.println(name);
			System.out.println(birth);
			System.out.println(phone);
			System.out.println(grade);
			System.out.println(totalPurchase);
			res = 1;

		} catch (SQLException e) {
			mPos.mCurrentErrorMessage = "Not exist";
		}
		return res;
	}

	public int addCustomerDB(String addName, Scanner sc) {
		int res = -1;
		if (searchCustomerDB(addName) == 1) {
			mPos.mCurrentErrorMessage = "DB : Same customer name is already exist";
			return res;
		}
		try {
			System.out.println("Input birthday");
			String birth = sc.next();
			System.out.println("Input phone number");
			String phone = sc.next();

			String sqlStr = "insert into customer (id, name, birth, phone) values("+ (mPos.jdbc.customerId++) +", '" + addName + "','" + birth + "', '" + phone + "')";
			mPos.jdbc.executeQuery(sqlStr);
			
			res = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
}
