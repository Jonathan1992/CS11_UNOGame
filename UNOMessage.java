import java.io.Serializable;
import java.util.*;

public class UNOMessage implements Serializable {

  public ArrayList<UNOCard> testContent = new ArrayList<UNOCard>();
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public int messageType;
  
  public ArrayList<Integer> playerCardCount = new ArrayList<Integer>();
  public int playerReply;

}
