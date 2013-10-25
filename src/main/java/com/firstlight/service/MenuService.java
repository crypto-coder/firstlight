/**
 * 
 */
package com.firstlight.service;

import org.jrebirth.core.ui.handler.AbstractNamedEventHandler;

import java.util.HashMap;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import org.jrebirth.core.command.Command;
import org.jrebirth.core.command.CommandBean;
import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.link.AbstractWaveReady;
import org.jrebirth.core.service.DefaultService;
import org.jrebirth.core.wave.Wave;
import org.jrebirth.core.wave.WaveBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.command.RegionCommand;
import com.firstlight.ui.menu.MenuItemModel;
import com.firstlight.ui.wallet.KnownWalletModel;
import com.firstlight.wallet.IWallet;
import com.firstlight.wave.FirstLightWaves;
import com.firstlight.wave.MenuWaveBean;
import com.firstlight.wave.RegionAction;
import com.firstlight.wave.RegionWaveBean;
import com.firstlight.wave.WalletWaveBean;
import com.firstlight.wave.MenuAction;

/**
 * The class <strong>MenuService</strong>.
 * 
 * @author MrMoneyChanger
 */
public class MenuService extends DefaultService implements IMenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuService.class);
    
    private HashMap<String, MenuItemModel> menuItemModels = new HashMap<String, MenuItemModel>();
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void ready() throws CoreException {
        super.ready(); // Can be omitted but it's a bad practice

        registerCallback(DO_REGISTER_MENU_ITEM, RE_MENU_ITEM_REGISTEREDD);
        registerCallback(DO_UNREGISTER_MENU_ITEM, RE_MENU_ITEM_UNREGISTERED);
        
        listen(FirstLightWaves.DO_CHANGE_MENU_ITEMS);
        listen(FirstLightWaves.DO_MENU_ITEMS_CHANGED);
    }
	
        

    
    /**
     * Get the wave bean and cast it.
     * 
     * @param wave the wave that hold the bean
     * 
     * @return the casted wave bean
     */
    public MenuWaveBean getWaveBean(final Wave wave) {
        return (MenuWaveBean) wave.getWaveBean();
    }


    
    public void doMenuItemsChanged(Wave wave){}
    
    
    public void doChangeMenuItems(Wave wave){
    	MenuWaveBean waveBean = getWaveBean(wave);
    	
    	switch(waveBean.getAction()){
	    	case add:
	    		this.doRegisterMenuItem(wave);
	    		break;
	    	case edit:
	    		break;
	    	case remove:
	    		this.doUnregisterMenuItem(wave);
	    		break;
	    	default:
	    		break;
    	}    	
    }
    

	@Override
	public void doRegisterMenuItem(Wave wave) {
		String key = getWaveBean(wave).getKey();
		MenuItemModel menuItem = getWaveBean(wave).getMenuItem();	
		
		if(!this.menuItemModels.containsKey(key)){
			this.menuItemModels.put(key, menuItem);
		}		

		//this.sendWave(FirstLightWaves.DO_MENU_ITEMS_CHANGED);	
	}

	@Override
	public void doUnregisterMenuItem(Wave wave) {
		String key = getWaveBean(wave).getKey();
		
		if(this.menuItemModels.containsKey(key)){
			this.menuItemModels.remove(key);
			
	        //this.sendWave(FirstLightWaves.DO_MENU_ITEMS_CHANGED);  
		}else{
    		LOGGER.error("Failed to unregister a menu item successfully. MenuItem key: " + key);
    	}
	}




	
	@Override
	public MenuItemModel registerMenuItem(String key, String text, String imageURL,
											final Class<? extends CommandBean<WaveBean>> commandClass,
											final WaveBean commandWaveBean, 
											final Callback<MouseEvent, Boolean> callback) {
		MenuItemModel registeredMenuItem = null;
		
		if(this.menuItemModels.containsKey(key)){
			registeredMenuItem = this.menuItemModels.get(key);
		}else{
			registeredMenuItem = new MenuItemModel();
			this.menuItemModels.put(key, registeredMenuItem);
		}
		registeredMenuItem.setText(text);
		registeredMenuItem.setImageURL(imageURL);
		registeredMenuItem.setCommandLink(commandClass, commandWaveBean, callback);		

		this.sendWave(FirstLightWaves.DO_MENU_ITEMS_CHANGED);
        
		return registeredMenuItem;	
	}
	
	@Override
	public Boolean unregisterMenuItem(String key) {
		if(this.menuItemModels.containsKey(key)){
			this.menuItemModels.remove(key);
			
	        this.sendWave(FirstLightWaves.DO_MENU_ITEMS_CHANGED);  
		}

		return true;
	}
	
	@Override
	public MenuItemModel updateMenuItem(String key, MenuItemModel menuItem) {
		menuItem = this.menuItemModels.put(key, menuItem);
		return menuItem;		
	}
	




	@Override
	public HashMap<String, MenuItemModel> getCurrentMenuItems() {
		return this.menuItemModels;
	}

    

}
