/**
 * 
 */
package com.firstlight.ui.wallet.ot;

import com.firstlight.wallet.WalletBase;

/**
 * @author MrMoneyChanger
 *
 */
public class FreeNetOpenTransactionsWallet extends WalletBase<FreeNetOpenTransactionsWallet> implements IOpenTransactionsWallet {
	

    /**
     * {@inheritDoc}
     */
    @Override protected String getFXMLPath() {
        return "/fxml/WalletDetails-FreeNetOpenTransactionsWallet.fxml";
    }

	
	
}
