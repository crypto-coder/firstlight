package eu.opentxs.bridge.core.commands.act;

import java.util.List;

import eu.opentxs.bridge.Text;
import eu.opentxs.bridge.Util;
import eu.opentxs.bridge.core.DataModel;
import eu.opentxs.bridge.core.Interpreter;
import eu.opentxs.bridge.core.commands.Command;
import eu.opentxs.bridge.core.commands.Commands;
import eu.opentxs.bridge.core.exceptions.OTException;
import eu.opentxs.bridge.core.modules.Module;
import eu.opentxs.bridge.core.modules.act.NymModule;

public class NymCommands extends Commands {

	public static void init() {
		addToCommands(new CreateNym(), Category.NYM, Sophistication.MINI);
		addToCommands(new EditNym(), Category.NYM, Sophistication.MINI);
		addToCommands(new RegisterNym(), Category.NYM, Sophistication.SIMPLE);
		addToCommands(new RemoveNym(), Category.NYM, Sophistication.SIMPLE);
		addToCommands(new DeleteNym(), Category.NYM, Sophistication.MINI);
		addToCommands(new ExportNym(), Category.NYM, Sophistication.ADVANCED);
		addToCommands(new ImportNym(), Category.NYM, Sophistication.ADVANCED);
	}

	public static class CreateNym extends Command {
		@Override
		protected void action(String[] args) throws OTException {
			String nymName = getString(0);
			execute(nymName);
		}
		public static void execute(String nymName) throws OTException {
			String nymId = NymModule.createNym();
			NymModule.renameNym(nymId, nymName);
			if (!Util.isValidString(DataModel.getMyNymId()))
				Module.setMyNymId(nymId);
			String serverId = DataModel.getMyServerId();
			if (!Util.isValidString(serverId))
				return;
			if (readBooleanFromInput("Would you like to register this nym on the server?")) {
				RegisterNym.execute(serverId, nymId);
				String assetId = DataModel.getMyAssetId();
				if (!Util.isValidString(assetId))
					return;
				if (readBooleanFromInput("Would you like to create a new account for this nym?"))
					AccountCommands.CreateAccount.execute(serverId, nymId, assetId, null);
			}
		}
	}

	public static class EditNym extends Command {
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
				return getListValidator(nymIds, DataModel.getMyNymId());
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String nymId = new PlainExtractor().eval(0, nymIds, DataModel.getMyNymId());
			String nymName = getString(1);
			execute(nymId, nymName);
		}
		public static void execute(String nymId, String nymName) throws OTException {
			NymModule.renameNym(nymId, nymName);
		}
	}

	public static class RegisterNym extends Command {
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
				return getListValidator(nymIds);
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
			nymModule.registerNymAtServer();
		}
	}

	public static class RemoveNym extends Command {
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
				return getListValidator(nymIds);
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
			Module.showNym(nymId);
			if (readBooleanFromInput("Are you sure you want to remove this nym from server?")) {
				NymModule nymModule = new NymModule(serverId, nymId);
				nymModule.removeNymFromServer();
			}
		}
	}

	public static class DeleteNym extends Command {
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
			Module.showNym(nymId);
			if (readBooleanFromInput("Are you sure you want to delete this nym?")) {
				String serverId = null;
				do {
					serverId = NymModule.isNymRegisteredAtAnyServer(nymId);
					if (serverId != null) {
						print(String.format("%s %s", "This nym is still registered at server", Module.getServerName(serverId)));
						if (readBooleanFromInput("Would you like to remove it from server?"))
							new NymModule(serverId, nymId).removeNymFromServer();
						else
							return;
					}
				} while (serverId != null);
				NymModule.deleteNym(nymId);
				if (nymId.equals(DataModel.getMyNymId()))
					Module.setMyNymId(null);
			}
		}
	}

	public static class ExportNym extends Command {
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
				return getListValidator(nymIds, DataModel.getMyNymId());
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String nymId = new PlainExtractor().eval(0, nymIds, DataModel.getMyNymId());
			execute(nymId);
		}

		public static void execute(String nymId) throws OTException {
			String contract = NymModule.exportNym(nymId);
			if (readBooleanFromInput("Would you like to save the nym contract to a file?"))
				writeStringToFile(Text.FOLDER_NYMS, Extension.CONTRACT, contract);
		}
	}

	public static class ImportNym extends Command {
		@Override
		public boolean introduceArgument(int index) {
			return false;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String contract = readStringFromInput("Paste a nym contract here");
			if (Util.isValidString(contract) && isValidNymContract(contract)) {
				contract = Interpreter.restoreNewLines(contract);
			} else {
				print("This does not look like a nym contract");
				if (!readBooleanFromInput("Would you like to open it from a file?"))
					error("No vaild nym contract was supplied");
				contract = readStringFromFile(Text.FOLDER_NYMS, Extension.CONTRACT);
				if (!Util.isValidString(contract))
					error("No vaild nym contract was supplied");
			}
			execute(contract);
		}

		public static void execute(String contract) throws OTException {
			NymModule.importNym(contract);
		}
	}
}
