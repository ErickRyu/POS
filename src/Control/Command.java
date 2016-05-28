package Control;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import Model.Staff;
import UI.LoginPanel;
import UI.TabbedPane;

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
		listenCommand();
	}
	
	public void listenCommand() {
		Scanner sc = new Scanner(System.in, "euc-kr");
		SaleControl saleCtr = new SaleControl(mPos);
		outer: while (true) {
			System.out.println();
			System.out.println("Input command");
			System.out.println("\t0. Login");
			System.out.println("\t1. Search");
			System.out.println("\t2. Add");
			System.out.println("\t3. Order");
			System.out.println("\t4. Purchase");
			System.out.println("\t5. Logout");
			System.out.println("\t6. Exit");
			int input = sc.nextInt();
			int res = -1;
			switch (input) {
			case 0:
				res = login(sc);
				break;
			case 1:
				res = search(sc);
				break;
			case 2:
				res = add(sc);
				break;
			case 3:
				res = saleCtr.order(sc);
				break;
			case 4: 
				res = saleCtr.purchaseDB(sc);
				break;
			case 5:
				mPos.mLoginStaffName = null;
				break;
			case 6:
				break outer;
			default:
				mPos.mCurrentErrorMessage = "Check command";
			}
			if (res == -1)
				System.out.println(mPos.mCurrentErrorMessage);
		}
		sc.close();
	}
	
	public int login(Scanner sc) {
		int res = -1;
		
		new LoginPanel(mPos);
		
//		System.out.println("Input name");
//		name = sc.next();
//		System.out.println("Input id");
//		id = sc.nextInt();
//
//		Staff staff = mPos.mStaffMap.get(name);
//
//		if (staff != null) {
//			if (staff.getName().equals(name)) {
//				if (staff.getId() == id) {
//					mPos.mLoginStaffName = name;
//					return 1;
//				}
//			}
//		}
//		mPos.mCurrentErrorMessage = "Fail to login";
		return res;
	}

	public int isLogin(){
		int res = -1;
		if(mPos.mLoginStaffName == null){
			mPos.mCurrentErrorMessage = "Please login first";
			return res;
		}
		res = 1;
		return res;
		
	}

	public int isLoginStaffSupervisor() {
		int res = -1;
		if (mPos.mLoginStaffName == null) {
			mPos.mCurrentErrorMessage = "Please login";
		} else if (!mPos.mStaffMap.get(mPos.mLoginStaffName).isSupervisor()) {
			mPos.mCurrentErrorMessage = "Only Supervisor can add";
		} else {
			res = 1;
		}
		return res;
	}
	
	
	public int search(Scanner sc) {
		int res = -1;
		
		if(isLogin() == -1){
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
		if(isLogin() == -1 || isLoginStaffSupervisor() == -1){
			return res;
		}
		new TabbedPane();
//		System.out.println("1. 고객등록");
//		System.out.println("2. 직원등록");
//		System.out.println("3. 메뉴등록");
//		int command = sc.nextInt();
//		System.out.println("input name");
//		String addName = sc.next();
//		switch (command) {
//		case 1:
//			res = custmCtr.addCustomerDB(addName, sc);
//			
//			break;
//		case 2:
//			res = staffCtr.addStaffDB(addName, sc);
//			break;
//		case 3:
//			res = menuCtr.addMenuDB(addName, sc);
//			break;
//		default:
//		}
		return res;
	}


}