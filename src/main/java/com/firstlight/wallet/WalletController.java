package com.firstlight.wallet;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.layout.Pane;

import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.ui.DefaultView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.ui.DefaultFXMLController;
import com.firstlight.wallet.WalletModel;

public class WalletController extends DefaultFXMLController<WalletModel>  {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(WalletController.class);

    


    /**
     * Blank Constructor.
     *      * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public WalletController() throws CoreException {
        this(null);
    }
         
    /**
     * Default Constructor.
     * 
     * @param view the view to control
     * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public WalletController(final DefaultView<WalletModel, Pane, DefaultFXMLController<WalletModel>> view) throws CoreException {
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
    public void setModel(final WalletModel model){
    	super.setModel(model);  	
    	
    }

    
    @Override
    public WalletModel getModel() {
    	return super.getModel();
    }

    
    
	
	
	
}
