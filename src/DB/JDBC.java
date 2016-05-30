package DB;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import Control.POS;
import UI.MenuPanel;

public class JDBC {
	public Connection db;
	private String username = "system";
	private String password = "system";

	POS mPos;

	public JDBC(POS pos) {
		mPos = pos;
		connectDB();
		initDB();
	}

	public void connectDB() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			db = DriverManager.getConnection("jdbc:oracle:thin:" + "@localhost:1521:XE", username, password);
			db.setAutoCommit(false);
		} catch (SQLException e) {
			System.out.println("SQLException " + e);
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Exception " + e);
			e.printStackTrace();
		}
	}

	public void initDB() {
		dropTable();
		createTable();
	}

	public void dropTable() {
		try {
			Scanner sc = new Scanner(new FileReader("droptable.sql"));

			while (sc.hasNext()) {
				try {
					String sqlStr = sc.nextLine();
					executeQuery(sqlStr);
				} catch (SQLException e) {
					// Do nothing
				}
			}
			System.out.println("[Info] Drop table");
			sc.close();
		} catch (IOException e) {
			System.out.println("IOException " + e);
			e.printStackTrace();
		}
	}

	public void createTable() {
		try {
			Scanner sc = new Scanner(new FileReader("Schema.sql"));
			String sqlStr = "";
			while (sc.hasNext()) {
				sqlStr = sc.nextLine();
				executeQuery(sqlStr);
			}
			/* Initiate grade */
			sc = new Scanner(new FileReader("grade.sql"));
			while (sc.hasNext()) {
				sqlStr = sc.nextLine();
				executeQuery(sqlStr);
			}
			sc.close();
		} catch (SQLException e) {
			System.out.println("CreateTable " + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Initiate grade " + e);
		}
	}

	public void openData() {
		try {

			Scanner sc = new Scanner(new FileReader("data.txt"));
			int customerNum = sc.nextInt();
			while (customerNum > 0) {
				String name = sc.next();
				String birth = sc.next();
				String phone = sc.next();
				String grade = sc.next();

				mPos.customerControl.addCustomer(name, birth, phone, grade);
				customerNum--;
			}
			int staffNum = sc.nextInt();
			while (staffNum > 0) {
				String name = sc.next();
				String position = sc.next();

				mPos.staffControl.addStaffDB(name, position);
				staffNum--;
			}

			int menuNum = sc.nextInt();
			for (int i = 0; i < menuNum; i++) {
				String name = sc.next();
				int price = sc.nextInt();

				mPos.menuControl.addMenuDB(name, price);
				MenuPanel.setButtonName(i, name);
			}
			commit();

			sc.close();
		} catch (SQLException e) {
			System.out.println("ReadData : SQLException " + e);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// public ResultSet executeQueryAndGetResultSet(String sqlStr) throws
	// SQLException {
	// PreparedStatement stmt;
	// stmt = db.prepareStatement(sqlStr);
	// ResultSet rs = stmt.executeQuery();
	// return rs;
	// }
	public void executeQuery(String sqlStr) throws SQLException {
		PreparedStatement stmt = db.prepareStatement(sqlStr);
		stmt.executeQuery();
		stmt.close();
	}

	// public void closePreparedStatement()throws SQLException{
	// if(stmt != null)
	// stmt.close();
	// }
	public void commit() throws SQLException {
		db.commit();
	}

	public void rollback() throws SQLException {
		db.rollback();
	}
}
