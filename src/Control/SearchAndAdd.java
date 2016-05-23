package Control;

import java.util.Scanner;

import Model.Customer;
import Model.Menu;
import Model.Staff;

public class SearchAndAdd {
	POS mPos;
	public SearchAndAdd(POS pos){
		mPos = pos;
	}
	public int search(Scanner sc) {
		int res = -1;
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
		if (mPos.isLoginStaffSupervisor() == -1) {
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

}