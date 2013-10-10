package com.firstlight.region.command;

import org.jrebirth.core.command.DefaultUIBeanCommand;
import org.jrebirth.core.wave.Wave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.region.RegionWaveBean;
import com.firstlight.region.service.RegionService;

/**
 * The class <strong>RegionCommand</strong>.
 * 
 * @author MrMoneyChanger
 */
public class RegionCommand extends DefaultUIBeanCommand<RegionWaveBean> {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RegionCommand.class);

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(final Wave wave) {

        final RegionService rs = getService(RegionService.class);

        LOGGER.info("Trigger stage action " + getWaveBean(wave).getAction());

        switch (getWaveBean(wave).getAction()) {
            case show:
            	rs.doShowRegion(wave);
                break;
            case hide:
            	rs.doHideRegion(wave);
                break;
            case register:
            	rs.doRegisterRegion(wave);
            	break;
            case unregister:
            	rs.doUnregisterRegion(wave);
            	break;
            default:
                LOGGER.error("Undefined RegionAction");
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
    public RegionWaveBean getWaveBean(final Wave wave) {
        return (RegionWaveBean) wave.getWaveBean();
    }

}
