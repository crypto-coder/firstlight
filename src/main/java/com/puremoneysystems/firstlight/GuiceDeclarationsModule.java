package com.puremoneysystems.firstlight;

import com.google.inject.AbstractModule;
import com.puremoneysystems.firstlight.ui.ApplicationShellController;
import com.puremoneysystems.firstlight.ui.dashboard.DashboardController;

import javafx.stage.Stage;


public class GuiceDeclarationsModule  extends AbstractModule {
	
    @Override
    protected void configure() {
    	//bind(FirstLightApplicationState.class).asEagerSingleton();
    	bind(ApplicationShellController.class);
    	bind(DashboardController.class);
    	
        //bind();
    }
    
    
    
}
