/**
 * 
 */
package com.firstlight.ui.wallet.ot;

import java.util.HashMap;
import java.util.List;

import com.firstlight.ot.IOpenTransactionsWallet;
import com.firstlight.service.IWalletService;
import com.firstlight.wallet.IWallet;
import com.firstlight.wallet.WalletBase;
import com.firstlight.wallet.WalletState;

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

	@Override
	public WalletState openWallet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WalletState closeWallet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<IWalletService> getWalletServiceClass() {
		return null;
	}

	@Override
	public HashMap<String, IWallet> getListOfKnownWallets() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
