package com.firstlight.ui.menu;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import org.jrebirth.core.command.Command;
import org.jrebirth.core.command.CommandBean;
import org.jrebirth.core.ui.DefaultModel;
import org.jrebirth.core.ui.fxml.DefaultFXMLModel;
import org.jrebirth.core.ui.handler.AbstractNamedEventHandler;
import org.jrebirth.core.wave.WaveBean;


public class MenuItemModel extends DefaultFXMLModel<MenuItemModel> {
		
	private String text = null;
	private String imageURL = null;
	private IMenuItemController controller = null;
	private Boolean permanent = false;
	
	private Boolean commandReadyForControllerLink = false;
	private Class<? extends CommandBean<WaveBean>> commandClass = null;
	private WaveBean commandWaveBean = null; 
	private Callback<MouseEvent, Boolean> callback = null;
	
	
	public String getText(){
		return this.text;
	}
	public void setText(String text){
		this.text = text;
	}
	
	public String getImageURL(){
		return this.imageURL;
	}
	public void setImageURL(String imageURL){
		this.imageURL = imageURL;
	}

	/**
	 * @return the controller
	 */
	public IMenuItemController getController() {
		return controller;
	}
	/**
	 * @param controller the controller to set
	 */
	public void setController(IMenuItemController controller) {
		this.controller = controller;
		
		if(this.commandReadyForControllerLink){
			this.commandReadyForControllerLink = false;
			
			this.getController().getFXMLNode().addEventHandler(MouseEvent.MOUSE_CLICKED,  new AbstractNamedEventHandler<MouseEvent>("LinkCommand") {
	            /**
	             * Handle the triggered event.
	             */
	            @SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
	            public void handle(final MouseEvent event) {
	                if (callback == null || callback.call(event)) {
	                    callCommand(commandClass, commandWaveBean);
	                }
	            }
	        });
		}
	}
	
	/**
	 * @return the permanent
	 */
	public Boolean isPermanent() {
		return permanent;
	}
	/**
	 * @param permanent the permanent to set
	 */
	public void setPermanent(Boolean permanent) {
		this.permanent = permanent;
	}
	
	
	
	public void setCommandLink(Class<? extends CommandBean<WaveBean>> commandClass, WaveBean commandWaveBean){
		this.setCommandLink(commandClass, commandWaveBean, null);
	}
	public <E extends Event> void setCommandLink(Class<? extends CommandBean<WaveBean>> commandClass, WaveBean commandWaveBean, Callback<MouseEvent, Boolean> callback){
		this.commandClass = commandClass;
		this.commandWaveBean = commandWaveBean;
		this.callback = callback;
		
		this.commandReadyForControllerLink = true;
	}
	
	
}
