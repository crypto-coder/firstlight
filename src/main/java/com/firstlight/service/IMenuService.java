/**
 * 
 */
package com.firstlight.service;


import java.util.HashMap;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import org.jrebirth.core.command.Command;
import org.jrebirth.core.command.CommandBean;
import org.jrebirth.core.wave.Wave;
import org.jrebirth.core.wave.WaveBean;
import org.jrebirth.core.wave.WaveTypeBase;

import com.firstlight.ui.menu.MenuItemModel;



/**
 * @author MrMoneyChanger
 *
 */
public interface IMenuService {

	
	public static final WaveTypeBase DO_REGISTER_MENU_ITEM = WaveTypeBase.build("REGISTER_MENU_ITEM");
    public static final WaveTypeBase RE_MENU_ITEM_REGISTEREDD = WaveTypeBase.build("MENU_ITEM_REGISTERED");
    
    public static final WaveTypeBase DO_UNREGISTER_MENU_ITEM = WaveTypeBase.build("UNREGISTER_MENU_ITEM");
    public static final WaveTypeBase RE_MENU_ITEM_UNREGISTERED = WaveTypeBase.build("MENU_ITEM_UNREGISTERED");

	
        
    void doRegisterMenuItem(final Wave wave);
    void doUnregisterMenuItem(final Wave wave);
	
    
    
    HashMap<String, MenuItemModel> getCurrentMenuItems();
    
    
    MenuItemModel registerMenuItem(String key, String text, String imageURL, 
													final Class<? extends CommandBean<WaveBean>> commandClass, 
													final WaveBean commandWaveBean, 
													final Callback<MouseEvent, Boolean> callback);
    Boolean unregisterMenuItem(String key);
        
    MenuItemModel updateMenuItem(String key, MenuItemModel menuItem);
    
}
