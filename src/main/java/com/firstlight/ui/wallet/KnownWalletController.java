package com.firstlight.ui.wallet;

import java.net.URL;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.Duration;

import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.ui.DefaultView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.command.RegionCommand;
import com.firstlight.ui.DefaultFXMLController;
import com.firstlight.ui.Region;
import com.firstlight.wallet.IWallet;
import com.firstlight.wallet.WalletBase;
import com.firstlight.wave.RegionAction;
import com.firstlight.wave.RegionWaveBean;

public class KnownWalletController extends DefaultFXMLController<KnownWalletModel>  {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(KnownWalletController.class);

    @FXML
    private AnchorPane knownWalletListRoot;   

    @FXML
    private AnchorPane knownWalletListContainer;    
    private ListView<IWallet> knownWalletList;  
    
    @FXML
    private AnchorPane knownWalletDetailContainer;
    private IWallet selectedKnownWallet;
    


    /**
     * Blank Constructor.
     *      * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public KnownWalletController() throws CoreException {
        this(null);
    }
         
    /**
     * Default Constructor.
     * 
     * @param view the view to control
     * 
     * @throws CoreException if an error occurred while creating event handlers
     */
    public KnownWalletController(final DefaultView<KnownWalletModel, Pane, DefaultFXMLController<KnownWalletModel>> view) throws CoreException {
        super(view);        
    }



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		  
		super.initialize(arg0, arg1);
		
		if(this.knownWalletListContainer != null){
			this.getModel().loadAllKnownWallets();
			
			this.knownWalletList = new ListView<IWallet>();
			this.knownWalletListContainer.getChildren().add(this.knownWalletList);
			
			AnchorPane.setBottomAnchor(this.knownWalletList, Double.valueOf(0));
			AnchorPane.setLeftAnchor(this.knownWalletList, Double.valueOf(0));
			AnchorPane.setRightAnchor(this.knownWalletList, Double.valueOf(0));
			AnchorPane.setTopAnchor(this.knownWalletList, Double.valueOf(0));			
			
			//Add the ListCell factory and use the KnownWalletListCell
			this.knownWalletList.setCellFactory(new Callback<ListView<IWallet>, ListCell<IWallet>>(){
				@Override public ListCell<IWallet> call(ListView<IWallet> arg0) {
					return new KnownWalletListCell();
				}			
			});
			
			//Add a selectedItemChangeListener and have it display the newly selected Known Wallet details
			this.knownWalletList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<IWallet>() {
				@Override
				public void changed(ObservableValue<? extends IWallet> ov, IWallet old_value, IWallet new_value) {
					selectedKnownWallet = new_value;
					showKnownWalletDetails(selectedKnownWallet);
				}
	        });
			
			//Register the known wallet details region
	        RegionWaveBean waveBean = new RegionWaveBean(RegionAction.register, "knownWalletDetailContainer");
	        waveBean.setRegion(new Region(false, this.knownWalletDetailContainer, this.createWalletShowAnimation(), this.createWalletHideAnimation()));
	        this.getModel().callCommand(RegionCommand.class, waveBean);      	
		}	
	}
	
	

	
    private Animation createWalletHideAnimation() {
    	try {
    		FadeTransition fade = new FadeTransition(Duration.millis(1000), this.knownWalletDetailContainer);
    		fade.setFromValue(1.0);
    		fade.setToValue(0.0);
    		fade.setCycleCount(1);
    		
    		ScaleTransition scale = new ScaleTransition(Duration.millis(1000), this.knownWalletDetailContainer);
    		scale.setFromX(1.0);
    		scale.setToX(0.1);
    		scale.setFromY(1.0);
    		scale.setToY(0.1);
    		scale.setCycleCount(1);
    		
    		ParallelTransition parallel = new ParallelTransition();
    		parallel.getChildren().addAll(fade, scale);
    		parallel.setCycleCount(1);
    		
    		return parallel;		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}    	
	}

	private Animation createWalletShowAnimation() {
    	try {
    		FadeTransition fade = new FadeTransition(Duration.millis(1000), this.knownWalletDetailContainer);
    		fade.setFromValue(0.0);
    		fade.setToValue(1.0);
    		fade.setCycleCount(1);
    		
    		ScaleTransition scale = new ScaleTransition(Duration.millis(1000), this.knownWalletDetailContainer);
    		scale.setFromX(0.1);
    		scale.setToX(1.0);
    		scale.setFromY(0.1);
    		scale.setToY(1.0);
    		scale.setCycleCount(1);
    		
    		ParallelTransition parallel = new ParallelTransition();
    		parallel.getChildren().addAll(fade, scale);
    		parallel.setCycleCount(1);
    		
    		return parallel;		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}    	
	}


	/**
     * {@inheritDoc}
     * @param <M>
     */
    @Override
    public void setModel(final KnownWalletModel model){
    	super.setModel(model);  	
    	
    	if(this.knownWalletList != null && !model.getKnownWalletLocations().isEmpty()){
    		Collection<IWallet> knownWalletLocations = model.getKnownWalletLocations().values();
    		ObservableList<IWallet> knownWalletsObservableList = FXCollections.observableArrayList(knownWalletLocations.toArray(new IWallet[]{}));
    		this.knownWalletList.setItems(knownWalletsObservableList);
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public KnownWalletModel getModel() {
    	return super.getModel();
    }

    
	
    @SuppressWarnings("rawtypes")
	private void showKnownWalletDetails(IWallet knownWallet){
    	@SuppressWarnings("unchecked")
		Class<? extends WalletBase> walletClass = (Class<? extends WalletBase>) knownWallet.getClass();
    	WalletBase walletInstance = (WalletBase) knownWallet;
    	
        //Show the provided knownWallet detail in a screen region called "knownWalletDetailContainer"
    	//We need to use a wave for this action, so construct a RegionWaveBean and call the RegionCommand with the bean
        RegionWaveBean waveBean = new RegionWaveBean(RegionAction.show, walletClass, "knownWalletDetailContainer");
        waveBean.setModelInstance(walletInstance);
        this.getModel().callCommand(RegionCommand.class, waveBean);      	
    }
    
	
}
