package Model;

import java.util.HashMap;
import java.util.Map;

public class TableInfo{
	public String mCustomerName;				/* Save customer who order menu at that table*/
	public int mTotalOrderPrice;				/* Save total order price */
	public Map<String, Integer> mOrderMap;		/* Map to save order menu and each order number*/
	public TableInfo(){
		mCustomerName = "비회원";					/* Default customer name is 비회원  */
		mTotalOrderPrice = 0;
		mOrderMap = new HashMap<>();
	}
}