import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * This class handles the functions of the client in the 
 * 	Client-Server relationship
 * @author Yansong Liu, Matthew McGranahan
 *
 */
public class UNOClient implements Runnable {

  private Socket socket;
  private int port;
  private ObjectInputStream inputStream;
  private ObjectOutputStream outputStream;
  
  //Same variables as the ClinetGUI class
  private ArrayList<UNOCard> playerHand = new ArrayList<UNOCard>();
  private ArrayList<Integer> playerCount = new ArrayList<Integer>();
  private ArrayList<String> playerName = new ArrayList<String>();
  private UNOCard pile;
  
  private String nickName;
  
  // Handle Graphical User Interface
  ClientGUI myGui;
  
  /**
   * Constructor for the UNOClent class
   * @param gui The ClientGUI used by the player
   */
  public UNOClient(ClientGUI gui, String name) {
    port = 8099;
    this.myGui = gui;
    this.nickName = name;
  }
  
  /**
   * Overrides the run method to make the Client constantly connect to
   * 	the server during the game
   */
  @Override
  public void run() {
    try {
      //socket = new Socket("146.148.77.57", port);
      //this statement is for connecting to the online server
      socket = new Socket("localhost", port);
      //this statement is for using the local machine as the server
      outputStream = new ObjectOutputStream(socket.getOutputStream());
      inputStream = new ObjectInputStream(socket.getInputStream());
      
      while (true) {  //always keep updating the game until the end
        UNOMessage wt = (UNOMessage) inputStream.readObject();
        
        if (wt.type == MessageType.BROADCAST) {
          // update hand info
          this.playerHand = wt.playerHand;
          this.playerCount = wt.playerCount;
          this.playerName = wt.playerName;
          this.pile = wt.pile;
          
          myGui.setPile(this.pile);
          myGui.setHand(this.playerHand);
          myGui.setCount(this.playerCount);
          myGui.setName(this.playerName);
          myGui.refreshGui();
          
        } else if (wt.type == MessageType.PROPOSE) {
          int num = myGui.proposeIndex();
          //gets the index of the card you want to play
          
          UNOMessage msg = new UNOMessage();
          msg.type = MessageType.REPLY;
          msg.proposedCard = num;
          
          if (num >= 0) {
            if (playerHand.get(num).isWild()) {
              msg.wildColor = myGui.proposeColor();
              //choose color of drawn Wild card to play
            }
          }
          
          // Update the name to the server
          msg.infoLine = this.nickName;
          
          outputStream.writeObject(msg);  //shows action taken
          
          System.out.println("Message sent");
        } else if (wt.type == MessageType.RESULT){
          myGui.endGame(wt.infoLine);
          break;  //end game
        }
        
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
