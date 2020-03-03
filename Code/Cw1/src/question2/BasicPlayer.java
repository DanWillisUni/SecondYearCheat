package question2;

public class BasicPlayer implements Player {
    private Hand h;
    private Strategy s;
    private CardGame g;
    /**
     * Constructor
     * @param s strategy
     * @param g card game
     */
    public BasicPlayer(Strategy s, CardGame g) {
        h=new Hand();
        setStrategy(s);
        setGame(g);
    }
    /**
     * Add card to hand
     * @param c: Card to add
     */
    @Override
    public void addCard(Card c) {
        h.add(c);
    }
    /**
     * Add hand to current hand
     * @param h: hand to add
     */
    @Override
    public void addHand(Hand h) {
        this.h.add(h);
    }
    /**
     * @return number of cards left in hand
     */
    @Override
    public int cardsLeft() {
        return h.size();
    }
    /**
     * Sets the game
     * @param g: the player should contain a reference to the game it is playing in
     */
    @Override
    public void setGame(CardGame g) {
        this.g = g;
    }
    /**
     * Sets the strategy
     * @param s: the player should contain a reference to its strategy
     */
    @Override
    public void setStrategy(Strategy s) {
        this.s=s;
    }
    /**
     * chooses the bid for the player and then removes the bid played from the hand
     * @param b: the last bid accepted by the game. .
     * @return new bid to be played by the player
     */
    @Override
    public Bid playHand(Bid b) {
        Bid nb = s.chooseBid(b,h,s.cheat(b,h));
        h.remove(nb.getHand());
        return nb;
    }
    /**
     * @param b: the last players bid
     * @return Whether the player calls cheat or not
     */
    @Override
    public boolean callCheat(Bid b) {
        return s.callCheat(h,b);
    }
    //accessors
    /**
     * ADDED
     * @return strategy
     */
    public Strategy getS() {
        return s;
    }
    /**
     * ADDED
     * @return hand
     */
    public Hand getH() {return h;}
    /**
     * ADDED
     * @return game
     */
    public CardGame getG() {return g;}
}
