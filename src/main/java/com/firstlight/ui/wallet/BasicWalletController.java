package com.firstlight.ui.wallet;

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
import jfx.messagebox.MessageBox;

import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.ui.DefaultView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.FirstLightApplication;
import com.firstlight.region.Region;
import com.firstlight.region.RegionAction;
import com.firstlight.region.RegionWaveBean;
import com.firstlight.region.command.RegionCommand;
import com.firstlight.ui.DefaultFXMLController;
import com.firstlight.wallet.IWallet;
import com.firstlight.wallet.WalletBase;

@SuppressWarnings("rawtypes")
public class BasicWalletController extends DefaultFXMLController<WalletBase>  {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicWalletController.class);

    @FXML private Label walletName;  
    @FXML private Label lastModified;  
    @FXML private Label hashCode;  
    
    @FXML private Button openWalletButton;
    
    @FXML private WalletBase wallet;


    /**
     * Blank Constructor.
     *      * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public BasicWalletController() throws CoreException {
        this(null);
    }
         
    /**
     * Default Constructor.
     * 
     * @param view the view to control
     * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public BasicWalletController(final DefaultView<WalletBase, Pane, DefaultFXMLController<WalletBase>> view) throws CoreException {
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
    public void setModel(final WalletBase model){
    	super.setModel(model);   	
    	this.wallet = model;
    	if(this.walletName != null){this.walletName.setText(model.getName());}
    	if(this.hashCode != null){this.hashCode.setText(model.getHashCode());}

    	//Attempt to load the file properties specified by the location
    	try{
    		Path walletPath = FileSystems.getDefault().getPath(model.getLocation());    		
    		if(this.lastModified != null){this.lastModified.setText(Files.getLastModifiedTime(walletPath).toString());}
    	}catch(Exception e){
    		if(this.lastModified != null){this.lastModified.setText("Unable to access wallet");}
    	}    	
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public WalletBase getModel() {
    	return super.getModel();
    }

        

    @FXML
    public void openCurrentWallet() {   
    	MessageBox.show(FirstLightApplication.getInstance().getScene().getWindow(), "he clicked me", "Clicked It", MessageBox.ICON_INFORMATION);
    }
	
}
