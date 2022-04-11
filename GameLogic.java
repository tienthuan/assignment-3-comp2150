
 
public class GameLogic implements GameLogicable{
    private Handable playerHand;
    private Handable cpuHand;
    private Deckable deck;
    private int stage;
    private int gameNum;
    boolean CPUSmart = false;
    private int playerWins;
    private int cpuWins;

    public GameLogic(){
        playerHand = new Hand();
        cpuHand = new Hand();
        deck = new Deck();
        stage = 0;
        gameNum = 1;
        playerWins = 0;
        cpuWins = 0;
    }

    public Handable getCPUHand(){
        return cpuHand;
    }

    public Handable getHumanHand(){
        return playerHand;
    }
    private void CPUThink(){
        if(!CPUSmart){
            for(int i = 0; i < 5; i ++){
                double discarded = Math.random();
                if(discarded > 0.5){
                    cpuHand.getCard(i).switchSelectedState();
                }
            }
        }
        else{
            cpuHand.evaluateHand();
            Hand hand = (Hand) cpuHand;
            if(hand.getPoints() <= 2000050){
                for(int i = 0; i < 5; i ++){
                    Card currCard = (Card) cpuHand.getCard(i);
                    int currCardValue = currCard.getValue();
                    if(currCardValue < 8){
                        cpuHand.getCard(i).switchSelectedState();
                    }
                }
            }
        }
        cpuHand.discard();
    }
    private String[] executeNextState(String[] message){
        switch (stage) {
            case 0:
                deck.shuffle();
                playerHand.draw(deck,true);
                cpuHand.draw(deck,false);
                message[0] = " beginning of game " + gameNum;
                message[1] = "click on the proceed button to start game";       
                stage ++;    
                break;
            case 1:
                message[0] = "player 1 choose which card to discard";
                message[1] = "and click on the proceed button"; 
                stage ++;
                break;
            case 2:
                deck.returnToDeck(playerHand.discard());  
                message[0] = "Player has discarded card"; 
                if(CPUSmart)
                message[1] = "CPU is thinking hard";
                else message[1] = "stoopid CPU is thinking hard";
                CPUThink();
                stage ++;
                break;
            case 3:
                message[0] = "CPU has discarded card";
                message[1] = "Each player will be dealt cards";
                playerHand.draw(deck,true);
                cpuHand.draw(deck,false);
                stage ++;
                break;
            case 4:
                message[0] = "Both players have drawn";
                message[1] = "Click on Proceed to see result";
                stage ++;
                break;
            case 5:
                cpuHand.showAllCards();
                String cpuType = cpuHand.evaluateHand();
                String playerType = playerHand.evaluateHand();
                int result = playerHand.compareTo(cpuHand);
                switch (result){
                    case 1:
                    message[2] = "Player has won";
                    playerWins++;
                    case 0:
                    message[2] = "Its a tie";
                    case -1:
                    message[2] = "You lost to a bot created by me, skill issue much?";
                    cpuWins++;
                }
                message[0] = "player hand type is: " + playerType;
                message[1] = "cpu hand type is: " + cpuType;
                message[3] = "Player has won " + playerWins + " games, cpu has won " + cpuWins + "games";
                stage ++;
                break;
            case 6:
                deck.returnToDeck(playerHand.returnCards());
                deck.returnToDeck(cpuHand.returnCards());
                gameNum++;
                message[0] = "press proceed to move on to next game";
                stage = 0; 
                break;
            
        }
        return message;
    }
    public boolean nextState(String[] message){
        message = executeNextState(message);
        return true;
    }
} 