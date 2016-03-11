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
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import java.util.ArrayList;
import java.util.Optional;

public class ClientGUI extends Application {

  private UNOCard pile;
  private ArrayList<UNOCard> hand = new ArrayList<UNOCard>();
  private ArrayList<Integer> counts = new ArrayList<Integer>();
  String color = "";
  boolean isColor = false;
  
	VBox handBox = new VBox(5);
	HBox countBox = new HBox(5);
	HBox pileBox = new HBox(5);
  
  ClientGUI thisGui = this;
	
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
	  
	  Scene scene = new Scene(root, 600, 600);
	  
	  primaryStage.setScene(scene);
	  primaryStage.show();
	  
	  
	  
	  
	}
	
	public void refreshGui() {
		refreshHand();
		refreshPile();
	}
	
	public String getFileName(UNOCard card) {
		String col = card.color.name();
		String act = card.action.name();
		String num = Integer.toString(card.cardNumber);
		String fileName = col + act + num;
		return fileName;
	}
	
	public void refreshHand() {
	  /*
		handBox.getChildren().clear();
		for(int i = 0; i < hand.size(); i++) {
			String fileName = getFileName(hand.get(i));
			Image cardImage = new Image("/image/" + fileName + ".png");
			ImageView viewCard = new ImageView(cardImage);
			handBox.getChildren().add(viewCard);
		}
		*/
	  handBox.getChildren().clear();
	  for (UNOCard cd : hand) {
	      //handBox.getChildren().add(new Label(cd.toString()));
		  //handBox.getChildren().add(new ImageView(getFileName(cd)));
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
	  //Dialog<Integer> dialog = new Dialog<Integer>();
	  //Optional<Integer> result = dialog.showAndWait();
	  TextInputDialog dialog = new TextInputDialog("");
	  dialog.setTitle("Enter your choice");
	  dialog.setHeaderText("Enter some text, or use default value.");
	  Optional<String> result = dialog.showAndWait();
	  //dialog.close();
	  String entered = "0";
	  if (result.isPresent()) {
	      entered = result.get();
	  }
	  

	  return Integer.parseInt(entered);
	}
	
	public UNOColor proposeColor() {
		Stage colorSelect = new Stage();
		
		while(isColor == false) {
			setNewColor(colorSelect);   //need a better way to do this
		}
		if(color.equals("BLUE")) 
			return UNOColor.BLUE;
		if(color.equals("GREEN")) 
			return UNOColor.GREEN;
		if(color.equals("RED"))
			return UNOColor.RED;		
		if(color.equals("YELLOW"))
			return UNOColor.YELLOW;
		return UNOColor.BLUE;
	}
	
	public void setNewColor(Stage colorSelect) {
		//Popup pop = new Popup();
		VBox comp = new VBox();
		Button b = new Button("BLUE");
		Button g = new Button("GREEN");
		Button r = new Button("RED");
		Button y = new Button("YELLOW");
		comp.getChildren().add(b);
		comp.getChildren().add(g);
		comp.getChildren().add(r);
		comp.getChildren().add(y);
		
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				color = "BLUE";
				isColor = true;
				//pop.hide();
				colorSelect.hide();
			}
		});
		g.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				color = "GREEN";
				isColor = true;
				//pop.hide();
				colorSelect.hide();
			}
		});	
		r.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				color = "RED";
				isColor = true;
				//pop.hide();
				colorSelect.hide();
			}
		});
		y.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				color = "YELLOW";
				isColor = true;
				//pop.hide();
				colorSelect.hide();
			}
		});
		
		Scene stageScene = new Scene(comp);
		colorSelect.setScene(stageScene);
		colorSelect.show();		
	}
	
	public void endGame(String msg) {
		Popup end = new Popup();
	}
}

