package com.firstlight.wave;

import org.jrebirth.core.wave.WaveBean;

import com.firstlight.wallet.IWallet;

/**
 * The class <strong>WalletWaveBean</strong> used for configurations used by the Wallet Wave.  Wallet specific details 
 * including the IWallet and the WalletAction are stored in the WalletWaveBean.
 * 
 * @author MrMoneyChanger
 */
public class WalletWaveBean implements WaveBean {

    /** The wallet action to be taken. */
    private WalletAction action;

    /** The FirstLight Wallet specifying the name, storage location, and any wallet specific information */
    private IWallet wallet;

       
    
    public WalletWaveBean(){
    	super();
    }

    public WalletWaveBean(WalletAction action, IWallet wallet){
    	this();
    	this.setAction(action);
    	this.setWallet(wallet);
    }
    
    
    
    
    
    /**
     * Gets the action.
     * 
     * @return the action
     */
    public WalletAction getAction() {
        return this.action;
    }

    /**
     * Sets the action.
     * 
     * @param action the new action
     */
    public void setAction(final WalletAction action) {
        this.action = action;
    }
    
    
    
    /**
     * Gets the wallet.
     * 
     * @return the model class
     */
    public IWallet getWallet() {
        return this.wallet;
    }
    
    /**
     * Sets the wallet.
     * 
     * @param wallet the wallet instance
     */
    public void setWallet(final IWallet wallet) {
        this.wallet = wallet;
    }

        

}
