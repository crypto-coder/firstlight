package com.puremoneysystems.firstlight;

import com.google.inject.AbstractModule;

import javafx.stage.Stage;


public class GuiceDeclarationsModule  extends AbstractModule {
	
    @Override
    protected void configure() {
    	//bind(FirstLightApplicationState.class).asEagerSingleton();
    	bind(FirstLightApplicationController.class);
    	bind(DashboardController.class);
    	
        //bind();
    }
    
    
    
}
