package eu.opentxs.bridge.core.modules;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import eu.ApplicationProperties;
import eu.opentxs.bridge.Text;
import eu.opentxs.bridge.CustomUTC;
import eu.opentxs.bridge.Util;
import eu.opentxs.bridge.core.DataModel;
import eu.opentxs.bridge.core.Settings;
import eu.opentxs.bridge.core.commands.Commands.Extension;
import eu.opentxs.bridge.core.commands.Commands.Sophistication;
import eu.opentxs.bridge.core.dto.Account;
import eu.opentxs.bridge.core.dto.Transaction.InstrumentType;
import eu.opentxs.bridge.core.exceptions.OTException;
import eu.opentxs.bridge.core.exceptions.OTSystemException;
import eu.opentxs.bridge.core.exceptions.OTSystemException.Event;
import eu.opentxs.bridge.core.exceptions.OTUserException;

public abstract class Module {

	protected interface RequestGenerator {
		public int getRequest();
	}

	protected static boolean verbose;
	protected static boolean verboseServer;
	protected static boolean verboseClientLog;
	protected static boolean verboseClientSkip;
	protected static boolean verboseClientWarn;
	protected static boolean verboseClientSuccess;

	protected enum AccountType {
		SIMPLE("simple"), ISSUER("issuer");
		private String value;

		private AccountType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static AccountType parse(String value) {
			if (value.equalsIgnoreCase(SIMPLE.getValue()))
				return SIMPLE;
			if (value.equalsIgnoreCase(ISSUER.getValue()))
				return ISSUER;
			return null;
		}
	}

	public static void init() {

		String walletId = DataModel.getWalletId();
		File file = new File(getWalletFileName(walletId));
		try {// fatal
			if (file.exists())
				loadWallet(walletId);
			else
				createWallet(walletId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String myServerId = DataModel.getMyServerId();
		if (myServerId != null)
			myServerId = OTAPI.Wallet.getServerIdFromPartial(myServerId);
		if (!Util.isValidString(myServerId))
			Settings.getInstance().setMyServerId(DataModel.EMPTY);

		String myNymId = DataModel.getMyNymId();
		if (myNymId != null)
			myNymId = OTAPI.Wallet.getNymIdFromPartial(myNymId);
		if (!Util.isValidString(myNymId))
			Settings.getInstance().setMyNymId(DataModel.EMPTY);

		String myAssetId = DataModel.getMyAssetId();
		if (myAssetId != null)
			myAssetId = OTAPI.Wallet.getAssetIdFromPartial(myAssetId);
		if (!Util.isValidString(myAssetId))
			Settings.getInstance().setMyAssetId(DataModel.EMPTY);

		String myAccountId = DataModel.getMyAccountId();
		if (myAccountId != null)
			myAccountId = OTAPI.Wallet.getAccountIdFromPartial(myAccountId);
		if (!Util.isValidString(myAccountId))
			Settings.getInstance().setMyAccountId(DataModel.EMPTY);

		Settings.getInstance().save();
		showConfig();
		verbose = false;
		applyVerbose();
	}

	public static void toggleVerbose() {
		verbose = !verbose;
		applyVerbose();
		info(String.format("Verbose is now %s", verbose ? "on" : "off"));
	}

	public static void setSophistication(Sophistication sophistication) {
		Settings.getInstance().setSophistication(sophistication.getValue());
		Settings.getInstance().save();
	}

	public static void setMyServerId(String serverId) throws OTException {
		String myServerId;
		if (!Util.isValidString(serverId)) {
			myServerId = DataModel.EMPTY;
		} else {
			myServerId = parseServerId(serverId);
			if (myServerId.equals(DataModel.getMyServerId())) {
				print(myServerId);
				info("Already using this server");
				return;
			}
		}
		Settings.getInstance().setMyServerId(myServerId);
		if (!myServerId.equals(getAccountServerId(DataModel.getMyAccountId())))
			Settings.getInstance().setMyAccountId(DataModel.EMPTY);
		Settings.getInstance().save();
		showConfig();
	}

	public static void setMyNymId(String nymId) throws OTException {
		String myNymId;
		if (!Util.isValidString(nymId)) {
			myNymId = DataModel.EMPTY;
		} else {
			myNymId = parseNymId(nymId);
			if (myNymId.equals(DataModel.getMyNymId())) {
				showNym(myNymId);
				info("Already using this nym");
				return;
			}
		}
		Settings.getInstance().setMyNymId(myNymId);
		if (!myNymId.equals(getAccountNymId(DataModel.getMyAccountId())))
			Settings.getInstance().setMyAccountId(DataModel.EMPTY);
		Settings.getInstance().save();
		showConfig();
	}

	public static void setMyAssetId(String assetId) throws OTException {
		String myAssetId;
		if (!Util.isValidString(assetId)) {
			myAssetId = DataModel.EMPTY;
		} else {
			myAssetId = parseAssetId(assetId);
			if (myAssetId.equals(DataModel.getMyAssetId())) {
				showAsset(myAssetId);
				info("Already using this asset");
				return;
			}
		}
		Settings.getInstance().setMyAssetId(myAssetId);
		if (!myAssetId.equals(getAccountAssetId(DataModel.getMyAccountId())))
			Settings.getInstance().setMyAccountId(DataModel.EMPTY);
		Settings.getInstance().save();
		showConfig();
	}

	public static void setMyAccountId(String accountId) throws OTException {
		String myAccountId;
		if (!Util.isValidString(accountId)) {
			myAccountId = DataModel.EMPTY;
		} else {
			myAccountId = parseAccountId(accountId);
			if (myAccountId.equals(DataModel.getMyAccountId())) {
				print(myAccountId);
				info("Already using this account");
				return;
			}
			String myAssetId = getAccountAssetId(myAccountId);
			String myNymId = getAccountNymId(myAccountId);
			String myServerId = getAccountServerId(myAccountId);
			Settings.getInstance().setMyServerId(myServerId);
			Settings.getInstance().setMyNymId(myNymId);
			Settings.getInstance().setMyAssetId(myAssetId);
		}
		Settings.getInstance().setMyAccountId(myAccountId);
		Settings.getInstance().save();
	}

	public static boolean hasAccess(Sophistication sophistication) {
		return DataModel.getSophistication().hasAccess(sophistication);
	}

	public static CustomUTC getTime() {
		return CustomUTC.getDateUTC(OTAPI.getTime());
	}

	public static void showTime() {
		print(CustomUTC.timeToString(getTime()));
	}

	public static void showConfig() {
		Sophistication sophistication = DataModel.getSophistication();
		String walletId = DataModel.getWalletId();
		String myServerId = DataModel.getMyServerId();
		String myNymId = DataModel.getMyNymId();
		String myAssetId = DataModel.getMyAssetId();
		String myAccountId = DataModel.getMyAccountId();
		print(Util.repeat("-", 13));
		print(String.format("%12s: %s", "Mode", sophistication));
		print(String.format("%12s: %s", "Wallet", walletId));
		print(String.format("%12s: %s (%s)", "Server", myServerId,
				getServerName(myServerId)));
		print(String.format("%12s: %s (%s)", "Nym", myNymId,
				getNymName(myNymId)));
		print(String.format("%12s: %s (%s)", "Asset", myAssetId,
				getAssetName(myAssetId)));
		print(String.format("%12s: %s (%s)", "Account", myAccountId,
				getAccountName(myAccountId)));
		print(Util.repeat("-", 13));
	}

	public static void createWallet(String walletId) {
		try {//fatal
			InputStream is = ClassLoader.getSystemResource(ApplicationProperties.get().getString("wallet.xml")).openStream();
			Scanner s = new Scanner(is);
			s.useDelimiter("\\A");
			String content = s.hasNext() ? s.next() : "";
			s.close();
			is.close();
			content = content.replaceAll("\\r", "");
			FileWriter fw = new FileWriter(getWalletFileName(walletId));
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadAndShowWallet(String walletId) throws OTException {
		loadWallet(walletId);
		Settings.getInstance().setWalletId(walletId);
		Settings.getInstance().save();
		showWallet();
	}

	public static void showWallet() {
		if (hasAccess(Sophistication.SIMPLE)) {
			print(Util.repeat("-", 70));
			showServers();
		}
		{
			print(Util.repeat("-", 70));
			showNyms();
		}
		{
			print(Util.repeat("-", 70));
			showAssets();
		}
		{
			print(Util.repeat("-", 70));
			Account.show();
		}
		print(Util.repeat("-", 70));
	}

	public static List<String> getServerIds() {
		List<String> servers = new ArrayList<String>();
		int count = OTAPI.getServerCount();
		for (int index = 0; index < count; index++)
			servers.add(OTAPI.GetServer.id(index));
		return servers;
	}

	public static List<String> getNymIds() {
		List<String> nyms = new ArrayList<String>();
		int count = OTAPI.getNymCount();
		for (int index = 0; index < count; index++)
			nyms.add(OTAPI.GetNym.id(index));
		return nyms;
	}

	public static List<String> getAssetIds() {
		List<String> assets = new ArrayList<String>();
		int count = OTAPI.getAssetCount();
		for (int index = 0; index < count; index++)
			assets.add(OTAPI.GetAsset.id(index));
		return assets;
	}

	public static List<String> getAccountIds() {
		List<String> accounts = new ArrayList<String>();
		int count = OTAPI.getAccountCount();
		for (int index = 0; index < count; index++)
			accounts.add(OTAPI.GetAccount.id(index));
		return accounts;
	}

	public static String getAccountServerId(String accountId) {
		if (!Util.isValidString(accountId))
			return null;
		return OTAPI.GetAccount.serverId(accountId);
	}

	public static String getAccountNymId(String accountId) {
		if (!Util.isValidString(accountId))
			return null;
		return OTAPI.GetAccount.nymId(accountId);
	}

	public static String getAccountAssetId(String accountId) {
		if (!Util.isValidString(accountId))
			return null;
		return OTAPI.GetAccount.assetId(accountId);
	}

	public static Integer convertAmountToValue(String amount)
			throws OTException {
		Integer value = null;
		try {
			value = new Integer(amount);
		} catch (NumberFormatException e) {
			error(Event.STRING_TO_INTEGER_CONVERSION_ERROR);
		}
		return value;
	}

	protected static Double convertValueToVolume(String assetId, Integer value)
			throws OTException {
		String format = convertValueToFormat(assetId, value);
		format = format.replaceAll("[^0-9.]", "");
		Double volume = null;
		try {
			volume = new Double(format);
		} catch (NumberFormatException e) {
			error(Event.STRING_TO_DOUBLE_CONVERSION_ERROR);
		}
		return volume;
	}

	protected static String convertVolumeToAmount(String assetId, Double volume) {
		return OTAPI.unformatAmount(assetId, volume.toString());
	}

	protected static Integer convertVolumeToValue(String assetId, Double volume)
			throws OTException {
		return convertAmountToValue(convertVolumeToAmount(assetId, volume));
	}

	protected static String convertAmountToFormat(String assetId, String amount) {
		return OTAPI.formatAmount(assetId, amount);
	}

	public static String convertValueToFormat(String assetId, Integer value) {
		return convertAmountToFormat(assetId, value.toString());
	}

	public static String convertVolumeToFormat(String assetId, Double volume) {
		return convertAmountToFormat(assetId,
				convertVolumeToAmount(assetId, volume));
	}

	public static String getServerName(String serverId) {
		if (!Util.isValidString(serverId))
			return Text.NAME_UNKNOWN.toString();
		String serverName = OTAPI.GetServer.name(serverId);
		if (Util.isValidString(serverName))
			return serverName;
		return Text.NAME_UNKNOWN.toString();
	}

	public static String getNymName(String nymId) {
		if (!Util.isValidString(nymId))
			return Text.NAME_UNKNOWN.toString();
		String nymName = OTAPI.GetNym.name(nymId);
		if (Util.isValidString(nymName))
			return nymName;
		return Text.NAME_UNKNOWN.toString();
	}

	public static String getAssetName(String assetId) {
		if (!Util.isValidString(assetId))
			return Text.NAME_UNKNOWN.toString();
		String assetName = OTAPI.GetAsset.name(assetId);
		if (Util.isValidString(assetName))
			return assetName;
		return Text.NAME_UNKNOWN.toString();
	}

	public static String getAccountName(String accountId) {
		if (!Util.isValidString(accountId))
			return Text.NAME_UNKNOWN.toString();
		if (ApplicationProperties.get().getBoolean("account.standardNaming"))
			return getAccountStandardName(accountId);
		return OTAPI.GetAccount.name(accountId);
	}

	public static String getAccountType(String accountId) {
		if (!Util.isValidString(accountId))
			return null;
		return OTAPI.GetAccount.type(accountId);
	}

	public static String getAccountStandardName(String accountId) {
		String accountType = getAccountType(accountId);
		if (accountType.equals(AccountType.ISSUER.getValue())) {
			return String.format("%s%s's %s", Text.ISSUER_SIGN,
					getNymName(getAccountNymId(accountId)),
					getAssetName(getAccountAssetId(accountId)));
		}
		return String.format("%s's %s", getNymName(getAccountNymId(accountId)),
				getAssetName(getAccountAssetId(accountId)));
	}

	public static String getPurseStandardName(String nymId, String assetId) {
		return String.format("%s's %s", getNymName(nymId),
				getAssetName(assetId));
	}

	public static InstrumentType getInstrumentType(String instrument) {
		return InstrumentType.parse(OTAPI.Instrument.getType(instrument));
	}

	public static void showServer(String serverId) {
		print(Util.repeat("-", 13));
		print(String.format("%12s: %s", "Name", getServerName(serverId)));
		print(String.format("%12s: %s", "Server", serverId));
		print(Util.repeat("-", 13));
	}

	public static void showNym(String nymId) {
		print(Util.repeat("-", 13));
		print(String.format("%12s: %s", "Name", getNymName(nymId)));
		print(String.format("%12s: %s", "Nym", nymId));
		print(Util.repeat("-", 13));
	}

	public static void showAsset(String assetId) {
		print(Util.repeat("-", 13));
		print(String.format("%12s: %s", "Name", getAssetName(assetId)));
		print(String.format("%12s: %s", "Asset", assetId));
		print(Util.repeat("-", 13));
	}

	public static void showAccount(String accountId) {
		print(Util.repeat("-", 13));
		print(String.format("%12s: %s", "Name", getAccountName(accountId)));
		print(String.format("%12s: %s", "Account", accountId));
		print(Util.repeat("-", 13));
	}

	public static void showLedger(String accountId) throws OTException {
		String serverId = getAccountServerId(accountId);
		String nymId = getAccountNymId(accountId);
		String assetId = getAccountAssetId(accountId);

		String purse = OTAPI.loadPurse(serverId, nymId, assetId);
		Integer purseBalanceValue = new Integer(0);
		if (Util.isValidString(purse))
			purseBalanceValue = getPurseBalanceValue(serverId, assetId, purse);

		print(Util.repeat("-", 13));
		print(String.format("%12s: %s", "Name", getAccountName(accountId)));
		if (purseBalanceValue > 0)
			print(String.format("%12s: %s (+ %s)", "Balance",
					getAccountBalanceFormatted(accountId),
					getPurseBalanceFormatted(serverId, assetId, purse)));
		else
			print(String.format("%12s: %s", "Balance",
					getAccountBalanceFormatted(accountId)));

		print(String.format("%12s: %s", "Account", accountId));
		print(String.format("%12s: %s (%s)", "Asset", assetId,
				getAssetName(assetId)));
		print(String.format("%12s: %s (%s)", "Nym", nymId, getNymName(nymId)));
		print(String.format("%12s: %s (%s)", "Server", serverId,
				getServerName(serverId)));

		print(Util.repeat("-", 13));
	}

	/**********************************************************************
	 * internal
	 *********************************************************************/

	private static String getWalletFileName(String walletId) {
		return String.format("%s/%s.%s", ApplicationProperties.getUserDataPath(),
				walletId, Extension.DEFINITION.getValue());
	}

	private static void loadWallet(String walletId) throws OTException {
		String fileName = String.format("%s.%s", walletId,
				Extension.DEFINITION.getValue());
		if (!OTAPI.setWallet(fileName))
			error("Failed to set wallet");
		if (!OTAPI.loadWallet())
			error("Failed to load wallet");
	}

	private static void applyVerbose() {
		if (verbose) {
			verboseServer = false;
			verboseClientLog = true;
			verboseClientSkip = true;
			verboseClientWarn = true;
			verboseClientSuccess = true;
		} else {
			verboseServer = ApplicationProperties.get().getBoolean("verbose.server");
			verboseClientLog = ApplicationProperties.get().getBoolean(
					"verbose.client.log");
			verboseClientSkip = ApplicationProperties.get().getBoolean(
					"verbose.client.skip");
			verboseClientWarn = ApplicationProperties.get().getBoolean(
					"verbose.client.warn");
			verboseClientSuccess = ApplicationProperties.get().getBoolean(
					"verbose.client.success");
		}
	}

	private static void showServers() {
		print(String.format("%12s:", "SERVERS"));
		int count = OTAPI.getServerCount();
		int i = 0;
		for (int index = 0; index < count; index++) {
			String serverId = OTAPI.GetServer.id(index);
			String serverName = getServerName(serverId);
			if (i == 0)
				print(Util.repeat("-", 13));
			print(String.format("%12d: %s (%s)", ++i, serverId, serverName));
		}
		if (i > 0)
			print(Util.repeat("-", 13));
	}

	private static void showNyms() {
		print(String.format("%12s:", "NYMS"));
		int count = OTAPI.getNymCount();
		int i = 0;
		for (int index = 0; index < count; index++) {
			String nymId = OTAPI.GetNym.id(index);
			String nymName = getNymName(nymId);
			if (i == 0)
				print(Util.repeat("-", 13));
			print(String.format("%12d: %s (%s)", ++i, nymId, nymName));
		}
		if (i > 0)
			print(Util.repeat("-", 13));
	}

	private static void showAssets() {
		print(String.format("%12s:", "ASSETS"));
		int count = OTAPI.getAssetCount();
		int i = 0;
		for (int index = 0; index < count; index++) {
			String assetId = OTAPI.GetAsset.id(index);
			String assetName = getAssetName(assetId);
			if (i == 0)
				print(Util.repeat("-", 13));
			print(String.format("%12d: %s (%s)", ++i, assetId, assetName));
		}
		if (i > 0)
			print(Util.repeat("-", 13));
	}

	private static Boolean isMeantForParsing(String id) {
		if (id == null)
			return null;
		int min = ApplicationProperties.get().getInteger("parsing.size.min");
		int len = id.length();
		if (len < min)
			return null;
		int max = ApplicationProperties.get().getInteger("parsing.size.max");
		return (len >= min && len <= max);
	}

	protected static String parseServerId(String serverId) throws OTException {
		Boolean parsing = isMeantForParsing(serverId);
		if (parsing == null)
			error(Event.PARSE_SERVER_ID_ERROR);
		if (parsing) {
			String id = OTAPI.Wallet.getServerIdFromPartial(serverId);
			if (!Util.isValidString(id))
				error(Event.PARSE_SERVER_ID_ERROR);
			serverId = id;
		}
		logServerId(serverId);
		return serverId;
	}

	protected static String parseNymId(String nymId) throws OTException {
		Boolean parsing = isMeantForParsing(nymId);
		if (parsing == null)
			error(Event.PARSE_NYM_ID_ERROR);
		if (parsing) {
			String id = OTAPI.Wallet.getNymIdFromPartial(nymId);
			if (!Util.isValidString(id))
				error(Event.PARSE_NYM_ID_ERROR);
			nymId = id;
		}
		logNymId(nymId);
		return nymId;
	}

	protected static String parseAssetId(String assetId) throws OTException {
		Boolean parsing = isMeantForParsing(assetId);
		if (parsing == null)
			error(Event.PARSE_ASSET_ID_ERROR);
		if (parsing) {
			String id = OTAPI.Wallet.getAssetIdFromPartial(assetId);
			if (!Util.isValidString(id))
				error(Event.PARSE_ASSET_ID_ERROR);
			assetId = id;
		}
		logAssetId(assetId);
		return assetId;
	}

	protected static String parseAccountId(String accountId) throws OTException {
		Boolean parsing = isMeantForParsing(accountId);
		if (parsing == null)
			error(Event.PARSE_ACCOUNT_ID_ERROR);
		if (parsing) {
			String id = OTAPI.Wallet.getAccountIdFromPartial(accountId);
			if (!Util.isValidString(id))
				error(Event.PARSE_ACCOUNT_ID_ERROR);
			accountId = id;
		}
		logAccountId(accountId);
		return accountId;
	}

	protected static void logServerId(String serverId) {
		log(String.format("%12s: %s", Text.SERVER_ID, serverId));
	}

	protected static void logNymId(String nymId) {
		log(String.format("%12s: %s", Text.NYM_ID, nymId));
	}

	protected static void logAssetId(String assetId) {
		log(String.format("%12s: %s", Text.ASSET_ID, assetId));
	}

	protected static void logAccountId(String accountId) {
		log(String.format("%12s: %s", Text.ACCOUNT_ID, accountId));
	}

	public static void print(Object s) {
		System.out.println(s);
	}

	protected static void info(String message) {
		print(String.format("%s: %s", Text.INFO, message));
	}

	protected static void publish(Object s) {
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(s);
		System.out.println();
		System.out.println();
		System.out.println();
	}

	protected static void log(String message) {
		if (verboseClientLog)
			print(String.format("%s: %s", Text.LOG, message));
	}

	protected static void skip(String message) {
		if (verboseClientSkip)
			print(String.format("%s: %s", Text.SKIP, message));
	}

	protected static void skip(Text text) {
		skip(text.toString());
	}

	protected static void warn(String message) {
		if (verboseClientWarn)
			print(String.format("%s: %s", Text.WARN, message));
	}

	protected static void warn(String message, int result) {
		if (verboseClientWarn)
			print(String.format("%s: %s (%d)", Text.WARN, message, result));
	}

	protected static void warn(Text text) {
		warn(text.toString());
	}

	protected static void warn(Event event) {
		warn(event.toString());
	}

	protected static void warn(Text text, int result) {
		warn(text.toString(), result);
	}

	protected static void warn(Event event, int result) {
		warn(event.toString(), result);
	}

	protected static void success(String message) {
		if (verboseClientSuccess)
			print(String.format("%s: %s", Text.SUCCESS, message));
	}

	protected static void success(String message, int result) {
		if (verboseClientSuccess)
			print(String.format("%s: %s (%d)", Text.SUCCESS, message, result));
	}

	protected static void success(Text text) {
		success(text.toString());
	}

	protected static void success(Text text, int result) {
		success(text.toString(), result);
	}

	protected static void attempt(String message) {
		log(String.format("%s..", message));
	}

	protected static void attempt(Text text) {
		attempt(text.toString());
	}

	public static void error(OTSystemException e) throws OTSystemException {
		throw e;
	}

	public static void error(String message) throws OTSystemException {
		throw new OTSystemException(message);
	}

	public static void error(String message, int result)
			throws OTSystemException {
		throw new OTSystemException(message, result);
	}

	public static void error(Event event) throws OTSystemException {
		throw new OTSystemException(event);
	}

	public static void error(Event event, int result) throws OTSystemException {
		throw new OTSystemException(event, result);
	}

	public static void error(Text text) throws OTUserException {
		throw new OTUserException(text);
	}

	protected static Integer getPurseBalanceValue(String serverId,
			String assetId, String purse) throws OTException {
		return convertAmountToValue(getPurseBalance(serverId, assetId, purse));
	}

	protected static String getAccountBalance(String accountId) {
		return OTAPI.GetAccount.balance(accountId);
	}

	private static String getPurseBalance(String serverId, String assetId,
			String purse) {
		return OTAPI.Purse.getBalance(serverId, assetId, purse);
	}

	private static String getAccountBalanceFormatted(String accountId) {
		return convertAmountToFormat(getAccountAssetId(accountId),
				getAccountBalance(accountId));
	}

	private static String getPurseBalanceFormatted(String serverId,
			String assetId, String purse) {
		return convertAmountToFormat(assetId,
				getPurseBalance(serverId, assetId, purse));
	}

}
