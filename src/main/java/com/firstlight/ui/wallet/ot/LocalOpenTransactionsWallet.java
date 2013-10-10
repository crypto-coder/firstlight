/**
 * 
 */
package com.firstlight.ui.wallet.ot;

import com.firstlight.wallet.WalletBase;

/**
 * @author MrMoneyChanger
 *
 */
public class LocalOpenTransactionsWallet extends WalletBase<LocalOpenTransactionsWallet> implements IOpenTransactionsWallet {
	
	

    /**
     * {@inheritDoc}
     */
    @Override protected String getFXMLPath() {
        return "/fxml/WalletDetails-LocalOpenTransactionsWallet.fxml";
    }

	
	
	
}
