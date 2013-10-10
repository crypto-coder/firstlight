/**
 * 
 */
package com.firstlight.ui.wallet;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.image.ImageView;

import com.firstlight.wallet.IWallet;

/**
 * @author MrMoneyChanger
 *
 */
public class KnownWalletListCell extends ListCell<IWallet> {

	GridPane walletListItem = new GridPane();
	ImageView walletImage = new ImageView("/images/Wallet.png");
	Label walletName = new Label("");
	Label hashCode = new Label("");
	Label walletLocation = new Label("");
	
	IWallet wallet = null;
	
	/**
	 * 
	 */
	public KnownWalletListCell() {
        super();
        walletListItem.setPrefSize(300, 100);
        walletListItem.setMinSize(300, 100);
        walletListItem.setMaxSize(Double.MAX_VALUE, 100);
        walletListItem.setStyle("walletListItem");
        walletListItem.getChildren().addAll(walletImage, walletName, hashCode, walletLocation);
                
        //Column Constraints        
        walletListItem.getColumnConstraints().add(new ColumnConstraints(100, 100, 100, Priority.NEVER, HPos.CENTER, false));
        walletListItem.getColumnConstraints().add(new ColumnConstraints(10, 200, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true));
        
        //Row Constraints        
        walletListItem.getRowConstraints().add(new RowConstraints(16, 30, 30, Priority.NEVER, VPos.CENTER, false));
        walletListItem.getRowConstraints().add(new RowConstraints(16, 30, 30, Priority.NEVER, VPos.CENTER, false));
        walletListItem.getRowConstraints().add(new RowConstraints(16, 30, 30, Priority.NEVER, VPos.CENTER, false));
                
        walletImage.setFitHeight(100);
        walletImage.setFitWidth(100);
        walletImage.setPickOnBounds(true);
        walletImage.setPreserveRatio(true);
        walletImage.getStyleClass().add("walletImage");
        GridPane.setConstraints(walletImage, 0, 0, 1, 3, HPos.CENTER, VPos.CENTER, Priority.NEVER, Priority.NEVER);        
        
        walletName.setPrefSize(200, 30);
        walletName.setTranslateX(0);
        walletName.setTranslateY(0);
        walletName.getStyleClass().add("walletName");
        GridPane.setConstraints(walletName, 1, 0, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER); 

        hashCode.setPrefSize(200, 30);
        hashCode.setTranslateX(0);
        hashCode.setTranslateY(0);
        hashCode.getStyleClass().add("hashCode");
        GridPane.setConstraints(hashCode, 1, 1, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER); 

        walletLocation.setPrefSize(200, 30);
        walletLocation.setTranslateX(0);
        walletLocation.setTranslateY(0);
        walletLocation.getStyleClass().add("walletLocation");
        GridPane.setConstraints(walletLocation, 1, 2, 1, 1, HPos.LEFT, VPos.CENTER, Priority.ALWAYS, Priority.NEVER); 
	}


    @Override
    protected void updateItem(IWallet item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);  // No text in label of super class
        if ( item != null) {
        	wallet = item;
        	walletName.setText(wallet.getName());
        	hashCode.setText(wallet.getHashCode());
        	walletLocation.setText(wallet.getLocation());
            setGraphic(walletListItem);
        }
    }
}
