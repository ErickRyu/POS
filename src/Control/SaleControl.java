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
	public SaleControl(POS pos){
		mPos = pos;
	}
	
	/* Search from JDBC */
	public int searchSalesDB(String searchName) {

		int res = -1;
		try {
			String sqlStr = "select * from sale where day = '" + searchName + "'";
			ResultSet rs = mPos.jdbc.executeQueryAndGetResultSet(sqlStr);
			rs.next();
			Date day = rs.getDate("day");
			String sales = rs.getString("sales");
			
			sqlStr = "select sum(sales) from sale";
			rs = mPos.jdbc.executeQueryAndGetResultSet(sqlStr);
			rs.next();
			String cumulitiveSales = rs.getString("sum(sales)");
			
			System.out.println("Menu Info");
			System.out.println(day);
			
			System.out.println("총매출액");
			System.out.println(cumulitiveSales);
			
			System.out.println("가장 많이 팔린 메뉴");
			sqlStr = "select name from menu T where T.cumulitive > 0 and T.cumulitive >= all(select cumulitive from menu)";
			rs = mPos.jdbc.executeQueryAndGetResultSet(sqlStr);
			while(rs.next()){
				String mostSalesMenu = rs.getString("name");
				System.out.println(mostSalesMenu);
			}
			System.out.println();
			System.out.println("가장 적게 팔린 메뉴");
			sqlStr = "select name from menu T where T.cumulitive > 0 and T.cumulitive <= all (select cumulitive from menu where cumulitive > 0)";
			rs = mPos.jdbc.executeQueryAndGetResultSet(sqlStr);
			while(rs.next()){
				String leastSalesMenu = rs.getString("name");
				System.out.println(leastSalesMenu);
			}
			res = 1;

		} catch (SQLException e) {
			e.printStackTrace();
			mPos.mCurrentErrorMessage = "Not exist";
		}
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
	
	
	/* Purchas and save into database */
	public int purchaseDB(Scanner sc){
		int res = -1;

		
		if(isLogin() == -1){
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
		float discount = getDiscountRate(customerName);
		System.out.println(discount);
		totalSale *= discount;
		
		/* Save today's total sales*/
		
		addSale(orderMenuMap, totalSale);
	
		
		/* Remove table information */
		mPos.mTableMap.remove(tableNum);
		
		
		System.out.println("Customer name : " + customerName);
		
		/* customer's total purchase up */
		
		String sqlStr = "update customer " + 
				"set total_purchase = total_purchase + " + totalSale +
				"where name = '" + customerName + "'";
		try{
			mPos.jdbc.executeQuery(sqlStr);
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		
		/* Update Sales of login staff */
		
		sqlStr = "update staff " + 
				"set sales = sales + " + totalSale +
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
		for(Entry<String, Integer> orderEntry : orderMap.entrySet()){
			String menuName = orderEntry.getKey();
			int saleNum = orderEntry.getValue();
			
			try{
			String sqlStr = "update menu " +
						"set cumulitive = cumulitive +" + saleNum +
						"where name = '" + menuName  +"'";
			mPos.jdbc.executeQuery(sqlStr);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		try{	
			
			String sqlStr = "select count(day) from sale where day = '" + today + "'";
			ResultSet rs = mPos.jdbc.executeQueryAndGetResultSet(sqlStr);
			rs.next();
			int count = rs.getInt("count(day)");
			if(count == 1){
				sqlStr = "update sale "+
				"set sales = sales + " +totalSale + 
				"where day = '" + today + "'";
			}else{
				sqlStr = "insert into sale values ('" + today + "', " + totalSale + ")";
			}
			System.out.println(sqlStr);
			System.out.println("today : " + today);
			mPos.jdbc.executeQuery(sqlStr);
			res = 1;
		}catch(SQLException e){
			System.out.println("fail");
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
			String sqlStr = "select discount from grade where grade_name = (select grade from customer where name = '" + customerName + "')";
			ResultSet rs = mPos.jdbc.executeQueryAndGetResultSet(sqlStr);
			rs.next();
			res = rs.getFloat("discount");
			System.out.println(res);
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
	
	public int isLogin(){
		int res = -1;
		if(mPos.mLoginStaffName == null){
			mPos.mCurrentErrorMessage = "Please login first";
			return res;
		}
		res = 1;
		return res;
		
	}
}

