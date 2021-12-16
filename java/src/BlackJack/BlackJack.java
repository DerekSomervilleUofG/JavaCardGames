package BlackJack;
import CardGame.Hand;
import CardGame.Card;
import Player.Player;
import CardGame.CardGame;
import CardGame.Deck;
import java.util.ArrayList;

public class BlackJack extends CardGame {
    private int maxScore = 21;
    private int noOfCards = 2;

    protected String generateHelp(){
        String help = "Please select one of the following options\n";
        for (BlackJackActions action : BlackJackActions.values()) {
            help += action.display() + "\n";
        }
        return help;
    }

    protected void help(){
        userOutput.output(generateHelp());
    }

    protected BlackJackActions getPlayerAction(Player player){
        String userChoice = " ";
        BlackJackActions userAction;
        help();
        if (player.hasHand()) {
            userOutput.output(player.getHand().toString());
        }
        userChoice = userInput.getString();
        userAction = BlackJackActions.getAction(userChoice.substring(0,1).toUpperCase());
        userOutput.output("You chose " + userAction.display());
        return userAction;
    }

    protected void userPlays(Player player, Deck deck){
        BlackJackActions userAction = BlackJackActions.PLAY;

        while (getScore(player.getHand()) <= maxScore && userAction != BlackJackActions.STICK){
            userAction = getPlayerAction(player);
            if (userAction == BlackJackActions.TWIST){
                player.getHand().add(deck.playACard());
            }
        }
        player.setWinner(true);
    }

    protected void computerPlays(Player player, Deck deck){
        Hand hand = player.getHand();
        while (getScore(hand) <= player.levelOfRisk){
            hand.add(deck.playACard());
        }
    }

    protected Player determineWinner(ArrayList<Player> players){
        Integer winningScore = 0;
        Player winningPlayer = null;
        int currentScore = 0;
        for (Player player : players){
            currentScore = getScore(player.getHand());
            if (currentScore <= maxScore && currentScore > winningScore){
                winningPlayer = player;
                winningScore = currentScore;
            }
        }
        return winningPlayer;
    }


    public int getScore(Hand hand){
        int score = 0;
        for (Card card: hand.getHandOfCards()){
            score += card.getRank().getValue();
        }
        return score;
    }

    public static void main(String[ ] args) {
        BlackJack blackJack = new BlackJack();
        blackJack.play();
    }


}
