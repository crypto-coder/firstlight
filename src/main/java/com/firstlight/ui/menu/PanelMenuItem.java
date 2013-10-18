/**
 * 
 */
package com.firstlight.ui.menu;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.property.*;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

/**
 * @author MrMoneyChanger
 *
 */
public class PanelMenuItem extends StackPane implements IMenuItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PanelMenuBar.class);
    
	@FXML private MenuItemModel model = new MenuItemModel();
	@FXML private ImageView panelMenuItemImage;
	@FXML private Label panelMenuItemLabel;

	
	
	public PanelMenuItem(){
	}
	public PanelMenuItem(MenuItemModel model){
		this();
		this.model = model;
		this.model.setController(this);
	}
	
	
	
	public MenuItemModel getModel(){
		return this.model;
	}
	public void setModel(MenuItemModel model){
		this.model = model;
	}
	
	
	
	
	@SuppressWarnings("deprecation")
	public String getImageURL(){
		//If an image URL wasn't provided with the menu item model, then default to the image currently loaded
		if(model == null || model.getImageURL() == null){			
			model.setImageURL(imageProperty().get().impl_getUrl());
		}
		return model.getImageURL();
	}
	
	public void setImageURL(String imageURL){
		if(model.getImageURL() != imageURL){model.setImageURL(imageURL);}
		if(imageProperty() != null){
			imageProperty().set(new Image(imageURL));
		}
	}
			
	
	public String getText(){
		return textProperty().get();
	}
	
	public void setText(String text){
		if(model.getText() != text){model.setText(text);}
		textProperty().set(text);
	}
	
	
	public ObjectProperty<Image> imageProperty(){
		return panelMenuItemImage.imageProperty();
	}
	
	public StringProperty textProperty(){
		return panelMenuItemLabel.textProperty();
	}
	
	
	public void show() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PanelMenuItem.fxml"));

		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
		
		try { 			
			fxmlLoader.load();
		} catch (IOException exception) {
			LOGGER.error("Failed to load the FXML file for the PanelMenuItem. Looking in /fxml/PanelMenuItem.fxml", exception);
			throw new RuntimeException(exception);
		} 
		
		//this.setText(this.getModel().getText());
		//this.setImageURL(this.getModel().getImageURL());
	}
	public void hide() {
	}
	@Override
	public Node getFXMLNode() {
		return this;
	}
	
	
	
	
	
}
