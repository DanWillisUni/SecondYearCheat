package question2;

import java.util.Iterator;
import java.util.Scanner;

public class HumanStrategy implements Strategy {
    /**
     * @param b   the bid this player has to follow (i.e the bid prior to this players turn.
     * @param h	  The players current hand
     * @return If the human has to cheat
     */
    @Override
    public boolean cheat(Bid b, Hand h) {
        if (hasToCheat(b,h)){
            System.out.println("You have to cheat");
            return true;
        } else {
            return false;
        }
    }
    /**
     * ADDED
     * Determines if the human has to cheat
     * @param b   the bid this player has to follow (i.e the bid prior to this players turn.
     * @param h	  The players current hand
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
     * gets the bid the human wants to play
     * @param b   the bid the player has to follow.
     * @param h	  The players current hand
     * @param cheat true if the Strategy has decided to cheat (by call to cheat())
     * @return The bid of the human
     */
    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat) {
        Hand bh = new Hand();
        Card.Rank r;
        System.out.println("Your hand is");
        h.sortDescending();
        Iterator<Card> iterator = h.sortIterator();
        int count = 0;
        while (iterator.hasNext()){
            Card card = iterator.next();
            System.out.println("  "+ (h.size() - count) +": " + card.toString());
            count++;
        }
        System.out.println("Last bid was: " + b.getCount() + " " +b.getRank());
        //choosing the cards to play
        System.out.println("Write down the number of the cards you want to play");
        Scanner input = new Scanner(System. in);
        String inputString = input.nextLine();
        String[] cardNumber = inputString.split(",");
        for (String cardNum:cardNumber) {
            if (Integer.parseInt(cardNum) > 0 && Integer.parseInt(cardNum) <=h.size()){
                bh.add(h.getSortedIndex(Integer.parseInt(cardNum) - 1));
            }
        }
        //choosing rank to say
        int number;
        do {
            System.out.println("Choose Rank\n0: " +b.getRank().getPrevious()+"\n1: " +b.getRank()+"\n2: " +b.getRank().getNext());
            number = input.nextInt();
        } while (number > 2 || number <0);
        if (number == 0){
            r = b.getRank().getPrevious();
        } else if (number == 1){
            r = b.getRank();
        } else {
            r = b.getRank().getNext();
        }
        h.remove(bh);
        return new Bid(bh,r);
    }
    /**
     * Asks if the human wants to call cheat or not
     * @param h	  The players current hand
     * @param b the current bid
     * @return if the human wants to call cheat or not
     */
    @Override
    public boolean callCheat(Hand h, Bid b) {
        h.sortAscending();
        System.out.println("Your hand is\n" + h.toStringSorted());
        System.out.println("Last bid was: " + b.getCount() + " " +b.getRank());
        Scanner input = new Scanner(System. in);
        int number;
        do {
            System.out.println("Do you want to call cheat?\n0:False\n1:True");
            number = input.nextInt();
        } while (number != 0 && number != 1);
        return number != 0;
    }
}
