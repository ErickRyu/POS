package Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Sales {
	final int INF = 9874321;
	int mTotalSale;
	String mDate;
	Map<String, SalePerDate> mSaleMap;
	public Sales(){
		mSaleMap = new HashMap<>();
	}
	
	
	public class SalePerDate{
		Map<String, Integer> mSaleMenuMap;
		int mTotalSale;
		SalePerDate(){
			mSaleMenuMap = new HashMap<String, Integer>();
		}
		public Map<String, Integer> getSaleMenuMap(){
			return mSaleMenuMap;
		}
	}
	
	
	
	public void addSale(String date, Map<String, Integer> saleMenuMap, int totalSale){
		SalePerDate saleInfo = mSaleMap.get(date);
		mTotalSale += totalSale;
		if(saleInfo == null){
			saleInfo = new SalePerDate();
			mSaleMap.put(date, saleInfo);
		}
		saleInfo.mTotalSale += totalSale;
		Map<String, Integer> savedMenuMap = saleInfo.mSaleMenuMap; /* ±‚¡∏ ¿˙¿Âµ≈¿÷¥¯ ∏  */
		for(Entry<String, Integer> saleMenu : saleMenuMap.entrySet()){
			String menuName = saleMenu.getKey();
			int saleNum = saleMenu.getValue();
			
			if(savedMenuMap.get(menuName)== null){
				savedMenuMap.put(menuName, saleNum);
			}else{
				savedMenuMap.put(menuName, savedMenuMap.get(menuName) + saleNum);
			}
		}
		
	}
	
	public int getTotalSale(){
		return mTotalSale;
	}
	
	public int getTotalSaleDay(String date){
		return mSaleMap.get(date).mTotalSale;
	}
	
	public String getMostSales(String date){
		String mostSales = "";
		int max = 0;
		SalePerDate saleInfo = mSaleMap.get(date);
		if(saleInfo == null){
			mSaleMap.put(date, new SalePerDate());
		}
		Map<String, Integer> saleMenuMap = mSaleMap.get(date).getSaleMenuMap();
		for(Entry<String, Integer> menu : saleMenuMap.entrySet()){
			if( max < menu.getValue()){
				max = menu.getValue();
				mostSales = menu.getKey();
			}
		}
		return mostSales;
		
	}
	public String getLeastSales(String date){
		String leastSales = "";
		int min = INF;
		SalePerDate saleInfo = mSaleMap.get(date);
		if(saleInfo == null){
			mSaleMap.put(date, new SalePerDate());
		}
		Map<String, Integer> saleMenuMap = mSaleMap.get(date).getSaleMenuMap();
		for(Entry<String, Integer> menu : saleMenuMap.entrySet()){
			if( min > menu.getValue()){
				min = menu.getValue();
				leastSales = menu.getKey();
			}
		}
		return leastSales;
	}
}
