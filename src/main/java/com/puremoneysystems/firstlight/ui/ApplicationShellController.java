package com.puremoneysystems.firstlight.ui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.net.URL;
import java.io.IOException;

import javafx.event.ActionEvent;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleObjectProperty;

import org.jrebirth.component.ui.stack.StackWaves;
import org.jrebirth.core.application.AbstractApplication;
import org.jrebirth.core.command.basic.ChainWaveCommand;
import org.jrebirth.core.command.basic.showmodel.ShowModelWaveBuilder;
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
import com.puremoneysystems.firstlight.ui.dashboard.DashboardMetrics;
import com.puremoneysystems.firstlight.ui.DefaultFXMLController;
import com.puremoneysystems.firstlight.GuiceControllerFactory;
import com.puremoneysystems.firstlight.FirstLightApplication;

import org.jrebirth.core.concurrent.JRebirth;
import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.ui.fxml.AbstractFXMLController;
import org.jrebirth.core.ui.fxml.FXMLComponent;
import org.jrebirth.core.wave.JRebirthWaves;
import org.jrebirth.core.wave.WaveBuilder;
import org.jrebirth.core.wave.WaveData;
import org.jrebirth.core.wave.WaveGroup;
import org.jrebirth.core.ui.DefaultView;
import org.jrebirth.core.key.UniqueKey;
import org.jrebirth.core.ui.Model;
import org.jrebirth.core.ui.View;
import org.jrebirth.component.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jfx.messagebox.*;



public class ApplicationShellController extends AbstractFXMLController<ApplicationShellState, View<ApplicationShellState,?,?>> {
												//DefaultFXMLController<ApplicationShellState> {
    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationShellController.class);

    private FXMLComponent applicationShell = null;    
    private FXMLComponent activeScreen = null;
    
    @FXML
    private AnchorPane fxmlShell;    
    @FXML
    private Pane navigationRegion;
    @FXML
    private Pane notificationRegion;
    @FXML
    private Pane activeScreenRegion;
    private SimpleObjectProperty<Node> activeScreenRegionProperty = new SimpleObjectProperty<Node>(this.activeScreenRegion);
    public final SimpleObjectProperty<Node> ActiveScreenRegionProperty() { return activeScreenRegionProperty; }
    public final Node getActiveScreenRegion() { return activeScreenRegionProperty.get(); }

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
    
    
    
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			FirstLightApplication app = FirstLightApplication.getInstance();
			
			//Load the FXML for the Application and set it as the Root node			
			this.applicationShell = new FXMLComponent(app.getRootNode(), this);
			app.getRootNode().setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			app.getRootNode().setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
			AnchorPane.setBottomAnchor(this.fxmlShell, Double.valueOf(0));
			AnchorPane.setLeftAnchor(this.fxmlShell, Double.valueOf(0));
			AnchorPane.setTopAnchor(this.fxmlShell, Double.valueOf(0));
			AnchorPane.setRightAnchor(this.fxmlShell, Double.valueOf(0));
			
			//Find the Navigation Region (id=navigationRegion) and create a fade in and down animation for it
			if(this.navigationRegion == null){
		        LOGGER.trace("Could not locate a Node with fx:id=navigationRegion in the /fxml/Application.fxml file.");				
			}else{				
				(new FadeInDownTransition(this.navigationRegion)).play();
			}
			
			//Find the Notification Region (id=notificationRegion) and create a fade in and up animation for it
			if(this.notificationRegion == null){
		        LOGGER.trace("Could not locate a Node with fx:id=notificationRegion in the /fxml/Application.fxml file.");				
			}else{				
				(new FadeInUpTransition(this.notificationRegion)).play();
			}
			
			//Find the Active Screen Region (id=activeScreenRegion) and create a fade in and fill animation for it
			if(this.activeScreenRegion == null){
		        LOGGER.trace("Could not locate a Node with fx:id=activeScreenRegion in the /fxml/Application.fxml file.");		
		        throw new NullPointerException("Could not locate a Node with id=activeScreenRegion in the /fxml/Application.fxml file.");
			}else{			
				//Load the FXML for the Home and set it as the first child node of the activeScreenRegion
//				this.getModel().sendWave(
//						WaveBuilder.create()
//								   .waveGroup(WaveGroup.CALL_COMMAND)
//								   .relatedClass(ShowHomeCommand.class)
//								   .build()						
//						);
//				this.getModel().sendWave(StackWaves.SHOW_PAGE_ENUM, WaveData.build(StackWaves.PAGE_ENUM, FXMLPages.DashboardFXML));
//				
		        final List<Wave> chainedWaveList = new ArrayList<>();
				Class<Model> dashboardModelClass = (Class<Model>) (DashboardMetrics.class).asSubclass(Model.class);
				Wave loadDashboardWave = ShowModelWaveBuilder.create()
															.uniquePlaceHolder(this.activeScreenRegionProperty)
															.showModelKey(this.getModel().getLocalFacade().buildKey(dashboardModelClass))
															.build();
				chainedWaveList.add(loadDashboardWave);
				
				JRebirth.runIntoJIT(runnable);
				this.getModel().getLocalFacade().getGlobalFacade().getNotifier().sendWave(
								                    WaveBuilder.create()
								                            .waveGroup(WaveGroup.CALL_COMMAND)
								                            .relatedClass(ChainWaveCommand.class)
								                            .data(WaveData.build(JRebirthWaves.CHAINED_WAVES, chainedWaveList))
								                            .build());
				
				
				
				//this.loadNewActiveScreen("/fxml/Dashboard.fxml");
				
				
//				linkWave(getView().getShowIncluded(), ActionEvent.ACTION, StackWaves.SHOW_PAGE_ENUM,
//		                WaveData.build(StackWaves.PAGE_ENUM, FXMLPage.IncludedFxml)/* , stackName */);
				
			}			
			
		} catch(Exception e) { 
			LOGGER.error("Error while customizing the main scene for the application : ", e);
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	private boolean hasActiveScreenLoaded(){
		return (this.activeScreen != null || this.activeScreenRegion.getChildren().size() > 0);		
	} 
    
    private void clearActiveScreen(){
    	this.activeScreenRegion.getChildren().clear(); 
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
    		//this.getModel().
    		//this.activeScreen = this.loadFXML(urlToFXML);			
	    	this.activeScreenRegion.getChildren().add(((Parent)this.activeScreen.getNode()));
    	}catch(Exception e){
			LOGGER.error("Error while loading an FXML file. URL : " + urlToFXML + " :: ", e);
			throw(e);
    	}    	    	
    	
		FadeTransition fade = new FadeTransition(Duration.millis(1000), this.activeScreenRegion);
		fade.setFromValue(0.0);
		fade.setToValue(1.0);
		fade.setCycleCount(1);
		
		ScaleTransition scale = new ScaleTransition(Duration.millis(1000), this.activeScreenRegion);
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
		FadeTransition fade = new FadeTransition(Duration.millis(1000), this.activeScreenRegion);
		fade.setFromValue(1.0);
		fade.setToValue(0.0);
		fade.setCycleCount(1);
		
		ScaleTransition scale = new ScaleTransition(Duration.millis(1000), this.activeScreenRegion);
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
		//fxmlLoader.setControllerFactory(new GuiceControllerFactory(this.injector));
		Pane fxmlNode = (Pane) fxmlLoader.load();
		
		DefaultFXMLController<Model> fxmlController =  null;
		if( fxmlLoader.getController() != null){
			fxmlController = (DefaultFXMLController<Model>) fxmlLoader.getController();
			//this.injector.injectMembers(fxmlController);
		}
				
		FXMLComponent returnFXML = new FXMLComponent(fxmlNode, fxmlController);
		fxmlLoader = null;
		
		return returnFXML;
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
