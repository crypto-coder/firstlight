package eu.opentxs.bridge.core.commands.act;

import java.util.List;

import eu.opentxs.bridge.Text;
import eu.opentxs.bridge.Util;
import eu.opentxs.bridge.core.DataModel;
import eu.opentxs.bridge.core.commands.Command;
import eu.opentxs.bridge.core.commands.Commands;
import eu.opentxs.bridge.core.commands.act.BusinessCommands.CancelCheque;
import eu.opentxs.bridge.core.commands.act.BusinessCommands.CancelInvoice;
import eu.opentxs.bridge.core.commands.act.BusinessCommands.DiscardCheque;
import eu.opentxs.bridge.core.commands.act.BusinessCommands.DiscardInvoice;
import eu.opentxs.bridge.core.commands.act.BusinessCommands.ExcecuteCheque;
import eu.opentxs.bridge.core.commands.act.BusinessCommands.ExecuteInvoice;
import eu.opentxs.bridge.core.dto.Account;
import eu.opentxs.bridge.core.dto.Transaction;
import eu.opentxs.bridge.core.dto.Transaction.InstrumentType;
import eu.opentxs.bridge.core.dto.Transaction.Side;
import eu.opentxs.bridge.core.exceptions.OTException;
import eu.opentxs.bridge.core.modules.Module;
import eu.opentxs.bridge.core.modules.act.AccountModule;
import eu.opentxs.bridge.core.modules.act.AssetModule;
import eu.opentxs.bridge.core.modules.act.NymModule;

public class AccountCommands extends Commands {

	public static void init() {
		addToCommands(new CreateAccount(), Category.ACCOUNT, Sophistication.MINI);
		addToCommands(new DeleteAccount(), Category.ACCOUNT, Sophistication.MINI);
		addToCommands(new ShowAccount(), Category.ACCOUNT, Sophistication.MINI);
		addToCommands(new ShowPurse(), Category.ACCOUNT, Sophistication.ADVANCED);
		addToCommands(new ShowTrxns(), Category.ACCOUNT, Sophistication.MINI);
		addToCommands(new DeleteTrxns(), Category.ACCOUNT, Sophistication.MINI);
		addToCommands(new ManageUnrealized(), Category.ACCOUNT, Sophistication.ADVANCED);
		addToCommands(new Refresh(), Category.ACCOUNT, Sophistication.MINI);
	}

	public static class CreateAccount extends Command {
		private List<String> serverIds;
		private List<String> nymIds;
		private List<String> assetIds;
		@Override
		public void sanity() throws OTException {
			serverIds = Module.getServerIds();
			if (serverIds.size() == 0)
				error("You have no servers in your wallet");
			nymIds = Module.getNymIds();
			if (nymIds.size() == 0)
				error("You have no nyms in your wallet");
			assetIds = Module.getAssetIds();
			if (assetIds.size() == 0)
				error("You have no assets in your wallet");
		}
		@Override
		public boolean introduceArgument(int index) {
			if (index == 0) {
				return (new PlainPresenter() {
					@Override
					protected String name(String s) {
						return Module.getServerName(s);
					}
				}).show(serverIds);
			}
			if (index == 1) {
				return (new PlainPresenter() {
					@Override
					protected String name(String s) {
						return Module.getNymName(s);
					}
				}).show(nymIds);
			}
			if (index == 2) {
				return (new PlainPresenter() {
					@Override
					protected String name(String s) {
						return Module.getAssetName(s);
					}
				}).show(assetIds);
			}
			return false;
		}
		@Override
		public Validator getValidator(int index) {
			if (index == 0) {
				return getListValidator(serverIds, DataModel.getMyServerId());
			}
			if (index == 1) {
				return getListValidator(nymIds, DataModel.getMyNymId());
			}
			if (index == 2) {
				return getListValidator(assetIds);
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String serverId = new PlainExtractor().eval(0, serverIds, DataModel.getMyServerId());
			String nymId = new PlainExtractor().eval(1, nymIds, DataModel.getMyNymId());
			String assetId = new PlainExtractor().eval(2, assetIds);
			String accountName = null;
			execute(serverId, nymId, assetId, accountName);
		}
		/**
		 * Create a new account for given nym and asset and register the new account on the server
		 * 
		 * @param serverId
		 * @param nymId
		 * @param assetId
		 * @param accountName
		 * @throws OTException
		 */
		public static void execute(String serverId, String nymId, String assetId, String accountName) throws OTException {
			String accountId = AccountModule.accountAlreadyExists(serverId, nymId, assetId);
			if (Util.isValidString(accountId)) {
				Module.showAccount(accountId);
				print("Account already exists");
				return;
			}
			if (!NymModule.isNymRegisteredAtServer(serverId, nymId)) {
				if (!readBooleanFromInput("Nym is not registered at the server. Register it now?"))
					error(Text.NYM_NEEEDS_TO_REGISTERED_ERROR);
				NymCommands.RegisterNym.execute(serverId, nymId);
			}
			AssetModule assetModule = new AssetModule(serverId, nymId, assetId);
			accountId = assetModule.createAccount(accountName);
			Module.showAccount(accountId);
		}
	}

	public static class DeleteAccount extends Command {
		private List<Account> accounts;
		@Override
		public void sanity() throws OTException {
			accounts = Account.getList();
			if (accounts.size() == 0)
				error("You have no accounts in your wallet");
		}
		@Override
		public boolean introduceArgument(int index) {
			if (accounts.size() == 1)
				return false;
			Account.present();
			return true;
		}
		@Override
		public Validator getValidator(int index) {
			if (index == 0) {
				return getListValidator(accounts);
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String accountId = (new Extractor<Account>() {
				@Override
				public String get(Account account) {
					return account.id;
				}
			}).eval(0, accounts);
			execute(accountId);
		}
		/**
		 * Delete an account and remove it from the server (fails if it's still used)
		 * 
		 * @param accountId
		 * @throws OTException
		 */
		public static void execute(String accountId) throws OTException {
			Module.showAccount(accountId);
			if (readBooleanFromInput("Are you sure you want to delete this account?")) {
				AccountModule.deleteAccount(accountId);
				if (accountId.equals(DataModel.getMyAccountId()))
					Module.setMyAccountId(null);
			}
		}
	}

	public static class ShowAccount extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			execute(DataModel.getMyAccountId());
		}
		/**
		 * Show an account and its balance
		 * 
		 * @param accountId
		 * @throws OTException
		 */
		public static void execute(String accountId) throws OTException {
			Module.showLedger(accountId);
		}
	}

	public static class ShowPurse extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			execute(DataModel.getMyAccountId());
		}
		/**
		 * Show the purse for an account
		 * 
		 * @param accountId
		 * @throws OTException
		 */
		public static void execute(String accountId) throws OTException {
			String serverId = Module.getAccountServerId(accountId);
			String nymId = Module.getAccountNymId(accountId);
			String assetId = Module.getAccountAssetId(accountId);
			AssetModule assetModule = new AssetModule(serverId, nymId, assetId);
			String purse = assetModule.loadPurse();
			if (!Util.isValidString(purse)) {
				print("Your purse seems to be empty");
				return;
			}
			assetModule.showPurse(purse);
		}
	}

	public static class ShowTrxns extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			execute(DataModel.getMyAccountId());
		}
		/**
		 * Show transactions for an account
		 * 
		 * @param accountId
		 * @throws OTException
		 */
		public static void execute(String accountId) throws OTException {
			AccountModule accountModule = AccountModule.getInstance(accountId);
			accountModule.showTransactions();
		}
	}
	
	public static class DeleteTrxns extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			execute(DataModel.getMyAccountId());
		}
		/**
		 * Delete realized transaction history for an account
		 * 
		 * @param accountId
		 * @throws OTException
		 */
		public static void execute(String accountId) throws OTException {
			if (readBooleanFromInput("Are you sure you want to delete all transaction history?")) {
				AccountModule accountModule = AccountModule.getInstance(accountId);
				accountModule.deleteTransactions();
			}
		}
	}

	public static class ManageUnrealized extends Command {
		public enum Action {
			CANCEL, EXECUTE, DISCARD,
		}
		private List<Transaction> transactions;
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
			AccountModule accountModule = AccountModule.getInstance(DataModel.getMyAccountId());
			transactions = accountModule.getTransactionsUnrealized();
			if (transactions.size() == 0)
				error("There are no unrealized transactions");
		}
		@Override
		public boolean introduceArgument(int index) {
			if (transactions.size() == 1)
				return false;
			Transaction.present(transactions);
			return true;
		}
		@Override
		public Validator getValidator(int index) {
			return getListValidator(transactions);
		}
		@Override
		protected void action(String[] args) throws OTException {
			Transaction transaction;
			if (transactions.size() == 1)
				transaction = transactions.get(0);
			else
				transaction = transactions.get(getInteger(0) - 1);
			transaction.print();
			Side side = transaction.getSide();
			Action action = null;
			if (side.equals(Side.SENT)) {
				action = Action.CANCEL;
			} else if (side.equals(Side.RECEIVED)) {
				InstrumentType instrumentType = transaction.getInstrumentType();
				String response = null;
				final String accept = "A";
				final String reject = "R";
				if (instrumentType.equals(InstrumentType.INVOICE))
					response = readStringFromInput(String.format("%s (%s/%s)",
							"Accept (and pay the invoice) or reject (and discard)?", accept, reject));
				else if (instrumentType.equals(InstrumentType.CHEQUE))
					response = readStringFromInput(String.format("%s (%s/%s)",
							"Accept (and deposit the cheque) or reject (and discard)?", accept, reject));
				if (response.equalsIgnoreCase(accept))
					action = Action.EXECUTE;
				else if (response.equalsIgnoreCase(reject))
					action = Action.DISCARD;
			}
			if (action != null) {
				execute(DataModel.getMyAccountId(), transaction, action);
			}
		}
		/**
		 * Manage unrealized transaction
		 * 
		 * @param accountId
		 * @param transaction
		 * @param action
		 * @throws OTException
		 */
		public static void execute(String accountId, Transaction transaction, Action action) throws OTException {
			String serverId = Module.getAccountServerId(accountId);
			String nymId = Module.getAccountNymId(accountId);
			InstrumentType instrumentType = transaction.getInstrumentType();
			if (instrumentType.equals(InstrumentType.INVOICE)) {
				String invoice = transaction.getInstrument();
				if (action.equals(Action.EXECUTE))
					ExecuteInvoice.execute(accountId, invoice);
				else if (action.equals(Action.DISCARD))
					DiscardInvoice.execute(serverId, nymId, invoice);
				else if (action.equals(Action.CANCEL))
					CancelInvoice.execute(accountId, invoice);
			} else if (instrumentType.equals(InstrumentType.CHEQUE)) {
				String cheque = transaction.getInstrument();
				if (action.equals(Action.EXECUTE))
					ExcecuteCheque.execute(accountId, cheque);
				else if (action.equals(Action.DISCARD))
					DiscardCheque.execute(serverId, nymId, cheque);
				else if (action.equals(Action.CANCEL))
					CancelCheque.execute(accountId, cheque);
			}
		}
	}

	public static class Refresh extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			execute(DataModel.getMyAccountId());
		}
		/**
		 * Download all files and accept all updates
		 * 
		 * @param accountId
		 * @throws OTException
		 */
		public static void execute(String accountId) throws OTException {
			AccountModule accountModule = AccountModule.getInstance(accountId);
			accountModule.refresh();
			ShowAccount.execute(accountId);
		}
	}

}
