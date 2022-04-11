import java.util.LinkedList;

public interface Handable extends Comparable<Handable>
//Extending Comparable<Handable> will require compareTo(Handable) to be implemented in the classes implementing Handable.
{
	//Constants:
	public static final int HAND_SIZE = 5;
	
	//Methods:
	public Cardable getCard(int i);  //Returns the ith Cardable element of the hand.
	
	public void draw(Deckable d, boolean faceUp);  //This should be called after the discard method (see below), and replace all discarded cards with cards drawn from the Deckable sent as a parameter (faceUp determines if the drawn cards should have the face up or down). 
	
	public void showAllCards();  //Flips all cards (Cardables) of the hand so that they face up. Useful for the showdown.
	
	public LinkedList<Cardable> discard();  //This method discards from the hand all the Cardables that have been selected  (that have selected state = true). The method returns a LinkedList<Cardable> containing all the cards that have been discarded from this hand.
	
	public LinkedList<Cardable> returnCards();  //This method will be called at the end of a round to empty the hand (discard the full hand, not considering the selected state). It returns a LinkedList<Cardable> containing all the cards that were in the hand.
	
	public String evaluateHand();  //This method evaluates what is in the hand, and returns a String description of the best poker hand that can be made with these cards.
}