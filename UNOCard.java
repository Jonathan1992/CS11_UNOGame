import java.io.Serializable;

enum UNOColor {YELLOW, RED, GREEN, BLUE, BLACK}

enum ActionCard {WILD_DRAW_4, WILD, DRAW_2, SKIP, REVERSE, NONE}

public class UNOCard implements Comparable<UNOCard>, Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  public UNOColor color;
  public ActionCard action;
  public int cardNumber;
  
  UNOCard(UNOColor color, ActionCard action, int number) {
    this.color = color;
    this.action = action;
    this.cardNumber = number;
  }

  @Override
  public int compareTo(UNOCard o) {
    if (this.color.ordinal() < o.color.ordinal()) {
      return -1;
    } else if (this.color.ordinal() > o.color.ordinal()) {
      return 1;
    }
    
    if (this.action.ordinal() < o.action.ordinal()) {
      return 1;
    } else if (this.action.ordinal() > o.action.ordinal()) {
      return -1;
    }
    
    return this.cardNumber - o.cardNumber;
  }
  
  @Override
  public String toString() {
    if (action == ActionCard.WILD || action == ActionCard.WILD_DRAW_4) {
      if (color == UNOColor.BLACK) {
        return action.toString();
      } else {
        return action.toString() + "-" + color.toString();
      }
    } else if (action != ActionCard.NONE) {
      return color.toString() + "-" + action.toString();
    } else {
      return color.toString() + "-" + cardNumber;
    }
  }
  
}
