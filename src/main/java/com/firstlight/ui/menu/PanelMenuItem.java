/**
 * 
 */
package com.firstlight.ui.menu;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.jrebirth.core.ui.handler.AbstractNamedEventHandler;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.beans.property.*;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

/**
 * @author MrMoneyChanger
 *
 */
public class PanelMenuItem extends StackPane implements IMenuItemController {
    
	private MenuItemModel model = null;
	public ImageView panelMenuItemImage;
	public Label panelMenuItemLabel;
	
	private StringProperty imageURL = new SimpleStringProperty();

	
	
	public PanelMenuItem(){
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PanelMenuItem.fxml"));

		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
				
		try { 			
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		} 		
		
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, new AbstractNamedEventHandler<MouseEvent>("PanelMenuItemClickHandler") {
				@Override
	            public void handle(final MouseEvent event) {

	            }
	        });
	}
	public PanelMenuItem(MenuItemModel model, MenuBarModel menuBarModel){
		this();
		this.setModel(model);		
		this.setMenuBarModel(menuBarModel);
		this.model.setController(this);
	}
	
	
	
	public MenuItemModel getModel(){
		if(this.model == null){
//			this.model = new MenuItemModel();
		}
		return this.model;
	}
	public void setModel(MenuItemModel model){
		this.model = model;
		
		this.setText(model.getText());
		this.setImageURL(model.getImageURL());
	}
	
	/**
	 * @return the menuBarModel
	 */
	public MenuBarModel getMenuBarModel() {
		if(this.getModel() == null){
			return null;
		}else{
			return this.getModel().getMenuBar();
		}
	}
	/**
	 * @param menuBarModel the menuBarModel to set
	 */
	public void setMenuBarModel(MenuBarModel menuBarModel) {
		if(this.getModel() != null){
			this.model.setMenuBar(menuBarModel);
		}
	}
	
	
	public String getImageURL(){
		return this.imageURLProperty().get();
	}
	
	public void setImageURL(String imageURL){
//		if(this.getModel().getImageURL() != imageURL){this.getModel().setImageURL(imageURL);}
		
		if(this.panelMenuItemImage == null){
			System.out.println("Could not find an FXML node with fx:id=panelMenuItemImage in the PanelMenuItem.fxml");
			return;
		}
		
		try{
			File imageFile = new File(imageURL);
			if(imageFile.exists()){
				this.panelMenuItemImage.setImage(new Image(Files.newInputStream(imageFile.toPath(), StandardOpenOption.READ)));
			}			

			this.imageURL.set(imageURL);
		}catch(Exception e){
			System.out.println("Could not load the provided image url in the ImageView. " + imageURL);			
		}
	}
	
	public StringProperty imageURLProperty(){
		return this.imageURLProperty();
	}
	
	
	
	
	public String getText(){
		return textProperty().get();
	}
	
	public void setText(String text){
		//if(this.getModel().getText() != text){this.getModel().setText(text);}
		textProperty().set(text);
	}
			
	public StringProperty textProperty(){
		try{
			return panelMenuItemLabel.textProperty();
		}catch(Exception e){
			return null;
		}
	}
	
	
	
	
	
	
	public void show() {
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
