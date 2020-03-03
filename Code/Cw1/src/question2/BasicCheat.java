package question2;

import java.util.*;
public class BasicCheat implements CardGame{
    private Player[] players;
    private int nosPlayers;
    public static final int MINPLAYERS=3;
    private int currentPlayer;
    private Hand discards;
    private Bid currentBid;
    private boolean notALLComputer = false;
//    static int correctCallsMade = 0;
//    static int incorrectCallsMade = 0;
//    static int callsAgainstCorrect = 0;
//    static int callsAgainstIncorrect = 0;
    // static variable single_instance of type Singleton
    private static BasicCheat singleInstance = null;

    public static void main(String[] args){
        test(1000,3);
    }
    /**
     * my testing function that plays games and prints the percentage that each player won
     */
    public static void test(int numberOfGames,int playerNumber) {
        if (playerNumber<MINPLAYERS){
            playerNumber=MINPLAYERS;
        }
        int[] winners = new int[playerNumber];
        BasicCheat cheat;
        for (int i = 1;i<numberOfGames+1;i++){
            System.out.println("Gamenum: " + i);
            cheat=new BasicCheat(playerNumber);
            int w = cheat.playGame();
            winners[w-1]++;
        }
        for (int i = 0;i<winners.length;i++) {
            if (numberOfGames/100>=1){
                double divider = numberOfGames/100;
                System.out.println((i+1) + ": " +winners[i]/divider + "%");
            } else {
                double multplyer = 100/numberOfGames;
                System.out.println((i+1) + ": " +winners[i]*multplyer + "%");
            }

        }
        //System.out.println("CorrectCalls: " + correctCallsMade + "\nIncorrectCalls: " +incorrectCallsMade + "\nSuccessfulCallsAgainst: " + callsAgainstCorrect+"\nFailtedCallsAgainst: " +callsAgainstIncorrect);
    }
    /**
     * Constructor
     */
    private BasicCheat(){
        this(MINPLAYERS);
    }
    /**
     * Constructor
     * @param n number of players
     */
    private BasicCheat(int n){
        nosPlayers=n;
        players=new Player[nosPlayers];
        for(int i=0;i<nosPlayers;i++)
            players[i]=(new BasicPlayer(new BasicStrategy(),this));
        currentBid=new Bid();
        Card.Rank[] v = Card.Rank.values();
        Random random = new Random();
        currentBid.setRank(v[random.nextInt(13)]);
        currentPlayer=0;
        singleInstance = this;
    }
    /**
     * ADDED
     * Gets the player
     * @param i index of player
     * @return Basicplayer of the player at index i
     */
    public BasicPlayer getPlayer(int i){
        return (BasicPlayer) players[i];
    }
    /**
     * ADDED
     * @return current player
     */
    public int getCurrentPlayer(){
        return currentPlayer;
    }
    /**
     * Plays the turn of the current player
     * @return true
     */
    @Override
    public boolean playTurn(){
        //lastBid=currentBid;
        //Ask player for a play,
        System.out.println("current bid = "+currentBid);
        currentBid=players[currentPlayer].playHand(currentBid);
        System.out.println("Player bid = "+currentBid);
        //Add hand played to discard pile
        discards.add(currentBid.getHand());
        //Offer all other players the chance to call cheat
        boolean cheat=false;
        for(int i=0;i<players.length && !cheat;i++){
            if(i!=currentPlayer){
                cheat=players[i].callCheat(currentBid);
                if(cheat){
                    System.out.println("Player called cheat by Player "+(i+1));
                    if (isCheat(currentBid)){
                        //CHEAT CALLED CORRECTLY
                        players[currentPlayer].addHand(discards);
                        System.out.println("Player " + (currentPlayer+1) + " cheats!");
//                        if (i==1){
//                            correctCallsMade+=1;
//                        }
//                        if (currentPlayer==1){
//                            callsAgainstCorrect +=1;
//                        }
                    } else {
                        //CHEAT CALLED INCORRECTLY
//                        if (i==1){
//                            incorrectCallsMade+=1;
//                        }
//                        if (currentPlayer==1){
//                            callsAgainstIncorrect +=1;
//                        }
                        System.out.println("Player " + (currentPlayer+ 1) + " Honest");
                        currentPlayer=i;
                        players[currentPlayer].addHand(discards);
                    }
                    System.out.println("Adding cards to player "+ (currentPlayer+1));
                    //If cheat is called, current bid reset to an empty bid with rank two whatever the outcome
                    currentBid=new Bid();
                    Card.Rank[] v = Card.Rank.values();
                    Random random = new Random();
                    currentBid.setRank(v[random.nextInt(13)]);
                    //Discards now reset to empty
                    discards=new Hand();
                    for(Player play: players){
                        BasicPlayer p = (BasicPlayer) play;
                        if (p.getS() instanceof ThinkerStrategy){
                            ThinkerStrategy T = (ThinkerStrategy) p.getS();
                            T.cheatCalled();
                        } else if (p.getS() instanceof MyStrategy){
                            MyStrategy M = (MyStrategy) p.getS();
                            M.cheatCalled(currentPlayer);
                        }
                    }
                }
            }
        }
        if(!cheat){
        //Go to the next player
            System.out.println("No Cheat Called");
            currentPlayer=(currentPlayer+1)%nosPlayers;
        }
        return true;
    }
    /**
     * Determines if the game has been won by anyone yet
     * @return -1, if nobody has won or player index if they have
     */
    public int winner(){
        for(int i=0;i<nosPlayers;i++){
            if(players[i].cardsLeft()==0)
                return i;
        }
        return -1;
    }
    /**
     * Initialises the game with PLayer 1 as Thinker and Player 2 as My
     */
    public void initialise(){
        StrategyFactory sf = new StrategyFactory();
        players[0].setStrategy(sf.factory("Thinker"));
        players[1].setStrategy(sf.factory("My"));
        players[1].setStrategy(sf.factory("Human"));
        //Create Deck of cards
        Deck d=new Deck();
        d.shuffle();
        //Deal cards to players
        Iterator<Card> it=d.iterator();
        int count=0;
        while(it.hasNext()){
            players[count%nosPlayers].addCard(it.next());
            it.remove();
            count++;
        }
        //Initialise Discards
        discards=new Hand();
        //Choose first player
        currentPlayer=0;
        currentBid=new Bid();
        Card.Rank[] v = Card.Rank.values();
        Random random = new Random();
        currentBid.setRank(v[random.nextInt(13)]);
        for(Player play: players){
            BasicPlayer p = (BasicPlayer) play;
            if (p.getS() instanceof HumanStrategy) {
                notALLComputer = true;
                break;
            }
        }


    }
    /**
     * Actually plays the game
     * @return the winner
     */
    public int playGame(){
        initialise();
        int c=0;
        int w = -1;
        Scanner in = new Scanner(System.in);
        boolean finished=false;
        while(!finished){
            //Play a hand
            System.out.println(" Cheat turn for player "+(currentPlayer+1));
            playTurn();
//            System.out.println(" Current discards =\n"+discards);
//            for (Player p:players){
//                BasicPlayer bp = (BasicPlayer) p;
//                bp.getH().sortAscending();
//                System.out.println("New player Hand\n" + bp.getH().toStringSorted());
//            }
            c++;
            System.out.println(" Turn "+c+ " Complete. Press any key to continue or enter Q to quit>");
            if(notALLComputer){
                String str=in.nextLine();
                if(str.equals("Q")||str.equals("q")||str.equals("quit"))
                    finished=true;
            }
            w=winner();
            if(w>=0){
                System.out.println("The Winner is Player "+(w+1));
                finished=true;
            }
        }
        return w+1;
    }
    /**
     * Checks if the player is cheating
     * @param b bid of the player
     * @return true if they are cheating, false if the bid isnt cheating
     */
    public static boolean isCheat(Bid b){
        for(Card c:b.getHand()){
            if(c.getRank()!=b.getRank())
                return true;
        }
        return false;
    }
    /**
     * ADDED
     *Gets instance of the BasicCheat
     * @return instance of the basicCheat Game
     */
    public static BasicCheat getInstance(){
        if (singleInstance == null){
            singleInstance = new BasicCheat();
        }
        return singleInstance;
    }
    /**
     * ADDED
     * @return numbere of players
     */
    public int getNosPlayers() {
        return nosPlayers;
    }
}
