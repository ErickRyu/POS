package Control;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import Model.Table;

public class SaleControl {
	POS mPos;
	public SaleControl(POS pos){
		mPos = pos;
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
		res = 0;
		return res;
	}
	
	public int order(Scanner sc){
		int res = -1;
		if(mPos.isLogin() == -1){
			return res;
		}
		
		System.out.println("Input table number");
		int tableNum = sc.nextInt();
		
		System.out.println("Input customer name");
		String customerName = sc.next();
		//customer name 이 데이터베이스에 없을 시 비회원으로 저장
		if (mPos.mCustomerMap.get(customerName) == null)
			customerName = "비회원";
		
		Table tableInfo = mPos.mTableMap.get(tableNum);
		if(tableInfo == null){
			tableInfo = new Table();
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
	
	public int purchase(Scanner sc){
		int res = -1;
		
		if(mPos.isLogin() == -1){
			return res;
		}
		
		System.out.println("Input table number");
		int tableNum = sc.nextInt();
		
		Table tableInfo = mPos.mTableMap.get(tableNum);
		if(tableInfo == null){
			mPos.mCurrentErrorMessage = "There is no order infomation";
			return res;
		}
		Map<String, Integer> orderMenuMap = tableInfo.mOrderMap;
		
		int totalSale = mPos.mTableMap.get(tableNum).mTotalOrderPrice;
		
		
//		String today = Integer.toString(day++);
		mPos.mSale.addSales(orderMenuMap, totalSale);
		
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
	
	public void showOrderStatus(Map<String, Integer> orderMap){
		System.out.println();
		for(Entry<String, Integer> entry : orderMap.entrySet()){
			String menuName = entry.getKey();
			int totalMenuPrice = mPos.mMenuMap.get(menuName).getPrice() * entry.getValue();
			System.out.format("%10s%10d\n", menuName, totalMenuPrice);
		}
	}
}
