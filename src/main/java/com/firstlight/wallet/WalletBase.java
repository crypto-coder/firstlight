/**
 * 
 */
package com.firstlight.wallet;

import org.jrebirth.core.ui.fxml.DefaultFXMLModel;

/**
 * @author chris
 *
 */
public class WalletBase extends DefaultFXMLModel<WalletBase> implements IWallet {

	private String name = "";
	private String hashCode = "";
	private String location = "";
	
	/**
	 * 
	 */
	public WalletBase() {
		// TODO Auto-generated constructor stub
	}

	@Override public String getName() {
		return this.name;
	}
	@Override public void setName(String newName) {
		this.name = newName;
	}

	@Override public String getHashCode() {		
		return this.hashCode;
	}
	@Override public void setHashCode(String newHashCode) {
		this.hashCode = newHashCode;
	}

	@Override public String getLocation() {
		return this.location;
	}
	@Override public void setLocation(String newLocation) {
		this.location = newLocation;
	}
	
	
	

    /**
     * {@inheritDoc}
     */
    @Override protected String getFXMLPath() {
        return "/fxml/WalletDetails.fxml";
    }

	
		
	
}
