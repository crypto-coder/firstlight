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

import org.jrebirth.core.application.DefaultApplication;
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
import com.puremoneysystems.firstlight.ui.ApplicationShellModel;
import com.puremoneysystems.firstlight.ui.DefaultFXMLController;
import com.puremoneysystems.firstlight.GuiceControllerFactory;


public final class FirstLightApplication extends DefaultApplication<AnchorPane> {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstLightApplication.class);
    //private final Injector injector = Guice.createInjector(new GuiceDeclarationsModule());
    
    public static Stage PrimaryStage;
    private static FirstLightApplication instance = null; 
        
    
    
    public static FirstLightApplication getInstance(){
    	if(instance == null){
    		instance = new FirstLightApplication();
    	}
    	
    	return instance;
    }
        
    
    
    public static void main(final String... args) {
        Application.launch(FirstLightApplication.class, args);
    }
	   
    


	
	
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Model> getFirstModelClass() {
        return ApplicationShellModel.class;
    }

    
    
    


	@Override
	protected void preInit() {
		FirstLightApplication.instance = this;		
	}


  
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeStage(final Stage stage) {
    	super.customizeStage(stage);
        stage.setFullScreen(false);        
        PrimaryStage = stage;
                
    }

    
        
    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeScene(final Scene scene) {
        super.customizeScene(scene);
    }
    
    
    
    	
	
	
	
	
	
}
