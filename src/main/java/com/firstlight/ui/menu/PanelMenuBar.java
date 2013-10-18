/**
 * 
 */
package com.firstlight.ui.menu;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
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

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class PanelMenuBar extends DefaultFXMLController<MenuBarModel> {

	/** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PanelMenuBar.class);
    
    @FXML private GridPane panelMenuMainContainer = null;
    @FXML private HBox menuItemContainer = null;
    @FXML private Pane backMenuItemContainer = null;
    
    
    
    
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
    	//Hide and remove menu items associated with the  current model
    	this.hideCurrentMenuItems();
    	
    	//Set the new model to be used
    	super.setModel(model);  	
	    	
    	//Add and show the menu items from the current model
    	this.showCurrentMenuItems();
    }

    
    @Override
    public MenuBarModel getModel() {
    	return super.getModel();
    }

    
    
	private void hideCurrentMenuItems(){
		if(this.getModel() == null){return;}
		
		if(!this.getModel().getCurrentMenuItems().isEmpty() && this.menuItemContainer != null){
			HashMap<String, MenuItemModel> currentMenuItems = this.getModel().getCurrentMenuItems();
    		List<String> keysToRemove = new ArrayList<String>();
			
    		for(String modelKey : currentMenuItems.keySet()){
    			MenuItemModel model = currentMenuItems.get(modelKey);
    			
				if(model.getController() != null){
					model.getController().hide();				
					keysToRemove.add(modelKey);
				}
				
    			this.menuItemContainer.getChildren().remove(model.getController());  
    		}
    		
    		for(String menuItemKey : keysToRemove){       			
    			currentMenuItems.remove(menuItemKey);
    		}    		
    	}
	}
	
	private void showCurrentMenuItems(){
		
    	if(!this.getModel().getCurrentMenuItems().isEmpty() && this.menuItemContainer != null){
    		HashMap<String, MenuItemModel> currentMenuItems = this.getModel().getCurrentMenuItems();
			
    		for(String modelKey : currentMenuItems.keySet()){
    			MenuItemModel model = currentMenuItems.get(modelKey);
    			PanelMenuItem menuItem = new PanelMenuItem(model);
    			
    			this.menuItemContainer.getChildren().add(menuItem);    			
    			if(model.getController() != null){menuItem.show();}
    		} 	
    	}
	}

}
