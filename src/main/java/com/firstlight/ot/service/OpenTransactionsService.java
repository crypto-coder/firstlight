/**
 * 
 */
package com.firstlight.ot.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.service.DefaultService;
import org.jrebirth.core.wave.Wave;
import org.opentransactions.otapi.NativeLoader;
import org.opentransactions.otapi.OTCallback;
import org.opentransactions.otapi.OTCaller;
import org.opentransactions.otapi.OTPassword;
import org.opentransactions.otjavalib.Load.IJavaPath;
import org.opentransactions.otjavalib.Load.IPasswordImage;
import org.opentransactions.otjavalib.Load.LoadingOpenTransactionsFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.service.IWalletService;
import com.firstlight.ui.wallet.ot.OpenTransactionsAccount;
import com.firstlight.wallet.IWallet;
import com.firstlight.wallet.WalletState;
import com.firstlight.wave.WalletWaveBean;

import eu.ApplicationProperties;
import eu.opentxs.bridge.Util;
import eu.opentxs.bridge.core.commands.Commands;
import eu.opentxs.bridge.core.commands.act.AccountCommands;
import eu.opentxs.bridge.core.commands.act.AssetCommands;
import eu.opentxs.bridge.core.commands.act.BusinessCommands;
import eu.opentxs.bridge.core.commands.act.ConfigCommands;
import eu.opentxs.bridge.core.commands.act.ContactCommands;
import eu.opentxs.bridge.core.commands.act.HackCommands;
import eu.opentxs.bridge.core.commands.act.MetaCommands;
import eu.opentxs.bridge.core.commands.act.NymCommands;
import eu.opentxs.bridge.core.commands.act.ServerCommands;
import eu.opentxs.bridge.core.commands.act.WalletCommands;
import eu.opentxs.bridge.core.dto.Account;
import eu.opentxs.bridge.core.dto.Transaction;
import eu.opentxs.bridge.core.exceptions.OTException;
import eu.opentxs.bridge.core.modules.Module;
import eu.opentxs.bridge.core.modules.OTAPI;
import eu.opentxs.bridge.core.modules.act.AccountModule;
import eu.opentxs.bridge.core.modules.act.AssetModule;

/**
 * The class <strong>OpenTransactionsService</strong>.
 * 
 * @author MrMoneyChanger
 */
public class OpenTransactionsService extends DefaultService implements IWalletService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenTransactionsService.class);
    
    
    //Startup and Shutdown hook for ensuring Open Transactions is kept to a single session and is shut down properly
    static {
		reset();
		
		Thread cleanupThread = new Thread() {
			@Override
			public void run() {
				OTAPI.appShutdown();
				System.out.println("SUCCESS: Open Transactions shutdown hook completed");
			}
		};
		Runtime.getRuntime().addShutdownHook(cleanupThread);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void ready() throws CoreException {
        super.ready(); // Can be omitted but it's a bad practice

        registerCallback(DO_OPEN_WALLET, RE_WALLET_OPENED);
        registerCallback(DO_CLOSE_WALLET, RE_WALLET_CLOSED);
    }
	
	
    
    
    public void doOpenWallet(final Wave wave){
    	IWallet requestedWallet = getWaveBean(wave).getWallet();
    	requestedWallet = this.openWallet(requestedWallet);
    }
    

    public void doCloseWallet(final Wave wave) {
    	IWallet requestedWallet = getWaveBean(wave).getWallet();
    	requestedWallet = this.closeWallet(requestedWallet);
    }
    
    
    
    @Override public IWallet openWallet(IWallet wallet) {
        LOGGER.trace("Opening a wallet.");
    	
		getLockFile().delete();
		try {
			if (!NativeLoader.getInstance().initNative())
				return null;

			if (NativeLoader.getInstance().init()) {
				IPasswordImage passwordImageMgmt = new PasswordImageMgmt();
				if (NativeLoader.getInstance().setupPasswordImage(passwordImageMgmt)) {
					OTCaller javaPasswordCaller = new OTCaller();
					OTCallback javaPasswordCallback = new OTCallback() {
						@Override
						public void runOne(String prompt, OTPassword output) {
							String password = ""; //getPasswordFromUser(prompt);
							output.setPassword(password, password.length());
						}

						@Override
						public void runTwo(String prompt, OTPassword output) {
							String password = ""; //getPasswordFromUser(prompt);
							output.setPassword(password, password.length());
						}
					};
					if (NativeLoader.getInstance().setupPasswordCallback(javaPasswordCaller, javaPasswordCallback)) {
						try {
							new FileInputStream(getLockFile());
						} catch (FileNotFoundException e) {
							e.getMessage();
						}

						Module.loadAndShowWallet(wallet.getName());
						
						wallet.setWalletState(WalletState.open);						
						LOGGER.trace("Wallet successfully opened.");
						
						//Retrieve the Accounts for the Wallet, repackage them, and attach them to the IWallet
						List<Account> accounts = Account.getList();
						List<OpenTransactionsAccount> otAccounts = new ArrayList<OpenTransactionsAccount>();
						for(Account account : accounts){
														
							AccountModule accountModule = AccountModule.getInstance(account.id);
							String assetID = AccountModule.getAccountAssetId(account.id);
							String assetName = AssetModule.getAssetName(assetID);
							String nymID = AccountModule.getAccountNymId(account.id);
							String nymName = AccountModule.getNymName(nymID);
							
							//Retrieve the Account Balance value
							Integer accountBalance = AccountModule.convertAmountToValue(OTAPI.GetAccount.balance(account.id));
							String accountBalanceFormatted = AccountModule.convertValueToFormat(assetID, accountBalance);							
							
							//Retrieve the Purse value
							String purse = OTAPI.loadPurse(account.serverId, nymID, assetID);
							Integer purseBalance = new Integer(0);
							String purseBalanceFormatted = "";
							if (Util.isValidString(purse)){
								purseBalance = AccountModule.convertAmountToValue(OTAPI.Purse.getBalance(account.serverId, assetID, purse));
								purseBalanceFormatted = AccountModule.convertValueToFormat(assetID, purseBalance);
							}

							//Retrieve the Pending Transactions value in the outbox							
							String outboxLedger = OTAPI.loadOutbox(account.serverId, nymID, account.id);
							Integer outboxLedgerValue = new Integer(0);
							String outboxLedgerValueFormatted = "";
							if (Util.isValidString(outboxLedger)){
								Integer transactionCount = OTAPI.Ledger.getCount(account.serverId,  nymID,  account.id, outboxLedger);
								
								for(int i = 0; i < transactionCount; i++){
									String currentTransaction = OTAPI.Ledger.getTransactionByIndex(account.serverId, nymID, account.id, outboxLedger, i);
									String currentTransactionAmount = OTAPI.Transaction.getAmount(account.serverId, nymID, account.id, currentTransaction);
									
									outboxLedgerValue += Module.convertAmountToValue(currentTransactionAmount);
								}
								
								outboxLedgerValueFormatted = Module.convertValueToFormat(assetID, outboxLedgerValue);
							}
							
							//Retrieve the Pending Transactions value in the inbox						
							String inboxLedger = OTAPI.loadInbox(account.serverId, nymID, account.id);
							Integer inboxLedgerValue = new Integer(0);
							String inboxLedgerValueFormatted = "";
							if (Util.isValidString(inboxLedger)){
								Integer transactionCount = OTAPI.Ledger.getCount(account.serverId,  nymID,  account.id, inboxLedger);
								
								for(int i = 0; i < transactionCount; i++){
									String currentTransaction = OTAPI.Ledger.getTransactionByIndex(account.serverId, nymID, account.id, inboxLedger, i);
									String currentTransactionAmount = OTAPI.Transaction.getAmount(account.serverId, nymID, account.id, currentTransaction);
									
									inboxLedgerValue += Module.convertAmountToValue(currentTransactionAmount);
								}
								
								inboxLedgerValueFormatted = Module.convertValueToFormat(assetID, inboxLedgerValue);
							}
							
							//Retrieve the Pending Checks drawn, but undeposited, against this account
							List<Transaction> pendingTransactions = accountModule.getTransactionsUnrealized();
							Integer pendingTransactionsValue = new Integer(0);
							String pendingTransactionsValueFormatted = "";					
							for(Transaction currentTransaction : pendingTransactions){
								
							}							
							pendingTransactionsValueFormatted = Module.convertValueToFormat(assetID, pendingTransactionsValue);
							
							//Create the new OpenTransactionsAccount and attach it to the accounts list
							OpenTransactionsAccount otAccount = new OpenTransactionsAccount(account.name, assetName, nymName, purseBalanceFormatted, accountBalanceFormatted, outboxLedgerValueFormatted, inboxLedgerValueFormatted, pendingTransactionsValueFormatted);
							wallet.getAssetAccounts().add(otAccount);
						}
					}
				}

			}
		} catch (LoadingOpenTransactionsFailure | OTException e) {
			LOGGER.error("Failed to load the requested wallet", e);
		}
		
		
		if (NativeLoader.getInstance().getInitialized())
			return wallet;
		
		return null;              		
	}


	@Override public IWallet closeWallet(IWallet wallet) {
        LOGGER.trace("Closing a wallet.");        
        
        //TODO Implement Open Transactions wallet close procedures
        wallet.setWalletState(WalletState.closed);
        LOGGER.trace("Need to implement Wallet Close procedures");
        
        return wallet;
	}
    

    
    /**
     * Get the wave bean and cast it.
     * 
     * @param wave the wave that hold the bean
     * 
     * @return the casted wave bean
     */
    private WalletWaveBean getWaveBean(final Wave wave) {
        return (WalletWaveBean) wave.getWaveBean();
    }
    
    private static File getLockFile() {
		return new File(String.format("%s/%s", ApplicationProperties.getUserDataPath(), "ot.pid"));
	}

	private static void reset() {
		Commands.reset();
		MetaCommands.init();
		ConfigCommands.init();
		WalletCommands.init();
		ServerCommands.init();
		AssetCommands.init();
		NymCommands.init();
		AccountCommands.init();
		ContactCommands.init();
		BusinessCommands.init();
		HackCommands.init();
	}


    static class DummyOTServerPathMgmt implements IJavaPath {
        public DummyOTServerPathMgmt() { }
        @Override public String GetJavaPathFromUser(String message) {return "/usr/local/lib";}
        @Override public Boolean GetIfUserCancelled() {return false;}
    }
    
    static class DummyPasswordImageMgmt implements IPasswordImage {
        @Override public String GetPasswordImageFromUser(String string) {return "/home/chris/workspace/firstlight/src/main/resources/images/Bank.png";}
        @Override public boolean SetPasswordImage(String string) { return true; }
        @Override public Boolean GetIfUserCancelled() { return false; }
    }

	private static class PasswordImageMgmt implements IPasswordImage {
		@Override
		public String GetPasswordImageFromUser(String value) {
			File file = new File(ApplicationProperties.getUserDataPath(), ApplicationProperties.get().getString("password.image"));
			if (file.exists())
				return file.getAbsolutePath();

			file = new File(ApplicationProperties.getApplBasePath(), ApplicationProperties.get().getString("password.image"));
			if (file.exists())
				return file.getAbsolutePath();
			return null;
		}

		@Override
		public boolean SetPasswordImage(String value) {
			return false;
		}

		@Override
		public Boolean GetIfUserCancelled() {
			return false;
		}
	}
//	
//	private static String getPasswordFromUser(String prompt) {
//		System.out.println(String.format("%s", prompt));
//		String password = null;
//		password = ConsoleApplication.readLineFromConsole();
//		return password;
//	}

	
    
}
