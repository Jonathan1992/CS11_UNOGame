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

public class ClientGUI extends Application {

  private UNOCard pile;
  private ArrayList<UNOCard> hand = new ArrayList<UNOCard>();
  private ArrayList<Integer> counts = new ArrayList<Integer>();
  String color = "";
  boolean isColor = false;
  Integer myChoice;
  int tmpChoice;
  
  HBox handBox = new HBox(5);
  HBox countBox = new HBox(5);
  HBox pileBox = new HBox(5);
  
  ClientGUI thisGui = this;
  
  //for proposeColor method
  private final String[] arrayData = {"BLUE", "GREEN", "RED", "YELLOW"};
  private List<String> dialogData;
  
  public void start(Stage primaryStage) {
    
    VBox root = new VBox(10);
    
    Label lb = new Label("Welcome to UNO");
    Button bt = new Button("Start");
    
    hand.add(new UNOCard(UNOColor.BLUE, ActionCard.REVERSE, 0));
    pile = new UNOCard(UNOColor.BLUE, ActionCard.REVERSE, 0);
    
    refreshGui();
    
    bt.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        UNOClient client = new UNOClient(thisGui);
        client.run();
      }
    });
    
    root.getChildren().addAll(lb, bt, countBox, pileBox, handBox);
    
    Scene scene = new Scene(root, 800, 600);
    
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  
  public void refreshGui() {
    refreshHand();
    refreshPile();
  }
  
  public String getFileName(UNOCard card) {
    String col;
    if (card.isWild()) {
      col = "";
    } else {
      col = card.color.toString();
    }
    String act = card.action.toString();
    String num = Integer.toString(card.cardNumber);
    String fileName = "image/" + col + act + num + ".png";
    return fileName;
  }
  
  public void refreshHand() {
    handBox.getChildren().clear();
    for (UNOCard cd : hand) {
      ImageView img = new ImageView(getFileName(cd));
      img.addEventHandler(MouseEvent.MOUSE_CLICKED, 
          new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          //get the card index to remove from hand 
          //and add to pile
        }
      });
      handBox.getChildren().add(img);
    }
  }
  
  public void refreshPile() {
    countBox.getChildren().clear();
    for (Integer it : counts) {
      countBox.getChildren().add(new Label(it.toString()));
    }
    if (pile != null) {
      pileBox.getChildren().clear();
      pileBox.getChildren().add(new Label(pile.toString()));
      if (pile.isWild()) {
        pileBox.getChildren().add(new Label(pile.color.toString()));
      }
    }
  }
  
  public static void main(String[] args) {
    launch(args);
  }
  
  // Client side APIs
  public void setPile(UNOCard pile) {
    this.pile = pile;
  }
  
  public void setHand(ArrayList<UNOCard> hand) {
    this.hand.clear();
    this.hand.addAll(hand);
  }
  
  public void setCount(ArrayList<Integer> counts) {
    this.counts.clear();
    this.counts.addAll(counts);
  }
  
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
      tmpChoice = i;
      ImageView img = new ImageView(getFileName(hand.get(i)));
      img.setId(""+i);
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
  
  public void getColor() {
    dialogData = Arrays.asList(arrayData);
    ChoiceDialog dialog;
    dialog = new ChoiceDialog(dialogData.get(0), dialogData);
    dialog.setTitle("New Color");
    dialog.setHeaderText("Select a new color from this list...");
    Optional<String> result = dialog.showAndWait();
    //String selected = "cancelled.";
    if(result.isPresent()) {
      color = result.get();
    }   
  }
  
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
  
  public UNOColor proposeColor() {
    UNOColor col = setColor();
    System.out.println(color);
    System.out.println(col);
    return col;
    
  }
  
  public void endGame(String msg) {
    Alert end = new Alert(AlertType.INFORMATION);
    end.setTitle("GAME OVER");
    end.setHeaderText(null);
    end.setContentText("A player has won the game.");
    end.showAndWait();
  }
}

