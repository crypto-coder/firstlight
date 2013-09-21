package com.puremoneysystems.firstlight;

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
import com.puremoneysystems.firstlight.GuiceControllerFactory;


public final class FirstLightApplication extends AbstractApplication<AnchorPane> {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstLightApplication.class);
    private final Injector injector = Guice.createInjector(new GuiceDeclarationsModule());
    
    private FXMLComponent applicationShell = null;    
    private FXMLComponent activeScreen = null;
    
    private Pane navigationRegionNode = null;
    private Pane notificationRegionNode = null;
    private Pane activeScreenRegionNode = null;
        
    public static Stage PrimaryStage;
        
    
    
    public static void main(final String... args) {
        Application.launch(FirstLightApplication.class, args);
    }
	   
    



	@Override
	protected void preInit() {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected void postInit() {
		// TODO Auto-generated method stub
		
	}
	
	
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Model> getFirstModelClass() {
        return FirstLightApplicationState.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Wave> getPreBootWaveList() {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Wave> getPostBootWaveList() {
        return Collections.emptyList();
    }
    
    
    
    
    
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeStage(final Stage stage) {
        stage.setFullScreen(false);
        PrimaryStage = stage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeScene(final Scene scene) {
		try {
			//Load the FXML for the Application and set it as the Root node			
			this.applicationShell = this.loadFXML("/fxml/Application.fxml");
			scene.setRoot(((Parent)this.applicationShell.getNode()));
			
			//Find the Navigation Region (id=navigationRegion) and create a fade in and down animation for it
			this.navigationRegionNode = (Pane) this.applicationShell.getNode().lookup("#navigationRegion");
			if(this.navigationRegionNode == null){
		        LOGGER.trace("Could not locate a Node with id=navigationRegion in the /fxml/Application.fxml file.");				
			}else{				
				(new FadeInDownTransition(this.navigationRegionNode)).play();
			}
			
			//Find the Notification Region (id=notificationRegion) and create a fade in and up animation for it
			this.notificationRegionNode = (Pane) this.applicationShell.getNode().lookup("#notificationRegion");
			if(this.notificationRegionNode == null){
		        LOGGER.trace("Could not locate a Node with id=notificationRegion in the /fxml/Application.fxml file.");				
			}else{				
				(new FadeInUpTransition(this.notificationRegionNode)).play();
			}
			
			//Find the Active Screen Region (id=activeScreenRegion) and create a fade in and fill animation for it
			this.activeScreenRegionNode = (Pane) this.applicationShell.getNode().lookup("#activeScreenRegion");
			if(this.activeScreenRegionNode == null){
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

    
    
    
    /**
     * TODO: REFACTOR and REMOVE this method
     * {@inheritDoc}
     */
    @Override
    public List<FontItem> getFontToPreload() {return null;}

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

	private boolean hasActiveScreenLoaded(){
		return (this.activeScreen != null || this.activeScreenRegionNode.getChildren().size() > 0);		
	} 
    
    private void clearActiveScreen(){
    	this.activeScreenRegionNode.getChildren().clear(); 
    	if(this.activeScreen != null){
    		this.activeScreen = null;
    	}
    }
        
	public void loadNewActiveScreen(String urlToFXML) throws Exception {
    	try{
    		if(this.hasActiveScreenLoaded()){
    			this.unloadActiveScreen();
    		}
	    	
			//Load the FXML for the screen and set it as the first child node of the activeScreenRegionNode	
    		this.activeScreen = this.loadFXML(urlToFXML);			
	    	this.activeScreenRegionNode.getChildren().add(((Parent)this.activeScreen.getNode()));
    	}catch(Exception e){
			LOGGER.error("Error while loading an FXML file. URL : " + urlToFXML + " :: ", e);
			throw(e);
    	}    	    	
    	
		FadeTransition fade = new FadeTransition(Duration.millis(1000), this.activeScreenRegionNode);
		fade.setFromValue(0.0);
		fade.setToValue(1.0);
		fade.setCycleCount(1);
		
		ScaleTransition scale = new ScaleTransition(Duration.millis(1000), this.activeScreenRegionNode);
		scale.setFromX(0.1);
		scale.setToX(1.0);
		scale.setFromY(0.1);
		scale.setToY(1.0);
		scale.setCycleCount(1);
		
		ParallelTransition parallel = new ParallelTransition();
		parallel.getChildren().addAll(fade, scale);
		parallel.setCycleCount(1);
		parallel.play();
    }
    
    

    private void unloadActiveScreen(){		
		FadeTransition fade = new FadeTransition(Duration.millis(1000), this.activeScreenRegionNode);
		fade.setFromValue(1.0);
		fade.setToValue(0.0);
		fade.setCycleCount(1);
		
		ScaleTransition scale = new ScaleTransition(Duration.millis(1000), this.activeScreenRegionNode);
		scale.setFromX(1.0);
		scale.setToX(0.1);
		scale.setFromY(1.0);
		scale.setToY(0.1);
		scale.setCycleCount(1);
		
		ParallelTransition parallel = new ParallelTransition();
		parallel.getChildren().addAll(fade, scale);
		parallel.setCycleCount(1);
		parallel.play();
		
		this.clearActiveScreen();	
    }
	
	
	
	
	
	
	
    @SuppressWarnings("unchecked")
	public FXMLComponent loadFXML(String urlToFXML) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(urlToFXML));
		fxmlLoader.setControllerFactory(new GuiceControllerFactory(this.injector));
		Pane fxmlNode = (Pane) fxmlLoader.load();
		
		DefaultFXMLController<Model> fxmlController =  null;
		if( fxmlLoader.getController() != null){
			fxmlController = (DefaultFXMLController<Model>) fxmlLoader.getController();
			this.injector.injectMembers(fxmlController);
		}
				
		FXMLComponent returnFXML = new FXMLComponent(fxmlNode, fxmlController);
		fxmlLoader = null;
		
		return returnFXML;
    }
         	
	
	
    
    
    
    
	
    
	private void showDefault(Stage primaryStage) {
		try {
			AnchorPane root = FXMLLoader.load(getClass().getResource("Application.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
	
	
	
	
}
