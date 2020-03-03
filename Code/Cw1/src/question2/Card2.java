package question2;

import java.io.*;
import java.util.Comparator;
import java.util.Random;
import java.util.ArrayList;
public class Card implements Comparable<Card>, Serializable {
    private Rank rank;//rank attribute
    private Suit suit;//suit attribute
    private static transient final long serialVersionUID = 100263247L;
    public static void main(String[] args) {
        Card a = new Card(Rank.TEN,Suit.CLUBS);
        Card b = new Card(Rank.QUEEN,Suit.DIAMONDS);
        String afn = write(a);//testing of serialization
        String bfn = write(b);
        Card c = read(afn);//testing deserialization
        Card d = read(bfn);
        System.out.println("Card a: " +a.toString());
        System.out.println("Card b: " +b.toString());
        assert c != null;
        System.out.println("Card c: " +c.toString());
        assert d != null;
        System.out.println("Card d: " +d.toString());
        System.out.println("Differance between a and b: " + difference(a,b));//test differance
        System.out.println("Differance between value of a and b: " + differenceValue(a,b));//test differance in value
        System.out.println("selectTest");
        selectTest(randomCard());//test select test
    }
    /**
     * The rank enum contains all the possible rank values
     * TWO-ACE
     */
    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;
        private static Rank[] v = values();
        private static int[] value = {2,3,4,5,6,7,8,9,10,10,10,10,11};
        /**
         * @return the previous enum value in the list
         */
        public Rank getPrevious(){
            if (this == TWO){
                return ACE;
            } else {
                return v[(this.ordinal()-1)];
            }
        }
        /**
         * ADDED
         * helps with getting which ranks can be played
         * @return the next enum value in the list
         */
        public Rank getNext(){
            if (this == ACE){
                return TWO;
            } else {
                return v[(this.ordinal()+1)];
            }
        }
        /**
         * @return returns the integer value of the card
         */
        public int getValue(){
            return value[this.ordinal()];
        }
    }
    /**
     * Suit enum containing all the suits
     */
    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES;
        private static Suit[] v = values();
        /**
         * @return a randomly selected suit
         */
        static Suit getRandom(){
            Random random = new Random();
            return v[random.nextInt(4)];
        }
    }
    /**
     * Constructor
     * @param rank the rank the card is
     * @param suit the suit the card is
     */
    public Card(Rank rank, Suit suit){
        this.rank=rank;
        this.suit=suit;
    }
    //accessors methods
    /**
     * @return the rank of the card
     */
    public Rank getRank(){
        return rank;
    }
    /**
     * @return the suit of the card
     */
    public Suit getSuit(){
        return suit;
    }
    //to string
    /**
     * toString method of the card
     * @return a string with of rank and suit
     */
    @Override
    public String toString(){
        return "The " + getRank() + " of " +getSuit();
    }
    //methods
    /**
     * Card Compare
     * Comparison by rank then by suit
     * @param o other card to compare this card to
     * @return 1 if greater,0 if the same,-1 if less
     */
    @Override
    public int compareTo(Card o) {
            int r = (this.getRank().ordinal())-(o.getRank().ordinal());
            if (r==0){
                int s = (this.getSuit().ordinal())-(o.getSuit().ordinal());
                if (s==0){
                    return s;
                } else if (s>0){
                    return -1;
                } else {
                    return 1;
                }
            } else if (r>0){
                return 1;
            } else {
                return -1;
            }
        }
    /**
     * Find the difference in ranks between Cards A and B
     * @param A first Card
     * @param B second Card
     * @return the difference in ranks between two cards
     */
    public static int difference(Card A, Card B){//returns the difference in ranks between two cards
        return Math.abs((A.rank.ordinal())-(B.rank.ordinal()));
    }
    /**
     * find the difference in value between the ranks of Cards A and B
     * @param A first Cards
     * @param B second Card
     * @return the difference between the value of the Ranks of two cards
     */
    public static int differenceValue(Card A, Card B){// returns the difference in values between two cards
        return Math.abs(A.rank.getValue()-B.rank.getValue());
    }
    //Comparator classes
    public static class CompareAscending implements Comparator<Card>{
        /**
         * Rank comparison between c1 and c2
         * @param c1 first card
         * @param c2 second rank
         * @return 1 if c1>c2 0 if they are equal -1 if c1<c2
         */
        @Override
        public int compare(Card c1, Card c2) {
            int n = (c1.getRank().ordinal())-(c2.getRank().ordinal());
            int r = 0;
            if(n>0){
                r = -1;
            } else if(n<0){
                r = 1;
            }
            return r;
        }
    }
    public static class CompareSuit implements Comparator<Card>{
        /**
         * Comparison between c1 and c2
         * @param c1 first card
         * @param c2 second card
         * @return 1 if c1>c2 0 if they are equal -1 if c1<c2
         */
        @Override
        public int compare(Card c1, Card c2) {
            int n = (c1.getSuit().ordinal())-(c2.getSuit().ordinal());
            int r = 0;
            if(n>0){
                r = -1;
            } else if(n<0){
                r = 1;
            }
            return r;
        }
    }
    /**
     * creates 3 random Cards and compares them with lamdas to the card passed in
     * @param a card to test
     */
    static void selectTest(Card a){
        System.out.println(a.toString());
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i <3;i++){
            cards.add(randomCard());
        }
        Card.CompareAscending RankObject = new Card.CompareAscending();
        Card.CompareSuit SuitObject = new Card.CompareSuit();
        cards.forEach(n -> System.out.println("  " + n.toString() +":\n    RANK: " + RankObject.compare(a,n) + "\n    SUIT: " +SuitObject.compare(a,n) + "\n    CARD: " + a.compareTo(n)));
    }
    //serialsation
    /**
     *  Writes out a card to a file
     * @param card card to serialize
     * @return the fileName of where the card is saved
     */
    public static String write(Card card){
        String fileName = "/" + card.toString() + ".ser";
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
            out.writeObject(card);
            out.close();
            System.out.println("Serialized data is saved");
        } catch (IOException i) {
            i.printStackTrace();
        }
        return fileName;
    }
    /**
     * reads in a card from a file (deserialization)
     * @param fileName File of the card
     * @return the Card
     */
    public static Card read(String fileName) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            return (Card) in.readObject();
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Card class not found");
            c.printStackTrace();
            return null;
        }
    }
    /**
     * ADDED
     * For select test to generate random cards
     * Creates a random Card
     * @return a card of random suit and rank
     */
    public static Card randomCard(){
        Random random = new Random();
        Rank[] v = Card.Rank.values();
        return new Card(v[random.nextInt(13)],Suit.getRandom());
    }
    /**
     * ADDED
     * needed by Collections.remove in hand
     * @param obj comparison obj
     * @return Card
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Card && obj != null && ((Card) obj).getRank() == this.getRank() && ((Card) obj).getSuit() == this.getSuit();
    }
}
