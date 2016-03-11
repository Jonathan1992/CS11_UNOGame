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
  
	VBox handBox = new VBox(5);
	HBox countBox = new HBox(5);
	HBox pileBox = new HBox(5);
  
  ClientGUI thisGui = this;
  
  //for proposeColor method
  private final String[] arrayData = {"BLUE", "GREEN", "RED", "YELLOW"};
  private List<String> dialogData;
  
	public void start(Stage primaryStage) {
		/*
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
	  */
	  //proposeColor();
	}

	
	public void refreshGui() {
		refreshHand();
		refreshPile();
	}
	
	public String getFileName(UNOCard card) {
		String col = card.color.toString();
		String act = card.action.toString();
		String num = Integer.toString(card.cardNumber);
		String fileName = "image/" + col + act + num + ".png";
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
	  dialog.setTitle("Enter the index of your choice");
	  dialog.setHeaderText("Enter some text, or use default value.");
	  Optional<String> result = dialog.showAndWait();
	  //dialog.close();
	  String entered = "0";
	  if (result.isPresent()) {
	      entered = result.get();
	  }
	  

	  return Integer.parseInt(entered) - 1;  //changed to -1 
	}
	
	//do not use
	/*
	public UNOColor proposeColor() {
		Stage colorSelect = new Stage();
		VBox comp = new VBox();
		Button b = new Button("BLUE");
		Button g = new Button("GREEN");
		Button r = new Button("RED");
		Button y = new Button("YELLOW");
		comp.getChildren().add(b);
		comp.getChildren().add(g);
		comp.getChildren().add(r);
		comp.getChildren().add(y);
		Scene stageScene = new Scene(comp);
		colorSelect.setScene(stageScene);
		colorSelect.showAndWait();		
		
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				color = "BLUE";
				isColor = true;
				colorSelect.close();
			}
		});
		g.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				color = "GREEN";
				isColor = true;
				colorSelect.close();
			}
		});	
		r.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				color = "RED";
				isColor = true;
				colorSelect.close();
			}
		});
		y.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				color = "YELLOW";
				isColor = true;
				colorSelect.close();
			}
		});
		
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

