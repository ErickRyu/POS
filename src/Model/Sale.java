package Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Sale {
	static final int INF = 987654321;
	int mCumulativeSales;
	Map<String, Integer> mDaySalesMap;
	Map<String, Integer> mSalesVolumePerMenuMap;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public Sale(){
		mDaySalesMap = new HashMap<>();
		mSalesVolumePerMenuMap = new HashMap<>();
	}
	
	public int getCumulativeSales(){return mCumulativeSales;}
	public int getDaySales(String date){
		if(mDaySalesMap.get(date) == null) return -1;
		return mDaySalesMap.get(date);
	}
	public void addSales(Map<String, Integer> orderMap, int totalSales){
		Date date = new Date();
		String today = dateFormat.format(date);
		// Update cumulative sales
		mCumulativeSales += totalSales;
		
		// Update sales per day
		if(mDaySalesMap.get(today) != null){
			totalSales += mDaySalesMap.get(today);
		}
		mDaySalesMap.put(today, totalSales);
		
		// Update sale volume per menu
		for(Entry<String, Integer>orderEntry : orderMap.entrySet()){
			String menuName = orderEntry.getKey();
			int salesVolume = orderEntry.getValue();
			
			if(mSalesVolumePerMenuMap.get(menuName) != null){
				salesVolume += mSalesVolumePerMenuMap.get(menuName);
			}
			mSalesVolumePerMenuMap.put(menuName, salesVolume);
		}
		
	}
	public ArrayList<String> getMostSalesMenu(){
		int max = 0;
		ArrayList<String> mostSalesMenuList = new ArrayList<>();
		for(Entry<String, Integer> entry : mSalesVolumePerMenuMap.entrySet()){
			int volume = entry.getValue();
			if(max < volume){
				max = volume;
				mostSalesMenuList = new ArrayList<>();
				mostSalesMenuList.add(entry.getKey());
			}else if(max == volume){
				mostSalesMenuList.add(entry.getKey());
			}
		}
		return mostSalesMenuList;
	}
	public ArrayList<String> getLeastSalesMenu(){
		int min = INF;
		ArrayList<String> leastSalesMenuList = new ArrayList<>();
		for(Entry<String, Integer> entry : mSalesVolumePerMenuMap.entrySet()){
			int volume = entry.getValue();
			if(min > volume){
				min = volume;
				leastSalesMenuList = new ArrayList<>();
				leastSalesMenuList.add(entry.getKey());
			}else if(min == volume){
				leastSalesMenuList.add(entry.getKey());
			}
		}
		return leastSalesMenuList;
	}
	public ArrayList<String> getSaleDates(){
		ArrayList<String> saleDates = new ArrayList<>();
		saleDates.addAll(mDaySalesMap.keySet());
		return saleDates;
	}
}
