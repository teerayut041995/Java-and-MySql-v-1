
public class Product {
	private int id;
	private String name;
	private String addDate;
	private float price;
	private byte[] picture;
	public Product(int pid , String pname , float pPrice , String pAddDate , byte[] pimage ){
		this.id = pid;
		this.name = pname;
		this.price = pPrice;
		this.addDate = pAddDate;
		this.picture = pimage;
	}
	public int getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public float getPrice(){
		return price;
	}
	public String getAddDate(){
		return addDate;
	}
	public byte[] getImage(){
		return picture;
	}
	
}