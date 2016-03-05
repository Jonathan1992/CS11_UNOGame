import java.io.Serializable;
import java.util.*;

enum MessageType {BROADCAST, PROPOSE, REPLY}

public class UNOMessage implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public MessageType type;
  public ArrayList<UNOCard> playerHand = new ArrayList<UNOCard>();
  public ArrayList<Integer> playerCount = new ArrayList<Integer>();
  public UNOCard pile;
  public String infoLine;
  public int proposedCard;
  public UNOColor wildColor;
}
