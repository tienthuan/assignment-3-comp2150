import java.util.LinkedList;
import java.lang.Math;


public class Hand implements TestableHand{
    protected LinkedList<Cardable> hand = new LinkedList<Cardable>();
    //each hand has points to compare to other hands
    //straight flush = 900000; 4 of a kind = 800000 and so on
    //value has to be high so the composite value of cards^times doesnt mess with results like A^4
    //because all cards showdown ultimately boils down to higher values
    //we assign cards points so it make things easier
    //points of each cards will be prime so there's no 4^2 = 2^4 situations
    // 2 = 2; 3 = 3;  4 = 5; 5 = 7;
    //points will be calculated based on (hand type) + [(card value ^ times it appears) + ...next cards] (on some type of hands)
    //main deciding factor will sit in the int portion while less important will sit in decimal factor
    //EXP: 70000049.45 vs 70000049.32 we compare the 700000049 first then the 45 vs 32
    private double points;
    public Hand(){
        for(int i = 0; i < Handable.HAND_SIZE; i ++){
            hand.add(null);
        }
        points = 0.0;
    }
    public Cardable getCard(int i){
        if(i >= Handable.HAND_SIZE || i < 0)
            return null;
        return hand.get(i);
    }

    public void draw(Deckable d, boolean faceUp){
        for(int i = 0; i < Handable.HAND_SIZE; i ++){
            Cardable card = d.drawACard(faceUp);
            if(hand.get(i) == null){
                hand.set(i,card);
            }
        }
    }
    
    public void showAllCards(){
        for(Cardable card : hand)
            card.setFaceUp(true);
    }
    //I was gonna just remove it but I think the GUI expect a hand with 5 objects so I'm not sure;
    public LinkedList<Cardable> discard(){
        LinkedList<Cardable> discarded = new LinkedList<Cardable>();
        for(int i = 0; i < hand.size(); i++){
            if(hand.get(i).getSelected()){
                discarded.add(hand.get(i));
                hand.set(i, null);
            }
        }
        return discarded;
    }
    
    public LinkedList<Cardable> returnCards(){
        LinkedList<Cardable> discarded = new LinkedList<Cardable>();
        for(int i = 0; i < hand.size(); i++){
            discarded.add(hand.get(i));
            hand.set(i, null);
        }
        points = 0;
        return discarded;
    }

    public double getPoints(){return points;}

    public int compareTo(Handable other){
        int result = 0;
        Hand otherHand = (Hand) other;
        int currMainPoint = (int) points;
        double currXtraPoint = points - currMainPoint;
        int otherMainPoint = (int) otherHand.getPoints();
        double otherXtraPoint =  otherHand.getPoints() - otherMainPoint;
        //if else hell
        if(currMainPoint > otherMainPoint)
            result = 1;
        else if(currMainPoint < otherMainPoint)
            result = -1;
        else{
            if(currXtraPoint > otherXtraPoint)
                result = 1;
            else if(currXtraPoint < otherXtraPoint)
                result = -1;
        }
        return result;
    }

    private int findLowCard(){
        int low = 100;
        for(Cardable card : hand){
            Card curr = (Card) card;
            if(curr.getValue() < low){
                low = curr.getValue();
            }
        }
        return low;
    }
    private int findHighCard(){
        int high = -1;
        for(Cardable card : hand){
            Card curr = (Card) card;
            if(curr.getValue() > high){
                high = curr.getValue();
            }
        }
        return high;
    }

    //turn a num into full decimals places, 35 -> 0.35
    //used to calculate points
    private double makeDecimals(int x){
        double result = 0;
        int xLength = (int) (Math.log10(x) + 1);
        result += x / Math.pow(10.0,xLength);
        return result;
    }

    private double makeDecimals(double x){
        double result = 0;
        int xLength = (int) (Math.log10(x) + 1);
        result += x / Math.pow(10.0,xLength);
        return result;
    }

    private void calculateFours(int[] primes,int[] valueArr){
        points = 8000000;
            for(int i = 13; i > 0; i --){
                if(valueArr[i] == 4)
                points += Math.pow(primes[i - 1],4);
                //refer to points cmts
                if(valueArr[i] != 0)
                points += makeDecimals(primes[i - 1]);
            }
    }

    private void calculateBoat(int[] primes,int[] valueArr){
        points = 7000000; 
            for(int i = 13; i > 0 ; i --){
                if(valueArr[i] == 3)
                points += Math.pow(primes[i - 1],3);
                if(valueArr[i] == 2)
                points += makeDecimals(Math.pow(primes[i - 1],2));
            }

    }
    private void calculateFlush(int[] primes,int[] valueArr){
        points = 6000000; 
        boolean highest = true;
        //I have to do this to prevent hands like A 5 4 3 2 vs K Q J 10 8
            for(int i = 13; i > 0 ; i --){
                if(valueArr[i] != 0){
                    if(highest){
                    points += primes[i - 1];
                    highest = false;
                    }
                    else
                    points += makeDecimals(primes[i - 1]);
                }
            }
    }
    private void calculateThrees(int[] primes,int[] valueArr){
        points = 3000000;
            for(int i = 13; i > 0; i --){
                if(valueArr[i] == 3)
                points += Math.pow(primes[i - 1],3);
                //refer to points cmts
                if(valueArr[i] != 0)
                points += makeDecimals(primes[i - 1]);
            }
    }
    private void calculatePairs(int[] primes,int[] valueArr){
        points = 2000000;
        for(int i = 13; i > 0; i --){
            if(valueArr[i] == 2)
            points += Math.pow(primes[i - 1],2);
            //refer to points cmts
            if(valueArr[i] != 0)
            points += makeDecimals(primes[i - 1]);
        }
    }
    private void calculateHigh(int[] primes,int[] valueArr){
        points = 100000; 
        boolean highest = true;
        //I have to do this to prevent hands like A 5 4 3 2 vs K Q J 10 8
            for(int i = 13; i > 0 ; i --){
                if(highest)
                points += primes[i - 1];
                else
                points += makeDecimals(primes[i - 1]);
            }
    }
    //there's a bit field card calculator right on stackoverflow btw
    public void calculatePoints(String handType,int[] valueArr){
        int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41};
        switch (handType){
            case "royal flush":
                points = 99999999;
                break;
            case "straight flush":
                points = 9000000;
                if(valueArr[0] == 0 & valueArr[4] == 0)
                points += findHighCard();
                else points += 4; // lowest kind of straight flush
                break;
            case "four of a kind":
                calculateFours(primes, valueArr);
                break;
            case "boat":
                calculateBoat(primes, valueArr);
                break;
            case "flush":
                calculateFlush(primes,valueArr);
                break;
            case "straights":
                points = 6000000;
                if(valueArr[0] == 0 & valueArr[4] == 0)
                points += findHighCard();
                else points += 4; // lowest kind of straights
                break;
            case "3 of a kind":
                calculateThrees(primes,valueArr);
                break;
            case "2 pairs":
                calculatePairs(primes, valueArr);
                break;
            case "1 pair":
                calculatePairs(primes, valueArr);
                break;
            default:
                calculateHigh(primes, valueArr);
                break;
        }
    }
    //based on numbers of quads, penta and triples...etc we can determine the hand type
    //we then feed the hand type and the value of the hand into the point calculator above
    private String evaluateHand(int noOfQuads,int noOfTris,int noOfPairs,int straightsCounter, boolean flush){
        String output = "";
            if(straightsCounter == 5 & flush){
                output = "straight flush";
                int high,low;
                high = findHighCard();
                low = findLowCard();
                if(high == 14 & low != 2){//HIGHEST IS ACE AND LOWEST AINT 2
                    output = "royal flush";
                }
            }
            else if(noOfQuads != 0){
                output = "four of a kind";
            }
            else if(noOfPairs == 1 & noOfTris == 1){
                output = "boat";
            }
            else if(flush){
                output = "flush";
            }
            else if(straightsCounter == 5){
                output = "straights";
            }
            else if(noOfTris == 1 & noOfPairs == 0){
                output = "3 of a kind";
            }
            else if(noOfPairs == 2){
                output = "2 pairs";
            }
            else if(noOfPairs == 1){
                output = "1 pair";
            }
            else{output = "highCard";}
        return output;
    }
    //count number of special occurances like triples and pairs
    private String evaluateHand(int[] valueArr,int[] suitArr){
        String output = "";
        //used for counter pairs, double pairs, triples... from value
        int noOfQuads,noOfTris,noOfPairs;
        noOfQuads = noOfTris = noOfPairs = 0;
        // if this hits 5 we got a straights.
        int straightsCounter = 0;
        
        //used to count flushes
        boolean flush = false;

        for(int i = 13; i >= 0; i--){
            switch (valueArr[i]){
                case 4:
                    noOfQuads++;
                    break;
                case 3:
                    noOfTris++;
                    break;
                case 2:
                    noOfPairs++;
                    break;
                case 1:
                    if(straightsCounter != 5)
                    straightsCounter++;
                    break;
                case 0:
                    if(straightsCounter != 5)
                    straightsCounter = 0;
                    break;
            }
        }
        for(int i = 3; i >= 0; i--){
            if(suitArr[i] == 5){
                flush = true;
                    break;
            }
        }
        output = evaluateHand(noOfQuads, noOfTris, noOfPairs, straightsCounter, flush);
        calculatePoints(output, valueArr);
        return output;
    }
    public String evaluateHand(){
        String output = "";
        int[] valueArr = new int[14];
        // [A|2|3|4|5|6|7|8|9|10|J|Q|K|A]
        //we have A at the start for counting straights like A2345.
        int[] suitArr = new int[4];
        // [SPADES|CLUBS|DIAMOND|HEART]
        for(Cardable card: hand){
            Card curr = (Card) card;
            Cardable.Suit cardSuit = curr.getSuit();
            int value = curr.getValue();
            //for Aces
            if(value == 14){
                valueArr[0]++;
                valueArr[value - 1]++;
            }
            else{
                valueArr[value - 1]++;
            }

            switch (cardSuit){
                case HEART:
                    suitArr[3]++;
                    break;
                case DIAMOND:
                    suitArr[2]++;
                    break;
                case CLUB:
                    suitArr[1]++;
                    break;
                case SPADE:
                    suitArr[0]++;
                    break;
            }
        }
        output = evaluateHand(valueArr, suitArr);
        return output;
    }

    public void addCards(Cardable[] cards){
        for(Cardable card : cards){
            if(hand.getFirst() == null){
                hand.removeFirst();
                hand.addLast(card);
            }
        }
        evaluateHand();
    }
}
