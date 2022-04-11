import java.util.Collections;
import java.util.LinkedList;

public class Deck implements Deckable {
    private LinkedList<Cardable> deck = new LinkedList<Cardable>();
    public Deck(){
        //fill the deck in order 2: heart diamond spade club -> 3: heart... A
        for(int i = 2; i < 15; i++){
            for(int j = 0; j < 4; j++){
                Cardable card = new Card();
                switch (j) {
                    case 0:
                        card = new Card(Cardable.Suit.HEART,i);
                        break;
                    case 1:
                        card = new Card(Cardable.Suit.DIAMOND,i);
                        break;
                    case 2:
                        card = new Card(Cardable.Suit.SPADE,i);
                        break;
                    case 3:
                        card = new Card(Cardable.Suit.CLUB,i);
                        break;
                }
                deck.addLast(card);
            }
        }
    }

    public void shuffle(){
        Collections.shuffle(deck);
    }

    public void returnToDeck(LinkedList<Cardable> discarded){
        deck.addAll(discarded);
    }

    public Cardable drawACard(boolean faceUp){
        Cardable toDraw = new Card();
        toDraw = deck.removeFirst();
        toDraw.setFaceUp(faceUp);
        return toDraw;
    }
}
