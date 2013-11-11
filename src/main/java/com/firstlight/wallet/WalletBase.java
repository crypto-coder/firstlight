/**
 * 
 */
package com.firstlight.wallet;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import org.jrebirth.core.ui.fxml.DefaultFXMLModel;

/**
 * @author MrMoneyChanger
 *
 */
@SuppressWarnings("rawtypes")
public abstract class WalletBase<W extends WalletBase> extends DefaultFXMLModel<W> implements IWallet {

    /** The wallet name. */
	private String name = "";

    /** The wallet hash code. */
	private String hashCode = "";

	//TODO: Refactor this using IWalletStorageLocation
    /** The wallet file location. */
	private String location = "";

    /** The wallet state. */
	private WalletState walletState = WalletState.closed;
	private BooleanProperty walletStateChanged = new SimpleBooleanProperty(false);
	
	/** The wallet asset accounts */
	private List<IAssetAccount> assetAccounts = new ArrayList<IAssetAccount>();
	
	
	
	
	
	
	
	
	
	
	
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
		return this.walletState;
	}

	@Override public void setWalletState(WalletState state) {
		this.walletState = state;
	}
	

	@Override public List<IAssetAccount> getAssetAccounts(){
		return this.assetAccounts;
	}
	@Override public void setAssetAccounts(List<IAssetAccount> assetAccounts){
		this.assetAccounts = assetAccounts;
	}
    	
	
	public Boolean getWalletStateChanged(){
		return this.walletStateChanged.get();
	}
	public ReadOnlyBooleanProperty walletStateChangedProperty(){
		return this.walletStateChanged;
	}
	public void indicateWalletStateChangeStarted(){
		this.walletStateChanged.set(true);
	}
	public void indicateWalletStateChangeCompleted(){
		this.walletStateChanged.set(false);
	}
	
}
