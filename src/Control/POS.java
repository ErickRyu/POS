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
	public String mLoginStaffName;

	/* Save Database information */
	public Map<String, Staff> mStaffMap;
	public Map<String, Menu> mMenuMap;
	public Map<String, Customer> mCustomerMap;

	
	public Map<Integer, Table> mTableMap;		/* Save order information per table */
	
	public Sale mSale;
	public String mCurrentErrorMessage = "";
	
	
	JDBC jdbc;
	public POS() {
		mStaffMap = new HashMap<String, Staff>();
		mMenuMap = new HashMap<String, Menu>();
		mCustomerMap = new HashMap<String, Customer>();
		mSale = new Sale();
		mTableMap = new HashMap<>();
		
		jdbc = new JDBC();
		readData();
		new Command(this);
	}

	public void readData() {
		try {
			Scanner sc = new Scanner(new FileReader("data.txt"));
			int customerNum = sc.nextInt();
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
}
