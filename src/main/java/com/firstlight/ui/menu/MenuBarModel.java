/**
 * 
 */
package com.firstlight.ui.menu;

import java.util.HashMap;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
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
public class MenuBarModel extends DefaultFXMLModel<MenuBarModel> {//, ChangeListener<ObservableMap<String, MenuItemModel>>, InvalidationListener {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuBarModel.class);
    
    private IMenuService menuService = null;
    private BooleanProperty menuItemsChanged = new SimpleBooleanProperty(false);
    private MapProperty<String, MenuItemModel> currentMenuItems = new SimpleMapProperty<String, MenuItemModel>();
    public ObservableMap<String, MenuItemModel> previousMenuItems = new SimpleMapProperty<String, MenuItemModel>();;       
    
    
    
    
	public MenuBarModel(){ 	
    	    	   
    }
    
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFXMLPath() {
        return "/fxml/PanelMenuBar.fxml";    	
    } 
    
    
    
    
    
    @Override
    protected void fxmlPreInitialize() {
    	super.fxmlPreInitialize();

    	listen(FirstLightWaves.DO_MENU_ITEMS_CHANGED);
    	listen(FirstLightWaves.DO_CHANGE_MENU_ITEMS);   
    	
    	try{
	    	this.menuService = (IMenuService)this.getService(MenuService.class);
	    	
	    	HashMap<String, MenuItemModel> menuItemsFromService = new HashMap<String, MenuItemModel>();
	    	menuItemsFromService.putAll(menuService.getCurrentMenuItems());
	    	
	        this.setCurrentMenuItems(menuItemsFromService);
    	}catch(Exception e){
    		LOGGER.error("Failed to find (or load) the MenuService during construction of the MenuBarModel.", e);
    		throw e;
    	}    	
    }
    
    
	/**
	 * @return the currentMenuItems
	 */
	public final ObservableMap<String, MenuItemModel> getCurrentMenuItems() {
		return this.currentMenuItems.get();
	}
	/**
	 * @param currentMenuItems the currentMenuItems to set
	 */
	public final void setCurrentMenuItems(HashMap<String, MenuItemModel> currentMenuItems) {
		this.currentMenuItems.set(FXCollections.observableMap(currentMenuItems));
		this.menuItemsChanged.set(true);
	}
	
	public final MapProperty<String, MenuItemModel> currentMenuItemsProperty(){
		return this.currentMenuItems;
	}
		
	
	
	
	public final boolean getMenuItemsChanged(){
		return this.menuItemsChanged.get();	
	}
	
	public final ReadOnlyBooleanProperty menuItemsChangedProperty(){
		return this.menuItemsChanged;
	}
	
	public final void indicatedMenuItemChangesAffected(){
		this.menuItemsChanged.set(false);
	}
	
		

	public void doChangeMenuItems(Wave wave) {    	
    	MenuWaveBean waveBean = getWaveBean(wave);
    	
    	synchronized(this.menuItemsChanged){
	    	switch(waveBean.getAction()){
		    	case add:
		    		if(!this.currentMenuItems.containsKey(waveBean.getKey())){		    	    	
		    			this.currentMenuItems.put(waveBean.getKey(), waveBean.getMenuItem());
		    			this.menuItemsChanged.set(true);
		    		}
		    		break;
		    	case edit:
		    		this.currentMenuItems.put(waveBean.getKey(), waveBean.getMenuItem());
		    		this.menuItemsChanged.set(true);
		    		break;
		    	case remove:
		    		if(this.currentMenuItems.containsKey(waveBean.getKey())){
		    			this.currentMenuItemsProperty().get().remove(waveBean.getKey());
		    			this.menuItemsChanged.set(true);
		    		}
		    		break;
		    	default:
		    		break;
	    	}    
    	}
	}
	public void doMenuItemsChanged(Wave wave) {    	
		//Reload the menuItems from the menuService
			
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
