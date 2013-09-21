package com.puremoneysystems.firstlight;

import com.google.inject.Inject;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.SceneBuilder;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;
import javafx.stage.WindowBuilder;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.ui.adapter.DefaultMouseAdapter;
import org.jrebirth.core.wave.WaveBuilder;
import org.jrebirth.core.wave.WaveGroup;
import org.jrebirth.core.ui.DefaultView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jfx.messagebox.*;

import com.puremoneysystems.firstlight.FirstLightApplication;


public class FirstLightApplicationController extends DefaultFXMLController<FirstLightApplicationState> {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstLightApplicationController.class);
     

    /**
     * Blank Constructor.
     *      * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public FirstLightApplicationController() throws CoreException {
        this(null);
    }
         
    /**
     * Default Constructor.
     * 
     * @param view the view to control
     * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public FirstLightApplicationController(final DefaultView<FirstLightApplicationState, AnchorPane, DefaultFXMLController<FirstLightApplicationState>> view) throws CoreException {
        super(view);
        
              
    }
    

    /**
     * {@inheritDoc}
     * @param <M>
     */
    @Override
    public void setModel(final FirstLightApplicationState model){
    	super.setModel(model);  	
    }

    
    @FXML
    @Override
    public FirstLightApplicationState getModel() {
    	return super.getModel();
    }

    
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		
		String test = "test";
		
		if(test == "test"){
			return;
		}
		
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
