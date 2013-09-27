package com.firstlight.ui;


import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.ui.DefaultView;
import org.jrebirth.core.ui.Model;
import org.jrebirth.core.ui.View;
import org.jrebirth.core.ui.Controller;
//import org.jrebirth.core.ui.fxml.FXMLController;
import org.jrebirth.core.ui.fxml.AbstractFXMLController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class <strong>DefaultFXMLController</strong>.
 * 
 * @author 
 * 
 * @param <M> The model responsible of the view
 * @param <V> The view hosting the FXML component
 */
public class DefaultFXMLController<M extends Model> extends AbstractFXMLController<M, View<M, Pane, DefaultFXMLController<M>>> implements Controller<M, View<M, Pane, DefaultFXMLController<M>>> {

//implements FXMLController<M, View<M, AnchorPane, DefaultFXMLController<M>>>, Controller<M, View<M, AnchorPane, DefaultFXMLController<M>>> {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFXMLController.class);

    private View<M, Pane, DefaultFXMLController<M>> innerView = null;

    
    /**
     * Main Constructor
     * 
     * @param view the view to control
     * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public DefaultFXMLController() throws CoreException {
    	this(null);
    }
    /**
     * Constructor with ability to provide a view
     * 
     * @param view the view to control
     * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public DefaultFXMLController(final View<M, Pane, DefaultFXMLController<M>> view) throws CoreException {
    	this.innerView = view;
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL url, final ResourceBundle resource) {
        LOGGER.trace("Initialize fxml node : " + url.toString());
    }
    

	@Override
	public void activate() throws CoreException {
		// TODO Auto-generated method stub
		return;
	}

	
	public void dispose() throws CoreException {
		return;
	}
	

	@Override
	public Node getRootNode() {
		// TODO Auto-generated method stub
		return null;
	}
    

}

