/**
 * 
 */
package com.firstlight.wallet;

/**
 * @author MrMoneyChanger
 *
 */
public interface IAssetAccount {

	String getAccountName();
	void setAccountName(String accountName);

	String getAssetName();
	void setAssetName(String assetName);

	String getNymName();
	void setNymName(String nymName);

	String getPurseBalance();
	void setPurseBalance(String purseBalance);

	String getAccountBalance();
	void setAccountBalance(String accountBalance);

	String getSentPending();
	void setSentPending(String sentPending);

	String getReceivedPending();
	void setReceivedPending(String receivedPending);

	String getChecksPending();
	void setChecksPending(String checksPending);
	
	
}
