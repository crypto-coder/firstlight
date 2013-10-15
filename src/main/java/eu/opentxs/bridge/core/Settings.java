package eu.opentxs.bridge.core;

import eu.PropertiesBase;

public class Settings extends PropertiesBase {

	private static final long serialVersionUID = 1L;

	public static Settings getInstance() {
		if (instance == null) {
			instance = new Settings();
			instance.setReadOnly(false);
			instance.init();
		}
		return instance;
	}

	public static Settings instance;

	public static final String sophisticationKey = "sophistication";
	public static final String walletIdKey = "walletId";
	public static final String myServerIdKey = "myServerId";
	public static final String myNymIdKey = "myNymId";
	public static final String myAssetIdKey = "myAssetId";
	public static final String myAccountIdKey = "myAccountId";
	public static final String hisNymIdKey = "hisNymId";

	private int sophistication = -1;
	private String walletId = "wallet";
	private String myServerId = null;
	private String myNymId = null;
	private String myAssetId = null;
	private String myAccountId = null;
	private String hisNymId = null;

	public Settings() {
		super();
	}

	public int getSophistication() {
		return getInteger(sophisticationKey, sophistication);
	}

	public void setSophistication(int sophistication) {
		setInteger(sophisticationKey, sophistication);
	}

	public String getWalletId() {
		return getString(walletIdKey, walletId);
	}

	public void setWalletId(String walletId) {
		setString(walletIdKey, walletId);
	}

	public String getMyServerId() {
		return getString(myServerIdKey, myServerId);
	}

	public void setMyServerId(String myServerId) {
		setString(myServerIdKey, myServerId);
	}

	public String getMyNymId() {
		return getString(myNymIdKey, myNymId);
	}

	public void setMyNymId(String myNymId) {
		setString(myNymIdKey, myNymId);
	}

	public String getMyAssetId() {
		return getString(myAssetIdKey, myAssetId);
	}

	public void setMyAssetId(String myAssetId) {
		setString(myAssetIdKey, myAssetId);
	}

	public String getMyAccountId() {
		return getString(myAccountIdKey, myAccountId);
	}

	public void setMyAccountId(String myAccountId) {
		setString(myAccountIdKey, myAccountId);
	}

	public String getHisNymId() {
		return getString(hisNymIdKey, hisNymId);
	}

	public void setHisNymId(String hisNymId) {
		setString(hisNymIdKey, hisNymId);
	}
}
