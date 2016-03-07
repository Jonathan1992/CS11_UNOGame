import java.io.IOException;
import java.util.*;

public class UNOPlayer {
  ArrayList<UNOCard> hand = new ArrayList<UNOCard>();
  CardDealer dealer;
  
  UNOServer.PlayerSock sock;
  
  public void setDealer(CardDealer dl) {
    this.dealer = dl;
    
    // Draw 8 cards
    for (int i=0; i<8; i++) {
      hand.add(dealer.drawCard());
    }
    
    sortHand();
  }
  
  UNOPlayer(UNOServer.PlayerSock sock) {
    this.sock = sock;
  }
  
  public int getNumOfCard() {
    return hand.size();
  }
  
  
  public void addCard(UNOCard card) {
    hand.add(card);
  }
  
  public UNOCard playCard(int index) {
    return hand.remove(index);
  }
  
  public UNOCard getCard(int index) {
    return hand.get(index);
  }
  
  public void forceDraw(int num) {
    for (int i=0; i<num; i++) {
      hand.add(dealer.drawCard());
    }
  }
  
  public int proposeCard() {
    // Propose the card over the network
    UNOMessage msg = new UNOMessage();
    UNOMessage rpy = new UNOMessage();
    msg.infoLine = "Please select a card";
    msg.type = MessageType.PROPOSE;
    
    try {
      sock.outStream.writeObject(msg);
      rpy = (UNOMessage) sock.inStream.readObject();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    
    int index = rpy.proposedCard;
    
    System.out.println("Player reply " + index);
    
    return index;
  }

  public void broadCastHand(UNOCard pile, ArrayList<Integer> counts) {
    UNOMessage msg = new UNOMessage();
    msg.playerHand.addAll(hand);
    msg.pile = pile;
    msg.playerCount = new ArrayList<Integer>();
    msg.playerCount.addAll(counts);
    msg.type = MessageType.BROADCAST;
    
    try {
      sock.outStream.writeObject(msg);
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    System.out.println("Hand Broadcasted!");
  }
  
  public void broadCastResult() {
    UNOMessage msg = new UNOMessage();
    
    msg.type = MessageType.RESULT;
    if (this.getNumOfCard() == 0) {
      msg.infoLine = "You are the Winner!";
    } else {
      msg.infoLine = "Sorry you lose!";
    }
    
    try {
      sock.outStream.writeObject(msg);
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    System.out.println("Result Broadcasted!");
  }
  
  public void sortHand() {
    Collections.sort(hand);
  }
}
