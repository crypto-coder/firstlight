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
import eu.opentxs.bridge.core.modules.act.ServerModule;

public class ServerCommands extends Commands {

	public static void init() {
		addToCommands(new CreateServer(), Category.SERVER, Sophistication.ADMIN);
		addToCommands(new AddServer(), Category.SERVER, Sophistication.SIMPLE);
		addToCommands(new EditServer(), Category.SERVER, Sophistication.SIMPLE);
		addToCommands(new DeleteServer(), Category.SERVER, Sophistication.SIMPLE);
		addToCommands(new ShowServerContract(), Category.SERVER, Sophistication.ADVANCED);
		// addToCommands(new ShowServerAccounts(), Category.SERVER, Sophistication.ADVANCED);
	}

	public static class CreateServer extends Command {
		private List<String> nymIds;
		@Override
		public void sanity() throws OTException {
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
						return Module.getNymName(s);
					}
				}).show(nymIds);
			}
			return false;
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
			String definition = readStringFromInput("Paste a server definition here");
			if (Util.isValidString(definition) && isValidServerDefinition(definition)) {
				definition = Interpreter.restoreNewLines(definition);
			} else {
				print("This does not look like a server definition");
				if (!readBooleanFromInput("Would you like to open it from a file?"))
					error("No vaild server definition was supplied");
				definition = readStringFromFile(Text.FOLDER_SERVERS, Extension.DEFINITION);
				if (!Util.isValidString(definition))
					error("No vaild server definition was supplied");
			}
			execute(nymId, definition);
		}
		public static void execute(String nymId, String definition) throws OTException {
			String serverId = ServerModule.createServer(nymId, definition);
			if (readBooleanFromInput("Would you like to save the new server contract to a file?")) {
				String contract = ServerModule.getServerContract(serverId);
				writeStringToFile(Text.FOLDER_SERVERS, Extension.CONTRACT, contract);
			}
			ServerModule.showServer(serverId);
		}
	}

	public static class AddServer extends Command {
		@Override
		public boolean introduceArgument(int index) {
			return false;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String contract = readStringFromInput("Paste a signed server contract here");
			if (Util.isValidString(contract) && isValidServerContract(contract)) {
				contract = Interpreter.restoreNewLines(contract);
			} else {
				print("This does not look like a signed server contract");
				if (!readBooleanFromInput("Would you like to open it from a file?"))
					error("No vaild server signed contract was supplied");
				contract = readStringFromFile(Text.FOLDER_SERVERS, Extension.CONTRACT);
				if (!Util.isValidString(contract))
					error("No vaild server signed contract was supplied");
			}
			execute(contract);
		}
		public static void execute(String contract) throws OTException {
			String serverId = ServerModule.addServer(contract);
			if (Module.getServerIds().size() == 1 || readBooleanFromInput("Would you like to set it as your server?"))
				Module.setMyServerId(serverId);
		}
	}

	public static class EditServer extends Command {
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
				return getListValidator(serverIds, DataModel.getMyServerId());
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String serverId = new PlainExtractor().eval(0, serverIds, DataModel.getMyServerId());
			String serverName = getString(1);
			execute(serverId, serverName);
		}

		public static void execute(String serverId, String serverName) throws OTException {
			ServerModule.renameServer(serverId, serverName);
		}
	}

	public static class ShowServerContract extends Command {
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
				return getListValidator(serverIds, DataModel.getMyServerId());
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String serverId = new PlainExtractor().eval(0, serverIds, DataModel.getMyServerId());
			execute(serverId);
		}
		public static void execute(String serverId) throws OTException {
			String contract = ServerModule.showServerContract(serverId);
			if (readBooleanFromInput("Would you like to save the server contract to a file?"))
				writeStringToFile(Text.FOLDER_SERVERS, Extension.CONTRACT, contract);
		}
	}

	public static class ShowServerAccounts extends Command {
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
				return getListValidator(serverIds, DataModel.getMyServerId());
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String serverId = new PlainExtractor().eval(0, serverIds, DataModel.getMyServerId());
			execute(serverId);
		}
		public static void execute(String serverId) throws OTException {
			ServerModule.showServerAccounts(serverId);
		}
	}

	public static class DeleteServer extends Command {
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
			Module.showServer(serverId);
			if (readBooleanFromInput("Are you sure you want to delete this server?")) {
				ServerModule.deleteServer(serverId);
				if (serverId.equals(DataModel.getMyServerId()))
					Module.setMyServerId(null);
			}
		}
	}

}
