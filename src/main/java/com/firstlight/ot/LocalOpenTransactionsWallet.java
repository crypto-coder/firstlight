/**
 * 
 */
package com.firstlight.ot;

import com.firstlight.wallet.WalletBase;
import com.firstlight.ot.IOpenTransactionsWallet;

/**
 * @author chris
 *
 */
public class LocalOpenTransactionsWallet extends WalletBase implements IOpenTransactionsWallet {
	
	

    /**
     * {@inheritDoc}
     */
    @Override protected String getFXMLPath() {
        return "/fxml/WalletDetails-LocalOpenTransactionsWallet.fxml";
    }

	
	
	
}
