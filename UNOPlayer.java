import java.io.IOException;
import java.util.*;

/**
 * This class is for implementing the UNO Player
 * Including maintaining the client-server socket for communication
 * @author Yansong Liu, Matthew McGranahan
 */
public class UNOPlayer {
  ArrayList<UNOCard> hand = new ArrayList<UNOCard>();
  CardDealer dealer;
  String nickName;
  
  UNOServer.PlayerSock sock;
  
  /**
   * Set the dealer for this player
   * @param dl the Card Dealer for this player
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
   * Constructor for the UNO Player
   * @param sock the client-server player socket class
   */
  UNOPlayer(UNOServer.PlayerSock sock) {
    this.sock = sock;
  }
  
  /**
   * Get the number of cards for this player
   * @return int the number of cards for this player
   */
  public int getNumOfCard() {
    return hand.size();
  }
  
  /**
   * Add a card to this player's hand
   * @param card UNO Card to add
   */
  public void addCard(UNOCard card) {
    hand.add(card);
  }
  
  /**
   * Player a card
   * @param index index of the card in player's hand
   * @return UNOCard the UNO Card played
   */
  public UNOCard playCard(int index) {
    return hand.remove(index);
  }
  
  /**
   * Get a card in hand
   * @param index index of the card in player's hand
   */
  public UNOCard getCard(int index) {
    return hand.get(index);
  }
  
  /**
   * Force this player to draw card
   * @param num number of cards of the force drawing
   */
  public void forceDraw(int num) {
    for (int i=0; i<num; i++) {
      hand.add(dealer.drawCard());
    }
  }
  
  /**
   * Get the nickname of this player
   * @return String player's name
   */
  public String getNickName() {
    return this.nickName;
  }
  
  /**
   * Propose the player over network to enter a choice
   * @return int the index return by client
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
   * Broadcast information to this player
   * @param pile pile card
   * @param counts number of cards for all players in the game
   * @param names nicknames for all players in the game
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
   * Broadcast the game result
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
   * Sort the player's hand of cards
   */
  public void sortHand() {
    Collections.sort(hand);
  }
}
