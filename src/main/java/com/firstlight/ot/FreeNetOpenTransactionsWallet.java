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
public class FreeNetOpenTransactionsWallet extends WalletBase implements IOpenTransactionsWallet {
	

    /**
     * {@inheritDoc}
     */
    @Override protected String getFXMLPath() {
        return "/fxml/WalletDetails-FreeNetOpenTransactionsWallet.fxml";
    }

	
	
}
