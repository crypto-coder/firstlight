package com.firstlight.wave;

import org.jrebirth.core.command.CommandWaveBuilder;
import org.jrebirth.core.ui.Model;
import org.jrebirth.core.wave.WaveBase;

import com.firstlight.command.RegionCommand;
import com.firstlight.wallet.IWallet;

/**
 * The class <strong>WalletWaveBuilder</strong>. is used to build a new Wallet Wave.
 * 
 * @author MrMoneyChanger
 */
public final class WalletWaveBuilder extends CommandWaveBuilder<WalletWaveBuilder, WalletWaveBean> {

    /** The action to perform for this wallet. */
    private WalletAction action;

    /** The wallet to perform an action with. */
    private IWallet wallet;

    
    
    
    /**
     * Private constructor.
     */
    private WalletWaveBuilder() {
        super(RegionCommand.class, WalletWaveBean.class);
    }

    /**
     * Static method to build a default builder.
     * 
     * @return a new fresh WalletWaveBuilder instance
     */
    public static WalletWaveBuilder create() {
        return new WalletWaveBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyTo(final WaveBase paramWave) {
        super.applyTo(paramWave);

        if (hasBit(0)) {
            getWaveBean(paramWave).setAction(this.action);
        }
        if (hasBit(1)) {
            getWaveBean(paramWave).setWallet(this.wallet);
        }
    }

    /**
     * Define action to process.
     * 
     * @param action the action to perform {@link WalletAction}
     * 
     * @return the builder
     */
    public WalletWaveBuilder action(final WalletAction action) {
        this.action = action;
        addBit(0);
        return this;
    }

    /**
     * Define the wallet to be affected by the requested action
     * 
     * @param wallet the wallet to be affected by the requested action
     * 
     * @return the builder
     */
    public WalletWaveBuilder wallet(final IWallet wallet) {
        this.wallet = wallet;
        addBit(1);
        return this;
    }

    
        
}
