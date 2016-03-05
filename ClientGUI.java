 import javafx.application.Application;
 import javafx.geometry.Rectangle2D;
 import javafx.scene.Group;
 import javafx.scene.Scene; 
 import javafx.scene.image.Image;
 import javafx.scene.image.ImageView;
 import javafx.scene.layout.HBox;
 import javafx.scene.paint.Color;
 import javafx.stage.Stage; 
 import javafx.scene.layout.HBox;
 import javafx.stage.PopupWindow;
 import java.util.ArrayList;
 
 
public class ClientGUI extends Application {

  public UNOCard pile;
  public ArrayList<UNOCard> hand;
  
	HBox handBox = new HBox(5);
	
	public void start(Stage primaryStage) {

	}
	
	public void update() {
		refreshHand();
		refreshPile();
	}
	
	public Integer proposeColor() {
		return 0;
	}
	
	public String getFileName(UNOCard card) {
		String col = card.color.name();
		String act = card.action.name();
		String num = Integer.toString(card.cardNumber);
		String fileName = col + act + num;
		return fileName;
	}
	
	public void refreshHand() {
		handBox.getChildren().clear();
		for(int i = 0; i < hand.size(); i++) {
			String fileName = getFileName(hand.get(i));
			Image cardImage = new Image("/image/" + fileName + ".png");
			ImageView viewCard = new ImageView(cardImage);
			handBox.getChildren().add(viewCard);
		}
	}
	
	public void refreshPile() {

	}
}

