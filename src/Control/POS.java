package Control;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import Model.Customer;
import Model.Menu;
import Model.Sales;
import Model.Staff;

public class POS {
	// member 변수 앞에는 m을 붙이는 것.

	/* Save name who login */
	String mLoginStaffName;

	/* Save Database information */
	Map<String, Staff> mStaffMap;
	Map<String, Menu> mMenuMap;
	Map<String, Customer> mCustomerMap;

	
	Map<Integer, TableInfo> mTableMap;		/* Save order information per table */
	
	/* Table Info class to save order information per table*/
	private class TableInfo{
		String mCustomerName;				/* Save customer who order menu at that table*/
		int mTotalOrderPrice;				/* Save total order price */
		Map<String, Integer> mOrderMap;		/* Map to save order menu and each order number*/
		public TableInfo(){
			mCustomerName = "비회원";			/* Default customer name is 비회원  */
			mTotalOrderPrice = 0;
			mOrderMap = new HashMap<>();
		}
	}
	
	
	String mToday = "";
	
	Sales sales;
	String mCurrentErrorMessage = "";

	public POS() {
		mStaffMap = new HashMap<String, Staff>();
		mMenuMap = new HashMap<String, Menu>();
		mCustomerMap = new HashMap<String, Customer>();
		sales = new Sales();
		mTableMap = new HashMap<>();
		mToday = "0523";
		readData();
		listenCommand();
	}

	public void readData() {
		try {
			Scanner sc = new Scanner(new FileReader("data.txt"));
			int customerNum = sc.nextInt();
			// 조건문 안에 증감연산자는 가독성이 떨어질 수 있다.
			// 파일을 읽은 뒤 제대로 읽혔는지 확인.
			while (customerNum > 0) {
				String name = sc.next();
				String birth = sc.next();
				String phone = sc.next();
				String grade = sc.next();

				Customer cutomer = new Customer(name, birth, phone, grade);
				mCustomerMap.put(name, cutomer);

				customerNum--;
			}
			int staffNum = sc.nextInt();
			while (staffNum > 0) {
				String name = sc.next();
				String position = sc.next();

				Staff staff = new Staff(name, position);

				mStaffMap.put(name, staff);

				staffNum--;
			}

			int menuNum = sc.nextInt();
			while (menuNum > 0) {
				String name = sc.next();
				int price = sc.nextInt();

				Menu menu = new Menu(name, price);

				mMenuMap.put(name, menu);
				menuNum--;
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void listenCommand() {
		Scanner sc = new Scanner(System.in, "euc-kr");
		SearchAndAdd searchAndAdd = new SearchAndAdd(this);
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
			int command = sc.nextInt();
			int res = -1;
			switch (command) {
			case 0:
				res = login(sc);
				break;
			case 1:
				res = searchAndAdd.search(sc);
				break;
			case 2:
				res = searchAndAdd.add(sc);
				break;
			case 3:
				res = order(sc);
				break;
			case 4: 
				res = purchase(sc);
				break;
			case 5:
				mLoginStaffName = null;
				break;
			case 6:
				break outer;
			default:
				mCurrentErrorMessage = "Check command";
			}
			if (res == -1)
				System.out.println(mCurrentErrorMessage);
		}
		sc.close();
	}
	
	public int order(Scanner sc){
		int res = -1;
		if(mLoginStaffName == null){
			mCurrentErrorMessage = "Please login first";
			return res;
		}
		
		System.out.println("Input table number");
		int tableNum = sc.nextInt();
		
		System.out.println("Input customer name");
		String customerName = sc.next();
		//customer name 이 데이터베이스에 없을 시 비회원으로 저장
		if (mCustomerMap.get(customerName) == null)
			customerName = "비회원";
		
		TableInfo tableInfo = mTableMap.get(tableNum);
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
			
			totalOrderPrice += mMenuMap.get(menuName).getPrice();
			showOrderStatus(orderMap);
		}
		
		System.out.println("1. Order\n2. Cancle");
		int command = sc.nextInt();
		boolean ok = (command == 1)? true : false;
		if(ok){
			tableInfo.mCustomerName = customerName;
			tableInfo.mTotalOrderPrice = totalOrderPrice;
			tableInfo.mOrderMap = orderMap;
			mTableMap.put(tableNum, tableInfo);
			System.out.println("Total price : " + tableInfo.mTotalOrderPrice);
		}else{
			mTableMap.remove(tableNum);
		}
		showTableStatus();
		res = 1;
		return res;
	}
	
	/*	tmp method for show table status */
	public void showTableStatus(){
		for(int i = 0; i < 20; i++){
			if(i%5 ==0 )System.out.println();
			String empty = " ";
			if(mTableMap.get(i+1)!= null){
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
			int totalMenuPrice = mMenuMap.get(menuName).getPrice() * entry.getValue();
			System.out.format("%10s%10d\n", menuName, totalMenuPrice);
		}
	}
	public int purchase(Scanner sc){
		int res = -1;
		
		if(mLoginStaffName == null){
			mCurrentErrorMessage = "Please login first";
			return res;
		}
		
		System.out.println("Input table number");
		int tableNum = sc.nextInt();
		
		TableInfo tableInfo = mTableMap.get(tableNum);
		if(tableInfo == null){
			mCurrentErrorMessage = "There is no order infomation";
			return res;
		}
		Map<String, Integer> orderMenuMap = tableInfo.mOrderMap;
		
		int totalSale = mTableMap.get(tableNum).mTotalOrderPrice;
		
		
		sales.addSale(mToday, orderMenuMap, totalSale);
		
		mTableMap.remove(tableNum);
		
		String customerName = tableInfo.mCustomerName;
		System.out.println("Customer name : " + customerName);
		
		// 해당 고객의 totalpurchase up
		mCustomerMap.get(customerName).addPurchase(totalSale);
		mCustomerMap.get(customerName).upgrade();
		
		// 로그인한 스태프의 실적 up
		mStaffMap.get(mLoginStaffName).addSales(totalSale);
		
		System.out.println("Today's total purchase : " + sales.getTotalSale());
		// 가장 많이 팔린 제품과 가장 적게 팔린 제품들이 같은 갯수로 중복 될 경우 처리를 해줘야함.
		System.out.println("Today's Most sales : " + sales.getMostSales(mToday));
		System.out.println("Today's Least sales : " + sales.getLeastSales(mToday));
		
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

		Staff staff = mStaffMap.get(name);

		if (staff != null) {
			if (staff.getName().equals(name)) {
				if (staff.getId() == id) {
					mLoginStaffName = name;
					return 1;
				}
			}
		}
		mCurrentErrorMessage = "Fail to login";
		return res;
	}


	public int isLoginStaffSupervisor() {
		int res = -1;
		if (mLoginStaffName == null) {
			mCurrentErrorMessage = "Please login";
		} else if (!mStaffMap.get(mLoginStaffName).isSupervisor()) {
			mCurrentErrorMessage = "Only Supervisor can add";
		} else {
			res = 1;
		}
		return res;
	}
}
