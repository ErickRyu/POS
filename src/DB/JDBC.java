package DB;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class JDBC {
	public Connection db;
	private String username = "system";
	private String password = "system";
	public static int customerId = 1000;
	public static int staffId = 1000;
	public static int menuId = 1000;
	public JDBC() {
		connectDB();
		initDB();
	}

	public void connectDB() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			db = DriverManager.getConnection("jdbc:oracle:thin:" + "@localhost:1521:XE", username, password);
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
		insertInitialData();
	}

	public void dropTable() {
		try {
			Scanner sc = new Scanner(new FileReader("droptable.sql"));
			System.out.println("[Info] Drop table");
			while (sc.hasNext()) {
				try {
					String sqlStr = sc.nextLine();
					executeQuery(sqlStr);
				} catch (SQLException e) {
					// Do nothing
				}
			}
		} catch (IOException e) {
			System.out.println("IOException " + e);
			e.printStackTrace();
		}
	}

	public void createTable() {
		// 파일로 읽어서 실행하고 싶은데 깔끔한 방법이 안떠오른다.

		try {
			String sqlStr = "create table sale("+
					"day	date	not null,"+
					"sales	integer	not null,"+
					"primary key(day)"+
					")";
			executeQuery(sqlStr);
			
			sqlStr = "create table menu("+
					"id	integer		not null,"+
					"name	varchar(30)	not null,"+
					"price	integer		not null,"+
					"cumulitive	integer	default 0,"+
					"primary key(id, name)"+
					")";
			executeQuery(sqlStr);
			sqlStr = "create table customer("+
					"id	integer		not null,"+
					"name	varchar(20)	not null,"+	
					"birth	char(4)	not null,"+
					"phone	char(4)	not null,"+
					"grade	varchar(10) default 'Normal',"+
					"total_purchase	integer default 0,"+
					"primary key(id, name)"+
					")";
			executeQuery(sqlStr);
			sqlStr = "create table staff("+
					"id	integer		not null,"+
					"name	varchar(20)	not null,"+
					"position	varchar(20)	not null,"+
					"sales	integer default 0,"+
					"primary key(id, name)"+
					")";
			executeQuery(sqlStr);
			sqlStr = "create table grade("+
					"grade_name	varchar(20)	not null,"+
					"threshold integer not null,"+
					"discount float		not null,"+
					"primary key(grade_name)"+
					")";
			executeQuery(sqlStr);
		} catch (SQLException e) {
			System.out.println("CreateTable : " + e);
			e.printStackTrace();
		}
	}
	
	
	public void insertInitialData() {
		try {
			Scanner sc = new Scanner(new FileReader("grade.sql"));
			while(sc.hasNext()){
				String sqlStr = sc.nextLine();
				executeQuery(sqlStr);
			}
			sc = new Scanner(new FileReader("data.txt"));
			int customerNum = sc.nextInt();
			while (customerNum > 0) {
				String name = sc.next();
				String birth = sc.next();
				String phone = sc.next();
				String grade = sc.next();

				/* Save to Database */
				String sqlStr = "select threshold from grade where grade_name = '" + grade + "'";
				ResultSet rs = executeQueryAndGetResultSet(sqlStr);
				rs.next();
				int totalPurchase = rs.getInt("threshold");
				rs.close();
				sqlStr = "insert into customer values(" + (customerId++) +", '" + name + "', '"+birth + "', '" + phone+"', '" +grade + "', " + totalPurchase + ")";
				
				executeQuery(sqlStr);
				customerNum--;
			}
			int staffNum = sc.nextInt();
			while (staffNum > 0) {
				String name = sc.next();
				String position = sc.next();
				
				/* Save to Database */
				String sqlStr = "insert into staff values(" + (staffId++) +", '" + name + "', '"+position + "', 0)";
				executeQuery(sqlStr);
				staffNum--;
			}

			int menuNum = sc.nextInt();
			while (menuNum > 0) {
				String name = sc.next();
				int price = sc.nextInt();
				/* Save to Database */
				String sqlStr = "insert into menu values(" + (menuId++) +", '" + name + "', "+price + ", 0)";
				executeQuery(sqlStr);
				menuNum--;
			}
			
			sc.close();
			System.out.println("[Info] Init Data");
		}catch(SQLException e){
			System.out.println("ReadData : SQLException " + e);
			e.printStackTrace();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet executeQueryAndGetResultSet(String sqlStr) throws SQLException {
		PreparedStatement stmt = db.prepareStatement(sqlStr);
		ResultSet rs = stmt.executeQuery();
//		stmt.close();
		return rs;
	}
	public void executeQuery(String sqlStr) throws SQLException {
		PreparedStatement stmt = db.prepareStatement(sqlStr);
		stmt.executeQuery();
		stmt.close();
	}
}
