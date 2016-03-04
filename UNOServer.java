import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
  
  private int port;
  
  public UNOServer(int port, int numOfPlayers) {
    this.port = port;
    this.numOfPlayers = numOfPlayers;
  }
  
  private void initSocket() {
    try {
      unoServerSock = new ServerSocket(port);
      for (int i=0; i<numOfPlayers; i++) {
        PlayerSock ps = new PlayerSock(unoServerSock.accept());
        playerSockets.add(ps);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }

  @Override
  public void run() {
    initSocket();
    
    
  }
  
  public static void main(String[] args) {
    UNOServer usr = new UNOServer(8099, 2);
    Thread t1 = new Thread(usr);
    t1.start();
  }
}
