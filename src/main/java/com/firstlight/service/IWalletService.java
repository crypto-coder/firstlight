/**
 * 
 */
package com.firstlight.service;

import org.jrebirth.core.wave.Wave;
import org.jrebirth.core.wave.WaveTypeBase;

import com.firstlight.wallet.IWallet;

/**
 * @author MrMoneyChanger
 *
 */
public interface IWalletService {

	
	public static final WaveTypeBase DO_OPEN_WALLET = WaveTypeBase.build("OPEN_WALLET");
    public static final WaveTypeBase RE_WALLET_OPENED = WaveTypeBase.build("WALLET_OPENED");
    
    public static final WaveTypeBase DO_CLOSE_WALLET = WaveTypeBase.build("CLOSE_WALLET");
    public static final WaveTypeBase RE_WALLET_CLOSED = WaveTypeBase.build("WALLET_CLOSED");

	
        
    void doOpenWallet(final Wave wave);
    void doCloseWallet(final Wave wave);
	
    
    IWallet openWallet(IWallet wallet);
    IWallet closeWallet(IWallet wallet);
    
	
}
