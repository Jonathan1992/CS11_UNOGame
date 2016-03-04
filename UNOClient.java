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
      socket = new Socket("localhost", port);
      outputStream = new ObjectOutputStream(socket.getOutputStream());
      inputStream = new ObjectInputStream(socket.getInputStream());
      
      UNOMessage wt = (UNOMessage) inputStream.readObject();
      
      Scanner scnr = new Scanner(System.in);
      int num = scnr.nextInt();
      
      
      UNOMessage msg = new UNOMessage();
      msg.testContent.add(new UNOCard(UNOColor.BLUE, ActionCard.REVERSE, num));
      
      outputStream.writeObject(msg);
      
      System.out.println("Message sent");
      
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
