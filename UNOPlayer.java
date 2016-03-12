import java.io.IOException;
import java.util.*;

/**
 * This class defines the methods and variables of a player
 * @author Yansong Liu, Matthew McGranahan
 *
 */

public class UNOPlayer {
  ArrayList<UNOCard> hand = new ArrayList<UNOCard>();
  CardDealer dealer;
  String nickName;
  
  UNOServer.PlayerSock sock;
  
   /**
   * Sets the CardDealer that will be used for this class
   * @param dl The CardDealer class for this game
   */
  public void setDealer(CardDealer dl) {
    this.dealer = dl;
    // Draw 8 cards
    for (int i=0; i<8; i++) {
      hand.add(dealer.drawCard());
    }
    sortHand();
  }
  
  /**
   * Constructor for the UNOPlayer class
   * @param sock The UNOServer connection being used
   */
  UNOPlayer(UNOServer.PlayerSock sock) {
    this.sock = sock;
  }
  
  /**
   * Gets the number of cards in a hand
   * @return int The size of a player's hand
   */
  public int getNumOfCard() {
    return hand.size();
  }
  
  /**
   * Adds a card to the player's hand
   * @param card The UNOCard to add to hand
   * @return void
   */
  public void addCard(UNOCard card) {
    hand.add(card);
  }
  
  /**
   * Plays the card the player clicks on in their hand
   * @param index int The position of the card to be played
   * @return UNOCard The UNOCard at the index
   */
  public UNOCard playCard(int index) {
    return hand.remove(index);
  }
  
  /**
   * Gets a card in the player's hand
   * @param index int The index in the hand of the card
   * @return UNOCard The card at the index
   */
  public UNOCard getCard(int index) {
    return hand.get(index);
  }
  
  /**
   * Draws cards because of an action card
   * @param num int The number of cards a player has to draw
   * @return void
   */
  public void forceDraw(int num) {
    for (int i=0; i<num; i++) {
      hand.add(dealer.drawCard());
    }
  }
  
  /**
   * Gets the nickanme of the player
   * @return String the nickname of the client
   */
  public String getNickName() {
    return this.nickName;
  }
  
  /**
   * Gets the index of a card clicked on to be played
   * @return int The index of the card to be played
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public int proposeCard() throws IOException, ClassNotFoundException {
    // Propose the card over the network
    UNOMessage msg = new UNOMessage();
    UNOMessage rpy = new UNOMessage();
    msg.infoLine = "Please select a card";
    msg.type = MessageType.PROPOSE;
    
    sock.outStream.writeObject(msg);
    rpy = (UNOMessage) sock.inStream.readObject();
    
    // Update player's name
    if (!rpy.infoLine.isEmpty()) {
      this.nickName = rpy.infoLine;
    }
    
    int index = rpy.proposedCard;
    
    if (index >= 0) {
      if (hand.get(index).isWild()) {
        hand.get(index).color = rpy.wildColor;
      }
    }
    
    return index;
  }

  /**
   * Shows the number of cards in this player's hand to the others
   * @param pile The last card in play
   * @param counts The ArratList<Integer> of the playerID of each player
   * @throws IOException
   */
  public void broadCastHand(UNOCard pile,
      ArrayList<Integer> counts,
      ArrayList<String> names) throws IOException {
    UNOMessage msg = new UNOMessage();
    msg.pile = pile;
    msg.playerHand.addAll(hand);
    msg.playerCount.addAll(counts);
    msg.playerName.addAll(names);
    msg.type = MessageType.BROADCAST;
    
    sock.outStream.writeObject(msg);
    
    System.out.println("Hand Broadcasted!");
  }
  
  /**
   * Gives each player the result of the previous game
   * @throws IOException
   */
  public void broadCastResult() throws IOException {
    UNOMessage msg = new UNOMessage();
    
    msg.type = MessageType.RESULT;
    if (this.getNumOfCard() == 0) {
      msg.infoLine = "You are the Winner!";
    } else {
      msg.infoLine = "Sorry you lose!";
    }
    
    sock.outStream.writeObject(msg);
    
    System.out.println("Result Broadcasted!");
  }
  
   /**
   * Sorts the player's hand
   */
  public void sortHand() {
    Collections.sort(hand);
  }
}
