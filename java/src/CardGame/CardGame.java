package CardGame;
import Display.ConsoleOutput;
import Display.ConsoleInput;
import Display.Input;
import Player.Player;
import Player.PlayerType;

import java.util.ArrayList;

public class CardGame {

    protected Integer noOfCards = 2;
    protected Deck deck;
    public ArrayList<Player> players;
    public Input userInput;
    public ConsoleOutput userOutput;


    public void setNoOfCards(Integer noOfCards) {
        this.noOfCards = noOfCards;
    }

    public Deck getDeck() {
        return deck;
    }

    public CardGame(){
        this.deck = new Deck();
        this.userInput = new ConsoleInput();
        this.userOutput = new ConsoleOutput();
        this.players = new ArrayList<Player>();
    }

    public void setUserInput(Input userInput){
        this.userInput = userInput;
    }

    private void createHumanPlayer(){
        userOutput.output("What is your name");
        String name = userInput.getString();
        players.add(new Player(PlayerType.USER,name,0));
    }

    private void createComputerPlayers(Integer noOfPlayers) {
        Player dealer = new Player(PlayerType.DEALER,"Dealer 1",17);
        players.add(dealer);
        noOfPlayers -= 1; //Remove the dealer
        for (int counter=2;counter < noOfPlayers;counter++){
            players.add(new Player(PlayerType.COMPUTER,"Comp" + counter,0));
        }
    }

    public void initiatePlayers(){
        players.clear();
        createHumanPlayer();
        userOutput.output("How many players, minimum of two?");
        int noOfPlayers = userInput.getInteger();
        createComputerPlayers(noOfPlayers);
    }

    protected void dealCards(){
        boolean allCards = false;
        int noOfCards;
        if (this.noOfCards == 0) {
            noOfCards = (int) Math.floor(deck.size()/players.size());
            allCards = true;
        } else {
            noOfCards = this.noOfCards;
        }
        for (Player player: players){
            Hand hand = new Hand();
            hand = dealHand(hand,noOfCards);
            player.setHand(hand);
        }
        if (allCards){
            for (Player player: players){
                if (deck.size() > 0){
                    player.getHand().add(deck.playACard());
                }
            }
        }
    }

    protected Hand dealHand(Hand hand, int noOfCards){

        for (int cardCounter=0;cardCounter<noOfCards;cardCounter++){
            if (deck.size() > 0) {
                hand.add(deck.playACard());
            }
        }
        return hand;
    }


    public void initiate(){
        initiatePlayers();
        deck.shuffleDeck();
        dealCards();
    }
    protected Player determineWinner(ArrayList<Player> players){
        Integer winningScore = 0;
        Player winningPlayer = null;
        int currentScore = 0;
        for (Player player : players){
            currentScore = getScore(player.getHand());
            if (currentScore > winningScore) {
                winningScore = currentScore;
                winningPlayer = player;
            }
        }
        return winningPlayer;
    }

    public void play(){
        initiate();
        Integer counterOfPlayers = 0;
        while (!players.get(counterOfPlayers).hasWon()){
            playerPlaysHand(players.get(counterOfPlayers));
            counterOfPlayers = (counterOfPlayers + 1) % players.size();
        }
        Player winner = determineWinner(players);
        showPlayers();

    }

    public int getScore(Hand hand){
        return 0;
    }

    protected void userPlays(Player player, Deck deck){}

    protected void computerPlays(Player player, Deck deck){}

    protected void playerPlaysHand(Player player){
        if (player.getPlayerType() == PlayerType.USER){
            userPlays(player, deck);
        } else {
            computerPlays(player, deck);
        }
    }


    public void showPlayers(){
        String theWinner = "";
        for (Player player: players){
            if (player.hasWon()){
                theWinner = "The winner is ";
            } else {
                theWinner = "";
            }
            userOutput.output(theWinner + player.getName() + " - " + player.getHand().toString());
        }
    }

    public static void main(String[ ] args) {
        CardGame cardGame = new CardGame();
        cardGame.play();
        cardGame.showPlayers();


    }
}
