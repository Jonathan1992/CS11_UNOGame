import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
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
  //hand of the player
  private ArrayList<UNOCard> hand = new ArrayList<UNOCard>();
  //index of each card in the hand
  private ArrayList<Integer> counts = new ArrayList<Integer>();
  private ArrayList<String> names = new ArrayList<String>();
  String color = "";
  boolean isColor = false;
  Integer myChoice;
  
  HBox countBox;
  HBox pileBox;
  HBox handBox;
  
  Scene gameScene;
  
  ClientGUI thisGui = this;
  
  //for proposeColor method
  private final String[] arrayData = {"BLUE", "GREEN", "RED", "YELLOW"};
  private List<String> dialogData;
  
  /**
   * Creates and displays the GUI
   * @return void Displays GUI
   */
  public void start(Stage primaryStage) throws IOException {
    
    primaryStage.setTitle("Online UNO");
    
    Parent startRoot = FXMLLoader.load(getClass().getResource("StartScene.fxml"));
    
    Button start_bt = (Button) startRoot.lookup("#start_bt");
    TextField name_fd = (TextField) startRoot.lookup("#player_name");
    
    start_bt.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        UNOClient client = new UNOClient(thisGui, name_fd.getText());
        try {
          initGame();
        } catch (IOException e) {
          e.printStackTrace();
        }
        primaryStage.setScene(gameScene);
        client.run();
      }
    });
    
    Scene startScene = new Scene(startRoot);
    
    primaryStage.setScene(startScene);
    primaryStage.show();
  }
  
  private void initGame() throws IOException {
    Parent gameRoot = FXMLLoader.load(getClass().getResource("GameScene.fxml"));
    
    countBox = (HBox) gameRoot.lookup("#countBox");
    pileBox = (HBox) gameRoot.lookup("#pileBox");
    handBox = (HBox) gameRoot.lookup("#handBox");
    
    gameScene = new Scene(gameRoot);
    
    gameScene.getStylesheets().add("myStyle.css");
    
    refreshGui();
  }

  /**
   * Updates GUI after a card is played
   * @return void updates GUI
   */
  public void refreshGui() {
    refreshHand();
    refreshCount();
  }
  
  /**
   * Takes the unique attributes of each UNO card
   *  and creates a unique filename based on that card
   * @param card The UNOCard that you want to display
   * @return String the filename corresponding to the card
   */
  public String getFileName(UNOCard card) {
    String col;
    if (card.isWild()) {
      col = "";
    } else {
      col = card.color.toString();
    }
    String act = card.action.toString();
    String num = Integer.toString(card.cardNumber);
    String fileName = "" + col + act + num + ".png";
    return fileName;
  }
  
  /**
   * Resets the hand if a card has been added of removed
   * @return void Updates hand
   */
  public void refreshHand() {
    // Refresh Pile
    if (pile != null) {
      pileBox.getChildren().clear();
      ImageView img = new ImageView(getFileName(pile));
      pileBox.getChildren().add(img);
      if (pile.isWild()) {
        pileBox.getChildren().add(new Label(pile.color.toString()));
      }
    }
    
    // Refresh Player's hand
    handBox.getChildren().clear();
    for (UNOCard cd : hand) {
      ImageView img = new ImageView(getFileName(cd));
      handBox.getChildren().add(img);
    }
  }
  
  /**
   * Updates the counts and name for all players
   * @return void Updates GUI
   */
  public void refreshCount() {
    countBox.getChildren().clear();
    for (int i=0; i<counts.size(); i++) {
      VBox ct = new VBox(10);
      ct.getChildren().add(new Label(names.get(i)));
      ct.getChildren().add(new Label(counts.get(i).toString()));
      ct.getStyleClass().add("count");
      ct.setMaxHeight(30);
      countBox.getChildren().add(ct);
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
  public void setName(ArrayList<String> names) {
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
    root.getChildren().add(drawBt);
    
    for (int i=0; i<hand.size(); i++) {
      ImageView img = new ImageView(getFileName(hand.get(i)));
      img.setId("" + i);
      img.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          myChoice = Integer.parseInt(img.getId());
          lb.setText("Current: " + myChoice);
        }
      });
      root.getChildren().add(img);
    }
    
    dialog.getDialogPane().setContent(vroot);
    dialog.setTitle("Please Select your Card to Play");
    
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
   * Gets the new color of a Wild Card 
   * and keeps track of color changes.
   * This method is called by other classes.
   * @return UNOColor The new color of a Wild card 
   * and prints this value
   */
  public UNOColor proposeColor() {
    dialogData = Arrays.asList(arrayData);
    ChoiceDialog<String> dialog =
        new ChoiceDialog<String>(dialogData.get(0), dialogData);
    dialog.setTitle("New Color");
    dialog.setHeaderText("Select a new color from this list...");
    Optional<String> result = dialog.showAndWait();
    
    switch (result.get()) {
    case "BLUE": return UNOColor.BLUE;
    case "GREEN": return UNOColor.GREEN;
    case "RED": return UNOColor.RED;
    default: return UNOColor.YELLOW;
    }
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

