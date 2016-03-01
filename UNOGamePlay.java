import java.util.*;

enum Direction {CLOCK, COUNTER}

public class UNOGamePlay {
  ArrayList<UNOPlayer> players = new ArrayList<UNOPlayer>();
  CardDealer dealer;
  
  UNOCard pile;
  int numOfPlayers;
  boolean skip = false;
  Direction dir = Direction.CLOCK;
  int minNumOfCardToDraw = 0;
  
  int currentPlayerID = 0;
  
  UNOGamePlay(int numOfPlayers) {
    // TODO initialize
    this.numOfPlayers = numOfPlayers;
    dealer = new CardDealer(3);
    for (int i=0; i<numOfPlayers; i++) {
      players.add(new UNOPlayer(dealer));
    }
    pile = dealer.drawCard();
  }
  
  public boolean checkCard(UNOCard card) {
    // TODO check whether the card can be put on pile
    return false;
  }
  
  public boolean checkUNO() {
    // TODO return UNO situation
    return false;
  }
  
  public int nextPlayer() {
    int di = (dir == Direction.CLOCK) ? 1 : -1;
    if (skip) {
      skip = false;
      return (currentPlayerID + 2 * di) % players.size();
    } else {
      return (currentPlayerID + 1 * di) % players.size();
    }
  }
  
  public void playATurn() {
    System.out.println("Pile: " + pile.toString());
    
    // Get the player in turn
    currentPlayerID = this.nextPlayer();
    UNOPlayer playerInTurn = players.get(currentPlayerID);
    
    // Check whether is player is force to draw card
    if (minNumOfCardToDraw > 0) {
      // This player is forced to draw card
      // Draw card and return
      minNumOfCardToDraw = 0;
      return;
    }
    
    boolean handChanged = false;
    
    while (!handChanged) {
      // If the player propose a card
      int proposedCard = playerInTurn.proposeCard();
      if (proposedCard >= 0) {
        // check whether this card can be played
        boolean playable = checkCard(playerInTurn.getCard(proposedCard));
        
        // If this card can be played
        if (playable) {
          // play this card, update the pile
          dealer.addBack(pile);
          pile = playerInTurn.playCard(proposedCard);
          handChanged = true;
        }
      } else {
        // If the player didn't propose card, but draw card
        // player draw a card
        UNOCard drawedCard = dealer.drawCard();
        
        // Check whether this card is playable
        boolean playable = checkCard(drawedCard);
        
        // If this card can be played
        if (playable) {
          // play this card, update the pile
          dealer.addBack(pile);
          pile = drawedCard;
          handChanged = true;
        } else {
          playerInTurn.addCard(drawedCard);
          handChanged = true;
        }
      }
    }
    
    updateStatus();
    // Turn ends here
  }
  
  public void updateStatus() {
    
  }
}
