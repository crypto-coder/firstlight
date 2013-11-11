/**
 * 
 */
package com.firstlight.ui.wallet.ot;

import com.firstlight.wallet.IAssetAccount;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author MrMoneyChanger
 *
 */
public class OpenTransactionsAccount implements IAssetAccount {

	private final SimpleStringProperty accountName = new SimpleStringProperty("");
	private final SimpleStringProperty assetName = new SimpleStringProperty("");
	private final SimpleStringProperty nymName = new SimpleStringProperty("");
	private final SimpleStringProperty purseBalance = new SimpleStringProperty("");
	private final SimpleStringProperty accountBalance = new SimpleStringProperty("");
	private final SimpleStringProperty sentPending = new SimpleStringProperty("");
	private final SimpleStringProperty receivedPending = new SimpleStringProperty("");
	private final SimpleStringProperty checksPending = new SimpleStringProperty("");
	
	
	public OpenTransactionsAccount(){}
	
	public OpenTransactionsAccount(String accountName, String assetName, String nymName, String purseBalance, String accountBalance, String sentPending, String receivedPending, String checksPending){
		this();
		this.setAccountName(accountName);
		this.setAssetName(assetName);
		this.setNymName(nymName);
		this.setPurseBalance(purseBalance);
		this.setAccountBalance(accountBalance);
		this.setSentPending(sentPending);
		this.setReceivedPending(receivedPending);	
		this.setChecksPending(checksPending);
	}
	
	
	
	public String getAccountName(){
		return this.accountName.get();
	}
	public void setAccountName(String accountName){
		this.accountName.set(accountName);
	}

	public String getAssetName(){
		return this.assetName.get();
	}
	public void setAssetName(String assetName){
		this.assetName.set(assetName);
	}

	public String getNymName(){
		return this.nymName.get();
	}
	public void setNymName(String nymName){
		this.nymName.set(nymName);
	}

	public String getPurseBalance(){
		return this.purseBalance.get();
	}
	public void setPurseBalance(String purseBalance){
		this.purseBalance.set(purseBalance);
	}

	public String getAccountBalance(){
		return this.accountBalance.get();
	}
	public void setAccountBalance(String accountBalance){
		this.accountBalance.set(accountBalance);
	}
	
	public String getSentPending(){
		return this.sentPending.get();
	}
	public void setSentPending(String sentPending){
		this.sentPending.set(sentPending);
	}

	public String getReceivedPending(){
		return this.receivedPending.get();
	}
	public void setReceivedPending(String receivedPending){
		this.receivedPending.set(receivedPending);
	}

	public String getChecksPending(){
		return this.checksPending.get();
	}
	public void setChecksPending(String checksPending){
		this.checksPending.set(checksPending);
	}
	
	
}
