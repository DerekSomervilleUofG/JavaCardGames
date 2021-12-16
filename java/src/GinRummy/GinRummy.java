package GinRummy;

import CardGame.CardGame;
import Player.Player;
import CardGame.Hand;
import CardGame.Card;
import CardGame.Suit;
import CardGame.CardRank;
import CardGame.Deck;

import java.util.ArrayList;

public class GinRummy extends CardGame {

    private Hand discardCards = new Hand();
    public int numberOfCards = 7;

    protected ArrayList<Hand> getCardsOfSameNumber(Hand hand){
        ArrayList<Hand> cardsGroupBySameFace = new ArrayList<Hand>();
        hand.sortHandByFace();
        CardRank previousFace = null;
        Hand cardsOfSameSameFace = new Hand();
        for (Card card : hand.getHandOfCards()) {
            if (card.getRank() == previousFace ){
                cardsOfSameSameFace.add(card);
            } else if (cardsOfSameSameFace.size() ==  0){
                cardsOfSameSameFace.add(card);
                previousFace = card.getRank();
            }
            else {
                if (cardsOfSameSameFace.size() > 1){
                    cardsGroupBySameFace.add(cardsOfSameSameFace.copy());
                }
                cardsOfSameSameFace.clear();
                cardsOfSameSameFace.add(card);
                previousFace = card.getRank();
            }
        }
        cardsGroupBySameFace.add(cardsOfSameSameFace.copy());
        return cardsGroupBySameFace;
    }

    protected ArrayList<Hand> hasWinningHands(ArrayList<Hand>  hands){
        ArrayList<Hand> winningHands = new ArrayList<Hand>();
        for (Hand hand : hands){
            if (hand.size() >= 3) {
                winningHands.add(hand);
            }
        }
        return winningHands;
    }

    protected ArrayList<Hand> getRuns(Hand hand){
        ArrayList<Hand> cardsGroupByRuns = new ArrayList<Hand>();
        Hand cardsInRun = new Hand();
        try {
            hand.sortHand();
        } catch (Exception e) {
            this.userOutput.output("sortHand failed");
            this.userOutput.output(hand.toString());
        }

        Card firstCard = hand.getFirstCard();
        int previousRank = firstCard.getRank().getRank();
        Suit previousSuit = firstCard.getSuit();
        for (Card card : hand.getHandOfCards()){
            if (previousSuit == card.getSuit() && previousRank + 1 == card.getRank().getRank()) {
                cardsInRun.add(card);
                previousRank = card.getRank().getRank();
                previousSuit = card.getSuit();
            } else if (cardsInRun.size() == 0){
                cardsInRun.add(card);
                previousRank = card.getRank().getRank();
                previousSuit = card.getSuit();
            }
            else {
                if (cardsInRun.size() > 1){
                    cardsGroupByRuns.add(cardsInRun.copy());
                }
                previousRank = card.getRank().getRank();
                previousSuit = card.getSuit();
                cardsInRun.clear();
                cardsInRun.add(card);
            }
        }
        cardsGroupByRuns.add(cardsInRun.copy());
        return cardsGroupByRuns;
    }


    protected void help(){
        this.userOutput.output("Take from the dis Guarded pile or from the Deck");
        if (discardCards.size() > 0) {
            this.userOutput.output("The dis guarded card " + discardCards.getHandOfCards().get(discardCards.size()-1).toString());
        }
    }

    protected void userPlays(Player player, Deck deck){
        help();
        String userChoice = userInput.getString();
        Card card;
        if (userChoice.toUpperCase().equals("D")){
            card = deck.playACard();
        } else {
            card = discardCards.playACard();
        }
        player.getHand().add(card);
        player.getHand().sortHand();
        userOutput.output(player.getHand().toString());
        userOutput.output("Please enter zero to seven to select card to dis guard or enter the card e.g. HA");
        String userCard = userInput.getString();
        discardCards.add(player.getHand().playACard(userCard));
    }

    protected void computerPlays(Player player, Deck deck){
        Card card = null;
        boolean playFromDeck = true;
        if (discardCards.size() > 0) {
            card = discardCards.playACard();
            player.getHand().add(card);
            if (!playerHasWon(player)) {
                player.getHand().playACard(card);
            } else {
                playFromDeck = false;
            }

        }

        if (playFromDeck) {
            player.getHand().add(deck.playACard());
        }

    }


    public int scoreHand(Hand hand){
        Hand newHand = hand.copy();
        //System.out.println("New Hand before same number" + newHand);
        int total = 0;
        ArrayList<Hand> listOfSameNumber = getCardsOfSameNumber(newHand);
        //System.out.println("List of Same Numbers" + listOfSameNumber.get(0));

        for (Hand currentHand : listOfSameNumber){
            if (currentHand.size() >= 3) {
                total += currentHand.size();
                for (Card card : currentHand.getHandOfCards()){
                    newHand.remove(card);
                }
            }
        }

        //System.out.println("New Hand before run" + newHand);
        ArrayList<Hand> listOfRuns = getRuns(newHand);
        //System.out.println("List of runs" + listOfRuns.get(0));
        for (Hand currentHand : listOfRuns){
            if (currentHand.size() >= 3) {
                total += currentHand.size();
            }
        }
        return total;
    }

    public boolean playerHasWon(Player player){
        boolean winner = false;
        int total = scoreHand(player.getHand());
        if (total >= numberOfCards){
            winner = true;
            userOutput.output("Winner is " + player.getName());
            userOutput.output(player.getHand().toString());
        }

        return winner;
    }


    public static void main(String[ ] args) {
        GinRummy ginRummy = new GinRummy();
        ginRummy.setNoOfCards(ginRummy.numberOfCards);
        ginRummy.play();

    }
}
