/**
 * 
 */
package com.puremoneysystems.firstlight;

import org.jrebirth.core.ui.fxml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fxexperience.javafx.scene.control.InputField;

import javafx.beans.property.*;

/**
 * The class <strong>DashboardMetrics</strong>.
 * 
 */
public final class DashboardMetrics extends DefaultFXMLModel<DashboardMetrics> {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardMetrics.class);
    
    
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
    
    
    
    public DashboardMetrics(){
    	this.otServerConnectionState.set("UNKNOWN");
    	this.httpsPeerCount.set(0);
    	this.i2pPeerCount.set(0);
    	this.torPeerCount.set(0);   
    }
    
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initModel() {
    	String test = new String("In the bind method");
    	test = (test != "") ? test : test; 	
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInnerModels() {
        LOGGER.debug("Init Sample Model");
        // Put the code to initialize inner models here (if any)
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
    
    
    
    
    
    
    
}
