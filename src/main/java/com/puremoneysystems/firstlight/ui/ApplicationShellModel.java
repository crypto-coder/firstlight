package com.puremoneysystems.firstlight.ui;





import org.jrebirth.core.ui.fxml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fxexperience.javafx.scene.control.InputField;

import javafx.beans.property.*;

/**
 * The class <strong>ApplicationStateModel</strong>.
 * 
 */
public final class ApplicationShellModel extends DefaultFXMLModel<ApplicationShellModel> {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationShellModel.class);
    
    
    public ApplicationShellModel(){
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFXMLPath() {
        return "/fxml/Application.fxml";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fxmlPreInitialize() {
        LOGGER.debug("Application Shell FXML Pre-initializing");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initModel() {
        LOGGER.debug("Init Model");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInnerModels() {
        // Put the code to initialize inner models here (if any)
        LOGGER.debug("Init Inner Models");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bind() {
        // Put the code to manage model object binding (if any)
        LOGGER.debug("Binding Model");
    }
    
    
    
    
    
    
    
}
