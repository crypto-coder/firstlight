/**
 * 
 */
package com.firstlight.ui.dashboard;

import org.jrebirth.core.command.CommandBean;
import org.jrebirth.core.ui.fxml.*;
import org.jrebirth.core.wave.WaveBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.command.RegionCommand;
import com.firstlight.service.IMenuService;
import com.firstlight.service.MenuService;
import com.firstlight.ui.wallet.KnownWalletModel;
import com.firstlight.wave.RegionAction;
import com.firstlight.wave.RegionWaveBean;
import com.fxexperience.javafx.scene.control.InputField;

import javafx.beans.property.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * The class <strong>DashboardModel</strong>.
 * 
 */
public final class DashboardModel extends DefaultFXMLModel<DashboardModel> {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardModel.class);
    private IMenuService menuService = null;
    
    
    /** Current OT Server connection state **/
    private StringProperty otServerConnectionState = new SimpleStringProperty(this, "otServerConnectionState");
    public final StringProperty OTServerConnectionStateProperty() { return otServerConnectionState; }
    public final String getOTServerConnectionState() { return otServerConnectionState.get(); }
    

    /** Current Number of HTTPS Peers **/
    private IntegerProperty httpsPeerCount = new SimpleIntegerProperty(this, "httpsPeerCount");
    public final IntegerProperty httpsPeerCountProperty() { return httpsPeerCount; }
    public final int getHTTPSPeerCount() { return httpsPeerCount.getValue(); }

    

    /** Current Number of I2P Peers **/
    private IntegerProperty i2pPeerCount = new SimpleIntegerProperty(this, "i2pPeerCount");
    public final IntegerProperty i2pPeerCountProperty() { return i2pPeerCount; }
    public final int getI2PPeerCount() { return i2pPeerCount.getValue(); }
    

    /** Current Number of TOR Peers **/
    private IntegerProperty torPeerCount = new SimpleIntegerProperty(this, "torPeerCount");
    public final IntegerProperty torPeerCountProperty() { return torPeerCount; }
    public final int getTORPeerCount() { return torPeerCount.getValue(); }
    
    
    
    public DashboardModel(){
    	this.otServerConnectionState.set("DEFAULT");
    	this.httpsPeerCount.set(-1);
    	this.i2pPeerCount.set(-1);
    	this.torPeerCount.set(-1);   
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFXMLPath() {
        return "/fxml/Dashboard.fxml";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void initModel() {
    	this.menuService = (IMenuService)this.getService(MenuService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInnerModels() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bind() {
        // Put the code to manage model object binding (if any)
    	String test = new String("In the bind method");
    	test = (test != "") ? test : test;
    }
    
    
    
    @SuppressWarnings("unchecked")
	@Override
    protected void showView() {
    	super.showView();
    	
    	this.menuService.registerMenuItem("WALLETS", "Show Wallets", "/images/Bank.png", (Class<? extends CommandBean<WaveBean>>)RegionCommand.class, 
    			new RegionWaveBean(RegionAction.show, KnownWalletModel.class, "activeScreenRegion"), WALLETS_CLICKED);
    }

    
    @Override
    protected void hideView() {
    	super.hideView();
    	
    	this.menuService.unregisterMenuItem("WALLETS");
    }
    
    
    
    
    
    

    
    public static final Callback<MouseEvent, Boolean> WALLETS_CLICKED = new Callback<MouseEvent, Boolean>() {
        /**
         * Wallets link was clicked
         * 
         * @param event the mouse event triggered
         * 
         * @return true for single click
         */
        @Override
        public Boolean call(final MouseEvent event) {
        	return true;
        }
    };
    
}
