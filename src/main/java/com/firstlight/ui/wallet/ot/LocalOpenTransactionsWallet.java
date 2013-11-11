/**
 * 
 */
package com.firstlight.ui.wallet.ot;

import java.io.File;
import java.util.HashMap;

import com.firstlight.ot.IOpenTransactionsWallet;
import com.firstlight.ot.service.OpenTransactionsService;
import com.firstlight.service.IWalletService;
import com.firstlight.wallet.IWallet;
import com.firstlight.wallet.WalletBase;
import com.firstlight.wallet.WalletState;

import eu.ApplicationProperties;
import eu.opentxs.bridge.core.commands.Commands.Extension;

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

	@SuppressWarnings("unchecked")
	@Override public WalletState openWallet() {
    	//Retrieve the OpenTransactionsService and tell it to open this wallet
        final IWalletService walletService = (IWalletService)this.getService(OpenTransactionsService.class);
        IWallet updatedWallet = walletService.openWallet(this);
	
        if(updatedWallet.getWalletState() == WalletState.open){
        	updatedWallet.indicateWalletStateChangeStarted();
        }
        
        return updatedWallet.getWalletState();
	}

	@Override public WalletState closeWallet() {
    	//Retrieve the OpenTransactionsService and tell it to close this wallet
        final IWalletService walletService = (IWalletService)this.getService(OpenTransactionsService.class);
        IWallet updatedWallet = walletService.closeWallet(this);
	
        return updatedWallet.getWalletState();
	}

	@Override
	public Class<? extends IWalletService> getWalletServiceClass() {		
		return OpenTransactionsService.class;
	}

	@Override
	public HashMap<String, IWallet> getListOfKnownWallets() {
		File dir = new File(ApplicationProperties.getUserDataPath());
		File[] files = dir.listFiles();
		HashMap<String, IWallet> knownWallets = new HashMap<String, IWallet>();
		for (File file : files) {
			if (file.isFile()) {
				String name = file.getName();
				if (Extension.DEFINITION.getValue().equals(getFileExtension(name))){
					LocalOpenTransactionsWallet newWallet = new LocalOpenTransactionsWallet();
					newWallet.setName(name.substring(0, name.lastIndexOf('.')));
					newWallet.setLocation(file.getAbsolutePath());
										
					knownWallets.put(newWallet.getName(), newWallet);   					
				}
			}
		}
		return knownWallets;
	}

	public static String getFileExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		if (i != -1)
			return fileName.substring(i + 1).toLowerCase();
		return null;
	}

	
}
