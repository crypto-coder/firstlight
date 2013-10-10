package com.firstlight.wallet;

/**
 * @author MrMoneyChanger
 *
 */
public interface IWallet {

	String getName();
	void setName(String newName);

	String getHashCode();
	void setHashCode(String newHashCode);

	String getLocation();
	void setLocation(String newLocation);

	WalletState getWalletState();
	
	
	
}
