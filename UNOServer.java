import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

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
  
  private ArrayList<PlayerSock> playerSockets = new ArrayList<PlayerSock>();
  private ArrayList<UNOPlayer> players = new ArrayList<UNOPlayer>();
  
  private int port;
  
  public UNOServer(int port, int numOfPlayers) {
    this.port = port;
    this.numOfPlayers = numOfPlayers;
  }
  
  private void initSocket() {
    try {
      unoServerSock = new ServerSocket(port);
      for (int i=0; i<numOfPlayers; i++) {
        System.out.println("Waiting for Player " + (i+1) + "...");
        PlayerSock ps = new PlayerSock(unoServerSock.accept());
        System.out.println("Player " + (i+1) + " is online!");
        playerSockets.add(ps);
        players.add(new UNOPlayer(ps));
      }
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }

  @Override
  public void run() {
    initSocket();
    
    try {
      UNOGamePlay test = new UNOGamePlay(players);
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
