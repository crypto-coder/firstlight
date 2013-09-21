package com.puremoneysystems.firstlight.ui;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.net.URL;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ScaleTransitionBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;

import org.jrebirth.core.application.AbstractApplication;
import org.jrebirth.core.resource.font.FontItem;
import org.jrebirth.core.ui.Model;
import org.jrebirth.core.wave.Wave;
import org.jrebirth.core.ui.fxml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.fxexperience.javafx.animation.*;
import com.puremoneysystems.firstlight.resource.Fonts;
import com.puremoneysystems.firstlight.ui.ApplicationShellState;
import com.puremoneysystems.firstlight.ui.DefaultFXMLController;
import com.puremoneysystems.firstlight.GuiceControllerFactory;


import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.ui.fxml.AbstractFXMLController;
import org.jrebirth.core.ui.fxml.FXMLComponent;
import org.jrebirth.core.wave.WaveBuilder;
import org.jrebirth.core.wave.WaveGroup;
import org.jrebirth.core.ui.DefaultView;
import org.jrebirth.core.ui.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jfx.messagebox.*;

import com.puremoneysystems.firstlight.FirstLightApplication;


public class ApplicationShellController extends AbstractFXMLController<ApplicationShellState, View<ApplicationShellState,?,?>> {
												//DefaultFXMLController<ApplicationShellState> {
    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationShellController.class);

    private FXMLComponent applicationShell = null;    
    private FXMLComponent activeScreen = null;
    
    @FXML
    private AnchorPane navigationRegion;
    @FXML
    private GridPane notificationRegion;
    @FXML
    private AnchorPane activeScreenRegion;

    /**
     * Blank Constructor.
     *      * 
     * @throws CoreException if an error occurred while creating event handlers
     * /
    public ApplicationShellController() throws CoreException {
        this(null);
    }
         
    /**
     * Default Constructor.
     * 
     * @param view the view to control
     * 
     * @throws CoreException if an error occurred while creating event handlers
     * /
    public ApplicationShellController(final DefaultView<ApplicationShellState, AnchorPane, DefaultFXMLController<ApplicationShellState>> view) throws CoreException {
        super(view);
        
              
    }
    

    /**
     * {@inheritDoc}
     * @param <M>
     * /
    @Override
    public void setModel(final ApplicationShellState model){
    	super.setModel(model);  	
    }

    
    @FXML
    @Override
    public ApplicationShellState getModel() {
    	return super.getModel();
    }
  */
    
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			FirstLightApplication app = FirstLightApplication.getInstance();
			
			//Load the FXML for the Application and set it as the Root node			
			this.applicationShell = new FXMLComponent(app.getRootNode(), this);
			//app.getScene().setRoot(((Parent)this.applicationShell.getNode()));
			
			//Find the Navigation Region (id=navigationRegion) and create a fade in and down animation for it
			//this.navigationRegion = (Pane) this.applicationShell.getNode().lookup("#navigationRegion");
			if(this.navigationRegion == null){
		        LOGGER.trace("Could not locate a Node with id=navigationRegion in the /fxml/Application.fxml file.");				
			}else{				
				(new FadeInDownTransition(this.navigationRegion)).play();
			}
			
			//Find the Notification Region (id=notificationRegion) and create a fade in and up animation for it
			//this.notificationRegion = (Pane) this.applicationShell.getNode().lookup("#notificationRegion");
			if(this.notificationRegion == null){
		        LOGGER.trace("Could not locate a Node with id=notificationRegion in the /fxml/Application.fxml file.");				
			}else{				
				(new FadeInUpTransition(this.notificationRegion)).play();
			}
			
			//Find the Active Screen Region (id=activeScreenRegion) and create a fade in and fill animation for it
			//this.activeScreenRegion = (Pane) this.applicationShell.getNode().lookup("#activeScreenRegion");
			if(this.activeScreenRegion == null){
		        LOGGER.trace("Could not locate a Node with id=activeScreenRegion in the /fxml/Application.fxml file.");		
		        throw new NullPointerException("Could not locate a Node with id=activeScreenRegion in the /fxml/Application.fxml file.");
			}else{			
				//Load the FXML for the Dashboard and set it as the first child node of the activeScreenRegion
				//this.loadNewActiveScreen("/fxml/Dashboard.fxml");
			}			
			
		} catch(Exception e) { 
			LOGGER.error("Error while customizing the main scene for the application : ", e);
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    
    @FXML
    public void openWalletFromFile() {   
    	MessageBox.show(FirstLightApplication.PrimaryStage.getScene().getWindow(), "Clicked It", "Item clicked", MessageBox.ICON_INFORMATION);
    }
	
    
    @FXML
    public void openCurrencySummary() {   
    	MessageBox.show(FirstLightApplication.PrimaryStage.getScene().getWindow(), "Clicked It", "Item clicked", MessageBox.ICON_INFORMATION);
    }
	
    
    @FXML
    public void openConnectionSummary() {   
    	MessageBox.show(FirstLightApplication.PrimaryStage.getScene().getWindow(), "Clicked It", "Item clicked", MessageBox.ICON_INFORMATION);
    }
	
    
    @FXML
    public void navigationBack() {   
    	MessageBox.show(FirstLightApplication.PrimaryStage.getScene().getWindow(), "Clicked It", "Item clicked", MessageBox.ICON_INFORMATION);
    }
    

	
	
	
	
	
	
	
	
	
	

}
