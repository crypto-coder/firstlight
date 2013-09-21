package com.puremoneysystems.firstlight;

import org.jrebirth.core.ui.fxml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fxexperience.javafx.scene.control.InputField;

import javafx.beans.property.*;

/**
 * The class <strong>ApplicationStateModel</strong>.
 * 
 */
public final class FirstLightApplicationState extends DefaultFXMLModel<FirstLightApplicationState> {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstLightApplicationState.class);
    
    
    public FirstLightApplicationState(){
    }
    
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initModel() {
    	String test = new String("In the bind method");
    	test = (test != "") ? test : test; 	
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInnerModels() {
        // Put the code to initialize inner models here (if any)
        LOGGER.debug("Init Sample Model");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bind() {
        // Put the code to manage model object binding (if any)
    	String test = new String("In the bind method");
    	test = (test != "") ? test : test;
    }
    
    
    
    
    
    
    
}
