import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

public class UNOClient implements Runnable {

  private Socket socket;
  private int port;
  private ObjectInputStream inputStream;
  private ObjectOutputStream outputStream;
  
  private ArrayList<UNOCard> playerHand = new ArrayList<UNOCard>();
  private ArrayList<Integer> playerCount = new ArrayList<Integer>();
  private UNOCard pile;
  
  public UNOClient() {
    port = 8099;
  }
  
  
  
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    UNOClient ucl = new UNOClient();
    Thread t2 = new Thread(ucl);
    t2.start();

  }
  @Override
  public void run() {
    // TODO Auto-generated method stub
    try {
      socket = new Socket("146.148.77.57", port);
      outputStream = new ObjectOutputStream(socket.getOutputStream());
      inputStream = new ObjectInputStream(socket.getInputStream());
      
      while (true) {
        UNOMessage wt = (UNOMessage) inputStream.readObject();
        
        if (wt.type == MessageType.BROADCAST) {
          // update hand info
          playerHand = wt.playerHand;
          playerCount = wt.playerCount;
          pile = wt.pile;
          
          System.out.println("Hand info updated!");
          System.out.println("Pile: " + wt.pile);
          System.out.println(playerCount);
          System.out.println(playerHand);
        } else if (wt.type == MessageType.PROPOSE) {
          System.out.println(wt.infoLine);
          Scanner scnr = new Scanner(System.in);
          int num = scnr.nextInt();
          
          UNOMessage msg = new UNOMessage();
          msg.type = MessageType.REPLY;
          msg.proposedCard = num;
          
          if (num >= 0) {
            if (playerHand.get(num).action == ActionCard.WILD ||
                playerHand.get(num).action == ActionCard.WILD_DRAW_4) {
              System.out.println("Please input a color");
              int cl = scnr.nextInt();
              if (cl == 1) msg.wildColor = UNOColor.BLUE;
              else if (cl == 2) msg.wildColor = UNOColor.GREEN;
              else if (cl == 3) msg.wildColor = UNOColor.RED;
              else msg.wildColor = UNOColor.YELLOW;
            }
          }
          
          outputStream.writeObject(msg);
          
          System.out.println("Message sent");
        } else if (wt.type == MessageType.RESULT){
          System.out.println(wt.infoLine);
          break;
        }
        
      }
    } catch (SocketException se) {
      se.printStackTrace();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

}
