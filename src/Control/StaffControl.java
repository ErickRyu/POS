package Control;

import java.util.Scanner;

import Model.Staff;

public class StaffControl {
	POS mPos;
	public StaffControl(POS pos){
		mPos = pos;
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
