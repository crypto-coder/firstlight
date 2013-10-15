package eu.opentxs.bridge.core.commands.act;

import java.util.List;

import eu.opentxs.bridge.core.OpenTransactions;
import eu.opentxs.bridge.core.commands.Command;
import eu.opentxs.bridge.core.commands.Commands;
import eu.opentxs.bridge.core.dto.Account;
import eu.opentxs.bridge.core.exceptions.OTException;
import eu.opentxs.bridge.core.modules.Module;

public class ConfigCommands extends Commands {

	public static void init() {
		addToCommands(new SetMode(), Category.CONFIG, Sophistication.MINI);
		addToCommands(new SetServer(), Category.CONFIG, Sophistication.SIMPLE);
		addToCommands(new SetNym(), Category.CONFIG, Sophistication.MINI);
		addToCommands(new SetAsset(), Category.CONFIG, Sophistication.MINI);
		addToCommands(new SetAccount(), Category.CONFIG, Sophistication.MINI);
		addToCommands(new ShowConfig(), Category.CONFIG, Sophistication.MINI);
	}

	public static class SetMode extends Command {
		@Override
		public boolean introduceArgument(int index) {
			if (index == 0) {
				int i = 0;
				for (Sophistication s : Sophistication.values())
					print(String.format("%3d: %s", ++i, s.name()));
			}
			return true;
		}
		@Override
		public Validator getValidator(int index) {
			return new IsInteger() {
				{
					setMinMax(1, Sophistication.values().length);
				}
			};
		}
		@Override
		protected void action(String[] args) throws OTException {
			Sophistication sophistication = Sophistication.values()[getInteger(0) - 1];
			execute(sophistication);
		}
		public static void execute(Sophistication sophistication) throws OTException {
			Module.setSophistication(sophistication);
			OpenTransactions.reset();
		}
	}

	public static class SetServer extends Command {
		private List<String> serverIds;
		@Override
		public void sanity() throws OTException {
			serverIds = Module.getServerIds();
			if (serverIds.size() == 0)
				error("You have no servers in your wallet");
		}
		@Override
		public boolean introduceArgument(int index) {
			return (new PlainPresenter() {
				@Override
				protected String name(String s) {
					return Module.getServerName(s);
				}
			}).show(serverIds);
		}
		@Override
		public Validator getValidator(int index) {
			if (index == 0) {
				return getListValidator(serverIds);
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String serverId = new PlainExtractor().eval(0, serverIds);
			execute(serverId);
		}
		public static void execute(String serverId) throws OTException {
			Module.setMyServerId(serverId);
		}
	}

	public static class SetNym extends Command {
		private List<String> nymIds;
		@Override
		public void sanity() throws OTException {
			nymIds = Module.getNymIds();
			if (nymIds.size() == 0)
				error("You have no nyms in your wallet");
		}
		@Override
		public boolean introduceArgument(int index) {
			return (new PlainPresenter() {
				@Override
				protected String name(String s) {
					return Module.getNymName(s);
				}
			}).show(nymIds);
		}
		@Override
		public Validator getValidator(int index) {
			if (index == 0) {
				return getListValidator(nymIds);
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String nymId = new PlainExtractor().eval(0, nymIds);
			execute(nymId);
		}
		public static void execute(String nymId) throws OTException {
			Module.setMyNymId(nymId);
		}
	}

	public static class SetAsset extends Command {
		private List<String> assetIds;
		@Override
		public void sanity() throws OTException {
			assetIds = Module.getAssetIds();
			if (assetIds.size() == 0)
				error("You have no assets in your wallet");
		}
		@Override
		public boolean introduceArgument(int index) {
			return (new PlainPresenter() {
				@Override
				protected String name(String s) {
					return Module.getAssetName(s);
				}
			}).show(assetIds);
		}
		@Override
		public Validator getValidator(int index) {
			if (index == 0) {
				return getListValidator(assetIds);
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String assetId = new PlainExtractor().eval(0, assetIds);
			execute(assetId);
		}
		public static void execute(String assetId) throws OTException {
			Module.setMyAssetId(assetId);
		}
	}

	public static class SetAccount extends Command {
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
		public static void execute(String accountId) throws OTException {
			Module.setMyAccountId(accountId);
			Module.showLedger(accountId);
		}
	}

	public static class ShowConfig extends Command {
		@Override
		protected void action(String[] args) throws OTException {
			execute();
		}
		public static void execute() throws OTException {
			Module.showConfig();
		}
	}

}
