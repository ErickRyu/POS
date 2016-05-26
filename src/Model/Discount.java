package Model;

public class Discount{
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
	public static void main(String[] args) {
		String grade = "Gold";
		DISCOUNT type = DISCOUNT.Gold;
		System.out.println(type.getDiscount());
		switch(type){
			case Gold:
				System.out.println(DISCOUNT.Gold.getDiscount());
				break;
			case Silver:
				System.out.println(DISCOUNT.Silver.getDiscount());
				break;
			case Bronze:
				System.out.println(DISCOUNT.Bronze.getDiscount());
				break;
		}
	}
}
