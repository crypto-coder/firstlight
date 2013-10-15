package eu.opentxs.bridge.core;

import eu.opentxs.bridge.Util;
import eu.opentxs.bridge.core.commands.Commands.Sophistication;

public class DataModel {

	public static final String EMPTY = "";

	public static Sophistication getSophistication() {
		int value = Settings.getInstance().getSophistication();
		if (value < 0)
			return Sophistication.SIMPLE;
		return Sophistication.parse(value);
	}

	public static String getWalletId() {
		String walletId = Settings.getInstance().getWalletId();
		if (!Util.isValidString(walletId))
			return null;
		return walletId;
	}

	public static String getMyServerId() {
		String myServerId = Settings.getInstance().getMyServerId();
		if (!Util.isValidString(myServerId))
			return null;
		return myServerId;
	}

	public static String getMyNymId() {
		String myNymId = Settings.getInstance().getMyNymId();
		if (!Util.isValidString(myNymId))
			return null;
		return myNymId;
	}

	public static String getMyAssetId() {
		String myAssetId = Settings.getInstance().getMyAssetId();
		if (!Util.isValidString(myAssetId))
			return null;
		return myAssetId;
	}

	public static String getMyAccountId() {
		String myAccountId = Settings.getInstance().getMyAccountId();
		if (!Util.isValidString(myAccountId))
			return null;
		return myAccountId;
	}

}
