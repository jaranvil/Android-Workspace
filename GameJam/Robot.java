import java.util.Random;

public class Robot {
	//properties
	private int health;
	private String purpose;
	private String[] purposes = {"BoogieBot", "TuesdayBot"};
	Random randomNum = new Random();
	
	public String getPurpose()
	{
		return purpose;
	}
	public int getHealth() 
	{
		return health;
	}
	public void setHealth(int health) 
	{
		this.health = health;
	}
	
	public Robot()
	{
		this.health = 10;
		assignPurpose();
	
	}//end Robot constructor
	
	private void assignPurpose()
	{
		int purposeNum = randomNum.nextInt(purposes.length);
		this.purpose = purposes[purposeNum];
	}// end getPurpose

}//end class
