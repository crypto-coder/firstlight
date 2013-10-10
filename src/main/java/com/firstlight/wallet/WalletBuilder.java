/**
 * 
 */
package com.firstlight.wallet;

import java.lang.reflect.Type;
import java.lang.ClassCastException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.ui.wallet.ot.FreeNetOpenTransactionsWallet;
import com.firstlight.ui.wallet.ot.LocalOpenTransactionsWallet;


/**
 * @author MrMoneyChanger
 *
 */
public class WalletBuilder<W extends WalletBase> {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(WalletBuilder.class);

    private final Class<W> clazz;
    
    /** The field used to store the property mask (allow up to 64 properties). */
    private long bitMask;
    
	private String name = "";
	private String hashCode = "";
	private String location = "";
	
	/**
	 * 
	 */
	protected WalletBuilder(Class<W> clazz) {
        this.clazz = clazz;
    }

    /**
     * Create a WalletBuilder instance.
     * 
     * @return new instance of WalletBuilder
     */
	public static WalletBuilder<? extends WalletBase> create(WalletType walletType) {		
		switch(walletType){
			case localOpenTransactions:
				return new WalletBuilder<LocalOpenTransactionsWallet>(LocalOpenTransactionsWallet.class);
			case freeNetOpenTransactions:
				return new WalletBuilder<FreeNetOpenTransactionsWallet>(FreeNetOpenTransactionsWallet.class);
			case bitcoin:
			case ripple:
			default:
				return new WalletBuilder<WalletBase>(WalletBase.class);
		}
    }
    
    		
	public WalletBuilder<W> name(String newName){
		this.name = newName;
		addBit(0);
		return this;
	}

	public WalletBuilder<W> hashCode(String newHashCode){
		this.hashCode = newHashCode;
		addBit(1);
		return this;
	}

	public WalletBuilder<W> location(String newLocation){
		this.location = newLocation;
		addBit(2);
		return this;
	}
	

    public W build() {    	
        W wallet;
		try {
			wallet = this.clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
        this.applyTo(wallet);
        return wallet;
    }

	

    /**
     * Apply all wallet properties.
     * 
     * @param paramWallet the wallet that need to be initialized with builder values
     */
    protected void applyTo(final W paramWallet) {
        if (hasBit(0)) {
        	paramWallet.setName(this.name);
        }
        if (hasBit(1)) {
        	paramWallet.setHashCode(this.hashCode);
        }
        if (hasBit(2)) {
        	paramWallet.setLocation(this.location);
        }
    }
	
	
    /**
     * Add a bit to the mask.
     * 
     * @param bit the bit to add
     */
    protected void addBit(final int bit) {
        this.bitMask |= 1 << bit;
    }

    /**
     * Check if the mask contains the requested bit.
     * 
     * @param bit the requested bit
     * 
     * @return true if the mask contains the requested bit
     */
    protected boolean hasBit(final int bit) {
        return (this.bitMask & 1 << bit) != 0;
    }

    
    
       
    
	

}
