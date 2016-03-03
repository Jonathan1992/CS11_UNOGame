import java.util.*;

public class UNOGamePlay {
  ArrayList<UNOPlayer> players = new ArrayList<UNOPlayer>();
  CardDealer dealer;
  
  UNOCard pile;
  int numOfPlayers;
  boolean skip = false;
  boolean dir = true;
  int minNumOfCardToDraw = 0;
  
  int currentPlayerID = 0;
  
  UNOGamePlay(int numOfPlayers) {
    this.numOfPlayers = numOfPlayers;
    dealer = new CardDealer(3);
    for (int i=0; i<numOfPlayers; i++) {
      players.add(new UNOPlayer(dealer));
    }
    pile = dealer.drawCard();
  }
  
  public boolean checkCard(UNOCard card) {
    if (card.action == ActionCard.WILD ||
        card.action == ActionCard.WILD_DRAW_4) return true;
    
    if (card.color == pile.color) return true;
    
    if (card.cardNumber == pile.cardNumber) return true;
    
    return false;
  }
  
  public boolean checkUNO() {
    for (UNOPlayer plyr : players) {
      if (plyr.getNumOfCard() <= 1) return true;
    }
    return false;
  }
  
  public int nextPlayer() {
    int di = dir ? 1 : -1;
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
    
    System.out.println("Player " + currentPlayerID + "'s turn");
    
    // Check whether is player is force to draw card
    if (minNumOfCardToDraw > 0) {
      // This player is forced to draw card
      // Draw card and return
      // TODO player draw card
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
    // Check if the pile is skip
    switch (pile.action) {
    case SKIP: {
      skip = true;
      break;
    }
    case DRAW_2: {
      minNumOfCardToDraw = 2;
      break;
    }
    case WILD_DRAW_4: {
      minNumOfCardToDraw = 4;
      break;
    }
    case REVERSE: {
      dir = !dir;
      break;
    }
    case WILD: {
      // TODO propose user to input a color
      pile.color = proposeWildColor();
      break;
    }
    default: break;
    }
  }
  
  public UNOColor proposeWildColor() {
    System.out.println("please enter a color:");
    return UNOColor.YELLOW;
    // TODO complete this function
  }
}
