import java.io.Serializable;

/**
 * This class is for the aspects and methods of an UNO card
 * @author Yansong Liu, Matthew McGranahan
 *
 */

enum UNOColor {YELLOW, RED, GREEN, BLUE}  //colors

enum ActionCard {WILD_DRAW_4, WILD, DRAW_2, SKIP, REVERSE, NONE}
//action types

public class UNOCard implements Comparable<UNOCard>, Serializable {
  
  private static final long serialVersionUID = 1L;
  public UNOColor color;
  public ActionCard action;
  public int cardNumber;
  
  /**
   * Constructor for an UNO card
   * @param color UNOColor enum for the color
   * @param action ActionCard enum for the types of cards
   * @param number int The number of the UNO card
   */
  UNOCard(UNOColor color, ActionCard action, int number) {
    this.color = color;
    this.action = action;
    this.cardNumber = number;
  }
  
  /**
   * Checks if a card is wild or not
   * @return boolean If a card is Wild of Wild Draw 4 returns true
   */
  public boolean isWild() {
    return action == ActionCard.WILD || action == ActionCard.WILD_DRAW_4;
  }

  /**
   * Overrides the compareTo() method to compare UNOCards for sorting
   * @param o UNOCard to compare to this UNOCard
   * @return int Returns -1 if this UNOCard is less than o, 1 if greater,
   * 	and 0 if they are equal
   */
  @Override
  public int compareTo(UNOCard o) {
    //first compare colors based on enum order
    if (this.color.ordinal() < o.color.ordinal()) {
      return -1;
    } else if (this.color.ordinal() > o.color.ordinal()) {
      return 1;
    }
    //next compare action types
    if (this.action.ordinal() < o.action.ordinal()) {
      return 1;
    } else if (this.action.ordinal() > o.action.ordinal()) {
      return -1;
    }
    //then compare numbers if all else equal
    return this.cardNumber - o.cardNumber;
  }
  
   /**
   * Overrides the toString() method to return a String based on the
   * 	values of the UNOCard
   * @return String The String representation of this UNOCard
   */
  @Override
  public String toString() {
    if (isWild()) {
      return action.toString();
    } else if (action != ActionCard.NONE) {
      return color.toString() + "-" + action.toString();
    } else {
      return color.toString() + "-" + cardNumber;
    }
  }
  
}
