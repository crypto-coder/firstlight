/**
 * 
 */
package com.firstlight.ui.menu;

import java.util.HashMap;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import org.jrebirth.core.ui.fxml.DefaultFXMLModel;
import org.jrebirth.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.service.IMenuService;
import com.firstlight.service.MenuService;
import com.firstlight.wave.FirstLightWaves;
import com.firstlight.wave.MenuWaveBean;

/**
 * @author MrMoneyChanger
 *
 */
public class MenuBarModel extends DefaultFXMLModel<MenuBarModel> {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuBarModel.class);
    
    private IMenuService menuService = null;
    private SimpleMapProperty<String, MenuItemModel> currentMenuItems = new SimpleMapProperty<String, MenuItemModel>();
    public ObservableMap<String, MenuItemModel> previousMenuItems = null;       
    
    
    
    
    public MenuBarModel(){    	
    	listen(FirstLightWaves.DO_MENU_ITEMS_CHANGED);
    	listen(FirstLightWaves.DO_CHANGE_MENU_ITEMS);
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
        this.setCurrentMenuItems(menuService.getCurrentMenuItems());
        LOGGER.trace("Loading Current Menu Items from the MenuService. " + this.currentMenuItems.size());
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
	public ObservableMap<String, MenuItemModel> getCurrentMenuItems() {
		return this.currentMenuItemsProperty().get();
	}
	/**
	 * @param currentMenuItems the currentMenuItems to set
	 */
	public void setCurrentMenuItems(HashMap<String, MenuItemModel> currentMenuItems) {
		this.currentMenuItemsProperty().set(FXCollections.observableMap(currentMenuItems));
	}
	
	public SimpleMapProperty<String, MenuItemModel> currentMenuItemsProperty(){
		return this.currentMenuItems;
	}
	
	
	
	
		

	public void doChangeMenuItems(Wave wave) {    	
    	MenuWaveBean waveBean = getWaveBean(wave);
    	
    	switch(waveBean.getAction()){
	    	case add:
	    	case edit:
	    		this.currentMenuItemsProperty().get().put(waveBean.getKey(), waveBean.getMenuItem());
	    		break;
	    	case remove:
	    		this.currentMenuItemsProperty().get().remove(waveBean.getKey());
	    		break;
	    	default:
	    		break;
    	}    	
	}
	public void doMenuItemsChanged(Wave wave) {    	
		//Reload the menuItems from the menuService
		this.loadCurrentMenuItems();		
	}
	
	
	
	/**
     * Get the wave bean and cast it.
     * 
     * @param wave the wave that hold the bean
     * 
     * @return the casted wave bean
     */
    private MenuWaveBean getWaveBean(final Wave wave) {
        return (MenuWaveBean) wave.getWaveBean();
    }
	
}
