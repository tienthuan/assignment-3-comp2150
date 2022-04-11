//This interface will be useful for the JUnit tests only
public interface TestableHand extends Handable
{
	//To add all the Cardables in the array to the hand.
	//Assume that the size of the array will be == Handable.HAND_SIZE
	public void addCards(Cardable[] cards);  
	
}