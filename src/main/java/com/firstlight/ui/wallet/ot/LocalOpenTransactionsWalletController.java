package com.firstlight.ui.wallet.ot;

import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.Duration;

import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.ui.DefaultView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.region.Region;
import com.firstlight.region.RegionAction;
import com.firstlight.region.RegionWaveBean;
import com.firstlight.region.command.RegionCommand;
import com.firstlight.ui.DefaultFXMLController;
import com.firstlight.ui.wallet.BasicWalletController;
import com.firstlight.wallet.IWallet;
import com.firstlight.wallet.WalletBase;

public class LocalOpenTransactionsWalletController extends DefaultFXMLController<LocalOpenTransactionsWallet>  {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalOpenTransactionsWalletController.class);

    @FXML private TabPane localOpenTransactionsWalletTabPane;   

    @FXML private AnchorPane generalWalletDetails;
    @FXML private BasicWalletController generalWalletDetailsController;
    @FXML private Label fileStorageLocation;  
    @FXML private Label fileSize;  
    @FXML private Label fileEncryption;   
    
    
    @FXML private LocalOpenTransactionsWallet wallet;


    /**
     * Blank Constructor.
     *      * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public LocalOpenTransactionsWalletController() throws CoreException {
        this(null);
    }
         
    /**
     * Default Constructor.
     * 
     * @param view the view to control
     * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public LocalOpenTransactionsWalletController(final DefaultView<LocalOpenTransactionsWallet, Pane, DefaultFXMLController<LocalOpenTransactionsWallet>> view) throws CoreException {
        super(view);        
    }



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {  

		//Add the openWalletButton handler
		
	}
	
	

	/**
     * {@inheritDoc}
     * @param <M>
     */
    @Override
    public void setModel(final LocalOpenTransactionsWallet model){
    	super.setModel(model);   
    	    	
    	this.wallet = model;
    	
    	if(this.generalWalletDetailsController != null){this.generalWalletDetailsController.setModel(model);}
    	
    	if(this.fileStorageLocation != null){this.fileStorageLocation.setText(model.getLocation());}
    	if(this.fileEncryption != null){this.fileEncryption.setText("UNKNOWN");}

    	//Attempt to load the file properties specified by the location
    	try{
    		Path walletPath = FileSystems.getDefault().getPath(model.getLocation());    		
    		if(this.fileSize != null){this.fileSize.setText(String.valueOf(Files.size(walletPath)));}
    	}catch(Exception e){
    		if(this.fileSize != null){this.fileSize.setText("Unable to access wallet");}
    	}    	
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public LocalOpenTransactionsWallet getModel() {
    	return super.getModel();
    }

        
	
}
