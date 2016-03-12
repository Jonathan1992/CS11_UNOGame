import java.util.*;

/**
 * This class deals the cards to each player form the deck and resets
 * the deck from the pile
 * 
 * @author Yansong Liu, Matthew McGranahan
 * 
 */

public class CardDealer {
  private ArrayList<UNOCard> cardStack;
  
  /**
   * The constructor for the card dealer class, which creates cards
   * based on the number of decks you want to use
   * @param numOfDecks
   */
  CardDealer(int numOfDecks) {
    cardStack = new ArrayList<UNOCard>();
    
    for (int i=0; i<numOfDecks; i++) {  //how many decks to use
      for (UNOColor cl : UNOColor.values()) {  //for each type of color
        for (int j=0; j<10; j++) {
          cardStack.add(new UNOCard(cl, ActionCard.NONE, j));
          //adds the number cards (0-9) for each color
        }
        cardStack.add(new UNOCard(cl, ActionCard.SKIP, 0));
        cardStack.add(new UNOCard(cl, ActionCard.REVERSE, 0));
        cardStack.add(new UNOCard(cl, ActionCard.WILD, 0));
        //adds the action cards of each color
      }
      cardStack.add(new UNOCard(UNOColor.BLUE, ActionCard.WILD_DRAW_4, 0));
      cardStack.add(new UNOCard(UNOColor.BLUE, ActionCard.WILD, 0));
      //adds wild draw 4 and wild cards
    }
    
    Collections.shuffle(cardStack);  //randomizes cards
  }
  
  /**
   * Gets the next card in the deck
   * @return UNOCard the next card in the deck
   */
  public UNOCard drawCard() {
    return cardStack.remove(cardStack.size()-1);
  }
  
  /**
   * Adds a card back into the deck randomly after it is no
   * longer being used on the field
   * @param card the card to add back into the deck
   */
  public void addBack(UNOCard card) {
    int index = new Random().nextInt(cardStack.size());
    cardStack.add(index, card);
  }
  
}
