package Control;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import Model.Customer;
import Model.Menu;
import Model.Staff;
import Model.TableInfo;

public class Command{
	POS mPos;
	int day = 523;
	public Command(POS pos){
		mPos = pos;
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
		System.out.println("input name");
		String searchName = sc.next();
		switch (command) {
		case 1:
			res = searchCustomer(searchName);
			break;
		case 2:
			res = searchPurchase(searchName);
			break;
		case 3:
			res = searchStaff(searchName);
			break;
		case 4:
			res = searchMenu(searchName);
			break;
		default:
		}
		return res;
	}

	public int searchCustomer(String searchName) {
		int res = -1;
		Customer customer = mPos.mCustomerMap.get(searchName);
		if (customer == null) {
			
			mPos.mCurrentErrorMessage = "Not exist";
			return res;
		}
		System.out.println("Customer Info");
		System.out.println(customer.getName());
		System.out.println(customer.getBirth());
		System.out.println(customer.getPhone());
		System.out.println(customer.getGrade());
		System.out.println(customer.getTotalPurchase());
		res = 1;
		return res;
	}

	public int searchPurchase(String searchName) {
		int res = -1;
		System.out.println("해당 날짜 판매");
		System.out.println(mPos.mSale.getDaySales(searchName));
		
		System.out.println("\nMost sales");
		mPos.mSale.getMostSalesMenu().forEach(System.out::println);
		System.out.println("\nLeast sales");
		mPos.mSale.getLeastSalesMenu().forEach(System.out::println);
		
		System.out.println("\n누적판매");
		System.out.println(mPos.mSale.getCumulativeSales());
		return res;
	}

	public int searchStaff(String searchName) {
		int res = -1;
		Staff staff = mPos.mStaffMap.get(searchName);
		if (staff == null) {
			mPos.mCurrentErrorMessage = "Not exist";
			return res;
		}
		System.out.println("Staff Info");
		System.out.println(staff.getName());
		System.out.println(staff.getPosition());
		System.out.println(staff.getSales());
		res = 1;
		return res;
	}

	public int searchMenu(String searchName) {
		int res = -1;
		Menu menu = mPos.mMenuMap.get(searchName);
		if (menu == null) {
			mPos.mCurrentErrorMessage = "Not exist";
			return res;
		}
		System.out.println("Menu Info");
		System.out.println(menu.getName());
		System.out.println(menu.getPrice());
		res = 1;
		return res;
	}	
	
	public int add(Scanner sc) {
		int res = -1;
		if(isLogin() == -1 || isLoginStaffSupervisor() == -1){
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
			res = addCustomer(addName, sc);
			
			break;
		case 2:
			res = addStaff(addName, sc);
			break;
		case 3:
			res = addMenu(addName, sc);
			break;
		default:
		}
		return res;
	}

	public int addCustomer(String addName, Scanner sc) {
		int res = -1;
		if (mPos.mCustomerMap.get(addName) != null) {
			mPos.mCurrentErrorMessage = "Same customer name is already exist";
			return res;
		}
		System.out.println("Input birthday");
		String birth = sc.next();
		System.out.println("Input phone number");
		String phone = sc.next();

		Customer customer = new Customer(addName, birth, phone);
		mPos.mCustomerMap.put(addName, customer);
		return res;
		
	}

	public int addMenu(String addName, Scanner sc) {
		int res = -1;
		if (mPos.mMenuMap.size() >= 20) {
			mPos.mCurrentErrorMessage = "No more add menu. 20 is maximum menu";
			return res;
		}
		if (mPos.mMenuMap.get(addName) != null) {
			mPos.mCurrentErrorMessage = "Same menu name is already exist";
			return res;
		}
		System.out.println("Input price");
		int price = sc.nextInt();
		Menu menu = new Menu(addName, price);
		mPos.mMenuMap.put(addName, menu);
		res = 1;
		return res;
	}

	public int addStaff(String addName, Scanner sc) {
		int res = -1;
		if (mPos.mStaffMap.get(addName) != null) {
			mPos.mCurrentErrorMessage = "Same staff name is already exist";
			return res;
		}
		System.out.println("Input position");
		String position = sc.next();
		Staff staff = new Staff(addName, position);
		mPos.mStaffMap.put(addName, staff);
		res = 1;
		return res;
	}
	public int order(Scanner sc){
		int res = -1;
		if(isLogin() == -1){
			return res;
		}
		
		System.out.println("Input table number");
		int tableNum = sc.nextInt();
		
		System.out.println("Input customer name");
		String customerName = sc.next();
		//customer name 이 데이터베이스에 없을 시 비회원으로 저장
		if (mPos.mCustomerMap.get(customerName) == null)
			customerName = "비회원";
		
		TableInfo tableInfo = mPos.mTableMap.get(tableNum);
		if(tableInfo == null){
			tableInfo = new TableInfo();
		}
		
		Map<String, Integer> orderMap = new HashMap<>(tableInfo.mOrderMap);
		
		int totalOrderPrice = tableInfo.mTotalOrderPrice;
		while(true){
			System.out.println("1. More Order \n2. Enough");
			int cont = sc.nextInt();
			if(cont==2)break;
			
			System.out.println("Input menu name");
			String menuName = sc.next();
			
			int menuNum = 1;
			if(orderMap.get(menuName) != null){
				menuNum = orderMap.get(menuName) + 1;
			}
			orderMap.put(menuName, menuNum);
			
			totalOrderPrice += mPos.mMenuMap.get(menuName).getPrice();
			showOrderStatus(orderMap);
		}
		
		System.out.println("1. Order\n2. Cancle");
		int command = sc.nextInt();
		boolean ok = (command == 1)? true : false;
		if(ok){
			tableInfo.mCustomerName = customerName;
			tableInfo.mTotalOrderPrice = totalOrderPrice;
			tableInfo.mOrderMap = orderMap;
			mPos.mTableMap.put(tableNum, tableInfo);
			System.out.println("Total price : " + tableInfo.mTotalOrderPrice);
		}else{
			mPos.mTableMap.remove(tableNum);
		}
		showTableStatus();
		res = 1;
		return res;
	}
	
	
	public int purchase(Scanner sc){
		int res = -1;
		
		if(isLogin() == -1){
			return res;
		}
		
		System.out.println("Input table number");
		int tableNum = sc.nextInt();
		
		TableInfo tableInfo = mPos.mTableMap.get(tableNum);
		if(tableInfo == null){
			mPos.mCurrentErrorMessage = "There is no order infomation";
			return res;
		}
		Map<String, Integer> orderMenuMap = tableInfo.mOrderMap;
		
		int totalSale = mPos.mTableMap.get(tableNum).mTotalOrderPrice;
		
		
//		String today = Integer.toString(day++);
		System.out.println("today is " + mPos.mToday);
		mPos.mSale.addSales(mPos.mToday, orderMenuMap, totalSale);
		
		mPos.mTableMap.remove(tableNum);
		
		String customerName = tableInfo.mCustomerName;
		System.out.println("Customer name : " + customerName);
		
		// 해당 고객의 totalpurchase up
		mPos.mCustomerMap.get(customerName).addPurchase(totalSale);
		// 업그레이드 해야 할 경우만 업그레이드 하도록 변경 
		mPos.mCustomerMap.get(customerName).upgrade();
		
		// 로그인한 스태프의 실적 up
		mPos.mStaffMap.get(mPos.mLoginStaffName).addSales(totalSale);
		
		showTableStatus();
		
		res = 1;
		return res;
	}
	public int login(Scanner sc) {
		int res = -1;
		String name;
		int id;
		System.out.println("Input name");
		name = sc.next();
		System.out.println("Input id");
		id = sc.nextInt();

		Staff staff = mPos.mStaffMap.get(name);

		if (staff != null) {
			if (staff.getName().equals(name)) {
				if (staff.getId() == id) {
					mPos.mLoginStaffName = name;
					return 1;
				}
			}
		}
		mPos.mCurrentErrorMessage = "Fail to login";
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
	
	
	/*	tmp method for show table status */
	public void showTableStatus(){
		for(int i = 0; i < 20; i++){
			if(i%5 ==0 )System.out.println();
			String empty = " ";
			if(mPos.mTableMap.get(i+1)!= null){
				empty = "*";
			}
			System.out.print(empty + (i+1) + "\t");
		}
	}
	/* Show order status */
	public void showOrderStatus(Map<String, Integer> orderMap){
		System.out.println();
		for(Entry<String, Integer> entry : orderMap.entrySet()){
			String menuName = entry.getKey();
			int totalMenuPrice = mPos.mMenuMap.get(menuName).getPrice() * entry.getValue();
			System.out.format("%10s%10d\n", menuName, totalMenuPrice);
		}
	}
}