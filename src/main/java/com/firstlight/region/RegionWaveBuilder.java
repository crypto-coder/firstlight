package com.firstlight.region;

import org.jrebirth.core.command.CommandWaveBuilder;
import org.jrebirth.core.ui.Model;
import org.jrebirth.core.wave.WaveBase;

import com.firstlight.region.command.RegionCommand;

/**
 * The class <strong>RegionWaveBuilder</strong>. is used to build a new Region Wave.
 * 
 * @author MrMoneyChanger
 */
public final class RegionWaveBuilder extends CommandWaveBuilder<RegionWaveBuilder, RegionWaveBean> {

    /** The unique key used to identify a region. */
    private String regionKey;

    /** The action to perform for this stage. */
    private RegionAction action;

    /** The root model class to show attached to the stage. */
    private Class<? extends Model> modelClass;

    /**
     * Private constructor.
     */
    private RegionWaveBuilder() {
        super(RegionCommand.class, RegionWaveBean.class);
    }

    /**
     * Static method to build a default builder.
     * 
     * @return a new fresh OpenStageWaveBuilder instance
     */
    public static RegionWaveBuilder create() {
        return new RegionWaveBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyTo(final WaveBase paramWave) {
        super.applyTo(paramWave);

        if (hasBit(0)) {
            getWaveBean(paramWave).setRegionKey(this.regionKey);
        }
        if (hasBit(1)) {
            getWaveBean(paramWave).setAction(this.action);
        }
        if (hasBit(2)) {
            getWaveBean(paramWave).setModelClass(this.modelClass);
        }
    }

    /**
     * Define key for the region.
     * 
     * @param regionKey the region key
     * 
     * @return the builder
     */
    public RegionWaveBuilder key(final String regionKey) {
        this.regionKey = regionKey;
        addBit(0);
        return this;
    }

    /**
     * Define action to process.
     * 
     * @param action the action to perform {@link RegionAction}
     * 
     * @return the builder
     */
    public RegionWaveBuilder action(final RegionAction action) {
        this.action = action;
        addBit(1);
        return this;
    }

    /**
     * Define the model class to load in a region.
     * 
     * @param modelClass the root model class
     * 
     * @return the builder
     */
    public RegionWaveBuilder modelClass(final Class<? extends Model> modelClass) {
        this.modelClass = modelClass;
        addBit(2);
        return this;
    }

    
    
}
