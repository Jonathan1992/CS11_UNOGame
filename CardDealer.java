import java.util.*;

public class CardDealer {
  private ArrayList<UNOCard> cardStack;
  
  CardDealer(int numOfDecks) {
    cardStack = new ArrayList<UNOCard>();
    
    for (int i=0; i<numOfDecks; i++) {
      for (UNOColor cl : UNOColor.values()) {
        for (int j=0; j<10; j++) {
          cardStack.add(new UNOCard(cl, ActionCard.NONE, j));
        }
        cardStack.add(new UNOCard(cl, ActionCard.SKIP, 0));
        cardStack.add(new UNOCard(cl, ActionCard.REVERSE, 0));
        cardStack.add(new UNOCard(cl, ActionCard.WILD, 0));
      }
      cardStack.add(new UNOCard(UNOColor.BLUE, ActionCard.WILD_DRAW_4, 0));
      cardStack.add(new UNOCard(UNOColor.BLUE, ActionCard.WILD, 0));
    }
    
    Collections.shuffle(cardStack);
  }
  
  public UNOCard drawCard() {
    return cardStack.remove(cardStack.size()-1);
  }
  
  public void addBack(UNOCard card) {
    int index = new Random().nextInt(cardStack.size());
    cardStack.add(index, card);
  }
  
}
