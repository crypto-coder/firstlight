package com.firstlight.command;


import org.jrebirth.core.command.DefaultBeanCommand;
import org.jrebirth.core.service.Service;
import org.jrebirth.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.service.IWalletService;
import com.firstlight.wave.WalletWaveBean;

/**
 * The class <strong>WalletCommand</strong>.
 * 
 * @author MrMoneyChanger
 */
public class WalletCommand extends DefaultBeanCommand<WalletWaveBean> {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(WalletCommand.class);

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(final Wave wave) {

    	//TODO: Ugly, refactor without all the type casting
    	//Use the WalletService class to lookup the appropriate service and retrieve it
    	@SuppressWarnings("unchecked")
		final Class<Service> serviceClass = (Class<Service>)getWaveBean(wave).getWallet().getWalletServiceClass();
        final IWalletService rs = (IWalletService)getService(serviceClass);

        LOGGER.info("Trigger wallet action " + getWaveBean(wave).getAction());

        switch (getWaveBean(wave).getAction()) {
            case open:
            	rs.doOpenWallet(wave);
                break;
            case close:
            	rs.doCloseWallet(wave);
                break;
            case createNew:
            	break;
            case securePackage:
            	break;
            default:
                LOGGER.error("No interpreter for this WalletAction has been created in the WalletCommand class.");
        }

    }

    /**
     * Get the wave bean and cast it.
     * 
     * @param wave the wave that hold the bean
     * 
     * @return the casted wave bean
     */
    @Override
    public WalletWaveBean getWaveBean(final Wave wave) {
        return (WalletWaveBean) wave.getWaveBean();
    }

}
