package Control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import UI.MenuPanel;
import UI.TabbedPane;

public class MenuControl {
	POS mPos;
	Connection db;
	String mName;
	String mPrice;
	String mCumulitive;
	static int mMenuId = 1000;
	public static String mCurrentErrorMessage = "";

	public MenuControl(POS pos) {
		mPos = pos;
		db = mPos.jdbc.db;
	}

	public int searchMenu(String searchName) {

		int res = -1;
		try {
			String sqlStr = "select * from menu where name = '" + searchName + "'";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			mName = rs.getString("name");
			mPrice = rs.getString("price");
			mCumulitive = rs.getString("cumulitive");
			res = 1;

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			mCurrentErrorMessage = "검색 결과 없음.";
		}
		return res;
	}

	public void searchAndShowMenu(String searchName) {
		int res = searchMenu(searchName);
		if (res == 1) {
			String result = "";
			result += "메뉴명 : " + mName + "\n";
			result += "가   격 : " + mPrice + "\n";
			TabbedPane.setMenuResultArea(result);
		} else {
			TabbedPane.setMenuResultArea(mCurrentErrorMessage);
		}
	}

	public int addMenuDB(String name, int price) {
		int res = -1;

		if (searchMenu(name) == 1) {
			mCurrentErrorMessage = "존재하는 이름입니다.";
			return res;
		}

		int menuNum = countMenuNum();
		if (menuNum >= 20) {
			mCurrentErrorMessage = "20개 이상 등록할 수 없습니다.";
			return res;
		}

		try {
			String sqlStr = "insert into menu (id, name, price) values(" + getNextMenuId() + ", '" + name + "'," + price
					+ ")";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			
			rs.close();
			stmt.close();
			db.commit();
			MenuPanel.setButtonName(menuNum, name);
			res = 1;
		} catch (SQLException e) {
			mCurrentErrorMessage = "다시 입력하세요.";
		}
		return res;
	}
	public void updateMenuSales(int tableNum) {
		try {
			String sqlStr = "select * from orderstatus where table_num = " + tableNum;
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String menuName = rs.getString("menu_name");
				int sales = rs.getInt("sales");
				sqlStr = "update menu set cumulitive= cumulitive + " + sales + " where name='" + menuName + "'";
				PreparedStatement stmt2 = db.prepareStatement(sqlStr);
				ResultSet rs2 = stmt2.executeQuery();
				rs2.close();
				stmt2.close();
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getPrice(String menuName) {
		int res = 0;
		try {
			String sqlStr = "select price from menu where name='" + menuName + "'";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			res = rs.getInt("price");

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public int countMenuNum() {
		int res = 0;
		try {
			String sqlStr = "select count(name) from menu";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			res = rs.getInt("count(name)");
			
			rs.close();
			stmt.close();
		} catch (SQLException e) {
		}
		return res;
	}

	public int getNextMenuId() {
		return mMenuId++;
	}
}
