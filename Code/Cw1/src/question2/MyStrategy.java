package question2;

import java.util.Iterator;
import java.util.Random;

public class MyStrategy implements Strategy {
    private int own = -1;//own player number
    private int playerNum;//number of players
    private int[] knownDiscardRankCount;//known rank count for cards in discard
    private int[][] prevKnownRankCount;//at the start of that round, what was known about each players hand
    private int[][] currentKnownRankCount;//what is currently known about each players hand
    private int[] saidRankCount;//the number that players have said have gone down of each rank that round
    /**
     * Constructor
     * Sets up all the variables
     */
    public MyStrategy(){
        BasicCheat x = BasicCheat.getInstance();
        playerNum = x.getNosPlayers();
        knownDiscardRankCount = new int[13];
        saidRankCount = new int[13];
        prevKnownRankCount = new int[playerNum][13];
        currentKnownRankCount = new int[playerNum][13];
    }
    /**
     * If it has to cheat it will
     * Has a random chance to cheat that varies depending upon how many cards in hand
     * @param b   the bid this player has to follow (i.e the bid prior to this players turn.
     * @param h	  The players current hand
     * @return if the player is going to cheat or not
     */
    @Override
    public boolean cheat(Bid b, Hand h) {
        if (hasToCheat(b,h)){//if it has to cheat
            return true;
        }
        //my algorithm for whether to cheat or not
        Random rand=new Random();
        int rnd = rand.nextInt(101);//random number between 1 and 100
        //giving it a chance to cheat
        return rnd < cheatLessWithLessCardsInHand(h);
    }
    /**
     * ADDED
     * Determies is the computer has to cheat
     * @param b previous bid
     * @param h current hand
     * @return if the computer has to cheat
     */
    private boolean hasToCheat(Bid b, Hand h) {
        Card.Rank br = b.getRank();
        Iterator<Card> iterator = h.iterator();//iterate through all the cards in the hand
        while (iterator.hasNext()){
            Card card = iterator.next();
            if ((card.getRank().getPrevious().equals(br))||(card.getRank().equals(br))||(card.getRank().getNext().equals(br))){//if the card is able to be played
                return false;
            }
        }
        return true;
    }
    /**
     * ADDED
     * Max is just under 20
     * 10% at dealt quota (52/number of players)
     * Gives percentage likelyhood to cheat
     * @param h hand
     * @return % of how much to cheat
     */
    private int cheatLessWithLessCardsInHand(Hand h){
        double probabilityOfCheat = 0.0;
        if (h.countRank(h.getIndex(0).getRank()) != h.size()){//if cant just play all cards and it be true
            if ((h.size()/(52/playerNum))/3 > 1){//if over peek of sin wave
                probabilityOfCheat = 20;//set to 20%
            } else {
                probabilityOfCheat = 20 * Math.sin((h.size()/(52/playerNum)) * Math.PI/6);//set to the sin wave I designed
            }
        }
        return (int) Math.floor(probabilityOfCheat);
    }
    /**
     * If has to cheat play only one card 80% of the time, 2 the rest
     * If cheating but not having too, play rank that it has and pretend to play cards it actually has
     * If not cheating will play all its cards it can 90% of the time, rest of the time will play one less of the chosen rank
     * @param b   the bid the player has to follow.
     * @param h	  The players current hand
     * @param cheat true if the Strategy has decided to cheat (by call to cheat())
     * @return The bid chosen
     */
    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat) {
        if (own == -1){//if own player number unknown
            BasicCheat x = BasicCheat.getInstance();
            own=x.getCurrentPlayer();//set own player number
        }
        Hand bh = new Hand();//init new bid hand
        Card.Rank r;//init new rank to say is being played
        Random rand=new Random();
        if (hasToCheat(b,h)) {
            //has to cheat
            //System.out.println("Has to Cheat ---");
            r=getRankToPlayCheating(b);
            //get the highest known number of that rank cards that were in the hand of a player at the start of the round
            int highest=0;
            for (int p = 0;p<playerNum;p++){
                if (highest<prevKnownRankCount[p][r.ordinal()]&&p!=own){
                    highest = prevKnownRankCount[p][r.ordinal()];
                }
            }
            int count = 1;//set the number of cards to play to 1
            if (saidRankCount[r.ordinal()]<7&&h.size()>=2&&highest<=2){//if there is enough cards in hand, you dont know that anybody will know you are cheating and the said rank is low enough
                count++;//add one to the cards to be played
                if (saidRankCount[r.ordinal()]<2&&h.size()>=3&&highest<=1){//if there is enough cards in hand, you dont know that anybody will know you are cheating and the said rank is low enough
                    count++;//add another to the cards to be played
                }
            }
            bh.add(getFurthestCards(h,count,r));//add the cards to be played to the hand
        } else {
            if (cheat){
                //cheating but not having to
                //System.out.println("Cheating but not having to===");
                r=getRankToPlay(h,b);
                int quota = h.countRank(r);//get the number of cards to pretend to have
                float rnd = rand.nextFloat();
                if(rnd>=0.9&&quota>1){//90% play full number you can pretend to play
                    quota--;
                }
                bh.add(getFurthestCards(h,quota,r));//get the cards to be added to hand
            } else {
                //not cheating
                //System.out.println("Not cheating +++");
                r=getRankToPlay(h,b);
                int quota = h.countRank(r);
                float rnd = rand.nextFloat();
                if(rnd>=0.95&&quota>1){//95% of the time add all the cards
                    quota--;
                }
                Iterator<Card> iterator = h.iterator();
                while (iterator.hasNext() && quota > 0) {
                    Card card = iterator.next();
                    if (card.getRank() == r){//iterate through
                        bh.add(card);//add the cards of the correct rank
                        quota--;
                    }
                }
            }
        }
        int[] bidRankCount = getRankCountArray(bh);
        for (int i = 0;i<bidRankCount.length;i++){
            knownDiscardRankCount[i] += bidRankCount[i];//add all the cards to the known discard rank count
        }
        return new Bid(bh,r);
    }
    /**
     * ADDED
     * add up all the ranks around the possible ranks that the player has
     * this gives it more of an edge in calling if people are cheating
     * Gets the rank with the most adjacent cards so its easier to call cheat
     * @param h hand of hte player
     * @param b bid of the player
     * @return the rank the player should play
     */
    private Card.Rank getRankToPlay(Hand h, Bid b){
        int prevPrevCount = h.countRank(b.getRank().getPrevious().getPrevious());//gets the previous previous rank count
        int prevCount = h.countRank(b.getRank().getPrevious());//gets the previous rank count
        int currentCount = h.countRank(b.getRank());//gets the current rank count
        int nextCount = h.countRank(b.getRank().getNext()); //gets the next rank count
        int nextNextCount = h.countRank(b.getRank().getNext().getNext()); // gets hte next next rank count
        int prevSum = 0;
        int currentSum = 0;
        int nextSum = 0;
        if (prevCount != 0){
            prevSum = prevPrevCount + prevCount + currentCount;//sums the rank counts of all the ranks around previous
        }
        if (currentCount != 0){
            currentSum = prevCount + currentCount + nextCount;//sums all the rank counts of all the ranks around current
        }
        if (nextCount != 0){
            nextSum = currentCount + nextCount + nextNextCount;//sums all the rank counts of all the ranks around the next
        }
        if (nextSum > currentSum && nextSum >= prevSum){
            return b.getRank().getNext();//returns next if its sum is greater than current and greater than or equal to the previous sum
        } else if (currentSum >= prevSum){
            return b.getRank();//returns current if current is greater than or equal to nextsum and greater than or equal to next
        } else {
            return b.getRank().getPrevious();//if it is the highest return previous
        }
    }
    /**
     * ADDED
     * Gets the rank to play when has to cheat using saidRankCount
     * @param b last bid
     * @return rank to play
     */
    private Card.Rank getRankToPlayCheating(Bid b){
        Card.Rank br = b.getRank();
        int saidCountPrev = saidRankCount[br.getPrevious().ordinal()];
        int saidCountCurr = saidRankCount[br.ordinal()];
        int saidCountNext = saidRankCount[br.getNext().ordinal()];
        int highestPrev=0;//get the number of known to have been in someones hand last cheat call of one down
        for (int p = 0;p<playerNum;p++){
            if (highestPrev<prevKnownRankCount[p][br.getPrevious().ordinal()]&&p!=own){
                highestPrev = prevKnownRankCount[p][br.getPrevious().ordinal()];
            }
        }
        int highestNext=0;//get the number of known to have been in someones hand last cheat call of one up
        for (int p = 0;p<playerNum;p++){
            if (highestNext<prevKnownRankCount[p][br.getNext().ordinal()]&&p!=own){
                highestNext = prevKnownRankCount[p][br.getNext().ordinal()];
            }
        }
        int highestCurrent=0;//get the number of known to have been in someones hand last cheat call of current rank
        for (int p = 0;p<playerNum;p++){
            if (highestCurrent<prevKnownRankCount[p][br.ordinal()]&&p!=own){
                highestCurrent = prevKnownRankCount[p][br.ordinal()];
            }
        }
        if (highestPrev<4&&highestNext<4&&highestCurrent<4){
            //can play all
            if (saidCountPrev<=saidCountCurr&&saidCountPrev<saidCountNext){
                return br.getPrevious();
            }else if(saidCountNext<=saidCountCurr&&saidCountNext<saidCountPrev){
                return br.getNext();
            }else{
                return br;
            }
        } else if (highestPrev==4&&highestNext<4&&highestCurrent<4){
            //cant play prev without someone possibly knowing
            if(saidCountNext<=saidCountCurr){
                return br.getNext();
            }else{
                return br;
            }
        } else if (highestPrev<4&&highestNext==4&&highestCurrent<4){
            //cant play next without somebody possibly knwing
            if(saidCountPrev<=saidCountCurr){
                return br.getPrevious();
            }else{
                return br;
            }
        } else if (highestPrev<4&&highestNext<4&&highestCurrent==4){
            //cant play current without somebody possibly knowing
            if(saidCountNext<saidCountPrev){
                return br.getNext();
            }else{
                return br.getPrevious();
            }
        } else if (highestPrev==4&&highestNext==4&&highestCurrent<4){
            return br;//has to pick else somebpody will possibly know
        } else if (highestPrev<4&&highestNext==4&&highestCurrent==4){
            return br.getPrevious();//has to pick will possibly
        } else if (highestPrev==4&&highestNext<4&&highestCurrent==4){
            return br.getNext();
        } else {
            //cant play any so just pick the lowest said rank trying to get out away from having to cheat
            if(saidCountNext<saidCountPrev){
                return br.getNext();
            }else{
                return br.getPrevious();
            }
        }
    }
    /**
     * ADDED
     * gets the cards furthest away from being played, and tries to cheat them in to improve its chances of having to cheat
     * @param h the hand
     * @param n the number of cards to return
     * @return a hand of cards to play
     */
    private Hand getFurthestCards(Hand h, int n, Card.Rank rankToPlay){
        Hand rh= new Hand();
        Hand dummyHand = new Hand(h);
        Card cardtoPlay = new Card(rankToPlay, Card.Suit.CLUBS);//temp card
        for (int quotaFull=0;quotaFull<n;quotaFull++){
            Iterator<Card> iterator = dummyHand.iterator();
            Card.Rank furthestRank = null;
            int distance = 0;
            while (iterator.hasNext()) {//gets the furthest distance rank from play
                Card card = iterator.next();
                if(Card.difference(card,cardtoPlay)>distance){
                    distance= Card.difference(card,cardtoPlay);
                    furthestRank = card.getRank();
                }
            }
            boolean added = false;
            iterator = dummyHand.iterator();
            while (iterator.hasNext()&&!added) {
                Card card = iterator.next();
                if(card.getRank() == furthestRank){
                    rh.add(card);//adds the furthest card to the hand to play
                    dummyHand.remove(card);
                    added=true;
                }
            }
        }
        return rh;
    }
    /**
     * ADDED
     * @param h hand to count
     * @return the rankcount of the hand
     */
    private int[] getRankCountArray(Hand h){
        int[] rc = new int[13];
        for(int i = 0;i<13;i++){
            rc[i] = h.getRankCount(i);
        }
        return rc;
    }
    /**
     * Calls cheat if it is the players final card
     * Calls cheat if the there is more than 4 of of that rank assuming they are telling the truth
     * Use said rank to call cheat, but weights up the risk and is more sure if there is more cards in the discard pile
     * @param h	  The players current hand
     * @param b the current bid
     * @return true if the player wants to call cheat
     */
    @Override
    public boolean callCheat(Hand h, Bid b) {
        saidRankCount[b.getRank().ordinal()] += b.getCount();//puts the bid into said rank count array
        BasicCheat x = BasicCheat.getInstance();
        subtractionOfRankCount(x.getCurrentPlayer(),b.getCount());
        BasicPlayer p = x.getPlayer(x.getCurrentPlayer());
        if (p.cardsLeft()==0){
            return true;//calls on players final turn meaning plays have to tell the truth on thier last turn
        }
        //use the knowndiscards,player hand,currentknowhplayer hands to call cheat
        int total = b.getCount();
        int rOrd = b.getRank().ordinal();
        total += knownDiscardRankCount[rOrd];
        total += h.countRank(b.getRank());
        for (int i = 0;i<playerNum;i++){
            if (i!=x.getCurrentPlayer() && i!=own){
                total += currentKnownRankCount[i][rOrd];
            }
        }
        if (total > 4){
            return true;
        }
        Random rand=new Random();
        float rnd = rand.nextFloat();
        int copyOfSaidRankCount = saidRankCount[b.getRank().ordinal()];
        if (copyOfSaidRankCount > 4){
            while(rnd>=beMoreSureWithMoreCardsInDiscard()&&copyOfSaidRankCount>=4){//be more sure on calling cheat
                rnd = rand.nextFloat();
                copyOfSaidRankCount--;
            }
            return copyOfSaidRankCount > 4;
        }
        return false;
    }
    /**
     * ADDED
     * Makes the probability higher the more cards there are in the discards
     * @return number between 0 and 1 to determine how sure you must be
     */
    private double beMoreSureWithMoreCardsInDiscard(){
        double prob;
        int numInDiscard = 0;
        for (int i = 0;i<13;i++){
            numInDiscard+=knownDiscardRankCount[i]+saidRankCount[i];
        }
        prob = Math.cos(numInDiscard * Math.PI/104)/5;//set to the cosine wave I designed
        return prob;
    }
    /**
     * ADDED
     * Called when a cheat is called in the game to reset all the known info
     * Called to reset all the known hands and arrays
     * @param p player whos hand the discards were added too
     */
    public void cheatCalled(int p){
        if (p!=own){
            int[] old = prevKnownRankCount[p];
            for (int i=0;i<playerNum;i++){
                if (i!=p){
                    prevKnownRankCount[i]=currentKnownRankCount[i];
                }
            }
            for(int i=0;i<13;i++){
                old[i]+=knownDiscardRankCount[i];
            }
            prevKnownRankCount[p] = old;
        }
        knownDiscardRankCount = new int[13];
        saidRankCount = new int[13];
    }
    /**
     * ADDED
     * Called each turn to subtract the known rank counts, so that the known cards are 100%accurate
     * @param playerNumber player who played cards
     * @param cardsPlayed The number of cards played
     */
    private void subtractionOfRankCount(int playerNumber,int cardsPlayed){
        int[] playersRankCount = currentKnownRankCount[playerNumber];
        for (int i = 0;i<playersRankCount.length;i++){
            if (playersRankCount[i]<=cardsPlayed){
                playersRankCount[i]=0;
            }else{
                playersRankCount[i]-=cardsPlayed;
            }
        }
        currentKnownRankCount[playerNumber]=playersRankCount;
    }
}
