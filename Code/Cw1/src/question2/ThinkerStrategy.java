package question2;

import java.util.Iterator;
import java.util.Random;

public class ThinkerStrategy implements Strategy {
    private Hand currentKnownDiscards = new Hand();
    private double p;
    private int[] saidRankCount = new int[13];
    /**
     * Constructor
     * @param p probability to call cheat
     */
    public ThinkerStrategy(double p){
        this.p=p;
    }
    /**
     * Sets p to 0.8 as this is a relativly sensible probability
     */
    public ThinkerStrategy(){
        this(0.1);
    }
    /**
     * The Thinker should of course cheat if it has to. It should also occasionally cheat when it doesn’t have to.
     * @param b   the bid this player has to follow (i.e the bid prior to this players turn.
     * @param h	  The players current hand
     * @return if its going to cheat
     */
    @Override
    public boolean cheat(Bid b, Hand h) {
        if (hasToCheat(b,h)){
            return true;
        }
        Random random = new Random();
        int i = random.nextInt(5);
        //I took ocassionally to mean 2/5
        return i <= 1;
    }
    /**
     * ADDED
     * Determines is the player has to cheat
     * @param b previous bid
     * @param h hand of player
     * @return if the player has to cheat
     */
    private boolean hasToCheat(Bid b, Hand h) {
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
     *  If cheating, the Thinker should be more likely to choose higher
     * cards to discard than low cards. If not cheating, it should usually play all its
     * cards but occasionally play a random number.
     * @param b   the bid the player has to follow.
     * @param h	  The players current hand
     * @param cheat true if the Strategy has decided to cheat (by call to cheat())
     * @return The bid that the computer selects
     */
    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat) {
        Hand bh = new Hand();
        Card.Rank r;
        if (cheat){
            bh.add(randomCard(h));
            Random random = new Random();
            int rand = random.nextInt(3);
            if (rand== 0){
                r = b.getRank().getPrevious();
            } else if (rand == 1){
                r = b.getRank();
            } else {
                r=b.getRank().getNext();
            }
        } else {
            int highest = h.countRank(b.getRank().getNext());
            r = b.getRank().getNext();
            if (h.countRank(b.getRank()) > highest){
                highest = h.countRank(b.getRank());
                r = b.getRank();
            }
            if (h.countRank(b.getRank().getPrevious()) > highest){
                r = b.getRank().getPrevious();
            }
            //occasionally (2/5) play a random number not all like below
            Random random = new Random();
            int rand = random.nextInt(5);
            if (rand > 1){
                Iterator<Card> iterator = h.iterator();
                while (iterator.hasNext()){
                    Card card = iterator.next();
                    if (card.getRank() == r){
                        bh.add(card);
                    }
                }
            } else {
                rand = random.nextInt(h.countRank(r)) + 1;
                Iterator<Card> iterator = h.iterator();
                while (iterator.hasNext() && rand > 0){
                    Card card = iterator.next();
                    if (card.getRank() == r){
                        bh.add(card);
                        rand--;
                    }
                }
            }
        }
        currentKnownDiscards.add(bh);
        //add bh cards to said rankcount non essential
        return new Bid(bh,r);
    }
    /**
     * ADDED
     * more likely to choose higher cards to discard than low cards
     * @param h current player hand
     * @return A card that is more likely to be a high rank
     */
    private Card randomCard(Hand h){
        h.sortAscending();
        int min = 0;
        int max = h.size() - 1;
        while (min != max){
            int mid = (max+min)/2;
            Random random = new Random();
            int i = random.nextInt(3);
            if (i == 0){//1/3 chance to move the max down
                max = mid;
            } else {//2/3 to move the min up
                min = mid;
            }
        }
        return h.getSortedIndex(max);
    }

    /**
     * Decides if the thinker wants to call cheat or not
     * @param h	  The players current hand
     * @param b the current bid
     * @return If the computer is calling cheat
     */
    @Override
    public boolean callCheat(Hand h, Bid b) {
        saidRankCount[b.getRank().ordinal()] += b.getCount();
        Hand allKnownPlay = new Hand(h);
        allKnownPlay.add(currentKnownDiscards);
        //System.out.println("CurrentKnowDiscards=\n" + currentKnownDiscards);
        int s = b.getCount();
        Card.Rank r = b.getRank();
        Iterator<Card> iterator = allKnownPlay.iterator();
        while (iterator.hasNext()){
            Card card = iterator.next();
            if (card.getRank() == r){
                s++;
            }
        }
        if (s>4){
            System.out.println("ThinkerCallingCheatCosOfKnownPLay");
            return true;
        } else {
            //use p and saidRankCount
            Random rand=new Random();
            float rnd = rand.nextFloat();
            int copyOfSaidRankCount = saidRankCount[b.getRank().ordinal()];
            if (copyOfSaidRankCount > 4){
                while(rnd>=p&&copyOfSaidRankCount>4){
                    rnd = rand.nextFloat();
                    copyOfSaidRankCount--;
                }
                if (copyOfSaidRankCount > 4){
                    System.out.println("ThinkerCallingCheatCosOfSaidRank");
                    return true;
                }
            }
            return false;
        }
    }
    /**
     * ADDED
     * Called when a player calls cheat to reset the known discard pile.
     */
    public void cheatCalled(){
        currentKnownDiscards = new Hand();
        saidRankCount = new int[13];
    }
}
