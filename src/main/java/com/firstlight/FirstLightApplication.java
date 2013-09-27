package com.firstlight;


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import org.jrebirth.core.application.DefaultApplication;
import org.jrebirth.core.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.ui.ApplicationShellModel;


public final class FirstLightApplication extends DefaultApplication<AnchorPane> {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstLightApplication.class);
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
    }

    
        
    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeScene(final Scene scene) {
        super.customizeScene(scene);
    }
    
    
	
}
