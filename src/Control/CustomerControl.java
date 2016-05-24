package Control;

import java.util.Scanner;

import Model.Customer;

public class CustomerControl {
	POS mPos;
	public CustomerControl(POS pos){
		mPos = pos;
	}
	public int searchCustomer(String searchName) {
		int res = -1;
		System.out.println(mPos);
		System.out.println(mPos.mCustomerMap);
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

}
