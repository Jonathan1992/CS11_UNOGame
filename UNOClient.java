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
  
  // Handle Graphical User Interface
  ClientGUI myGui;
  
  public UNOClient(ClientGUI gui) {
    port = 8099;
    this.myGui = gui;
  }
  
  @Override
  public void run() {
    try {
      socket = new Socket("146.148.77.57", port);
      //socket = new Socket("localhost", port);
      outputStream = new ObjectOutputStream(socket.getOutputStream());
      inputStream = new ObjectInputStream(socket.getInputStream());
      
      while (true) {
        UNOMessage wt = (UNOMessage) inputStream.readObject();
        
        if (wt.type == MessageType.BROADCAST) {
          // update hand info
          this.playerHand = wt.playerHand;
          this.playerCount = wt.playerCount;
          this.pile = wt.pile;
          
          myGui.setPile(this.pile);
          myGui.setHand(this.playerHand);
          myGui.setCount(this.playerCount);
          myGui.refreshGui();
          
        } else if (wt.type == MessageType.PROPOSE) {
          //System.out.println(wt.infoLine);
          //Scanner scnr = new Scanner(System.in);
          //int num = scnr.nextInt();
          int num = myGui.proposeIndex();
          
          UNOMessage msg = new UNOMessage();
          msg.type = MessageType.REPLY;
          msg.proposedCard = num;
          
          if (num >= 0) {
            if (playerHand.get(num).action == ActionCard.WILD ||
                playerHand.get(num).action == ActionCard.WILD_DRAW_4) {
              //System.out.println("Please input a color");
              msg.wildColor = myGui.proposeColor();
              
            }
          }
          
          outputStream.writeObject(msg);
          
          System.out.println("Message sent");
        } else if (wt.type == MessageType.RESULT){
          myGui.endGame(wt.infoLine);
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
      e.printStackTrace();
    }
    
  }

}
