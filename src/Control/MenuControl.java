package Control;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import Model.Menu;

public class MenuControl {
	POS mPos;
	public MenuControl(POS pos){
		mPos = pos;
	}
	/* Search from JDBC */
	public int searchMenuDB(String searchName) {

		int res = -1;
		try {
			String sqlStr = "select * from menu where name = '" + searchName + "'";
			ResultSet rs = mPos.jdbc.executeQueryAndGetResultSet(sqlStr);
			rs.next();
			String name = rs.getString("name");
			String price = rs.getString("price");
			String cumulitive = rs.getString("cumulitive");
			
			System.out.println("Menu Info");
			System.out.println(name);
			System.out.println(price);
			System.out.println(cumulitive);
			res = 1;

		} catch (SQLException e) {
			mPos.mCurrentErrorMessage = "Not exist";
		}
		return res;
	}
	
	
	public int addMenuDB(String addName, Scanner sc) {
		int res = -1;
		if (searchMenuDB(addName) == 1) {
			mPos.mCurrentErrorMessage = "Same menu name is already exist";
			return res;
		}
		
//		if (mPos.mMenuMap.size() >= 20) {
//			mPos.mCurrentErrorMessage = "No more add menu. 20 is maximum menu";
//			return res;
//		}
		
		try {
			System.out.println("Input price");
			int price = sc.nextInt();

			String sqlStr = "insert into menu (id, name, price) values("+ (mPos.jdbc.menuId++) +", '" + addName + "'," + price + ")";
			mPos.jdbc.executeQuery(sqlStr);
			
			res = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
}
