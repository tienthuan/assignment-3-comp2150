

public interface Cardable
{
	//Enum for the suit. Enum works like a type that has predefined constant values that can be assigned to it.
	//Declare a variable of this type like this: Suit mySuit;
	//Access one specific value like this: Suit.HEART
	//From a different class, access using the interface name: Cardable.Suit.DIAMOND
	public enum Suit {HEART, DIAMOND, SPADE, CLUB};
	
	//Methods:
	public boolean getSelected();  //Returns the selected state: selected (true) or not selected (false).
	
	public boolean getFaceUp();  //Returns true if the face is up, false if it is facing down.
	
	public Suit getSuit();  //Returns the suit.
	
	public void switchSelectedState();  //Switches the selected state: if it was true it becomes false and vice versa.
	
	public void resetSelected();  //Sets selected state to false (the default state).
	
	public void setFaceUp(boolean faceUp);  //Sets the faceUp to what is received as a parameter.
}