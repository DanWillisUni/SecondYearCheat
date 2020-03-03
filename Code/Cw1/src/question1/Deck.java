package question1;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
public class Deck implements Iterable<Card>, Serializable {
    private Card[] cards = new Card[52];
    private int numOfCards = 52;
    private static transient final long serialVersionUID = 100263237L;
    public static void main(String[] args) {
        Deck deck = new Deck();
        System.out.println("Odd even iterator=");//demo odd even iterator
        Iterator<Card> iterator = deck.oddEvenIterator();
        while (iterator.hasNext()){
            Card card = iterator.next();
            System.out.println("  " + card.toString());
        }
        System.out.println("\nTo deal Iterator");//demos dealing iterator
        System.out.println(deck.toString());
        System.out.println("Deal 1 card");
        deck.Deal();
        System.out.println("Odd even iterator=");//demo odd even iterator
        iterator = deck.oddEvenIterator();
        while (iterator.hasNext()){
            Card card = iterator.next();
            System.out.println("  " + card.toString());
        }
        System.out.println("\nTo deal Iterator");//demos dealing iterator
        System.out.println(deck.toString());
        deck.shuffle();//demonstrates shuffle
        System.out.println("After shuffle");
        System.out.println(deck.toString());
        String shuf = "/ShuffledDeck.ser";
        write(deck,shuf);
        System.out.println("\nNew Deck");
        deck.newDeck();
        System.out.println(deck.toString());
        String org = "/orgDeck.ser";
        write(deck,org);
        System.out.println("Deserialization of shuffle");
        deck = read(shuf);
        assert deck != null;
        System.out.println(deck.toString());
        System.out.println("Deserialization of newDeck");
        deck = read(org);
        System.out.println(deck.toString());
    }
    /**
     * Constructor
     * Creates every card in the deck in order
     */
    public Deck() {
        Card.Rank[] ranks = {Card.Rank.TWO, Card.Rank.THREE, Card.Rank.FOUR, Card.Rank.FIVE, Card.Rank.SIX, Card.Rank.SEVEN, Card.Rank.EIGHT, Card.Rank.NINE, Card.Rank.TEN, Card.Rank.JACK, Card.Rank.QUEEN, Card.Rank.KING, Card.Rank.ACE};
        Card.Suit[] suits = {Card.Suit.CLUBS, Card.Suit.DIAMONDS, Card.Suit.HEARTS, Card.Suit.SPADES};
        for (int s = 0; s < suits.length; s++) {
            for (int r = 0; r < ranks.length; r++) {
                cards[s*13+r] = new Card(ranks[r], suits[s]);
            }
        }
    }
    /**
     * @return the number of undelt cards in the deck
     */
    public int size(){//returns number of cards remaining in the deck
        return numOfCards;
    }
    /**
     * reinitialises the deck to the class Deck
     */
    final void newDeck(){
        Card.Rank[] ranks = {Card.Rank.TWO, Card.Rank.THREE, Card.Rank.FOUR, Card.Rank.FIVE, Card.Rank.SIX, Card.Rank.SEVEN, Card.Rank.EIGHT, Card.Rank.NINE, Card.Rank.TEN, Card.Rank.JACK, Card.Rank.QUEEN, Card.Rank.KING, Card.Rank.ACE};
        Card.Suit[] suits = {Card.Suit.CLUBS, Card.Suit.DIAMONDS, Card.Suit.HEARTS, Card.Suit.SPADES};
        for (int s = 0; s < suits.length; s++) {
            for (int r = 0; r < ranks.length; r++) {
                cards[s*13+r] = new Card(ranks[r], suits[s]);
            }
        }
    }
    /**
     * @return The dealing Deck Iterator obj
     */
    @Override
    public Iterator<Card> iterator(){
        return new DeckIterator(this);
    }
    /**
     * Traverses the cards in order to be dealt
     * Goes from top to bottom (starts at the card in position 51, goes down)
     */
    class DeckIterator implements Iterator<Card> {
        private int nextCard;//index of next card
        /**
         * init next card to be at the top
         * @param deck
         */
        public DeckIterator(Deck deck) {
            this.nextCard = deck.size() - 1;
        }
        /**
         * @return if there is a next card
         */
        @Override
        public boolean hasNext(){
            return nextCard >= 0;
        }
        /**
         * Decements nextcard
         * @return card at index next card
         */
        @Override
        public Card next(){
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            return cards[nextCard--];
        }
        /**
         * removes a card from the deck
         */
        public void remove(){
            numOfCards--;
            cards[size()] = null;
        }
    }
    /**
     * Shuffles the deck
     * goes through every card in the deck and swap with another random card
     */
    public void shuffle(){
        Random random = new Random();
        for (int i = 0; i < size(); i++) {
            int randomIndexToSwap = random.nextInt(size());
            Card temp = cards[randomIndexToSwap];
            cards[randomIndexToSwap] = cards[i];
            cards[i] = temp;
        }
    }
    /**
     * removes the top card from the deck and returns it
     * @return top card
     */
    public Card Deal(){
        Card n = null;
        if (size() > 0){
            numOfCards--;
            n = cards[size()];
            cards[size()] = null;
        }
        return n;
    }
    /**
     * @return returns the odd even Deck Iterator
     */
    public Iterator<Card> oddEvenIterator(){
        return new OddEvenIterator(this);
    }
    /**
     * traverses the Cards by first going through all the cards in odd positions, then the ones in even positions
     */
    class OddEvenIterator implements Iterator<Card> {
        private int nextCard;
        private boolean Odds;
        private int decksize;
        /**
         * Constructor
         *  Sets next card to the highest index thats odd
         * @param deck deck to iterate
         */
        public OddEvenIterator(Deck deck) {
            if (deck.size()%2==0){
                this.nextCard = deck.size() - 1;
            } else {
                this.nextCard = deck.size() - 2;
            }
            this.decksize = deck.size();
            Odds = true;
        }
        /**
         * @return if the next card is out the index
         */
        @Override
        public boolean hasNext(){
            return nextCard >= 0;
        }
        /**
         * @return the next card
         */
        @Override
        public Card next(){
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            Card card = cards[nextCard];
            nextCard -= 2;
            if(Odds && nextCard < 0){
                if (decksize%2==0){
                    nextCard = decksize - 2;
                } else {
                    nextCard = decksize - 1;
                }
                Odds = false;
            }
            return card;
        }
    }
    //serialsation
    /**
     *  Writes out a deck to a file
     * @param deck deck to serialize
     * @param file the file to save to
     */
    public static void write(Deck deck,String file){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(file)));
            out.writeObject(deck);
            out.close();
            System.out.println("Serialized data is saved");
        } catch (IOException i) {
            i.printStackTrace();
        }
        //return fileName;
    }
    /**
     * reads in a card from a file (deserialization)
     * @param fileName File of the card
     * @return the Card
     */
    public static Deck read(String fileName) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            return (Deck) in.readObject();
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Deck class not found");
            c.printStackTrace();
            return null;
        }
    }
    /**
     * ADDED
     * ToString
     * @return String of all the cards in the Deck on different lines
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        Iterator<Card> iterator = this.iterator();
        while (iterator.hasNext()){
            Card card = iterator.next();
            str.append("  ").append(card.toString()).append("\n");
        }
        return str.toString();
    }
}
