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
import java.util.Random;
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
import javafx.collections.MapChangeListener;
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
public class PanelMenuBar extends DefaultFXMLController<MenuBarModel> implements ChangeListener<Boolean> {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PanelMenuBar.class);
    
    @FXML private GridPane panelMenuMainContainer = null;
    @FXML private HBox menuItemContainer = null;
    @FXML private Pane backMenuItemContainer = null;
    
    private boolean hasBeenInitialized = false;
    
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
		hasBeenInitialized = true;
	}
		
	
    /**
     * {@inheritDoc}
     * @param <M>
     */
    @Override
    public void setModel(final MenuBarModel model){
    	if(!hasBeenInitialized){return;}
    	
    	//If the Model has been previously defined and it is changing, we need to remove listeners
    	if(this.getModel() != null){
    		if(this.getModel().currentMenuItemsProperty() != null){
	    		this.getModel().menuItemsChangedProperty().removeListener(this);
    		}	
    		
    		if(this.getModel().previousMenuItems != null){
    	    	//Hide and remove menu items associated with the  current model
    			this.hideMenuItems(this.getModel().currentMenuItemsProperty().values());
    		}
    	}    		    	

		if(model.currentMenuItemsProperty() != null){
	    	//Indicate that rendering of the Model is complete,  and add them to the new Model	    	
	    	model.menuItemsChangedProperty().addListener(this);

	    	//Set the new model to be used
        	super.setModel(model);  
	    	
	    	//Add and show the menu items from the current model
	    	this.showMenuItems(this.getModel().currentMenuItemsProperty().values());	
	    	this.getModel().indicatedMenuItemChangesAffected();
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
					LOGGER.info("--------------------------- Hiding MenuItem " + model.getText());		
				}
    		}
    	}
	}
	
	private void showMenuItems(Collection<MenuItemModel> menuItems){
		if(!menuItems.isEmpty() && this.menuItemContainer != null){

			for(MenuItemModel model : menuItems){	
				if(model == null){
					LOGGER.info("--------------------------- Showing a MenuItem....but its model is null");
					continue;}
    			PanelMenuItem menuItem = new PanelMenuItem(model, this.getModel());    			    			
    			this.menuItemContainer.getChildren().add(menuItem);    
				LOGGER.info("--------------------------- Showing MenuItem " + model.getText());			
    			//if(model.getController() != null){menuItem.show();}
    		} 	
    	}
	}

	
	
	/* 
	 * Used to receive notification from the Model that the MenuItems have changed in some way
	 * 
	 * (non-Javadoc)
	 * @see javafx.beans.value.ChangeListener#changed(javafx.beans.value.ObservableValue, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
		if(arg0.getValue() == false){
			return;
		}

		HashMap<String, MenuItemModel> newMenuItems = new HashMap<String, MenuItemModel>();
		synchronized(this.getModel().currentMenuItemsProperty()){
			newMenuItems.putAll(this.getModel().currentMenuItemsProperty().get());
		}
					
		synchronized(this.getModel().previousMenuItems){
			if(this.getModel().previousMenuItems.size() > 0){
				Collection<MenuItemModel> menuItemsToHide = new ArrayList<MenuItemModel>();
				Collection<MenuItemModel> menuItemsToShow = new ArrayList<MenuItemModel>();
				
				//Determine what changed and hide it
				for(String previousMenuItemKey : this.getModel().previousMenuItems.keySet()){
					//If the previousMenuItem is not in the newMenuItems, then add it to the list to hide
					if(!newMenuItems.containsKey(previousMenuItemKey)){
						menuItemsToHide.add(this.getModel().previousMenuItems.get(previousMenuItemKey));
					}
				}
	
				hideMenuItems(menuItemsToHide);
				
				//Determine what changed and show it
				for(String newMenuItemKey : newMenuItems.keySet()){
					//If the newMenuItem is not in the previousMenuItems, then add it to the list to show
					if(!this.getModel().previousMenuItems.containsKey(newMenuItemKey)){
						menuItemsToShow.add(newMenuItems.get(newMenuItemKey));
					}
				}
	
				if(menuItemsToShow.size() > 0){
					showMenuItems(menuItemsToShow);
				}
			}else{
				showMenuItems(newMenuItems.values());
			}
			
			this.getModel().previousMenuItems = FXCollections.observableMap(newMenuItems);	
		}
		
		this.getModel().indicatedMenuItemChangesAffected();
	}
	
	
	
	
	
	
	
	

}
