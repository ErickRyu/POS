package Control;

import java.util.Scanner;

import Model.Menu;

public class MenuControl {
	POS mPos;
	public MenuControl(POS pos){
		mPos = pos;
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
}
