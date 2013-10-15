package eu.opentxs.bridge.core.commands.act;

import java.util.List;

import eu.opentxs.bridge.Text;
import eu.opentxs.bridge.Util;
import eu.opentxs.bridge.core.DataModel;
import eu.opentxs.bridge.core.OpenTransactions;
import eu.opentxs.bridge.core.commands.Command;
import eu.opentxs.bridge.core.commands.Commands;
import eu.opentxs.bridge.core.dto.Contact;
import eu.opentxs.bridge.core.dto.ContactAccount;
import eu.opentxs.bridge.core.exceptions.OTException;
import eu.opentxs.bridge.core.modules.Module;
import eu.opentxs.bridge.core.modules.act.AccountModule;
import eu.opentxs.bridge.core.modules.act.AssetModule;
import eu.opentxs.bridge.core.modules.act.NymModule;

public class HackCommands extends Commands {

	public static void init() {
		addToCommands(new Reset(), Category.HACK, Sophistication.TOP);
		addToCommands(new Ping(), Category.HACK, Sophistication.TOP);
		addToCommands(new Time(), Category.HACK, Sophistication.TOP);
		addToCommands(new Download(), Category.HACK, Sophistication.TOP);
		addToCommands(new ProcessInbox(), Category.HACK, Sophistication.TOP);
		addToCommands(new ProcessIncome(), Category.HACK, Sophistication.TOP);
		addToCommands(new Verify(), Category.HACK, Sophistication.TOP);
		addToCommands(new Inbox(), Category.HACK, Sophistication.TOP);
		addToCommands(new Outbox(), Category.HACK, Sophistication.TOP);
		addToCommands(new Nymbox(), Category.HACK, Sophistication.TOP);
		addToCommands(new PayInbox(), Category.HACK, Sophistication.TOP);
		addToCommands(new PayOutbox(), Category.HACK, Sophistication.TOP);
		addToCommands(new QueryAsset(), Category.HACK, Sophistication.TOP);
		addToCommands(new Resync(), Category.HACK, Sophistication.TOP);
		addToCommands(new Hack1(), Category.HACK, Sophistication.TOP);
		addToCommands(new Hack2(), Category.HACK, Sophistication.TOP);
	}

	public static class Reset extends Command {
		@Override
		protected void action(String[] args) throws OTException {
			execute();
		}
		public static void execute() throws OTException {
			Module.setMyServerId(null);
			Module.setMyNymId(null);
			Module.setMyAssetId(null);
			Module.setMyAccountId(null);
//			List<Contact> contacts = Contact.getList();
//			for (Contact contact : contacts)
//				Contact.delete(contact.getNymId());
//			for (ContactAccount contactAccount : ContactAccount.getList())
//				ContactAccount.delete(contactAccount.getAccountId());
			Module.setSophistication(Sophistication.SIMPLE);
			OpenTransactions.reset();
		}
	}

	public static class Ping extends Command {
		private List<String> serverIds;
		private List<String> nymIds;
		@Override
		public void sanity() throws OTException {
			serverIds = Module.getServerIds();
			if (serverIds.size() == 0)
				error("You have no servers in your wallet");
			nymIds = Module.getNymIds();
			if (nymIds.size() == 0)
				error("You have no nyms in your wallet");
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
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String serverId = new PlainExtractor().eval(0, serverIds, DataModel.getMyServerId());
			String nymId = new PlainExtractor().eval(1, nymIds, DataModel.getMyNymId());
			execute(serverId, nymId);
		}
		public static void execute(String serverId, String nymId) throws OTException {
			NymModule nymModule = new NymModule(serverId, nymId);
			nymModule.pingServer();
		}
	}

	public static class Time extends Command {
		@Override
		protected void action(String[] args) throws OTException {
			execute();
		}
		public static void execute() throws OTException {
			Module.showTime();
		}
	}

	public static class Download extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			execute(DataModel.getMyAccountId());
		}
		public static void execute(String accountId) throws OTException {
			AccountModule accountModule = AccountModule.getInstance(accountId);
			accountModule.downloadFiles();
		}
	}

	public static class ProcessInbox extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			execute(DataModel.getMyAccountId());
		}
		public static void execute(String accountId) throws OTException {
			AccountModule accountModule = AccountModule.getInstance(accountId);
			accountModule.processInbox();
			System.out.println(Text.COMMAND_ENDED_WITH_SUCCESS);
		}
	}

	public static class ProcessIncome extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			execute(DataModel.getMyAccountId());
		}
		public static void execute(String accountId) throws OTException {
			AccountModule accountModule = AccountModule.getInstance(accountId);
			accountModule.processIncome();
			System.out.println(Text.COMMAND_ENDED_WITH_SUCCESS);
		}
	}

	public static class Verify extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			execute(DataModel.getMyAccountId());
		}
		public static void execute(String accountId) throws OTException {
			AccountModule accountModule = AccountModule.getInstance(accountId);
			accountModule.verifyLastReceipt();
		}
	}

	public static class Inbox extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			execute(DataModel.getMyAccountId());
		}
		public static void execute(String accountId) throws OTException {
			AccountModule accountModule = AccountModule.getInstance(accountId);
			accountModule.showInbox();
		}
	}

	public static class Outbox extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			execute(DataModel.getMyAccountId());
		}
		public static void execute(String accountId) throws OTException {
			AccountModule accountModule = AccountModule.getInstance(accountId);
			accountModule.showOutbox();
		}
	}

	public static class Nymbox extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			execute(DataModel.getMyServerId(), DataModel.getMyNymId());
		}
		public static void execute(String serverId, String nymId) throws OTException {
			NymModule nymModule = new NymModule(serverId, nymId);
			nymModule.showNymbox();
		}
	}

	public static class PayInbox extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			execute(DataModel.getMyServerId(), DataModel.getMyNymId(), DataModel.getMyAssetId());
		}
		public static void execute(String serverId, String nymId, String assetId) throws OTException {
			AssetModule assetModule = new AssetModule(serverId, nymId, assetId);
			assetModule.showPayInbox();
		}
	}

	public static class PayOutbox extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			execute(DataModel.getMyServerId(), DataModel.getMyNymId(), DataModel.getMyAssetId());
		}
		public static void execute(String serverId, String nymId, String assetId) throws OTException {
			AssetModule assetModule = new AssetModule(serverId, nymId, assetId);
			assetModule.showPayOutbox();
		}
	}

	public static class QueryAsset extends Command {
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
				(new PlainPresenter() {
					@Override
					protected String name(String s) {
						return Module.getServerName(s);
					}
				}).show(serverIds);
			} else if (index == 1) {
				(new PlainPresenter() {
					@Override
					protected String name(String s) {
						return Module.getNymName(s);
					}
				}).show(nymIds);
			} else if (index == 2) {
				(new PlainPresenter() {
					@Override
					protected String name(String s) {
						return Module.getAssetName(s);
					}
				}).show(assetIds);
			}
			return true;
		}
		@Override
		public Validator getValidator(int index) {
			if (index == 0) {
				return getListValidator(serverIds, DataModel.getMyServerId());
			} else if (index == 1) {
				return getListValidator(nymIds, DataModel.getMyNymId());
			} else if (index == 2) {
				return getListValidator(assetIds, DataModel.getMyAssetId());
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String serverId = new PlainExtractor().eval(0, serverIds, DataModel.getMyServerId());
			String nymId = new PlainExtractor().eval(1, nymIds, DataModel.getMyNymId());
			String assetId = new PlainExtractor().eval(2, assetIds, DataModel.getMyAssetId());
			execute(serverId, nymId, assetId);
		}
		public static void execute(String serverId, String nymId, String assetId) throws OTException {
			AssetModule assetModule = new AssetModule(serverId, nymId, assetId);
			assetModule.queryAsset();
		}
	}

	public static class Resync extends Command {
		private List<String> serverIds;
		private List<String> nymIds;
		@Override
		public void sanity() throws OTException {
			serverIds = Module.getServerIds();
			if (serverIds.size() == 0)
				error("You have no servers in your wallet");
			nymIds = Module.getNymIds();
			if (nymIds.size() == 0)
				error("You have no nyms in your wallet");
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
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String serverId = new PlainExtractor().eval(0, serverIds, DataModel.getMyServerId());
			String nymId = new PlainExtractor().eval(1, nymIds);
			execute(serverId, nymId);
		}
		public static void execute(String serverId, String nymId) throws OTException {
			NymModule nymModule = new NymModule(serverId, nymId);
			nymModule.resyncNymWithServer();
		}
	}

	public static class Hack1 extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			AccountModule.getInstance(DataModel.getMyAccountId()).hack1();
		}
	}

	public static class Hack2 extends Command {
		@Override
		public void sanity() throws OTException {
			if (!Util.isValidString(DataModel.getMyAccountId()))
				error("You need to set your account first");
		}
		@Override
		protected void action(String[] args) throws OTException {
			AccountModule.getInstance(DataModel.getMyAccountId()).hack2();
		}
	}

}
