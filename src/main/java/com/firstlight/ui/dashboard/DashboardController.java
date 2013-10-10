/**
 * 
 */
package com.firstlight.ui.dashboard;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.ui.DefaultView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.ui.DefaultFXMLController;

/**
 * @author MrMoneyChanger
 * @param <M>
 *
 */
public class DashboardController extends DefaultFXMLController<DashboardModel> {

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
    public DashboardController(final DefaultView<DashboardModel, Pane, DefaultFXMLController<DashboardModel>> view) throws CoreException {
        super(view);        
    }



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {    	
	}
	
	
    /**
     * {@inheritDoc}
     * @param <M>
     */
    @Override
    public void setModel(final DashboardModel model){
    	super.setModel(model);  	
    	
    	if(this.otServerStatus != null){
    		this.otServerStatus.setText(this.getModel().getOTServerConnectionState());
    	}
    	if(this.otServerStatus != null){
    		this.httpsPeerCount.setText(String.valueOf(this.getModel().getHTTPSPeerCount()));
    	}
    	if(this.otServerStatus != null){
    		this.i2pPeerCount.setText(String.valueOf(this.getModel().getI2PPeerCount()));
    	}
    	if(this.otServerStatus != null){
    		this.torPeerCount.setText(String.valueOf(this.getModel().getTORPeerCount()));  
    	}
    }

    
    @Override
    public DashboardModel getModel() {
    	return super.getModel();
    }

    
    
	
	
	

}
