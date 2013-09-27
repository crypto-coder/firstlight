package com.firstlight.wallet;

import org.jrebirth.core.ui.fxml.DefaultFXMLModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class <strong>WalletModel</strong>.
 * 
 */
public class WalletModel extends DefaultFXMLModel<WalletModel>  implements IWallet {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(WalletModel.class);

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public WalletModel(){
    	
    }
    
    
    
    
    
    
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFXMLPath() {
        return "/fxml/ListKnownWallets.fxml";
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
        LOGGER.debug("Init Sample Model");
        // Put the code to initialize inner models here (if any)
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
