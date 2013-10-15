package eu.opentxs.bridge.core.commands.act;

import java.util.List;

import eu.opentxs.bridge.Util;
import eu.opentxs.bridge.core.commands.Command;
import eu.opentxs.bridge.core.commands.Commands;
import eu.opentxs.bridge.core.exceptions.OTException;
import eu.opentxs.bridge.core.modules.Module;

public class WalletCommands extends Commands {

	public static void init() {
		addToCommands(new CreateWallet(), Category.WALLET, Sophistication.ADVANCED);
		addToCommands(new SetWallet(), Category.WALLET, Sophistication.ADVANCED);
		addToCommands(new ShowWallet(), Category.WALLET, Sophistication.MINI);
	}

	public static class CreateWallet extends Command {
		@Override
		public Validator getValidator(int index) {
			return new Validator() {
				@Override
				public boolean validate(String s) {
					if (!Util.isValidString(s))
						return error("WalletId cannot be empty");
					if (getWalletIds().contains(s))
						return error("A wallet with this id already exists");
					return true;
				}
			};
		}
		@Override
		protected void action(String[] args) throws OTException {
			String walletId = getString(0);
			execute(walletId);
		}
		public static void execute(String walletId) throws OTException {
			Module.createWallet(walletId);
			Module.loadAndShowWallet(walletId);
		}
	}

	public static class SetWallet extends Command {
		private List<String> ids;
		@Override
		public void sanity() throws OTException {
			ids = getWalletIds();
			if (ids.size() == 0)
				error("no wallets found");
		}
		@Override
		public boolean introduceArgument(int index) {
			if (index == 0) {
				return (new PlainPresenter() {
					@Override
					protected String name(String s) {
						return null;
					}
				}).show(ids);
			}
			return false;
		}
		@Override
		public Validator getValidator(int index) {
			if (index == 0) {
				return getListValidator(ids);
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String walletId = new PlainExtractor().eval(0, ids);
			execute(walletId);
		}
		public static void execute(String walletId) throws OTException {
			Module.loadAndShowWallet(walletId);
		}
	}

	public static class ShowWallet extends Command {
		@Override
		protected void action(String[] args) throws OTException {
			execute();
		}
		public static void execute() throws OTException {
			Module.showWallet();
		}
	}
}
