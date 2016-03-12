import java.io.Serializable;
import java.util.*;

/**
 * This class allows the creation of game messages
 * @author Yansong Liu, Matthew McGranahan
 *
 */

enum MessageType {BROADCAST, PROPOSE, REPLY, RESULT}

public class UNOMessage implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public MessageType type;
  public ArrayList<UNOCard> playerHand = new ArrayList<UNOCard>();
  public ArrayList<Integer> playerCount = new ArrayList<Integer>();
  public ArrayList<String> playerName = new ArrayList<String>();
  public UNOCard pile;
  public String infoLine;
  public int proposedCard;
  public UNOColor wildColor;
}
