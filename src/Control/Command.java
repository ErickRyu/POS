package Control;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Command{
	POS mPos;
	CustomerControl custmCtr;
	MenuControl menuCtr;
	SaleControl saleCtr;
	StaffControl staffCtr;
	TableControl tableCtr;
	
	public Command(POS pos){
		mPos = pos;
		custmCtr = new CustomerControl(mPos);
		menuCtr = new MenuControl(mPos);
		saleCtr = new SaleControl(mPos);
		staffCtr = new StaffControl(mPos);
		tableCtr = new TableControl(mPos);
	}
	public int search(Scanner sc) {
		int res = -1;
		
		if(mPos.isLogin() == -1){
			return res;
		}
		
		System.out.println("1. 고객조회");
		System.out.println("2. 매출조회");
		System.out.println("3. 직원조회");
		System.out.println("4. 메뉴조회");
		int command = sc.nextInt();
		
		String searchName="";
		switch (command) {
		case 1:
			System.out.println("input name");
			searchName = sc.next();
			res = custmCtr.searchCustomerDB(searchName);
			
			break;
		case 2:
			showSaleDatesDB();
			System.out.println("input date");
			searchName = sc.next();
			res = saleCtr.searchSalesDB(searchName);
			break;
		case 3:
			System.out.println("input name");
			searchName = sc.next();
			res = staffCtr.searchStaffDB(searchName);
			break;
		case 4:
			System.out.println("input name");
			searchName = sc.next();
			res = menuCtr.searchMenuDB(searchName);
			break;
		default:
		}
		return res;
	}
	public void showSaleDates(){
		System.out.println("Chose date");
		mPos.mSale.getSaleDates().forEach(System.out::println);
		System.out.println("-----------");
	}
	public void showSaleDatesDB(){
		System.out.println("Chose date");
		try{
			String sqlStr = "select distinct(day) from sale";
			ResultSet rs = mPos.jdbc.executeQueryAndGetResultSet(sqlStr);
			while(rs.next()){
				System.out.println(rs.getDate("day"));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		mPos.mSale.getSaleDates().forEach(System.out::println);
		System.out.println("-----------");
	}

	public int add(Scanner sc) {
		int res = -1;
		if(mPos.isLogin() == -1 || mPos.isLoginStaffSupervisor() == -1){
			return res;
		}
		System.out.println("1. 고객등록");
		System.out.println("2. 직원등록");
		System.out.println("3. 메뉴등록");
		int command = sc.nextInt();
		System.out.println("input name");
		String addName = sc.next();
		switch (command) {
		case 1:
			res = custmCtr.addCustomerDB(addName, sc);
			
			break;
		case 2:
			res = staffCtr.addStaffDB(addName, sc);
			break;
		case 3:
			res = menuCtr.addMenuDB(addName, sc);
			break;
		default:
		}
		return res;
	}


}