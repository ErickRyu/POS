package Control;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import Model.Staff;

public class StaffControl {
	POS mPos;
	public StaffControl(POS pos){
		mPos = pos;
	}
	
	/* Search from JDBC */
	public int searchStaffDB(String searchName) {

		int res = -1;
		try {
			String sqlStr = "select * from staff where name = '" + searchName + "'";
			ResultSet rs = mPos.jdbc.executeQueryAndGetResultSet(sqlStr);
			rs.next();
			String name = rs.getString("name");
			String birth = rs.getString("position");
			String sales = rs.getString("sales");
			
			System.out.println("Staff Info");
			System.out.println(name);
			System.out.println(birth);
			System.out.println(sales);
			res = 1;

		} catch (SQLException e) {
			mPos.mCurrentErrorMessage = "Not exist";
		}
		return res;
	}

	public int addStaffDB(String addName, Scanner sc) {
		int res = -1;
		if (searchStaffDB(addName) == 1) {
			mPos.mCurrentErrorMessage = "Same staff name is already exist";
			return res;
		}
		try {
			System.out.println("Input position");
			String position = sc.next();

			String sqlStr = "insert into staff (id, name, position) values("+ (mPos.jdbc.staffId++) +", '" + addName + "','" + position + "')";
			mPos.jdbc.executeQuery(sqlStr);
			
			res = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
}
