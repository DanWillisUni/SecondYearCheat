package question2;

import java.util.Iterator;
import java.util.Random;

public class BasicStrategy implements Strategy {
    /**
     * Decides on whether to cheat or not
     * @param b   the bid this player has to follow (i.e the
     * bid prior to this players turn.
     * @param h	  The players current hand
     * @return False unless has to cheat
     */
    @Override
    public boolean cheat(Bid b, Hand h) {
        Card.Rank br = b.getRank();
        Iterator<Card> iterator = h.iterator();
        while (iterator.hasNext()){
            Card card = iterator.next();
            if ((card.getRank().getPrevious().equals(br))||(card.getRank().equals(br))||(card.getRank().getNext().equals(br))){
                return false;
            }
        }
        return true;
    }
    /**
     * If Cheating:  play a single card selected randomly
     * If not cheating: always play the maximum number of cards possible of the lowest rank possible
     * @param b   the bid the player has to follow.
     * @param h	  The players current hand
     * @param cheat true if the Strategy has decided to cheat (by call to cheat())      *
     * @return The bid to be played
     */
    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat) {
        Hand bh = new Hand();
        Card.Rank r;
        if (cheat){
            Random random = new Random();
            int i = random.nextInt(h.size());            
            bh.add(h.getIndex(i));
            int rand = random.nextInt(3);            
            if (rand== 0){
                r = b.getRank().getPrevious();
            } else if (rand == 1){
                r = b.getRank();
            } else {
                r=b.getRank().getNext();
            }            
        }else {
            int highest = h.countRank(b.getRank().getPrevious());
            r = b.getRank().getPrevious();
            if (h.countRank(b.getRank()) > highest){
                highest = h.countRank(b.getRank());
                r = b.getRank();
            }
            if (h.countRank(b.getRank().getNext()) > highest){
                r = b.getRank().getNext();
            }
            Iterator<Card> iterator = h.iterator();
            while (iterator.hasNext()){
                Card card = iterator.next();
                if (card.getRank() == r){
                    bh.add(card);
                }
            }
        }
        return new Bid(bh,r);
    }
    /**
     * @param h	The players current hand
     * @param b the current bid
     * @return True: only when certain they are cheating (based on your own hand)
     */
    @Override
    public boolean callCheat(Hand h, Bid b) {
        int s = b.getCount();
        s+= h.getRankCount(b.getRank().ordinal());
        return s > 4;
    }
}
