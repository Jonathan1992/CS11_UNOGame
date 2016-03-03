import java.util.*;

public class UNOPlayer {
  ArrayList<UNOCard> hand = new ArrayList<UNOCard>();
  CardDealer dealer;
  
  Scanner scnr = new Scanner(System.in);
  
  UNOPlayer(CardDealer dealer) {
    this.dealer = dealer;
    
    // Draw 8 cards
    for (int i=0; i<8; i++) {
      hand.add(dealer.drawCard());
    }
    
    sortHand();
  }
  
  public int getNumOfCard() {
    return hand.size();
  }
  
  public void pressUNO() {
    
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
  
  public int proposeCard() {
    // TODO implement
    System.out.println("Currently you have:");
    System.out.println(hand);
    
    
    int index = scnr.nextInt();
    return index;
  }
  
  public void sortHand() {
    Collections.sort(hand);
  }
}
