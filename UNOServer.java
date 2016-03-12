import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

**
 * Class for performing the responsibilities of a server
 * @author Yansong Liu, Matthew McGranahan
 *
 */
public class UNOServer implements Runnable {
  
  public class PlayerSock {
    public Socket socket;
    public ObjectInputStream inStream;
    public ObjectOutputStream outStream;
    
    public PlayerSock(Socket sock) throws IOException {
      this.socket = sock;
      inStream = new ObjectInputStream(socket.getInputStream());
      outStream = new ObjectOutputStream(socket.getOutputStream());
    }
  }
  
  private ServerSocket unoServerSock;
  private int numOfPlayers;
  
  private int port;
  
  /**
   * Constructor
   * @param port The int number of the port for teh server
   * @param numOfPlayers The int number of players in the game
   */
  public UNOServer(int port, int numOfPlayers) {
    this.port = port;
    this.numOfPlayers = numOfPlayers;
  }
  
  /**
   * Connects the players to the server
   * @return ArrayList<UNOPlayer> The list of players in the game
   */
  private ArrayList<UNOPlayer> initSocket() throws IOException {
    if (unoServerSock == null) {
      unoServerSock = new ServerSocket(port);
    }
    
    ArrayList<UNOPlayer> retPlayer = new ArrayList<UNOPlayer>();
    for (int i=0; i<numOfPlayers; i++) {
      System.out.println("Waiting for Player " + (i+1) + "...");
      PlayerSock ps = new PlayerSock(unoServerSock.accept());
      System.out.println("Player " + (i+1) + " is online!");
      retPlayer.add(new UNOPlayer(ps));
    }
    
    return retPlayer;
  }

  /**
   * Overrides the run method to play the game
   */
  @Override
  public void run() {
    try {
      ArrayList<UNOPlayer> plyrs = initSocket();
      Thread t = new Thread(this);
      t.start();
      UNOGamePlay test = new UNOGamePlay(plyrs);
      while (test.playATurn()) {}
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Creates the server and a new thread for each player
   * @param args
   */
  public static void main(String[] args) {
    UNOServer usr = new UNOServer(8099, 1);
    Thread t1 = new Thread(usr);
    t1.start();
  }
}
