
//-----------------------------------------
// NAME		: Nguyen Tien Thuan (Joseph Nguyen)
// STUDENT NUMBER	: 7900457
// COURSE		: COMP 2150
// INSTRUCTOR	: Dr Ali
// ASSIGNMENT	: assignment 3
// QUESTION	: question (all of it)      
// 
// REMARKS: This is the class containing the main method that will run the game. 
// You should add the appropriate constructor call in the main, to build a GameLogicable.
// You should not add any more code here than that constructor call.
//
//
//-----------------------------------------

public class PokerGame
{
	public static void main(String[] args)
	{
		//Build a game logic, feed it into the PokerTableDisplay
		GameLogicable gl = new GameLogic();  //Insert a call to the constructor of your class that implements GameLogicable
		PokerTableDisplay ptd = new PokerTableDisplay(gl);
	}
}
	