

public class Card implements Cardable{
    private Suit suit = null;
    private int value = 0;
    private boolean selected = false;
    private boolean faceUp = false;
    public Card(){
    }
    public Card(Suit suitInput, int valueInput){
        suit = suitInput;
        value = valueInput;
    }

    public Card(int valueInput, Suit suitInput){
        suit = suitInput;
        value = valueInput;
    }

    public boolean getSelected(){
        return selected;
    }
    public boolean getFaceUp(){
        return faceUp;
    }
	
	public Suit getSuit(){
        return suit;
    }

    public int getValue(){
        return value;
    }
	
	public void switchSelectedState(){
        if(selected) selected = false;
        else selected = true;
    }
	
	public void resetSelected(){
        selected = false;
    }
	
	public void setFaceUp(boolean input){
        faceUp = input;
    }

    @Override
    public String toString(){
        String output = "";
        if(value > 10){
            switch (value) {
                case 11:
                    output += "J";
                    break;
                case 12:
                    output += "Q";
                    break;
                case 13:
                    output += "K";
                    break;
                case 14:
                    output += "A";
                    break;
            }
        }
        else output += Integer.toString(value);
        switch (suit) {
            case HEART:
                output += '\u2665';
                break;
            case DIAMOND:
                output += '\u2666';
                break;
            case SPADE:
                output += '\u2660';
                break;
            case CLUB:
                output += '\u2663';
                break;
        }
        return output;
    }
}