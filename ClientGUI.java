import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The GUI the players see and can interact with
 * 
 * @author Yansong Liu, Matthew McGranahan
 */

public class ClientGUI extends Application {

  private UNOCard pile;  //last card played
  private ArrayList<UNOCard> hand = new ArrayList<UNOCard>();
  //hand of the player
  private ArrayList<Integer> counts = new ArrayList<Integer>();
  //index of each card in the hand
  private ArrayList<String> names = new ArrayList<String>();
  String color = ""; //variables for changing color
  boolean isColor = false;
  Integer myChoice;  //index of card to play
  int tmpChoice;
  
  HBox handBox = new HBox(5);   //hand
  HBox countBox = new HBox(5);  //index of cards in hand
  HBox pileBox = new HBox(5);   //last card played
  
  ClientGUI thisGui = this;
  
  //for proposeColor method
  private final String[] arrayData = {"BLUE", "GREEN", "RED", "YELLOW"};
  private List<String> dialogData;
  
  /**
   * Creates and displays the GUI
   * @return void Displays GUI
   */
  public void start(Stage primaryStage) {
    
    VBox root = new VBox(10);
    
    Label lb = new Label("Welcome to UNO");
    Button bt = new Button("Start");
    
    hand.add(new UNOCard(UNOColor.BLUE, ActionCard.REVERSE, 0));
    pile = new UNOCard(UNOColor.BLUE, ActionCard.REVERSE, 0);
    //first card played
    
    refreshGui();
    
    bt.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        UNOClient client = new UNOClient(thisGui, "Jonathan");
        client.run();   //starts game
      }
    });
    
    root.getChildren().addAll(lb, bt, countBox, pileBox, handBox);
    
    Scene scene = new Scene(root, 800, 600);
    
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Updates GUI after a card is played
   * @return void updates GUI
   */
  public void refreshGui() {
    refreshHand();
    refreshPile();
  }
  
  /**
   * Takes the unique attributes of each UNO card
   * 	and creates a unique filename based on that card
   * @param card The UNOCard that you want to display
   * @return String the filename corresponding to the card
   */
  public String getFileName(UNOCard card) {
    String col;
    if (card.isWild()) {  //wild cards have no color
      col = "";
    } else {
      col = card.color.toString();                  //gets color
    }
    String act = card.action.toString();            //gets type
    String num = Integer.toString(card.cardNumber); //gets number
    String fileName = "image/" + col + act + num + ".png";
    return fileName;
  }
  
   /**
   * Resets the hand if a card has been added of removed
   * @return void Updates hand
   */
  public void refreshHand() {
    handBox.getChildren().clear();  //removes images of cards
    for (UNOCard cd : hand) {       //new images for each card in hand
      ImageView img = new ImageView(getFileName(cd));
      img.addEventHandler(MouseEvent.MOUSE_CLICKED, 
          new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          //get the card index to remove from hand 
          //and add to pile
        }
      });
      handBox.getChildren().add(img);  //adds card images back to GUI
    }
  }
  
  /**
   * Updates the last card played by any player
   * @return void Updates GUI
   */
  public void refreshPile() {
    countBox.getChildren().clear();
    for (int i=0; i<counts.size(); i++) {
      countBox.getChildren().add(
          new Label(names.get(i) + ":" + counts.get(i).toString()));
          //adds new card if there is none
    }
    if (pile != null) {  //replaces old card
      pileBox.getChildren().clear();
      pileBox.getChildren().add(new Label(pile.toString()));
      if (pile.isWild()) {  //displays color of wild card played
        pileBox.getChildren().add(new Label(pile.color.toString()));
      }
    }
  }
  
  /**
   *Main method of the program
   * @param args
   * @return void
   */
  public static void main(String[] args) {
    launch(args);
  }
  
  // Client side APIs
  
  /**
   * Sets the pile on the GUI
   * @param pile The last card played
   */
  public void setPile(UNOCard pile) {
    this.pile = pile;
  }
  
  /**
   * Sets the hand on the GUI
   * @param hand the ArrayList of UnoCards
   * @return void
   */
  public void setHand(ArrayList<UNOCard> hand) {
    this.hand.clear();
    this.hand.addAll(hand);
  }
  
  /**
   * Sets the indexes of the cards in the hand on the GUI
   * @param counts the ArrayList<Integer> of indexes of the hand
   * @return void
   */
  public void setCount(ArrayList<Integer> counts) {
    this.counts.clear();
    this.counts.addAll(counts);
  }
  
  /**
   * Sets the name of each player
   * @param names The ArrayList<String> of the player names
   */
  public void setNane(ArrayList<String> names) {
    this.names.clear();
    this.names.addAll(names);
  }
  
  /**
   * Gets the index of a card the player clicks on in the GUI
   * @return int Index of the card a player wants to play
   */
  public int proposeIndex() {
    myChoice = 0;
    Dialog<Integer> dialog = new Dialog<>();
    VBox vroot = new VBox(10);
    HBox root = new HBox(5);
    Label lb = new Label();
    
    vroot.getChildren().addAll(root, lb);
    
    Button drawBt = new Button("Draw Card");
    drawBt.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        myChoice = -1;
        lb.setText("Current: Draw Card");
      }
    });
    root.getChildren().add(drawBt);  //adds drawn card
    
    for (int i=0; i<hand.size(); i++) {  //for each card in hand
      tmpChoice = i;
      ImageView img = new ImageView(getFileName(hand.get(i)));
      img.setId(""+i);
      img.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          myChoice = Integer.parseInt(img.getId());
          lb.setText("Current: " + myChoice);
          //shows the index of card you want to play on the GUI
        }
      });
      root.getChildren().add(img);
    }
    
    dialog.getDialogPane().setContent(vroot);
    
    // Button
    ButtonType buttonTypeOk = new ButtonType("Send", ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
    
    dialog.setResultConverter(new Callback<ButtonType, Integer>() {
      @Override
      public Integer call(ButtonType param) {
        return myChoice;
      }
    });
    
    Optional<Integer> result = dialog.showAndWait();
    return result.get();
  }
  
  /**
   * Gets the index of the card you wants to play.
   * This Version of the method is a prototype for a CLI
   * @return int Index of the card to be played
   */
  public int proposeIndex2() {
    //Dialog<Integer> dialog = new Dialog<Integer>();
    //Optional<Integer> result = dialog.showAndWait();
    TextInputDialog dialog = new TextInputDialog("");
    dialog.setTitle("Enter the index of your choice");
    dialog.setHeaderText("Enter some text, or use default value.");
    Optional<String> result = dialog.showAndWait();
    String entered = "0";
    if (result.isPresent()) {
        entered = result.get();
    }
    
    return Integer.parseInt(entered) - 1;  //changed to -1 
  }
  
  /**
   * Sets the String color to the color chosen by the user in
   * a Dialog box
   * @return void Sets the String color;
   */
  public void getColor() {
    dialogData = Arrays.asList(arrayData);
    ChoiceDialog dialog;
    dialog = new ChoiceDialog(dialogData.get(0), dialogData);
    dialog.setTitle("New Color");
    dialog.setHeaderText("Select a new color from this list...");
    Optional<String> result = dialog.showAndWait();
    //String selected = "cancelled.";
    if(result.isPresent()) {
      color = result.get();  //player selects one of the colors
    }   
  }
  
  /**
   * Turns the String color into a UNOColor enum
   * @return UNOColor The new color of the Wild card
   */
  public UNOColor setColor() {
    getColor(); 
    UNOColor col;
    if(color.equals("BLUE")) 
      return col = UNOColor.BLUE;
    if(color.equals("GREEN")) 
      return col = UNOColor.GREEN;
    if(color.equals("RED"))
      return col = UNOColor.RED;    
    if(color.equals("YELLOW"))
      return col = UNOColor.YELLOW;
    return null;
  }
  
  /**
   * Gets the new color of a Wild Card 
   * and keeps track of color changes.
   * This method is called by other classes.
   * @return UNOColor The new color of a Wild card 
   * and prints this value
   */
  public UNOColor proposeColor() {
    UNOColor col = setColor();
    System.out.println(color);
    System.out.println(col);
    return col;
    
  }
  
  /**
   * Creates a Popup message to tell the players the game is over.
   * @param msg String
   */
  public void endGame(String msg) {
    Alert end = new Alert(AlertType.INFORMATION);
    end.setTitle("GAME OVER");
    end.setHeaderText(null);
    end.setContentText("A player has won the game.");
    end.showAndWait();
  }
}

