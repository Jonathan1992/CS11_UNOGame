import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * This class is for implementing the UNO Server
 * It's multithreaded supporting many groups of players
 * @author Yansong Liu, Matthew McGranahan
 */
public class UNOServer implements Runnable {
  
  /**
   * This class is for packaging the player sockets
   * @author Yansong Liu, Matthew McGranahan
   */
  public class PlayerSock {
    public Socket socket;
    public ObjectInputStream inStream;
    public ObjectOutputStream outStream;
    
    /**
     * Constructor for Players socket
     * @param sock a socket accepted
     */
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
   * Constructor for UNOServer
   * @param port port to listen for connecting
   * @param numOfPlayers number of players supported
   */
  public UNOServer(int port, int numOfPlayers) {
    this.port = port;
    this.numOfPlayers = numOfPlayers;
  }
  
  /**
   * Initialize the socket
   * @return ArrayList<UNOPlayer> a list of connected players
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
  
  public static void main(String[] args) {
    UNOServer usr = new UNOServer(8099, 1);
    Thread t1 = new Thread(usr);
    t1.start();
  }
}
