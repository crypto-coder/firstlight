/**
 * 
 */
package com.puremoneysystems.firstlight.ui.dashboard;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.ui.DefaultView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puremoneysystems.firstlight.ui.DefaultFXMLController;

/**
 * @author chris
 * @param <M>
 *
 */
public class DashboardController extends DefaultFXMLController<DashboardMetrics> {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

    @FXML 
    private Label otServerStatus;
    @FXML 
    private Label httpsPeerCount;
    @FXML 
    private Label i2pPeerCount;
    @FXML 
    private Label torPeerCount;
    
      
    

    /**
     * Blank Constructor.
     *      * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public DashboardController() throws CoreException {
        this(null);
    }
         
    /**
     * Default Constructor.
     * 
     * @param view the view to control
     * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public DashboardController(final DefaultView<DashboardMetrics, AnchorPane, DefaultFXMLController<DashboardMetrics>> view) throws CoreException {
        super(view);        
    }



    /**
     * {@inheritDoc}
     * @param <M>
     */
    @Override
    public void setModel(final DashboardMetrics model){
    	super.setModel(model);  	
    }

    
    @FXML
    @Override
    public DashboardMetrics getModel() {
    	return super.getModel();
    }

    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {    	
    	this.otServerStatus.setText(this.getModel().getOTServerConnectionState());
    	this.httpsPeerCount.setText(String.valueOf(this.getModel().getHTTPSPeerCount()));
    	this.i2pPeerCount.setText(String.valueOf(this.getModel().getI2PPeerCount()));
    	this.torPeerCount.setText(String.valueOf(this.getModel().getTORPeerCount()));  
	}
	
	
	

}
