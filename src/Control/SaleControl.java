package Control;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import Model.Table;

public class SaleControl {
	POS mPos;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	enum DISCOUNT{
		Gold(.7f), Silver(.8f), Bronze(.9f), Normal(1);
		float discount;
		DISCOUNT(float value){
			this.discount = value;
		}
		
		public float getDiscount(){
			return this.discount;
		}
	}
	public SaleControl(POS pos){
		mPos = pos;
	}
	
	public int searchSales(String searchName) {
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
	
	/* Search from JDBC */
	public int searchSalesDB(String searchName) {

		int res = -1;
		try {
			String sqlStr = "select * from sale where day = '" + searchName + "'";
			ResultSet rs = mPos.jdbc.executeQueryAndGetResultSet(sqlStr);
			rs.next();
			String name = rs.getString("day");
			String sales = rs.getString("sales");
			
			sqlStr = "select sum(sales) from sale";
			rs = mPos.jdbc.executeQueryAndGetResultSet(sqlStr);
			rs.next();
			String cumulitiveSales = rs.getString("sum(sales");
			
			System.out.println("Menu Info");
			System.out.println(name);
			System.out.println(sales);
			System.out.println(cumulitiveSales);
			
			res = 1;

		} catch (SQLException e) {
			mPos.mCurrentErrorMessage = "Not exist";
		}
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
		String customerName = tableInfo.mCustomerName;
		
		
		// Get original totalSale
		int totalSale = mPos.mTableMap.get(tableNum).mTotalOrderPrice;
		// discount totalSale using grade
		String customerGrade = mPos.mCustomerMap.get(customerName).getGrade();
		float discount = DISCOUNT.valueOf(customerGrade).getDiscount();
		System.out.println(discount);
		totalSale *= discount;
		
		/* Save today's total sales*/
		mPos.mSale.addSales(orderMenuMap, totalSale);
		
		/* Remove table information */
		mPos.mTableMap.remove(tableNum);
		
		
		System.out.println("Customer name : " + customerName);
		
		// 해당 고객의 totalpurchase up
		mPos.mCustomerMap.get(customerName).addPurchase(totalSale); 
		mPos.mCustomerMap.get(customerName).upgrade();
		
		// 로그인한 스태프의 실적 up
		mPos.mStaffMap.get(mPos.mLoginStaffName).addSales(totalSale);
		
		showTableStatus();
		
		res = 1;
		return res;
	}
	
	/* Purchas and save into database */
	public int purchaseDB(Scanner sc){
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
		String customerName = tableInfo.mCustomerName;
		
		
		// Get original totalSale
		int totalSale = mPos.mTableMap.get(tableNum).mTotalOrderPrice;
		// discount totalSale using grade
//		String customerGrade = mPos.mCustomerMap.get(customerName).getGrade();
		float discount = getDiscountRate(customerName);
		System.out.println(discount);
		totalSale *= discount;
		
		/* Save today's total sales*/
//		mPos.mSale.addSales(orderMenuMap, totalSale);
		
		addSale(orderMenuMap, totalSale);
	
		
		/* Remove table information */
		mPos.mTableMap.remove(tableNum);
		
		
		System.out.println("Customer name : " + customerName);
		
		/* customer's total purchase up */
//		mPos.mCustomerMap.get(customerName).addPurchase(totalSale); 
//		mPos.mCustomerMap.get(customerName).upgrade();
		
		String sqlStr = "update set customer" + 
				"total_purchase = total_purchase + " + totalSale;
		try{
			mPos.jdbc.executeQuery(sqlStr);
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		
		/* Update Sales of login staff */
//		mPos.mStaffMap.get(mPos.mLoginStaffName).addSales(totalSale);
		
		sqlStr = "update set staff" + 
				"sales = sales + " + totalSale +
				"where name = '" + mPos.mLoginStaffName + "'";
		try{
			mPos.jdbc.executeQuery(sqlStr);
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		
		showTableStatus();
		
		res = 1;
		return res;
	}
	
	public int addSale(Map<String, Integer>orderMap, int totalSale){
		int res = -1;
		Date date = new Date();
		String today = dateFormat.format(date);
		try{
			//menu update
			
			
			
			String sqlStr = "update set sale"+
				"sales = sales + " +totalSale + 
				"where day = '" + today + "'";
			mPos.jdbc.executeQuery(sqlStr);
			res = 1;
		}catch(SQLException e){
			System.out.println("fail");
//			e.printStackTrace();
		}
		if(res == 1){
			return res;
		}
		res = -1;
		
		try{
			String sqlStr = "insert into sale values('" + today + "', " + totalSale + ")";
			mPos.jdbc.executeQuery(sqlStr);
			res = 1;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return res;
	}
	
	public float getDiscountRate(String customerName){
		float res = 1f;
		try{
			String sqlStr = "select discount from grade customer where name = '" + customerName + "'";
			ResultSet rs = mPos.jdbc.executeQueryAndGetResultSet(sqlStr);
			rs.next();
			res = rs.getFloat("discount");
		}catch(SQLException e){
			e.printStackTrace();
		}
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
