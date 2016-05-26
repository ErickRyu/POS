package Control;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import DB.JDBC;
import Model.Customer;
import Model.Menu;
import Model.Sale;
import Model.Staff;
import Model.Table;

public class POS {
	// member 변수 앞에는 m을 붙이는 것.

	/* Save name who login */
	String mLoginStaffName;

	/* Save Database information */
	Map<String, Staff> mStaffMap;
	Map<String, Menu> mMenuMap;
	Map<String, Customer> mCustomerMap;

	
	Map<Integer, Table> mTableMap;		/* Save order information per table */
	
	Sale mSale;
	String mCurrentErrorMessage = "";
	
	
	JDBC jdbc;
	public POS() {
		mStaffMap = new HashMap<String, Staff>();
		mMenuMap = new HashMap<String, Menu>();
		mCustomerMap = new HashMap<String, Customer>();
		mSale = new Sale();
		mTableMap = new HashMap<>();
		
		
		jdbc = new JDBC();
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
		Command command = new Command(this);
		SaleControl saleCtr = new SaleControl(this);
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
				res = command.search(sc);
				break;
			case 2:
				res = command.add(sc);
				break;
			case 3:
				res = saleCtr.order(sc);
				break;
			case 4: 
				res = saleCtr.purchase(sc);
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

	public int isLogin(){
		int res = -1;
		if(mLoginStaffName == null){
			mCurrentErrorMessage = "Please login first";
			return res;
		}
		res = 1;
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
