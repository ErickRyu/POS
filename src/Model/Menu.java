package Model;

public class Menu {
	private String mName;
	private int mGenId = 1000;
	private int mId;
	private int mPrice;
	public Menu(String name, int price){
		this.mName = name;
		this.mPrice = price;
		mId = mGenId++;
	}
	public String getName(){return mName;}
	public int getPrice(){return mPrice;}
	public int getId(){return mId;}
}
