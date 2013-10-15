package com.firstlight.ui.wallet;

import java.util.HashMap;

import static org.jrebirth.core.resource.Resources.create;

import org.jrebirth.core.resource.ResourceBuilders;
import org.jrebirth.core.ui.fxml.DefaultFXMLModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.FirstLightParameters;
import com.firstlight.ui.wallet.ot.LocalOpenTransactionsWallet;
import com.firstlight.wallet.IWallet;
import com.firstlight.wallet.WalletBase;
import com.firstlight.wallet.WalletType;
import com.firstlight.wallet.WalletBuilder;

/**
 * The class <strong>KnownWalletModel</strong>.
 * 
 */
public class KnownWalletModel extends DefaultFXMLModel<KnownWalletModel> {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(KnownWalletModel.class);

    private String DefaultWalletStorageLocation = (String) ResourceBuilders.PARAMETER_BUILDER.get(FirstLightParameters.DefaultWalletStorageLocation);
    private HashMap<String, IWallet> knownWalletLocations = new HashMap<String, IWallet>();
    
        
    
    public KnownWalletModel(){
    	
    }
    
    
    
    
    

    /**
     * Used to load all known wallet types from sources
     */
    protected void loadAllKnownWallets() {

    	//Discover all wallets before the FXML (and controller) get loaded so that they are available when the ListView<IWallet> gets created
    	HashMap<String, IWallet> localOTWallets = (new LocalOpenTransactionsWallet()).getListOfKnownWallets();
    	this.setKnownWalletLocations(localOTWallets);
//    		try{
//    			String currentWalletName = (String) ResourceBuilders.PARAMETER_BUILDER.get(create("knownWallet" + i.toString() + "Name", ""));
//    			String currentWalletLocation = (String) ResourceBuilders.PARAMETER_BUILDER.get(create("knownWallet" + i.toString() + "Location", ""));
//    			String currentWalletHash = (String) ResourceBuilders.PARAMETER_BUILDER.get(create("knownWallet" + i.toString() + "Hash", "256de91fe4709f598d26c036e82e91fa"));
//    			String currentWalletTypeString = (String) ResourceBuilders.PARAMETER_BUILDER.get(create("knownWallet" + i.toString() + "Type", "localOpenTransactions"));
//    			WalletType currentWalletType;
//    			IWallet wallet;
//    			
//    			if(currentWalletName == null || currentWalletName.length() == 0){
//    				LOGGER.trace("Failed to create a wallet from configuration values because the Wallet Name was not supplied.");
//    				continue;
//    			}
//    			
//    			if(currentWalletLocation == null || currentWalletLocation.length() == 0){
//    				LOGGER.trace("Failed to create a wallet from configuration values because the Wallet Location was not supplied : " + currentWalletName);
//    				continue;
//    			}
//    			
//    			if(currentWalletTypeString == null || currentWalletTypeString.length() == 0){
//    				LOGGER.trace("Failed to create a wallet from configuration values because the Wallet Type was not supplied : " + currentWalletName);
//    				continue;
//    			}
//    			
//    			try{
//    				currentWalletType = WalletType.valueOf(currentWalletTypeString);
//    			}catch(Exception walletTypeConversionException){
//    				LOGGER.error("Failed to convert a wallet type string to its enum value :" + currentWalletTypeString + " : " + currentWalletName, walletTypeConversionException);
//    				continue;
//    			}
//    			
//    			//Create the WalletBuilder and add basic configuration values
//    			WalletBuilder<? extends WalletBase> walletBuilder = WalletBuilder.create(currentWalletType)
//    																			.name(currentWalletName)
//																				.location(currentWalletLocation)
//																				.hashCode(currentWalletHash);
//    			
//    			//TODO: Refactor this so that the wallet specific configurations can be supplied in a generic way and the specific wallet will know how to use it
//    			//Add any wallet type specific configurations
//    			switch(currentWalletType){
//    				case localOpenTransactions:
//    					break;
//    				case freeNetOpenTransactions:
//    					break;
//    				case bitcoin:
//    					break;
//    				case ripple:
//    					break;    			
//    			}
//
//    			//Build the wallet and attach it to the HashMap
//				wallet = walletBuilder.build();
//				
//				//Ensure the key is unique
//				if(this.knownWalletLocations.containsKey(currentWalletName)){
//					LOGGER.error("Could not add a Wallet becuase it is already added to the list : " + currentWalletName);
//					continue;					
//				}
//				
//				this.knownWalletLocations.put(currentWalletName, wallet);    			
//    		}catch(Exception e){
//				LOGGER.error("Error while attempting to load a Known Wallet : ", e);   
//				continue;
//    		}    	
//    	}    	    
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
    	super.initModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bind() {}







	
	
	/**
	 * @return the defaultWalletStorageLocation
	 */
	public String getDefaultWalletStorageLocation() {
		return DefaultWalletStorageLocation;
	}

	/**
	 * @param defaultWalletStorageLocation the defaultWalletStorageLocation to set
	 */
	public void setDefaultWalletStorageLocation(String defaultWalletStorageLocation) {
		DefaultWalletStorageLocation = defaultWalletStorageLocation;
	}



	/**
	 * @return the knownWalletLocations
	 */
	public HashMap<String, IWallet> getKnownWalletLocations() {
		return knownWalletLocations;
	}

	/**
	 * @param knownWalletLocations the knownWalletLocations to set
	 */
	public void setKnownWalletLocations(HashMap<String, IWallet> knownWalletLocations) {
		this.knownWalletLocations = knownWalletLocations;
	}
    
    
    
	
	
	
}
