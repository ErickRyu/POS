package Control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import UI.OrderPanel;
import UI.TabbedPane;
import UI.TablePanel;

public class SaleControl {
	POS mPos;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public String mCurrentErrorMessage = "";
	Connection db;

	public SaleControl(POS pos) {
		mPos = pos;
		db = mPos.jdbc.db;
	}

	public int searchSales(String searchName) {
		int res = -1;
		if (!mPos.isLogin() || !mPos.isSupervisor()) {

			TabbedPane.setSaleResultArea(null);
			return res;
		}
		try {
			String result = "";

			String sqlStr = "select * from sale where day = '" + searchName + "'";

			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			String sales = rs.getString("sales");

			result += "일 매출 : " + sales + "\n";
			result += "-------------------------------------\n";
			result += "가장 많이 팔린 메뉴\n";

			sqlStr = "select name from menu T where T.cumulitive > 0 and T.cumulitive >= all(select cumulitive from menu)";

			stmt = db.prepareStatement(sqlStr);
			rs = stmt.executeQuery();

			while (rs.next()) {
				String mostSalesMenu = rs.getString("name");
				result += mostSalesMenu + "\n";
			}
			result += "\n";
			result += "가장 적게 팔린 메뉴\n";
			sqlStr = "select name from menu T where T.cumulitive > 0 and T.cumulitive <= all (select cumulitive from menu where cumulitive > 0)";
			stmt = db.prepareStatement(sqlStr);
			rs = stmt.executeQuery();

			while (rs.next()) {
				String leastSalesMenu = rs.getString("name");
				result += leastSalesMenu + "\n";
			}
			result += "-------------------------------------\n";
			sqlStr = "select sum(sales) from sale";

			stmt = db.prepareStatement(sqlStr);
			rs = stmt.executeQuery();

			rs.next();
			String cumulitiveSales = rs.getString("sum(sales)");
			result += "누적 매출 : " + cumulitiveSales;
			TabbedPane.setSaleResultArea(result);
			res = 1;

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			mCurrentErrorMessage = "Not exist";
		}
		return res;
	}

	public int getTotalPrice(int tableNum) {
		int res = 0;
		try {
			String sqlStr = "select sum(total_price) from orderstatus where table_num=" + tableNum;
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			res = rs.getInt("sum(total_price)");

			rs.close();
			stmt.close();
		} catch (SQLException e) {

		}
		return res;
	}

	public void updateSales(int totalSale) {
		try {
			Date date = new Date();
			String today = dateFormat.format(date);
			String sqlStr = "select count(day) from sale where day='" + today + "'";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			int count = rs.getInt("count(day)");
			if (count > 0) {
				sqlStr = "update sale " + "set sales = sales + " + totalSale + "where day = '" + today + "'";
			} else {
				sqlStr = "insert into sale values ('" + today + "', " + totalSale + ")";
			}
			stmt = db.prepareStatement(sqlStr);
			rs = stmt.executeQuery();
			db.commit();

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int purchase(int tableNum, String customerName) {
		/* Check is valid customer name and unless change name to 비회원 */
		customerName = mPos.customerControl.getCustomerName(customerName);

		/* Get original totalSale */
		int totalSale = getTotalPrice(tableNum);

		/* apply discount to totalSale */
		float discount = getDiscountRate(customerName);
		totalSale *= discount;

		/* Save today's total sales */
		updateSales(totalSale);

		/* update menu cumulitive */
		mPos.menuControl.updateMenuSales(tableNum);

		/* Remove order information */
		deleteOrder(tableNum);

		/* customer's total purchase up */
		String sqlStr = "update customer " + "set total_purchase = total_purchase + " + totalSale + "where name = '"
				+ customerName + "'";
		try {
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			stmt.executeQuery();
			stmt.close();
			db.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		/* Update customer grade */
		if (!customerName.equals("비회원"))
			mPos.customerControl.updateCustomerGrade(customerName);

		/* Update Sales of login staff */
		sqlStr = "update staff " + "set sales = sales + " + totalSale + "where name = '" + mPos.mLoginStaffName + "'";
		try {
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			stmt.executeQuery();
			stmt.close();
			db.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		OrderPanel.setResultArea(null);
		TabbedPane.updateSaleTabBox();
		TablePanel.setTableColorWhite(tableNum);
		return 1;

	}

	public float getDiscountRate(String customerName) {
		float res = 1f;
		try {
			String sqlStr = "select discount from grade where grade_name = (select grade from customer where name='"
					+ customerName + "')";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			res = rs.getFloat("discount");

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public void addTempOrder(int tableNum, String menuName, String customerName) {
		try {
			customerName = mPos.customerControl.getCustomerName(customerName);

			String sqlStr = "select count(menu_name) from orderstatus where menu_name = '" + menuName
					+ "' and table_num = " + tableNum;
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			int count = rs.getInt("count(menu_name)");
			rs.close();
			stmt.close();

			updateOrderCustomerName(tableNum, customerName);
			if (count == 0) {
				sqlStr = "insert into orderstatus values(" + tableNum + ", '" + menuName + "', '" + customerName
						+ "', 1, " + mPos.menuControl.getPrice(menuName) + ")";
				PreparedStatement stmt2 = db.prepareStatement(sqlStr);
				stmt2.executeQuery();
				stmt2.close();
			} else {
				sqlStr = "update orderstatus set sales = sales + 1, total_price = total_price + "
						+ mPos.menuControl.getPrice(menuName) + " where menu_name='" + menuName + "' and table_num = "
						+ tableNum;
				PreparedStatement stmt2 = db.prepareStatement(sqlStr);
				stmt2.executeQuery();
				stmt2.close();
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		showOrder(tableNum);
	}

	public void updateOrderCustomerName(int tableNum, String customerName) {
		try {
			String sqlStr = "select customer_name from orderstatus where table_num = " + tableNum;
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			String eqCustomerName = rs.getString("customer_name");
			if (!eqCustomerName.equals(customerName)) {
				// update customer name
				sqlStr = "update orderstatus set customer_name = '" + customerName + "' where table_num = " + tableNum;
				stmt = db.prepareStatement(sqlStr);
				rs = stmt.executeQuery();
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// when empty resultset, do nothing
		}
	}

	public void showOrder(int tableNum) {
		try {
			String sqlStr = "select * from orderstatus where table_num = " + tableNum;

			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			String result = "";
			rs.next();
			String customerName = rs.getString("customer_name");
			do {
				result += String.format("%-10s%-10d\n", rs.getString("menu_name"), rs.getInt("total_price"));
			} while (rs.next());

			sqlStr = "select sum(total_price) from orderstatus where table_num = " + tableNum;

			stmt = db.prepareStatement(sqlStr);
			rs = stmt.executeQuery();

			rs.next();
			int totalSum = rs.getInt("sum(total_price)");
			result += "\n\n\n";
			result += "----------------------------------\n";
			result += String.format("%-10s%-10d", "총합계 : ", totalSum);

			rs.close();
			stmt.close();

			OrderPanel.setResultArea(result);

			if (totalSum == 0)
				customerName = "";
			if (customerName.equals("비회원"))
				customerName = "";
			OrderPanel.setCustomerName(customerName);

		} catch (SQLException e) {
			OrderPanel.setCustomerName("");
		}
	}

	public void deleteOrder(int tableNum) {
		try {
			String sqlStr = "delete from orderstatus where table_num=" + tableNum;
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			rs.close();
			stmt.close();
			db.commit();
			showOrder(tableNum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void rollBack() {
		try {
			db.rollback();
		} catch (SQLException e) {
		}
	}

	public void commit() {
		try {
			db.commit();
		} catch (SQLException e) {
		}
	}

}
