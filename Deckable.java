

import java.util.LinkedList;

public interface Deckable
{
	//Constant:
	public static final int NUM_CARDS = 52;
	
	//Methods:
	public void shuffle();  //This must shuffle the deck randomly.
	
	public void returnToDeck(LinkedList<Cardable> discarded);  //This must return the cards (Cardables) that were drawn previously (passed as a LinkedList<Cardable> parameter) back to the deck (do not recreate new cards, the same cards that were drawn must go back).
	
	public Cardable drawACard(boolean faceUp);  //This deals the card (Cardable) that is at the top of the deck, either with the face up (true) or down (false).
	
	
}