/**
 * 
 */
package com.firstlight.wallet;

import org.jrebirth.core.ui.fxml.DefaultFXMLModel;

/**
 * @author MrMoneyChanger
 *
 */
@SuppressWarnings("rawtypes")
public class WalletBase<W extends WalletBase> extends DefaultFXMLModel<W> implements IWallet {

    /** The wallet name. */
	private String name = "";

    /** The wallet hash code. */
	private String hashCode = "";

	//TODO: Refactor this using IWalletStorageLocation
    /** The wallet file location. */
	private String location = "";

    /** The wallet state. */
	private WalletState walletState = WalletState.closed;
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override public String getName() {
		return this.name;
	}
	@Override public void setName(String newName) {
		this.name = newName;
	}

	@Override public String getHashCode() {		
		return this.hashCode;
	}
	@Override public void setHashCode(String newHashCode) {
		this.hashCode = newHashCode;
	}

	@Override public String getLocation() {
		return this.location;
	}
	@Override public void setLocation(String newLocation) {
		this.location = newLocation;
	}
   
	@Override public WalletState getWalletState() {
		// TODO Auto-generated method stub
		return this.walletState;
	}
    
    	
}
