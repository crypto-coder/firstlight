package eu.opentxs.bridge.core.modules.act;

import java.util.ArrayList;
import java.util.List;

import eu.opentxs.bridge.Text;
import eu.opentxs.bridge.CustomUTC;
import eu.opentxs.bridge.Util;
import eu.opentxs.bridge.core.dto.Transaction;
import eu.opentxs.bridge.core.dto.Transaction.InstrumentType;
import eu.opentxs.bridge.core.dto.Transaction.TransactionType;
import eu.opentxs.bridge.core.exceptions.OTException;
import eu.opentxs.bridge.core.exceptions.OTSystemException;
import eu.opentxs.bridge.core.exceptions.OTSystemException.Event;
import eu.opentxs.bridge.core.modules.OTAPI;

public class AccountModule extends NymModule {

	protected String accountId;

	private AccountModule(String serverId, String nymId, String accountId) throws OTException {
		super(serverId, nymId);
		this.accountId = parseAccountId(accountId);
	}
	
	public static AccountModule getInstance(String accountId) throws OTException {
		String serverId = getAccountServerId(accountId);
		String nymId = getAccountNymId(accountId);
		return new AccountModule(serverId, nymId, accountId);
	}

	public static String accountAlreadyExists(String serverId, String nymId, String assetId) {
		List<String> accountIds = getAccountIds();
		for (String accountId : accountIds) {
			AccountType accountType = AccountType.parse(getAccountType(accountId));
			if (accountType.equals(AccountType.ISSUER))
				continue;
			if (getAccountServerId(accountId).equals(serverId) 
					&& getAccountNymId(accountId).equals(nymId)
					&& getAccountAssetId(accountId).equals(assetId))
				return accountId;
		}
		return null;
	}

	public static void renameAccount(String accountId, String accountName) throws OTException {
		attempt("Renaming account");
		accountId = parseAccountId(accountId);
		String accountNymId = getAccountNymId(accountId);
		if (!Util.isValidString(accountName)) {
			accountName = getAccountStandardName(accountId);
			print(accountName);
		}
		if (!OTAPI.SetAccount.name(accountNymId, accountId, accountName))
			error("failed to rename");
		success("Account is renamed");
	}

	public static void deleteAccount(String accountId) throws OTException {
		attempt("Deleting account");
		accountId = parseAccountId(accountId);
		String serverId = getAccountServerId(accountId);
		String nymId = getAccountNymId(accountId);
		new AccountModule(serverId, nymId, accountId).deleteItself();
		success("Account is deleted");
	}

	private void deleteItself() throws OTException {
		refresh();
		sendRequest(new RequestGenerator() {
			@Override
			public int getRequest() {
				return OTAPI.deleteAccountFromServer(serverId, nymId, accountId);
			}
		});
	}

	public void showInbox() throws OTException {
		String ledger = OTAPI.loadInbox(serverId, nymId, accountId);
		if (!Util.isValidString(ledger)) {
			log("Inbox is empty");
			return;
		}
		int size = OTAPI.Ledger.getCount(serverId, nymId, accountId, ledger);
		if (size <= 0) {
			if (size < 0)
				warn("Inbox size is abnormal", size);
			else
				log("Inbox size is zero");
			return;
		}
		List<Transaction> list = new ArrayList<Transaction>();
		for (int index = 0; index < size; index++) {
			Transaction transaction = Transaction.getTransactionForAccount(
					serverId, nymId, accountId, ledger, index);
			if (transaction != null)
				list.add(transaction);
		}
		Transaction.showV(list);
	}

	public void showOutbox() throws OTException {
		String ledger = OTAPI.loadOutbox(serverId, nymId, accountId);
		if (!Util.isValidString(ledger)) {
			log("Outbox is empty");
			return;
		}
		int size = OTAPI.Ledger.getCount(serverId, nymId, accountId, ledger);
		if (size <= 0) {
			if (size < 0)
				warn("Outbox size is abnormal", size);
			else
				log("Outbox size is zero");
			return;
		}
		List<Transaction> list = new ArrayList<Transaction>();
		for (int index = 0; index < size; index++) {
			Transaction transaction = Transaction.getTransactionForAccount(
					serverId, nymId, accountId, ledger, index);
			if (transaction != null)
				list.add(transaction);
		}
		Transaction.showV(list);
	}

	public void showTransactions() throws OTException {
		print(Util.repeat("-", 70));
		{
			print(String.format("%12s:", "UNREALIZED"));
			print(Util.repeat("-", 13));
			List<Transaction> list = getTransactionsUnrealized();
			if (list.size() == 0)
				print("There are no unrealized transactions");
			else
				Transaction.showH(list);
		}
		print(Util.repeat("-", 70));
		{
			print(String.format("%12s:", "REALIZED"));
			print(Util.repeat("-", 13));
			List<Transaction> list = getTransactionsRealized();
			if (list.size() == 0)
				print("There are no realized transactions");
			else
				Transaction.showH(list);
		}
		print(Util.repeat("-", 70));
	}

	public List<Transaction> getTransactionsUnrealized() throws OTException {
		List<Transaction> list = new ArrayList<Transaction>();
		{
			String ledger = OTAPI.loadPayInbox(serverId, nymId);
			if (Util.isValidString(ledger)) {
				int size = OTAPI.Ledger.getCount(serverId, nymId, nymId, ledger);
				if (size > 0) {
					for (int index = 0; index < size; index++) {
						Transaction transaction = Transaction.getTransactionForNym(
								serverId, nymId, getAccountAssetId(accountId), ledger, index);
						if (transaction != null)
							list.add(transaction);
					}
				} else if (size < 0) {
					warn("Recordbox for nym size is abnormal", size);
				}
			}
		}
		{
			int size = OTAPI.GetNym.outpaymentsCount(nymId);
			if (size > 0) {
				for (int index = 0; index < size; index++) {
					Transaction transaction = Transaction.getTransactionForOutpayments(
							serverId, nymId, getAccountAssetId(accountId), index,
							new InstrumentType[]{InstrumentType.CHEQUE, InstrumentType.INVOICE});
					if (transaction != null)
						list.add(transaction);
				}
			} else if (size < 0) {
				warn("Outpayments size is abnormal", size);
			}
		}
		return list;
	}

	public List<Transaction> getTransactionsRealized() throws OTException {
		List<Transaction> list = new ArrayList<Transaction>();
		{
			String ledger = OTAPI.loadRecordbox(serverId, nymId, nymId);
			if (Util.isValidString(ledger)) {
				int size = OTAPI.Ledger.getCount(serverId, nymId, nymId, ledger);
				if (size > 0) {
					for (int index = 0; index < size; index++) {
						Transaction transaction = Transaction.getTransactionForNym(
								serverId, nymId, getAccountAssetId(accountId), ledger, index);
						if (transaction != null)
							list.add(transaction);
					}
				} else if (size < 0) {
					warn("Recordbox for nym size is abnormal", size);
				}
			}
		}
		{
			String ledger = OTAPI.loadRecordbox(serverId, nymId, accountId);
			if (Util.isValidString(ledger)) {
				int size = OTAPI.Ledger.getCount(serverId, nymId, accountId, ledger);
				if (size > 0) {
					for (int index = 0; index < size; index++) {
						Transaction transaction = Transaction.getTransactionForAccount(
								serverId, nymId, accountId, ledger, index);
						if (transaction != null)
							list.add(transaction);
					}
				} else if (size < 0) {
					warn("Recordbox for account size is abnormal", size);
				}
			}
		}
		{
			String ledger = OTAPI.loadOutbox(serverId, nymId, accountId);
			if (Util.isValidString(ledger)) {
				int size = OTAPI.Ledger.getCount(serverId, nymId, accountId, ledger);
				if (size > 0) {
					for (int index = 0; index < size; index++) {
						Transaction transaction = Transaction.getTransactionForOutbox(
								serverId, nymId, accountId, ledger, index);
						if (transaction != null)
							list.add(transaction);
					}
				} else if (size < 0) {
					warn("Outbox size is abnormal", size);
				}
			}
		}
		{
			int size = OTAPI.GetNym.outpaymentsCount(nymId);
			if (size > 0) {
				for (int index = 0; index < size; index++) {
					Transaction transaction = Transaction.getTransactionForOutpayments(
							serverId, nymId, getAccountAssetId(accountId), index,
							new InstrumentType[]{InstrumentType.CASH, InstrumentType.VOUCHER});
					if (transaction != null)
						list.add(transaction);
				}
			} else if (size < 0) {
				warn("Outpayments size is abnormal", size);
			}
		}
		return list;
	}
	
	public void deleteTransactions() throws OTException {
		int i = 0;
		{
			String ledger = OTAPI.loadRecordbox(serverId, nymId, nymId);
			if (Util.isValidString(ledger)) {
				int size = OTAPI.Ledger.getCount(serverId, nymId, nymId, ledger);
				if (size > 0) {
					for (int index = size - 1; index >= 0; index--) {
						Transaction transaction = Transaction.getTransactionForNym(
								serverId, nymId, getAccountAssetId(accountId), ledger, index);
						if (transaction != null) {
							if (!OTAPI.clearRecord(serverId, nymId, nymId, index))
								error("Failed to clear record");
							i++;
						}
					}
				} else if (size < 0) {
					warn("Recordbox for nym size is abnormal", size);
				}
			}
		}
		{
			String ledger = OTAPI.loadRecordbox(serverId, nymId, accountId);
			if (Util.isValidString(ledger)) {
				int size = OTAPI.Ledger.getCount(serverId, nymId, accountId, ledger);
				if (size > 0) {
					for (int index = size - 1; index >= 0; index--) {
						Transaction transaction = Transaction.getTransactionForAccount(
								serverId, nymId, accountId, ledger, index);
						if (transaction != null) {
							if (!OTAPI.clearRecord(serverId, nymId, nymId, index))
								error("Failed to clear record");
							i++;
						}
					}
				} else if (size < 0) {
					warn("Recordbox for account size is abnormal", size);
				}
			}
		}
		{
			int size = OTAPI.GetNym.outpaymentsCount(nymId);
			if (size > 0) {
				for (int index = size - 1; index >= 0; index--) {
					Transaction transaction = Transaction.getTransactionForOutpayments(
							serverId, nymId, getAccountAssetId(accountId), index,
							new InstrumentType[]{InstrumentType.CASH, InstrumentType.VOUCHER});
					if (transaction != null) {
						if (!OTAPI.Nym.removeOutpaymentsByIndex(nymId, index))
							error("failed to remove outpayment");
						i++;
					}
				}
			} else if (size < 0) {
				warn("Outpayments size is abnormal", size);
			}
		}
		info(String.format("%s: %d", "Number of deleted transactions", i));
		success("Transactions are deleted");
	}

	public void refresh() throws OTException {
		downloadFiles();
		List<Transaction> inboxTransactions = processInbox();
		List<Transaction> incomeTransactions = processIncome();
		verifyLastReceipt();
		
		List<Transaction> transactions = new ArrayList<Transaction>();
		if (inboxTransactions != null)
			transactions.addAll(inboxTransactions);
		if (incomeTransactions != null)
			transactions.addAll(incomeTransactions);
			
		if (transactions.size() > 0) {
			int transfers = 0;
			int cash = 0;
			int vouchers = 0;
			int cheques = 0;
			int invoices = 0;
			for (Transaction transaction : transactions) {
				InstrumentType instrumentType = transaction.getInstrumentType();
				TransactionType trxnType = transaction.getTrxnType();
				if (instrumentType.equals(InstrumentType.TRANSFER) && trxnType.equals(TransactionType.PENDING))
					transfers++;
				else if (instrumentType.equals(InstrumentType.CASH) && trxnType.equals(TransactionType.INSTRUMENT_NOTICE))
					cash++;
				else if (instrumentType.equals(InstrumentType.VOUCHER) && trxnType.equals(TransactionType.INSTRUMENT_NOTICE))
					vouchers++;
				else if (instrumentType.equals(InstrumentType.CHEQUE) && trxnType.equals(TransactionType.INSTRUMENT_NOTICE))
					cheques++;
				else if (instrumentType.equals(InstrumentType.INVOICE) && trxnType.equals(TransactionType.INSTRUMENT_NOTICE))
					invoices++;
			}
			if (transfers + cash + vouchers + cheques + invoices > 0) {
				print(Util.repeat("-", 13));
				print(String.format("%12s: %2d", "Transfers", transfers));
				print(String.format("%12s: %2d", "Cash", cash));
				print(String.format("%12s: %2d", "Vouchers", vouchers));
				print(String.format("%12s: %2d", "Cheques", cheques));
				print(String.format("%12s: %2d", "Invoices", invoices));
				print(Util.repeat("-", 13));
			}
		}
	}

	public void downloadFiles() throws OTException {
		attempt("Downloading files");
		getIntermediaryFiles(true);
		success("Files are downloaded");
	}

	public List<Transaction> processInbox() throws OTException {
		attempt("Processing inbox");
		getIntermediaryFiles(false);

		String ledger = OTAPI.loadInbox(serverId, nymId, accountId);
		int size = OTAPI.Ledger.getCount(serverId, nymId, accountId, ledger);
		if (size == 0) {
			skip("Nothing to process, inbox is empty");
			return null;
		}

		getTransactionNumbers();

		String responseLedger = OTAPI.Ledger.createResponse(serverId, nymId, accountId, ledger);
		if (!Util.isValidString(responseLedger))
			error("response ledger is not valid");

		List<Transaction> transactions = new ArrayList<Transaction>();
		for (int index = 0; index < size; index++) {
			String transaction = OTAPI.Ledger.getTransactionByIndex(serverId, nymId, accountId, ledger, index);
			if (!Util.isValidString(transaction)) {
				warn("skipping empty transaction");
				continue;
			}
			String temp = new String(responseLedger);
			responseLedger = OTAPI.Transaction.createResponse(serverId, nymId, accountId, temp, transaction, true);
			transactions.add(Transaction.getTransactionForAccount(serverId, nymId, accountId, ledger, index));
		}
		if (transactions.size() == 0)
			error("inbox has only empty transactions");

		if (!Util.isValidString(responseLedger))
			error("response ledger is not valid");
		final String finalResponseLedger = OTAPI.Ledger.finalizeResponse(serverId, nymId, accountId, responseLedger);
		if (!Util.isValidString(finalResponseLedger))
			error("final response ledger is not valid");

		sendTransaction(new RequestGenerator() {
			@Override
			public int getRequest() {
				return OTAPI.processInbox(serverId, nymId, accountId, finalResponseLedger);
			}
		});
		success("Inbox is processed");
		return transactions;
	}

	public List<Transaction> processIncome() throws OTException {
		attempt("Processing income");
		List<Transaction> transactions = getAndProcessNymbox(true);
		
		String ledger = OTAPI.loadPayInbox(serverId, nymId);
		if (!Util.isValidString(ledger)) {
			log("PayInbox ledger is empty");
			return null;
		}
		int size = OTAPI.Ledger.getCount(serverId, nymId, nymId, ledger);
		if (size <= 0) {
			if (size < 0)
				warn("PayInbox size is abnormal", size);
			else
				log("PayInbox size is zero");
			return null;
		}
		for (int index = size - 1; index >= 0; index--) {/** going backwards to enable removing items */
			String instrument = OTAPI.Ledger.getInstrumentbyIndex(serverId, nymId, nymId, ledger, index);
			if (!Util.isValidString(instrument))
				error("instrument is empty");
			String assetId = OTAPI.Instrument.getAssetId(instrument);
			if (!getAccountAssetId(accountId).equals(assetId))
				continue;
			InstrumentType instrumentType = getInstrumentType(instrument);
			if (instrumentType.equals(InstrumentType.VOUCHER)) {
				try {
					executeVoucher(instrument);
				} catch (OTSystemException e) {
					if (!e.getEvent().equals(Event.BALANCE_AGREEMENT_ERROR))
						error(e);
					info("Voucher failed to execute and it will be removed");
					if (!OTAPI.recordPayment(serverId, nymId, true, index, false))
						error("failed to record payment");
				}
			} else if (instrumentType.equals(InstrumentType.CASH)) {
				importCashToAccount(instrument);
				if (!OTAPI.recordPayment(serverId, nymId, true, index, true))
					error("failed to record payment");
			} else if (instrumentType.equals(InstrumentType.CHEQUE)) {
				
			} else if (instrumentType.equals(InstrumentType.INVOICE)) {
				
			}
		}
		success("Income is processed");
		return transactions;
	}

	public void verifyLastReceipt() throws OTException {
		if (!OTAPI.exists("receipts", serverId, String.format("%s.%s", nymId, "success"))
				&& !OTAPI.exists("receipts", serverId, String.format("%s.%s", accountId, "success"))) {
			info("There is no receipt for this account");
			return;
		}
		if (!OTAPI.verifyAccountReceipt(serverId, nymId, accountId)) {
			String ledger = OTAPI.loadNymbox(serverId, nymId);
			if (!Util.isValidString(ledger)) {
				warn("Nymbox ledger is empty");
				return;
			}
			int size = OTAPI.Ledger.getCount(serverId, nymId, accountId, ledger);
			if (size <= 0) {
				if (size < 0)
					warn("Nymbox size is abnormal", size);
				else
					log("Nymbox size is zero");
				return;
			}
			error("Failed to verify against the last signed receipt");
		}
		success("Verified against the last signed receipt");
	}

	public static void checkAvailableFunds(String accountId, Double requiredVolume) throws OTException {
		AccountType accountType = AccountType.parse(getAccountType(accountId));
		if (accountType.equals(AccountType.ISSUER))
			return;
		String assetId = getAccountAssetId(accountId);
		Integer balanceValue = convertAmountToValue(getAccountBalance(accountId));
		Integer requiredValue = convertVolumeToValue(assetId, requiredVolume);
		if (requiredValue > balanceValue)
			error(String.format("Not enough money in the account. You have only %s left.",
					convertValueToFormat(assetId, balanceValue)));
		info("Enough money is available");
	}

	public void showTransfer(Double volume, String hisAccountId, String note) {
		print(Util.repeat("-", 13));
		print(String.format("%12s: %s (%s)", "Server", serverId, getServerName(serverId)));
		print(String.format("%12s: %s (%s)", "From", accountId, getAccountName(accountId)));
		//print(String.format("%12s: %s (%s)", "To", hisAccountId, getContactAccountName(hisAccountId)));
		print(String.format("%12s: %s", "Volume", convertVolumeToFormat(volume)));
		print(String.format("%12s: %s", "Note", note));
		print(Util.repeat("-", 13));
	}

	public void transfer(Double volume, final String hisAccountId, final String note) throws OTException {
		attempt("Moving account to account");
		if (!Util.isValidString(hisAccountId))
			error("hisAccountId is empty");
		getIntermediaryFiles(true);// /needed only to unblock a newly created account
		final String amount = convertVolumeToAmount(volume);
		info(String.format("Amount being trasferred: %s (%d)", 
				convertAmountToFormat(amount), convertAmountToValue(amount)));
		sendTransaction(new RequestGenerator() {
			@Override
			public int getRequest() {
				return OTAPI.notarizeTransfer(serverId, nymId, accountId, amount, hisAccountId, note);
			}
		});
		success("Move account to account is made");
	}
	
	public void moveAccountToPurse(Double volume) throws OTException {
		attempt("Moving account to purse");
		final String assetId = getAccountAssetId(accountId);
		if (!OTAPI.Mint.isStillGood(serverId, assetId)) {
			sendRequest(new RequestGenerator() {
				@Override
				public int getRequest() {
					return OTAPI.getMint(serverId, nymId, assetId);
				}
			});
		}
		String mintContract = OTAPI.loadMint(serverId, assetId);
		if (!Util.isValidString(mintContract))
			error("mint contract failed to load");
		final String amount = convertVolumeToAmount(volume);
		sendTransaction(new RequestGenerator() {
			@Override
			public int getRequest() {
				return OTAPI.notarizeWithdrawal(serverId, nymId, accountId, amount);
			}
		});
		String purse = OTAPI.loadPurse(serverId, nymId, assetId);
		if (!Util.isValidString(purse))
			error("purse is empty");
		success("Move account to purse is made");
	}

	public void movePurseToAccount(String purse, List<Integer> indices) throws OTException {
		attempt("Moving purse to account");
		String assetId = getAccountAssetId(accountId);
		AssetModule assetModule = new AssetModule(serverId, nymId, assetId);
		final String newPurse = assetModule.processPurse(purse, indices, nymId, false);
		sendTransaction(new RequestGenerator() {
			@Override
			public int getRequest() {
				return OTAPI.notarizeDeposit(serverId, nymId, accountId, newPurse);
			}
		});
		success("Move purse to account is made");
	}

	public void verifyCash(String cash) throws OTException {
		String hisNymId = OTAPI.Instrument.getRecipientNymId(cash);
		if (Util.isValidString(hisNymId) && !nymId.equals(hisNymId))
			error("Your nym and the recipient nym do not match");
		if (!getAccountAssetId(accountId).equals(OTAPI.Instrument.getAssetId(cash)))
			error("Your account's asset type is incompatible with this cash");
	}

	public void importCashToAccount(final String cash) throws OTException {
		attempt("Importing cash to account");
		sendTransaction(new RequestGenerator() {
			@Override
			public int getRequest() {
				return OTAPI.notarizeDeposit(serverId, nymId, accountId, cash);
			}
		});
		success("Cash is successfully imported to account");
	}

	public String exportAccountToCash(Double volume, String hisNymId) throws OTException {
		attempt("Exporting account to cash");
		String assetId = getAccountAssetId(accountId);
		AssetModule assetModule = new AssetModule(serverId, nymId, assetId);

		/** first if there is any existing purse deposit it back to account */
		String oldPurse = OTAPI.loadPurse(serverId, nymId, assetId);
		Double oldPurseVolume = null;
		if (Util.isValidString(oldPurse)) {
			Integer value = getPurseBalanceValue(serverId, assetId, oldPurse);
			oldPurseVolume = convertValueToVolume(assetId, value);
			if (oldPurseVolume > 0)
				movePurseToAccount(oldPurse, null);
			else
				oldPurseVolume = null;
		}

		/** next withdraw from account to purse and export it to cash */
		moveAccountToPurse(volume);
		String newPurse = assetModule.exportPurseToCash(null, hisNymId);

		/** finally if there existed any purse reinstate it */
		if (oldPurseVolume != null)
			moveAccountToPurse(oldPurseVolume);

		success("Account is successfully exported to cash");
		return newPurse;
	}

	public String writeVoucher(Double volume, final String hisNymId, final String note) throws OTException {
		attempt("Writing voucher");
		if (!Util.isValidString(hisNymId))
			error("hisNymId is empty");
		final String amount = convertVolumeToAmount(volume);
		String message = sendTransaction(new RequestGenerator() {
			@Override
			public int getRequest() {
				return OTAPI.writeVoucher(serverId, nymId, accountId, amount, hisNymId, note);
			}
		});
		String ledger = OTAPI.Message.getLedger(message);
		if (!Util.isValidString(ledger))
			error("legder is empty");
		String transaction = OTAPI.Ledger.getTransactionByIndex(serverId, nymId, accountId, ledger, 0);
		if (!Util.isValidString(transaction))
			error("transaction is empty");
		String voucher = OTAPI.Transaction.getVoucher(serverId, nymId, accountId, transaction);
		if (!Util.isValidString(voucher))
			error("voucher is empty");
		publish(voucher);
		success("Voucher is written");
		return voucher;
	}

	public void sendVoucher(String voucher, String hisNymId) throws OTException {
		attempt("Sending voucher");
		if (!Util.isValidString(voucher))
			error("voucher is empty");
		if (!Util.isValidString(hisNymId))
			error("hisNymId is empty");
		sendPayment(voucher, hisNymId);
		success("Voucher is sent");
	}
	
	public void cancelVoucher(String voucher) throws OTException {
		attempt("Cancelling voucher");
		if (!Util.isValidString(voucher))
			error("voucher is empty");
		if (!serverId.equals(OTAPI.Instrument.getServerId(voucher)))
			error("This voucher is not present on this server");
		if (!getAccountAssetId(accountId).equals(OTAPI.Instrument.getAssetId(voucher)))
			error("Your account's asset type is incompatible with this voucher");
		{
			String myNymId = OTAPI.Instrument.getRemitterNymId(voucher);
			String myAccountId = OTAPI.Instrument.getRemitterAccountId(voucher);
			if (!nymId.equals(myNymId) || !accountId.equals(myAccountId))
				error("Attempt to cancel a voucher when not being its owner");
			//executeVoucher(voucher);
		}
		removeVoucher(voucher);
		success("Voucher is cancelled");
	}
	
	public void verifyVoucher(String voucher) throws OTException {
		attempt("Verifying voucher");
		if (!Util.isValidString(voucher))
			error("voucher is empty");
		if (!serverId.equals(OTAPI.Instrument.getServerId(voucher)))
			error("This voucher is not present on this server");
		String hisNymId = OTAPI.Instrument.getRecipientNymId(voucher);
		if (Util.isValidString(hisNymId) && !nymId.equals(hisNymId))
			error("Your nym and the recipient nym do not match");
		if (!getAccountAssetId(accountId).equals(OTAPI.Instrument.getAssetId(voucher)))
			error("Your account's asset type is incompatible with this voucher");
		String amount = OTAPI.Instrument.getAmount(voucher);
		info(String.format("Voucher amount: %s", convertAmountToFormat(amount)));
		success("Voucher is verified");
	}

	public void executeVoucher(final String voucher) throws OTException {
		attempt("Executing voucher");
		if (!Util.isValidString(voucher))
			error("voucher is empty");
		getIntermediaryFiles(true);// /needed only to unblock a newly created account
		sendTransaction(new RequestGenerator() {
			@Override
			public int getRequest() {
				return OTAPI.executeCheque(serverId, nymId, accountId, voucher);
			}
		});
		success("Voucher is executed");
	}

	public String writeCheque(Double volume, String hisNymId, String note, CustomUTC expiry) throws OTException {
		attempt("Writing cheque");
		String amount = convertVolumeToAmount(volume);
		if (!Util.isValidString(hisNymId))
			hisNymId = "";
		if (!Util.isValidString(note))
			note = "";
		String validFrom = "";
		String validTo = (expiry != null ? new Long(expiry.getSeconds()).toString() : "");
		String cheque = OTAPI.writeCheque(serverId, nymId, accountId, amount, hisNymId, note, validFrom, validTo);
		if (!Util.isValidString(cheque))
			error("cheque is empty");
		publish(cheque);
		success("Cheque is written");
		return cheque;
	}

	public void sendCheque(String cheque, String hisNymId) throws OTException {
		attempt("Sending cheque");
		if (!Util.isValidString(cheque))
			error("cheque is empty");
		if (!Util.isValidString(hisNymId))
			error("hisNymId is empty");
		sendPayment(cheque, hisNymId);
		success("Cheque is sent");
	}

	public void cancelCheque(String cheque) throws OTException {
		attempt("Cancelling cheque");
		if (!Util.isValidString(cheque))
			error("cheque is empty");
		if (!serverId.equals(OTAPI.Instrument.getServerId(cheque)))
			error("This cheque is not present on this server");
		if (!getAccountAssetId(accountId).equals(OTAPI.Instrument.getAssetId(cheque)))
			error("Your account's asset type is incompatible with this cheque");
		CustomUTC now = getTime();
		CustomUTC validFrom = CustomUTC.getDateUTC(OTAPI.Instrument.getValidFrom(cheque));
		if (validFrom != null && validFrom.isAfter(now))
			error("This cheque cannot be cancelled because is not valid yet");
		CustomUTC validTo = CustomUTC.getDateUTC(OTAPI.Instrument.getValidTo(cheque));
		if (validTo != null && validTo.isBefore(now)) {
			info("Unable to cancel this cheque becasue it is already expired. So only removing it from outpayments.");
		} else {
			// /does not work this way, what does work is self-execution of the instrument
			//if (!OTAPI.cancelCheque(serverId, nymId, accountId, cheque))
			//	error("failed to cancel cheque");
			String myNymId = OTAPI.Instrument.getSenderNymId(cheque);
			String myAccountId = OTAPI.Instrument.getSenderAccountId(cheque);
			if (!nymId.equals(myNymId) || !accountId.equals(myAccountId))
				error("Attempt to cancel a cheque when not being its owner");
			executeCheque(cheque);
		}
		removeOutpayment(OTAPI.Instrument.getTransactionNum(cheque));
		success("Cheque is cancelled");
	}

	public void verifyCheque(String cheque) throws OTException {
		attempt("Verifying cheque");
		if (!Util.isValidString(cheque))
			error("cheque is empty");
		if (!serverId.equals(OTAPI.Instrument.getServerId(cheque)))
			error("This cheque is not present on this server");
		String hisNymId = OTAPI.Instrument.getRecipientNymId(cheque);
		if (Util.isValidString(hisNymId) && !nymId.equals(hisNymId))
			error("Your nym and the recipient nym do not match");
		if (!getAccountAssetId(accountId).equals(OTAPI.Instrument.getAssetId(cheque)))
			error("Your account's asset type is incompatible with this cheque");
		CustomUTC now = getTime();
		CustomUTC validFrom = CustomUTC.getDateUTC(OTAPI.Instrument.getValidFrom(cheque));
		if (validFrom != null && validFrom.isAfter(now))
			error("This cheque is not valid yet");
		CustomUTC validTo = CustomUTC.getDateUTC(OTAPI.Instrument.getValidTo(cheque));
		if (validTo != null && validTo.isBefore(now))
			error("This cheque is expired");
		String amount = OTAPI.Instrument.getAmount(cheque);
		info(String.format("Cheque amount: %s", convertAmountToFormat(amount)));
		success("Cheque is verified");
	}

	public void executeCheque(final String cheque) throws OTException {
		attempt("Executing cheque");
		if (!Util.isValidString(cheque))
			error("cheque is empty");
		getIntermediaryFiles(true);// /needed only to unblock a newly created account
		sendTransaction(new RequestGenerator() {
			@Override
			public int getRequest() {
				return OTAPI.executeCheque(serverId, nymId, accountId, cheque);
			}
		});
		success("Cheque is executed");
	}

	public String writeInvoice(Double volume, String hisNymId, String note) throws OTException {
		attempt("Writing invoice");
		String amount = convertVolumeToAmount(volume);
		volume = new Double(0 - volume);
		String validFrom = "";
		String validTo = "";
		String invoice = OTAPI.writeCheque(serverId, nymId, accountId, amount, hisNymId, note, validFrom, validTo);
		if (!Util.isValidString(invoice))
			error("invoice is empty");
		publish(invoice);
		success("Invoice is written");
		return invoice;
	}

	public void sendInvoice(String invoice, String hisNymId) throws OTException {
		attempt("Sending invoice");
		if (!Util.isValidString(invoice))
			error("invoice is empty");
		if (!Util.isValidString(hisNymId))
			error("hisNymId is empty");
		sendPayment(invoice, hisNymId);
		success("Invoice is sent");
	}

	public void cancelInvoice(String invoice) throws OTException {
		attempt("Cancelling invoice");
		if (!Util.isValidString(invoice))
			error("invoice is empty");
		if (!serverId.equals(OTAPI.Instrument.getServerId(invoice)))
			error("This invoice is not present on this server");
		if (!getAccountAssetId(accountId).equals(OTAPI.Instrument.getAssetId(invoice)))
			error("Your account's asset type is incompatible with this invoice");
		CustomUTC now = getTime();
		CustomUTC validFrom = CustomUTC.getDateUTC(OTAPI.Instrument.getValidFrom(invoice));
		if (validFrom != null && validFrom.isAfter(now))
			error("This invoice cannot be cancelled because is not valid yet");
		CustomUTC validTo = CustomUTC.getDateUTC(OTAPI.Instrument.getValidTo(invoice));
		if (validTo != null && validTo.isBefore(now)) {
			info("Unable to cancel this invoice becasue it is already expired. So only removing it from outpayments.");
		} else {
			// /does not work this way, what does work is self-execution of the instrument
			//if (!OTAPI.cancelCheque(serverId, nymId, accountId, invoice))
			//	error("failed to cancel invoice");
			String myNymId = OTAPI.Instrument.getSenderNymId(invoice);
			String myAccountId = OTAPI.Instrument.getSenderAccountId(invoice);
			if (!nymId.equals(myNymId) || !accountId.equals(myAccountId))
				error("Attempt to cancel an invoice when not being its owner");
			executeInvoice(invoice);
		}
		removeOutpayment(OTAPI.Instrument.getTransactionNum(invoice));
		success("Invoice is cancelled");
	}

	public void verifyInvoice(String invoice) throws OTException {
		attempt("Verifying invoice");
		if (!Util.isValidString(invoice))
			error("invoice is empty");
		if (!serverId.equals(OTAPI.Instrument.getServerId(invoice)))
			error("This invoice is not present on this server");
		String hisNymId = OTAPI.Instrument.getRecipientNymId(invoice);
		if (Util.isValidString(hisNymId) && !nymId.equals(hisNymId))
			error("Your nym and the recipient nym do not match");
		if (!getAccountAssetId(accountId).equals(OTAPI.Instrument.getAssetId(invoice)))
			error("Your account's asset type is incompatible with this invoice");
		CustomUTC now = getTime();
		CustomUTC validFrom = CustomUTC.getDateUTC(OTAPI.Instrument.getValidFrom(invoice));
		if (validFrom != null && validFrom.isAfter(now))
			error("This invoice is not valid yet");
		CustomUTC validTo = CustomUTC.getDateUTC(OTAPI.Instrument.getValidTo(invoice));
		if (validTo != null && validTo.isBefore(now))
			error("This invoice is expired");
		String amount = OTAPI.Instrument.getAmount(invoice);
		info(String.format("Invoice amount: %s", convertAmountToFormat(amount)));
		success("Invoice is verified");
	}

	public void executeInvoice(final String invoice) throws OTException {
		attempt("Execute invoice");
		if (!Util.isValidString(invoice))
			error("invoice is empty");
		getIntermediaryFiles(true);// /needed only to unblock a newly created account
		sendTransaction(new RequestGenerator() {
			@Override
			public int getRequest() {
				return OTAPI.executeCheque(serverId, nymId, accountId, invoice);
			}
		});
		success("Invoice is executed");
	}

	public void hack1() throws OTException {
		synchronizeRequestNumber();
		getAndProcessNymbox(true);
		getTransactionNumbers();
	}

	public void hack2() throws OTException {
		error(Text.FEATURE_UNSUPPORTED_YET);
	}

	/**********************************************************************
	 * internal
	 *********************************************************************/

	private String sendTransaction(RequestGenerator generator) throws OTException {
		return sendTransaction(generator, true);
	}

	private String sendTransaction(RequestGenerator generator, boolean canRetry) throws OTException {
		getIntermediaryFiles(false);
		getTransactionNumbers();
		OTAPI.flushMessageBuffer();
		int requestId = generator.getRequest();
		String message = processRequest(requestId);
		{
			attempt("Verifying balance agreement");
			int result = OTAPI.Message.getBalanceAgreementSuccess(serverId, nymId, accountId, message);
			if (result != 1) {
				if (canRetry) {
					warn("Failed to verify balance agreement, proceeding to contingency plan");
					synchronizeRequestNumber();
					getIntermediaryFiles(true);
					getAndProcessNymbox(false);
					warn("Retrying sendTransaction() after contingency plan");
					sendTransaction(generator, false);
					return null;
				}
				error(Event.BALANCE_AGREEMENT_ERROR);
			}
		}
		{
			attempt("Verifying transaction message");
			int result = OTAPI.Message.getTransactionSuccess(serverId, nymId, accountId, message);
			if (result != 1)
				error(Event.TRANSACTION_MESSAGE_VERIFICATION_ERROR);
		}
		getIntermediaryFiles(true);
		return message;
	}

	private void getIntermediaryFiles(boolean forceDownload) throws OTException {
		{
			attempt("Synchronizing account");
			sendRequest(new RequestGenerator() {
				@Override
				public int getRequest() {
					return OTAPI.synchronizeAccount(serverId, nymId, accountId);
				}
			});
		}
		{
			attempt("Verifying inbox hash");
			String cachedHash = OTAPI.GetAccount.inboxHashCached(accountId);
			if (!Util.isValidString(cachedHash))
				warn("Unable to retrieve cached copy of server-side inbox hash");
			String localHash = OTAPI.GetNym.inboxHashLocal(nymId, accountId);
			if (!Util.isValidString(localHash))
				warn("Unable to retrieve client-side inbox hash");
			if (!forceDownload && cachedHash.equals(localHash)) {
				skip("The inbox hashes already match, skipping inbox download");
			} else {
				attempt("Downloading inbox");
				sendRequest(new RequestGenerator() {
					@Override
					public int getRequest() {
						return OTAPI.getInbox(serverId, nymId, accountId);
					}
				});
				insureHaveAllBoxReceipts(OTAPI.Box.INBOX, accountId);
			}
		}
		{
			attempt("Verifying outbox hash");
			String cachedHash = OTAPI.GetAccount.outboxHashCached(accountId);
			if (!Util.isValidString(cachedHash))
				warn("Unable to retrieve cached copy of server-side outbox hash");
			String localHash = OTAPI.GetNym.outboxHashLocal(nymId, accountId);
			if (!Util.isValidString(localHash))
				warn("Unable to retrieve client-side outbox hash");
			if (!forceDownload && cachedHash.equals(localHash)) {
				skip("The outbox hashes already match, skipping outbox download");
			} else {
				attempt("Downloading outbox");
				sendRequest(new RequestGenerator() {
					@Override
					public int getRequest() {
						return OTAPI.getOutbox(serverId, nymId, accountId);
					}
				});
				insureHaveAllBoxReceipts(OTAPI.Box.OUTBOX, accountId);
			}
		}
	}
	
	private String convertVolumeToAmount(Double volume) {
		return convertVolumeToAmount(getAccountAssetId(accountId), volume);
	}
	private String convertAmountToFormat(String amount) {
		return convertAmountToFormat(getAccountAssetId(accountId), amount);
	}
	private String convertVolumeToFormat(Double volume) {
		return convertVolumeToFormat(getAccountAssetId(accountId), volume);
	}
	
	private void removeVoucher(String voucher) throws OTException {
		attempt("Removing voucher");
		String ledger = OTAPI.loadRecordbox(serverId, nymId, accountId);
		if (!Util.isValidString(ledger))
			error("recordbox ledger is empty");
		int size = OTAPI.Ledger.getCount(serverId, nymId, accountId, ledger);
		if (size <= 0)
			error("recordbox size is zero or negative");
		String trxnNum = OTAPI.Instrument.getTransactionNum(voucher);
		for (int index = 0; index < size; index++) {
			String transaction = OTAPI.Ledger.getTransactionByIndex(serverId, nymId, accountId, ledger, index);
			if (!Util.isValidString(transaction))
				error("transaction is empty");
			String refNum = OTAPI.Transaction.getDisplayReferenceToNum(serverId, nymId, accountId, transaction);
			if (trxnNum.equals(refNum)) {
				if (!OTAPI.clearRecord(serverId, nymId, accountId, index))
					error("failed to clear record");
				success("Voucher is removed");
				return;
			}
		}
		error("Voucher not found");
	}
}
