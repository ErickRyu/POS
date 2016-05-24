package Control;

public class TableControl {
	POS mPos;
	public TableControl(POS pos){
		mPos = pos;
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

}
