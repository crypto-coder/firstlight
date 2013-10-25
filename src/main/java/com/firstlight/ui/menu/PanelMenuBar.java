/**
 * 
 */
package com.firstlight.ui.menu;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.ui.DefaultView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.ui.DefaultFXMLController;
import com.firstlight.ui.dashboard.DashboardController;
import com.firstlight.ui.dashboard.DashboardModel;
import com.firstlight.wallet.IWallet;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;


/**
 * @author MrMoneyChanger
 *
 */
public class PanelMenuBar extends DefaultFXMLController<MenuBarModel> implements InvalidationListener {

    
    @FXML private GridPane panelMenuMainContainer = null;
    @FXML private HBox menuItemContainer = null;
    @FXML private Pane backMenuItemContainer = null;
    
	//ChangeListener<Boolean> menuItemsNeedRenderChangeListener = null;
    
    
    /**
     * Blank Constructor.
     *      * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public PanelMenuBar() throws CoreException {
        this(null);
    }
         
    /**
     * Default Constructor.
     * 
     * @param view the view to control
     * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public PanelMenuBar(final DefaultView<MenuBarModel, Pane, DefaultFXMLController<MenuBarModel>> view) throws CoreException {
        super(view);        
    }



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {    
		super.initialize(arg0, arg1);
	}
	
	
    /**
     * {@inheritDoc}
     * @param <M>
     */
    @Override
    public void setModel(final MenuBarModel model){
    	boolean thisModelIsNew = (model != this.getModel());
    	
    	//If the Model has been previously defined and it is changing, we need to remove listeners
    	if(this.getModel() != null){
    		if(thisModelIsNew && this.getModel().currentMenuItemsProperty() != null){
    			this.getModel().currentMenuItemsProperty().removeListener(this);
    		}	
    		
    		if(this.getModel().previousMenuItems != null){
    	    	//Hide and remove menu items associated with the  current model
    			this.hideMenuItems(this.getModel().currentMenuItemsProperty().values());
    		}
    	}
    	
    	//Set the new model to be used
    	super.setModel(model);  	    	    	

		if(model.currentMenuItemsProperty() != null){
	    	//Add and show the menu items from the current model
	    	this.showMenuItems(this.getModel().currentMenuItemsProperty().values());
	    	
	    	//Indicate that rendering of the Model is complete,  and add them to the new Model
	    	if(thisModelIsNew){    		
	    		model.currentMenuItemsProperty().addListener(this);
    		}
    	}
    }

    
    @Override
    public MenuBarModel getModel() {
    	return super.getModel();
    }

    
    
	private void hideMenuItems(Collection<MenuItemModel> menuItems){
		if(!menuItems.isEmpty() && this.menuItemContainer != null && !this.menuItemContainer.getChildren().isEmpty()){
			
			for(MenuItemModel model : menuItems){			
				if(model.getController() != null && this.menuItemContainer.getChildren().contains(model.getController().getFXMLNode())){
					this.menuItemContainer.getChildren().remove(model.getController().getFXMLNode());  
				}
    		}
    	}
	}
	
	private void showMenuItems(Collection<MenuItemModel> menuItems){
		if(!menuItems.isEmpty() && this.menuItemContainer != null){

			for(MenuItemModel model : menuItems){	
    			PanelMenuItem menuItem = new PanelMenuItem(model, this.getModel());    			    			
    			this.menuItemContainer.getChildren().add(menuItem);    			
    			//if(model.getController() != null){menuItem.show();}
    		} 	
    	}
	}

	
	
	/*
	 * Used to receive notification from the Model that the MenuItems have changed in some way
	 * 
	 * (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void invalidated(Observable newMenuItems) {
		if(this.getModel().previousMenuItems != null){
			Collection<MenuItemModel> menuItemsToHide = Collections.emptySet();
			Collection<MenuItemModel> menuItemsToShow = Collections.emptySet();
			
			//Determine what changed and hide it
			for(String previousMenuItemKey : this.getModel().previousMenuItems.keySet()){
				//If the previousMenuItem is not in the newMenuItems, then add it to the list to hide
				if(!((ObservableMap<String, MenuItemModel>)newMenuItems).containsKey(previousMenuItemKey)){
					menuItemsToHide.add(this.getModel().previousMenuItems.get(previousMenuItemKey));
				}
			}
				
			hideMenuItems(menuItemsToHide);
			
			//Determine what changed and show it
			for(String newMenuItemKey : ((ObservableMap<String, MenuItemModel>)newMenuItems).keySet()){
				//If the newMenuItem is not in the previousMenuItems, then add it to the list to show
				if(!this.getModel().previousMenuItems.containsKey(newMenuItemKey)){
					menuItemsToShow.add(this.getModel().previousMenuItems.get(newMenuItemKey));
				}
			}
			
			showMenuItems(menuItemsToShow);
		}else{
			showMenuItems(((ObservableMap<String, MenuItemModel>)newMenuItems).values());
		}
		
		this.getModel().previousMenuItems = ((ObservableMap<String, MenuItemModel>)newMenuItems);
	}
	
	
	
	
	
	
	
	

}
