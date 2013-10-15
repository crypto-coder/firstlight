package eu.opentxs.bridge.core.commands.act;

import java.util.List;

import eu.opentxs.bridge.Text;
import eu.opentxs.bridge.Util;
import eu.opentxs.bridge.core.DataModel;
import eu.opentxs.bridge.core.Interpreter;
import eu.opentxs.bridge.core.commands.Command;
import eu.opentxs.bridge.core.commands.Commands;
import eu.opentxs.bridge.core.commands.act.ConfigCommands.SetAccount;
import eu.opentxs.bridge.core.exceptions.OTException;
import eu.opentxs.bridge.core.modules.Module;
import eu.opentxs.bridge.core.modules.act.AssetModule;
import eu.opentxs.bridge.core.modules.act.NymModule;

public class AssetCommands extends Commands {

	public static void init() {
		addToCommands(new CreateAsset(), Category.ASSET, Sophistication.ISSUER);
		addToCommands(new AddAsset(), Category.ASSET, Sophistication.MINI);
		addToCommands(new IssueAsset(), Category.ASSET, Sophistication.ISSUER);
		addToCommands(new EditAsset(), Category.ASSET, Sophistication.SIMPLE);
		addToCommands(new DeleteAsset(), Category.ASSET, Sophistication.MINI);
		addToCommands(new ShowAssetContract(), Category.ASSET, Sophistication.ADVANCED);
		// addToCommands(new ShowAssetAccounts(), Category.ASSET, Sophistication.ADVANCED);
	}

	public static class CreateAsset extends Command {
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
			String definition = readStringFromInput("Paste an asset definition here");
			if (Util.isValidString(definition) && isValidAssetDefinition(definition)) {
				definition = Interpreter.restoreNewLines(definition);
			} else {
				print("This does not look like an asset definition");
				if (!readBooleanFromInput("Would you like to open it from a file?"))
					error("No vaild asset definition was supplied");
				definition = readStringFromFile(Text.FOLDER_ASSETS, Extension.DEFINITION);
				if (!Util.isValidString(definition))
					error("No vaild asset definition was supplied");
			}
			execute(nymId, definition);
		}

		public static void execute(String nymId, String definition) throws OTException {
			String assetId = AssetModule.createAsset(nymId, definition);
			String contract = AssetModule.getAssetContract(assetId);
			if (readBooleanFromInput("Would you like to save the new asset contract to a file?"))
				writeStringToFile(Text.FOLDER_ASSETS, Extension.CONTRACT, contract);
			String serverId = DataModel.getMyServerId();
			if (!Util.isValidString(serverId))
				return;
			if (readBooleanFromInput("Would you like to issue the new asset?"))
				IssueAsset.execute(serverId, nymId, contract, null);
		}
	}

	public static class AddAsset extends Command {
		@Override
		public boolean introduceArgument(int index) {
			return false;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String contract = readStringFromInput("Paste an asset contract here");
			if (Util.isValidString(contract) && isValidAssetContract(contract)) {
				contract = Interpreter.restoreNewLines(contract);
			} else {
				print("This does not look like an asset contract");
				if (!readBooleanFromInput("Would you like to open it from a file?"))
					error("No vaild asset contract was supplied");
				contract = readStringFromFile(Text.FOLDER_ASSETS, Extension.CONTRACT);
				if (!Util.isValidString(contract))
					error("No vaild asset contract was supplied");
			}
			execute(contract);
		}

		public static void execute(String contract) throws OTException {
			String assetId = AssetModule.addAsset(contract);
			if (!Util.isValidString(DataModel.getMyAssetId()))
				Module.setMyAssetId(assetId);
			String serverId = DataModel.getMyServerId();
			String nymId = DataModel.getMyNymId();
			if (!Util.isValidString(serverId) || !Util.isValidString(nymId))
				return;
			if (readBooleanFromInput("Would you like to create a new account for this asset?"))
				AccountCommands.CreateAccount.execute(serverId, nymId, assetId, null);
		}
	}

	public static class IssueAsset extends Command {
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
			String contract = readStringFromInput("Paste an asset contract here");
			if (Util.isValidString(contract) && isValidAssetContract(contract)) {
				contract = Interpreter.restoreNewLines(contract);
			} else {
				print("This does not look like an asset contract");
				if (!readBooleanFromInput("Would you like to open it from a file?"))
					error("No vaild asset contract was supplied");
				contract = readStringFromFile(Text.FOLDER_ASSETS, Extension.CONTRACT);
				if (!Util.isValidString(contract))
					error("No vaild asset contract was supplied");
			}
			String accountName = null;
			execute(serverId, nymId, contract, accountName);
		}

		public static void execute(String serverId, String nymId, String contract, String accountName) throws OTException {
			{
				int index = contract.indexOf("nymID=");
				if (index == 0)
					error("Contract is not signed by any nym");
				int beg = contract.indexOf("\"", index) + 1;
				int end = contract.indexOf("\"", beg);
				String signerId = contract.substring(beg, end);
				if (!nymId.equals(signerId))
					error("Contract signer and issuer must be the same");
			}
			if (!NymModule.isNymRegisteredAtServer(serverId, nymId)) {
				if (!readBooleanFromInput("Nym is not registered at the server. Register it now?"))
					error(Text.NYM_NEEEDS_TO_REGISTERED_ERROR);
				NymCommands.RegisterNym.execute(serverId, nymId);
			}
			NymModule nymModule = new NymModule(serverId, nymId);
			String accountId = nymModule.issueAsset(contract, accountName);
			if (Module.getAccountIds().size() == 1 || readBooleanFromInput("Would you like to set the issuer account as your account?"))
				SetAccount.execute(accountId);
			else
				Module.showAccount(accountId);
		}
	}

	public static class EditAsset extends Command {
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
				return getListValidator(assetIds, DataModel.getMyAssetId());
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String assetId = new PlainExtractor().eval(0, assetIds, DataModel.getMyAssetId());
			String assetName = getString(1);
			execute(assetId, assetName);
		}
		public static void execute(String assetId, String assetName) throws OTException {
			AssetModule.renameAsset(assetId, assetName);
		}
	}

	public static class ShowAssetContract extends Command {
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
				return getListValidator(assetIds, DataModel.getMyAssetId());
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String assetId = new PlainExtractor().eval(0, assetIds, DataModel.getMyAssetId());
			execute(assetId);
		}
		public static void execute(String assetId) throws OTException {
			String contract = AssetModule.showAssetContract(assetId);
			if (readBooleanFromInput("Would you like to save the asset contract to a file?"))
				writeStringToFile(Text.FOLDER_ASSETS, Extension.CONTRACT, contract);
		}
	}

	public static class ShowAssetAccounts extends Command {
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
				return getListValidator(assetIds, DataModel.getMyAssetId());
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String assetId = new PlainExtractor().eval(0, assetIds, DataModel.getMyAssetId());
			execute(assetId);
		}
		public static void execute(String assetId) throws OTException {
			AssetModule.showAssetAccounts(assetId);
		}
	}

	public static class DeleteAsset extends Command {
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
			Module.showAsset(assetId);
			if (readBooleanFromInput("Are you sure you want to delete this asset?")) {
				AssetModule.deleteAsset(assetId);
				if (assetId.equals(DataModel.getMyAssetId()))
					Module.setMyAssetId(null);
			}
		}
	}

}
