

public interface GameLogicable
{
	//Constants:
	public static final int MAX_GAME_STATES = 6;  //The total number of states in a game.
	
	//Methods:
	public Handable getCPUHand();  //Returns the hand (Handable) of the CPU player.

	public Handable getHumanHand();  //Returns the hand (Handable) of the human player.
	
	public boolean nextState(String[] messages);  //The GUI will call this method to proceed to the next stage/state of the game. The String[] parameter is an empty array, which the method can fill up with messages that will be displayed in the GUI to describe the current state of the game. The size of this array is determined by the number of lines that can be displayed in the GUI, and this is stored in PokerTableDisplay.NUM_MESSAGE_ROWS. It is set to 4 for this assignment (4 lines max). Leaves empty rows (where nothing should be displayed) to null.
	//The method returns a boolean that indicates if the proceed button in the GUI should be enabled (return true) or not (return false). This was done to get more flexibility in the GUI, but is not really useful currently. You can just return true all the time.
}