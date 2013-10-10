package com.firstlight.wallet;

/**
 * @author MrMoneyChanger
 *
 */
public interface IWalletStorageLocation {

	String getName();
	void setName(String newName);

	String getHashCode();
	void setHashCode(String newHashCode);

	String getLocation();
	void setLocation(String newLocation);

	WalletState getWalletState();
	
	
	
	
	WalletState openWallet();
	WalletState closeWallet();
		
}
