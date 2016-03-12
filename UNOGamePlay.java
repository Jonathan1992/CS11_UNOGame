import java.io.IOException;
import java.util.*;

/**
 * Class for performing the gameplay functions of UNO
 * @author Yansong Liu, Matthew McGranahan
 *
 */

public class UNOGamePlay {
  ArrayList<UNOPlayer> players;  //list of all players
  ArrayList<Integer> playersCount = new ArrayList<Integer>();
  //list of the cards in each player's hands
  ArrayList<String> playersName = new ArrayList<String>();
  //list of names of each player
  CardDealer dealer;  //dealer used to draw cards
  
  UNOCard pile;
  int numOfPlayers;
  boolean skip = false;  //true if next player will be skipped
  boolean draw = false;  //true if next player has to draw cards
  boolean dir = true;    //true for clockwise
  int minNumOfCardToDraw = 0;  //number of cards forced to draw
  
  int currentPlayerID = 0;  //the player who's turn it is
  
  /**
   * Sets up the starting conditions of the game
   * @param plyrs ArrayList<UNOPlayer> The ArrayList of players
   * 	in the game
   * @throws IOException If cannot find image file of a card
   */
  UNOGamePlay(ArrayList<UNOPlayer> plyrs) throws IOException {
    this.numOfPlayers = plyrs.size();
    this.players = plyrs;
    dealer = new CardDealer(3);
    for (UNOPlayer plyr : this.players) {
      plyr.setDealer(dealer);
      playersCount.add(plyr.getNumOfCard());
      playersName.add("");
    }
    pile = dealer.drawCard();
    
    updateStatus();
  }
  
  /**
   * Checks to see if a player's proposed card is valid
   * @param card The card a player wants to play
   * @return boolean Returns true if this card can be played
   */
  public boolean checkCard(UNOCard card) {
    if (card.isWild()) return true;
    
    if (card.color == pile.color) return true;
    
    if (card.cardNumber == pile.cardNumber) return true;
    
    return false;
  }
  
  /**
   * Checks to see if a player only has one card in their hand
   * @return boolean Returns true if a player has one card left in hand
   */
  public boolean checkUNO() {
    for (UNOPlayer plyr : players) {
      if (plyr.getNumOfCard() <= 1) return true;
    }
    return false;
  }
  
  /**
   * This gives the Player ID of the next player
   * @return int Returns the Player ID of the next player.
   * 
   */
  public int nextPlayer() {
    int di = dir ? 1 : -1;
    if (skip) {
      skip = false;
      return (currentPlayerID + 2 * di + players.size()) % players.size();
    } else {
      return (currentPlayerID + 1 * di + players.size()) % players.size();
    }
  }
  
  /**
   * Plays one turn
   * @return boolean Returns true if a turn was played, false if there
   * 	was a problem
   * @throws ClassNotFoundException
   * @throws IOException
   */
  public boolean playATurn() throws ClassNotFoundException, IOException {
    System.out.println("Pile: " + pile.toString());
    
    // Get the player in turn
    currentPlayerID = this.nextPlayer();
    UNOPlayer playerInTurn = players.get(currentPlayerID);
    
    // Check whether is player is force to draw card
    if (minNumOfCardToDraw > 0) {
      // This player is forced to draw card
      // Draw card and return
      playerInTurn.forceDraw(minNumOfCardToDraw);
      minNumOfCardToDraw = 0;
      draw = false;
    } else {
      
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
          if (playable && !drawedCard.isWild()) {
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
      draw = true;
    }
    
    if (playerInTurn.getNumOfCard() == 0) {
      endGame();
      return false;
    }
    
    updateStatus();
    return true;
    // Turn ends here
  }
  
  /**
   * Updates the game variables after each turn
   * @return void
   * @throws IOException
   */
  public void updateStatus() throws IOException {
    // Broadcast the changed hand information
    for (int i=0; i<players.size(); i++) {
      playersCount.set(i, players.get(i).getNumOfCard());
      playersName.set(i, players.get(i).getNickName());
    }
    
    for (UNOPlayer plyr : players) {
      plyr.broadCastHand(pile, playersCount, playersName);
    }
    
    // Check if the pile is skip
    switch (pile.action) {
    case SKIP: {
      skip = true;
      break;
    }
    case DRAW_2: {
      if (draw) minNumOfCardToDraw = 2;
      break;
    }
    case WILD_DRAW_4: {
      if (draw) minNumOfCardToDraw = 4;
      break;
    }
    case REVERSE: {
      dir = !dir;
      break;
    }
    default: break;
    }
  }
  
  /**
   * Ends the game
   * @return void
   * @throws IOException
   */
  public void endGame() throws IOException {
    for (UNOPlayer plyr : players) {
      plyr.broadCastResult();
    }
  }
}
