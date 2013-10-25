


/**
 * Get more info at : www.jrebirth.org .
 * Copyright JRebirth.org © 2011-2013
 * Contact : sebastien.bordes@jrebirth.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.firstlight.wave;

import com.firstlight.ui.menu.IMenuItemController;
import com.firstlight.ui.menu.MenuItemModel;

import org.jrebirth.core.wave.DefaultWaveBean;
import org.jrebirth.core.wave.Wave;
import org.jrebirth.core.wave.WaveBase;
import org.jrebirth.core.wave.WaveBean;
import org.jrebirth.core.wave.WaveBuilder;
import org.jrebirth.core.wave.WaveGroup;


import org.jrebirth.core.wave.WaveListener;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import org.jrebirth.core.command.CommandBean;
import org.jrebirth.core.command.CommandWaveBuilder;
import org.jrebirth.core.command.basic.showmodel.ShowModelWaveBuilder;
import org.jrebirth.core.concurrent.AbstractJrbRunnable;
import org.jrebirth.core.concurrent.JRebirth;
import org.jrebirth.core.exception.JRebirthThreadException;
import org.jrebirth.core.key.UniqueKey;
import org.jrebirth.core.ui.Model;
import org.jrebirth.core.wave.WaveBase;
import org.jrebirth.core.wave.Wave.Status;

import com.firstlight.command.RegionCommand;



/**
 * The class <strong>ChangeMenuWaveBuilder</strong>. is used to build a new Change Menu Wave.
 * 
 * @author MrMoneyChanger
 */
public class ChangeMenuWaveBuilder extends WaveBuilder<ChangeMenuWaveBuilder> {

    /** The unique key used to identify a menu item. */
    private String menuItemKey;

    /** The action to perform for this menu item. */
    private MenuAction action;

    /**  */
	private String text = null;

    /**  */
	private String imageURL = null;

    /**  */
	private Class<? extends CommandBean<WaveBean>> commandClass = null;

    /**  */
	private WaveBean commandWaveBean = null; 

    /**  */
	private Callback<MouseEvent, Boolean> callback = null;
        
    
    
    
    
    
    
    
    
    /**
     * Private constructor.
     */
    private ChangeMenuWaveBuilder() {
    	super();
    	
    }

 
    
    
    
    
    /**
     * Static method to build a default builder.
     * 
     * @return a new fresh ChangeMenuWaveBuilder instance
     */
    public static ChangeMenuWaveBuilder create() {
        return new ChangeMenuWaveBuilder()
        					.waveType(FirstLightWaves.DO_CHANGE_MENU_ITEMS)
        					.waveBeanClass(MenuWaveBean.class)
        					.waveGroup(WaveGroup.UNDEFINED);
    }

    
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void applyTo(final WaveBase paramWave) {
        super.applyTo(paramWave);

        if (hasBit(10)) {
            getWaveBean(paramWave).setKey(this.menuItemKey);
        }
        if (hasBit(11)) {
            getWaveBean(paramWave).setAction(this.action);
        }
        
        //Create a new MenuItemModel and assign its properties
        MenuItemModel model = new MenuItemModel();
        
        if (hasBit(12)) {
            model.setText(this.text);
        }
        if (hasBit(13)) {
            model.setImageURL(this.imageURL);
        }
        if (hasBit(14) && hasBit(15)) {
            if(hasBit(16)){
            	model.setCommandLink(this.commandClass, this.commandWaveBean, this.callback);
            }else{
            	model.setCommandLink(this.commandClass, this.commandWaveBean);
            }
        }
        
        getWaveBean(paramWave).setMenuItem(model);
    }
    
    
    
    
    
    
    

    /**
     * Define key for the region.
     * 
     * @param menuItemKey the menu item key
     * 
     * @return the builder
     */
    public ChangeMenuWaveBuilder menuItemKey(final String menuItemKey) {
        this.menuItemKey = menuItemKey;
        addBit(10);
        return this;
    }

    /**
     * Define action to process.
     * 
     * @param action the action to perform {@link MenuAction}
     * 
     * @return the builder
     */
    public ChangeMenuWaveBuilder action(final MenuAction action) {
        this.action = action;
        addBit(11);
        return this;
    }

    /**
     * Define text for the Menu Item.
     * 
     * @param text for the Menu Item. 
     * 
     * @return the builder
     */
    public ChangeMenuWaveBuilder text(final String text) {
        this.text = text;
        addBit(12);
        return this;
    }

    /**
     * Define image URL for the Menu Item.
     * 
     * @param image URL for the Menu Item. 
     * 
     * @return the builder
     */
    public ChangeMenuWaveBuilder imageURL(final String imageURL) {
        this.imageURL = imageURL;
        addBit(13);
        return this;
    }

    /**
     * The command class to activate when the Menu Item is clicked
     * 
     * @param commandClass Class of the command to activate when the Menu Item is clicked
     * 
     * @return the builder
     */
    public ChangeMenuWaveBuilder commandClass(final Class<? extends CommandBean<WaveBean>> commandClass) {
        this.commandClass = commandClass;
        addBit(14);
        return this;
    }
    
    /**
     * The WaveBean class to use with the Command when the Menu Item is clicked
     * 
     * @param commandWaveBean WaveBean to use with the command when the Menu Item is clicked
     * 
     * @return the builder
     */
    public ChangeMenuWaveBuilder commandWaveBean(final WaveBean commandWaveBean) {
        this.commandWaveBean = commandWaveBean;
        addBit(15);
        return this;
    }
    
    /**
     * The Callback function to use with the Command when the Menu Item is clicked
     * 
     * @param callback Callback function to use with the command when the Menu Item is clicked
     * 
     * @return the builder
     */
    public ChangeMenuWaveBuilder callback(final Callback<MouseEvent, Boolean> callback) {
        this.callback = callback;
        addBit(16);
        return this;
    }
    
    
    
    /**
     * Get the wave Bean from the wave and cast it.
     * 
     * @param wave the wave that hold the bean
     * 
     * @return the casted wavebean
     */
    protected MenuWaveBean getWaveBean(final Wave wave) {
        return (MenuWaveBean) wave.getWaveBean();
    }

    
        
    
    public void buildAndSend(final Model modelToSendFrom){
        final Wave changeMenuWave = this.build();
                        
//        changeMenuWave.addWaveListener(new WaveListener(){
//    		@Override public void waveCreated(Wave showWave) {}
//    		@Override public void waveSent(Wave showWave) {}
//    		@Override public void waveProcessed(Wave showWave) {}
//    		@Override public void waveCancelled(Wave showWave) {}
//    		@Override public void waveConsumed(Wave showWave) {
//    			changeMenuWave.removeWaveListener(this);
//    		}
//    		@Override public void waveFailed(Wave showWave) {}
//    		@Override public void waveDestroyed(Wave showWave) {}		        			        	
//        });
        
        //changeMenuWave.setStatus(Status.Sent);
    
        // Use the JRebirth Thread to manage Waves
        JRebirth.runIntoJAT(new AbstractJrbRunnable("Send Wave " + changeMenuWave.toString()) {
            @Override
            public void runInto() throws JRebirthThreadException {
            	modelToSendFrom.sendWave(changeMenuWave);
            }
        });        	    	
    	
    }
    
    
        
}
