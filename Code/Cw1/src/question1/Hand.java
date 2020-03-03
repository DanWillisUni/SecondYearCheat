package question1;

import java.io.*;
import java.util.*;
public class Hand implements Iterable<Card>, Serializable {
    private List<Card> hand; //This is List so I can change the underlying implementation easily
    private transient List<Card> sortedHand;//The sorted list of all the cards in hand
    private transient int[] rankCount = new int[13];
    private static transient final long serialVersionUID = 100263257L;
    public static void main(String[] args) {
        Hand h = new Hand();//testing all the adds
        h.add(new Card(Card.Rank.TWO,Card.Suit.CLUBS));
        h.add(new Card(Card.Rank.THREE,Card.Suit.CLUBS));
        Hand handToAddAdd = new Hand();
        handToAddAdd.add(new Card(Card.Rank.FOUR,Card.Suit.CLUBS));
        handToAddAdd.add(new Card(Card.Rank.FIVE,Card.Suit.CLUBS));
        Hand handToAdd = new Hand(handToAddAdd);
        handToAdd.add(new Card(Card.Rank.SIX,Card.Suit.CLUBS));
        handToAdd.add(new Card(Card.Rank.SEVEN,Card.Suit.CLUBS));
        h.add(handToAdd);
        ArrayList<Card> collectionToAdd = new ArrayList<>();
        collectionToAdd.add(new Card(Card.Rank.EIGHT,Card.Suit.CLUBS));
        collectionToAdd.add(new Card(Card.Rank.NINE,Card.Suit.CLUBS));
        collectionToAdd.add(new Card(Card.Rank.TEN,Card.Suit.CLUBS));
        h.add(collectionToAdd);
        System.out.println(h.toString());
        String org = "/orginal.ser";//test serialization
        write(h,org);
        System.out.println("Is flush: " + h.isFlush());//test is Flush
        System.out.println("Is straight: " + h.isStraight());//test is straight
        System.out.println("Hand Value: " + h.handValue());//test hand value
        System.out.println("\nBefore Removal=");//testing all the different removal methods
        Hand h1 = new Hand();
        h1.add(new Card(Card.Rank.NINE,Card.Suit.CLUBS));
        h1.add(new Card(Card.Rank.THREE,Card.Suit.CLUBS));
        System.out.println("Hand to remove=");
        System.out.println(h1.toString());
        h.remove(h1);
        System.out.println("After Removal=");
        System.out.println(h.toString());
        String afterRem = "/afterRem.ser";
        write(h,afterRem);
        int index = 1;
        System.out.println("Index to Remove: " + index);
        h.remove(index);
        System.out.println("After Removal=");
        System.out.println(h.toString());
        index = 3;
        System.out.println("Index to Remove: " + index);
        h.remove(index);
        System.out.println("After Removal=");
        System.out.println(h.toString());
        h.add(new Card(Card.Rank.TEN,Card.Suit.SPADES));
        System.out.println("Added The TEN of SPADES");
        System.out.println(h.toString());
        System.out.println("Is flush: " + h.isFlush());//testing is flush again
        System.out.println("Is straight: " + h.isStraight());//testing is straight again
        System.out.println("Hand Value: " + h.handValue());//testing hand value again
        System.out.println("Count Rank TEN: "+ h.countRank(Card.Rank.TEN));//testing count rank
        //deserializeation
        System.out.println("Original");
        h=read(org);
        assert h != null;
        System.out.println(h.toString());
        System.out.println("AfterRemoval");
        h=read(afterRem);
        assert h != null;
        System.out.println(h.toString());
        System.out.println("\nSorting");//testing sorting
        Hand sort = new Hand();
        for (int i = 0;i<10;i++){
            sort.add(Card.randomCard());
        }
        System.out.println(sort.toString());
        System.out.println("Ascending");
        sort.sortAscending();
        System.out.println(sort.toStringSorted());
        System.out.println("Decending");
        sort.sortDescending();
        System.out.println(sort.toStringSorted());
        System.out.println("Original order=\n" + sort.toString());//shows original order of cards after sorting
    }
    /**
     * Constructor
     * sets hand to an empty arraylist
     */
    public Hand(){
        hand = new ArrayList<>();
    }
    /**
     * Constructor
     * Creates a new hand with the cards in the arraylist
     * @param arrayToAdd arraylist that they hand is initialised to
     */
    public Hand(Card[] arrayToAdd){
        this();
        add(Arrays.asList(arrayToAdd));
    }
    /**
     * Constructor
     * add a hand into the new hand
     * @param handToAdd hand to add into the new hand
     */
    public Hand(Hand handToAdd){
        this();
        add(handToAdd);
    }
    /**
     * Add a card to a hand
     * @param card card to add to hand
     */
    public void add(Card card){
        hand.add(card);
        Card.Rank cardRank = card.getRank();
        rankCount[cardRank.ordinal()]++;
    }
    /**
     * Add a collection to the hand
     * @param collectionToAdd the collection to add to the hand
     */
    public void add(Collection<Card> collectionToAdd){
        for (Card card :collectionToAdd){
            add(card);
        }
    }
    /**
     * Add another hand to the current one
     * @param handToAdd hand to add to the current one
     */
    public void add(Hand handToAdd){
        add(handToAdd.hand);
    }
    /**
     * Removes a card from the hand
     * @param card card to remove
     * @return True if the card was in the hand and False if it wasnt
     */
    public boolean remove(Card card){
        if (hand.contains(card)){
            Card.Rank cardRank = card.getRank();
            rankCount[cardRank.ordinal()]--;
            hand.remove(card);
            return true;
        } else {
            return false;
        }
    }
    /**
     * Removes all the cards int one hand from another hand
     * @param handToRemove hand to remove
     * @return True if all the cards were in the hand and False if it werent
     */
    public boolean remove(Hand handToRemove){
        boolean n = true;
        for (Card card:handToRemove.hand) {
            boolean rem = remove(card);
            if (!rem){
                n=false;
            }
        }
        return n;
    }
    /**
     * removes card in index
     * @param index the index of the card to remove
     * @throws IndexOutOfBoundsException
     */
    public void remove(int index) throws IndexOutOfBoundsException{
        remove(hand.get(index)); //List.get() throws index out of bounds exception if out of range
    }
    /**
     * @return the hand iterator
     */
    public Iterator<Card> iterator(){
        return new HandIterator(this);
    }
    /**
     * Iterates through the hand in last added first
     */
    class HandIterator implements Iterator<Card> {
        private int nextCard;
        private int size;
        /**
         *  Initilises next card and size
         * @param h hand to iterate
         */
        public HandIterator(Hand h) {
            this.nextCard = 0;
            this.size = h.hand.size();
        }
        /**
         * @return if the next card is there
         */
        @Override
        public boolean hasNext(){
            return nextCard < size;
        }
        /**
         * gets the next card that was added
         * @return the next card
         */
        @Override
        public Card next(){
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            return hand.get(nextCard++);
        }
    }
    /**
     *  Writes out a hand to a file
     * @param hand hand to serialize
     * @param file file to write to
     */
    public static void write(Hand hand,String file){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(file)));
            out.writeObject(hand);
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
    public static Hand read(String fileName) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            return (Hand) in.readObject();
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Hand class not found");
            c.printStackTrace();
            return null;
        }
    }
    /**
     * Sorts the cards in the hand into descending order with Card.compareTo
     */
    public void sortDescending(){
        sortedHand = new ArrayList<>();
        sortedHand.addAll(hand);
        sortedHand.sort(Card::compareTo);
    }
    /**
     * Sort the hand into ascending order by Rank ASC
     */
    public void sortAscending(){
        sortedHand = new ArrayList<>();
        sortedHand.addAll(hand);
        Comparator<Card> CompAsc = new Card.CompareAscending();
        Collections.sort(sortedHand,CompAsc);
    }
    /**
     * Counts the number of cards of rank r in the hand
     * @param r the rank to count
     * @return the number of cards in the hand of rank r
     */
    public int countRank(Card.Rank r){
        int count = 0;
        Iterator<Card> iterator = this.iterator();
        while (iterator.hasNext()){
            Card card = iterator.next();
            if (r.equals(card.getRank())){
                count++;
            }
        }
        return count;
    }
    /**
     * Gets the value of the hand
     * @return the summation of value of all the card ranks in the hand
     */
    public int handValue(){
        int count = 0;
        for (Card.Rank r : Card.Rank.values()) {
            int n = countRank(r);
            if(n > 0){
                int v = r.getValue();
                count = count + (n*v);
            }
        }
        return count;
    }
    /**
     * @return string with each card in the hand on a new line
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
    /**
     * Checks if the hand is a flush
     * @return if the hand is flush True, else False
     */
    public boolean isFlush(){
        Iterator<Card> iterator = this.iterator();
        Card.Suit firstSuit = iterator.next().getSuit();
        while (iterator.hasNext()){
            Card card = iterator.next();
            if (!card.getSuit().equals(firstSuit)){
                return false;
            }
        }
        return true;
    }
    /**
     * Gets if the hand is a straight
     * @return true if all the cards in the hand are consecutive ranks
     */
    public boolean isStraight(){
        sortDescending();
        Iterator<Card> iterator = this.sortIterator();
        Card.Rank Prev = null;
        while (iterator.hasNext()){
            Card card = iterator.next();
            if (Prev != null){
                if (!card.getRank().equals(Prev.getPrevious())){
                    return false;
                }
            }
            Prev = card.getRank();
        }
        return true;
    }
    /**
     * ADDED
     * @return the size of the hand
     */
    public int size(){
        return hand.size();
    }
    /**
     * ADDED
     * the hand in order as a string
     * @return the string with all the cards in the hand in order
     */
    public String toStringSorted(){
        StringBuilder str = new StringBuilder();
        if (sortedHand == null){
            sortAscending();
        }
        Iterator<Card> iterator = this.sortIterator();
        while (iterator.hasNext()){
            Card card = iterator.next();
            str.append("  ").append(card.toString()).append("\n");
        }
        return str.toString();
    }
    /**
     * ADDED
     * @return the sorted hand iterator
     */
    public Iterator<Card> sortIterator(){
        return new SortIterator();
    }
    /**
     * ADDED
     * Iterates through the sorted hand array
     */
    class SortIterator implements Iterator<Card> {
        private int nextCard;
        /**
         * Constructor
         * Sorts ascending if no sort has been done
         */
        public SortIterator() {
            if(sortedHand.size()!=hand.size()){
                sortAscending();
            }
            this.nextCard = sortedHand.size() - 1;
        }
        /**
         * @return if there is a next card
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
            return sortedHand.get(nextCard--);
        }
    }
}
