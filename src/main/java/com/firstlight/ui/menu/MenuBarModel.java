/**
 * 
 */
package com.firstlight.ui.menu;

import java.util.HashMap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import org.jrebirth.core.ui.fxml.DefaultFXMLModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.service.IMenuService;
import com.firstlight.service.MenuService;

/**
 * @author MrMoneyChanger
 *
 */
public class MenuBarModel extends DefaultFXMLModel<MenuBarModel> {


    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuBarModel.class);
    
    private IMenuService menuService = null;

    private HashMap<String, MenuItemModel> currentMenuItems = new HashMap<String, MenuItemModel>();

    /** Current Number of TOR Peers **/
    private IntegerProperty torPeerCount = new SimpleIntegerProperty(this, "torPeerCount");
    public final IntegerProperty torPeerCountProperty() { return torPeerCount; }
    public final int getTORPeerCount() { return torPeerCount.getValue(); }
    
    
    
    public MenuBarModel(){
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFXMLPath() {
        return "/fxml/PanelMenuBar.fxml";
    } 
    
    
    
    public void loadCurrentMenuItems(){
    	//Retrieve the MenuService and request the current menu items
        this.currentMenuItems = menuService.getCurrentMenuItems();
    }
    
    
    @Override
    protected void fxmlPreInitialize() {
    	super.fxmlPreInitialize();
    	
    	try{
	    	this.menuService = (IMenuService)this.getService(MenuService.class);
	    	this.loadCurrentMenuItems();
    	}catch(Exception e){
    		LOGGER.error("Failed to find (or load) the MenuService during construction of the MenuBarModel.", e);
    		throw e;
    	}    	
    }
    
	/**
	 * @return the currentMenuItems
	 */
	public HashMap<String, MenuItemModel> getCurrentMenuItems() {
		return currentMenuItems;
	}
	/**
	 * @param currentMenuItems the currentMenuItems to set
	 */
	public void setCurrentMenuItems(HashMap<String, MenuItemModel> currentMenuItems) {
		this.currentMenuItems = currentMenuItems;
	}
    
    
	
}
