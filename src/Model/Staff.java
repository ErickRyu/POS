package Model;

public class Staff {
	private String mName;
	private static int mGenerateId = 1000;
	private int mId;
	private String mPosition;
	private int mSales;
	public Staff(String name,String position){
		this.mName = name;
		this.mPosition = position;
		this.mId = mGenerateId++;
	}
	
	public String getName(){return mName;}
	public int getId(){return mId;}
	public String getPosition(){return mPosition;}
	public boolean isSupervisor(){return mPosition.equals("Supervisor");}
	public void addSales(int sales){mSales += sales;}
	public int getSales(){return mSales;}
}
