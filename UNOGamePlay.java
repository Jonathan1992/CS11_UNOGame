import java.util.*;

enum Direction {CLOCK, COUNTER}

public class UNOGamePlay {
  ArrayList<UNOPlayer> players = new ArrayList<UNOPlayer>();
  CardDealer dealer;
  
  UNOCard pile;
  int numOfPlayers;
  boolean skip = false;
  Direction dir = Direction.CLOCK;
  int minNumOfCardToDraw = 1;
  
  int currentPlayerID;
  
  UNOGamePlay(int numOfPlayers) {
    // TODO initialize
  }
  
  public boolean checkCard(UNOCard card) {
    // TODO check whether the card can be put on pile
    return false;
  }
  
  public boolean checkUNO() {
    // TODO return UNO situation
    return false;
  }
  
  public int nextPlayer() {
    // TODO check direction, and skip
    return 0;
  }
  
  public int getPlayerChoice(int playerID) {
    return 0;
  }
}
