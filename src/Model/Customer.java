package Model;

public class Customer {
	private String mName;
	private static int genId = 1000;
	private int mId;
	private String mBirth;
	private String mPhone;
	private String mGrade;
	private int mTotalPurchase;
	
	public Customer(String name, String birth, String phone, String grade){
		mName = name;
		mBirth = birth;
		mPhone = phone;
		mGrade = grade;
		mId = genId++;
		mTotalPurchase = initTotalPurchase(grade);
	}
	public Customer(String name, String birth, String phone){
		mName = name;
		mBirth = birth;
		mPhone = phone;
		mGrade = "Normal";
	}
	private int initTotalPurchase(String grade){
		int res = 0;
//		(Gold:100만/Silver:50만/Bronze:30만/Normal)등의자료가있다
		switch(grade){
		case "Gold":
			res = 1000000;
			break;
		case "Silver":
			res = 500000;
			break;
		case "Bronze":
			res = 300000;
			break;
		case "Normal":
			res = 0;
			break;
		default:
		}
		return res;
	}
	public String getName(){return mName;}
	public int getId(){return mId;}
	public String getBirth(){return mBirth;}
	public String getPhone(){return mPhone;}
	public String getGrade(){return mGrade;}
	public void setGrade(){}
	public int getTotalPurchase(){return mTotalPurchase;}
	public void setTotalPurchase(){}
}
