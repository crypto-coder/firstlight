package com.firstlight.wallet;

import java.util.HashMap;
import java.util.List;

import javafx.beans.property.ReadOnlyBooleanProperty;

import com.firstlight.service.IWalletService;

/**
 * @author MrMoneyChanger
 *
 */
public interface IWallet {
	
	
	Class<? extends IWalletService> getWalletServiceClass();
	

	String getName();
	void setName(String newName);

	String getHashCode();
	void setHashCode(String newHashCode);

	String getLocation();
	void setLocation(String newLocation);

	WalletState getWalletState();
	void setWalletState(WalletState state);
		
	WalletState openWallet();
	WalletState closeWallet();
	HashMap<String, IWallet> getListOfKnownWallets();

	List<IAssetAccount> getAssetAccounts();
	void setAssetAccounts(List<IAssetAccount> assetAccounts);

	
	Boolean getWalletStateChanged();
	ReadOnlyBooleanProperty walletStateChangedProperty();
	void indicateWalletStateChangeStarted();
	void indicateWalletStateChangeCompleted();
	
	
}
