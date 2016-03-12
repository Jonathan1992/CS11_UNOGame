import java.io.IOException;
import java.util.*;

public class UNOPlayer {
  ArrayList<UNOCard> hand = new ArrayList<UNOCard>();
  CardDealer dealer;
  String nickName;
  
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
  
  public String getNickName() {
    return this.nickName;
  }
  
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
  
  public void sortHand() {
    Collections.sort(hand);
  }
}
