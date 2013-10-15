/**
 * 
 */
package com.firstlight.service;

import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.service.DefaultService;
import org.jrebirth.core.wave.Wave;
import org.jrebirth.core.wave.WaveTypeBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.service.IWalletService;
import com.firstlight.wave.RegionWaveBean;
import com.firstlight.wave.WalletWaveBean;

/**
 * The class <strong>WalletService</strong>.
 * 
 * @author MrMoneyChanger
 */
public abstract class WalletService extends DefaultService implements IWalletService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletService.class);
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void ready() throws CoreException {
        super.ready(); // Can be omitted but it's a bad practice

        registerCallback(DO_OPEN_WALLET, RE_WALLET_OPENED);
        registerCallback(DO_CLOSE_WALLET, RE_WALLET_CLOSED);
    }
	
	
        

    
    /**
     * Get the wave bean and cast it.
     * 
     * @param wave the wave that hold the bean
     * 
     * @return the casted wave bean
     */
    public WalletWaveBean getWaveBean(final Wave wave) {
        return (WalletWaveBean) wave.getWaveBean();
    }
    
    

    
}
