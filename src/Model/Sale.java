package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Sale {
	static final int INF = 987654321;
	int mCumulativeSales;
	Map<String, Integer> mDaySalesMap;
	Map<String, Integer> mSalesVolumePerMenuMap;
	public Sale(){
		mDaySalesMap = new HashMap<>();
		mSalesVolumePerMenuMap = new HashMap<>();
	}
	
	public int getCumulativeSales(){return mCumulativeSales;}
	public int getDaySales(String date){
		if(mDaySalesMap.get(date) == null) return -1;
		return mDaySalesMap.get(date);
	}
	public void addSales(String date, Map<String, Integer> orderMap, int totalSales){
		// Update cumulative sales
		mCumulativeSales += totalSales;
		
		// Update sales per day
		if(mDaySalesMap.get(date) != null){
			totalSales += mDaySalesMap.get(date);
		}
		mDaySalesMap.put(date, totalSales);
		
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
}
