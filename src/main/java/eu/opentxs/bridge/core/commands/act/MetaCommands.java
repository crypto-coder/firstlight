package eu.opentxs.bridge.core.commands.act;

import eu.opentxs.bridge.core.commands.Command;
import eu.opentxs.bridge.core.commands.Commands;
import eu.opentxs.bridge.core.exceptions.OTException;
import eu.opentxs.bridge.core.modules.Module;

public class MetaCommands extends Commands {

	public static void init() {
		addToCommands(new Verbose(), Category.META, Sophistication.TOP);
	}

	public static class Verbose extends Command {
		@Override
		protected void action(String[] args) throws OTException {
			Module.toggleVerbose();
		}
	}

}
