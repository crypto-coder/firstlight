package eu.opentxs.bridge.core.modules;

import org.opentransactions.otapi.Storable;
import org.opentransactions.otapi.otapi;
import org.opentransactions.otapi.otapiJNI;

public class OTAPI {

	public enum Box {
		NYMBOX(0), INBOX(1), OUTBOX(2);
		private int index;
		private Box(int index) {
			this.index = index;
		}
		public int getIndex() {
			return index;
		}
	}

	public static class Message {
		public static String getLedger(String message) {
			return otapiJNI.OTAPI_Basic_Message_GetLedger(message);
		}
		public static String getPayload(String message) {
			return otapiJNI.OTAPI_Basic_Message_GetPayload(message);
		}
		public static int getSuccess(String message) {
			return otapiJNI.OTAPI_Basic_Message_GetSuccess(message);
		}
		public static int getTransactionSuccess(String serverId, String nymId, String accountId, String message) {
			return otapiJNI.OTAPI_Basic_Message_GetTransactionSuccess(serverId, nymId, accountId, message);
		}
		public static int getBalanceAgreementSuccess(String serverId, String nymId, String accountId, String message) {
			return otapiJNI.OTAPI_Basic_Message_GetBalanceAgreementSuccess(serverId, nymId, accountId, message);
		}
		public static String getNewIssuerAccountId(String message) {
			return otapiJNI.OTAPI_Basic_Message_GetNewIssuerAcctID(message);
		}
		public static String getNewAssetId(String message) {
			return otapiJNI.OTAPI_Basic_Message_GetNewAssetTypeID(message);
		}
		public static String getNewAccountId(String message) {
			return otapiJNI.OTAPI_Basic_Message_GetNewAcctID(message);
		}
	}

	public static class Wallet {
		public static String exportNym(String nymId) {
			return otapiJNI.OTAPI_Basic_Wallet_ExportNym(nymId);
		}
		public static String importNym(String content) {
			return otapiJNI.OTAPI_Basic_Wallet_ImportNym(content);
		}
		public static String getServerIdFromPartial(String id) {
			return otapiJNI.OTAPI_Basic_Wallet_GetServerIDFromPartial(id);
		}
		public static String getNymIdFromPartial(String id) {
			return otapiJNI.OTAPI_Basic_Wallet_GetNymIDFromPartial(id);
		}
		public static String getAssetIdFromPartial(String id) {
			return otapiJNI.OTAPI_Basic_Wallet_GetAssetIDFromPartial(id);
		}
		public static String getAccountIdFromPartial(String id) {
			return otapiJNI.OTAPI_Basic_Wallet_GetAccountIDFromPartial(id);
		}
		public static boolean canDeleteServer(String serverId) {
			return otapiJNI.OTAPI_Basic_Wallet_CanRemoveServer(serverId);
		}
		public static boolean deleteServer(String serverId) {
			return otapiJNI.OTAPI_Basic_Wallet_RemoveServer(serverId);
		}
		// not working as expected
		public static boolean canDeleteNym(String nymId) {
			return otapiJNI.OTAPI_Basic_Wallet_CanRemoveNym(nymId);
		}
		public static boolean deleteNym(String nymId) {
			return otapiJNI.OTAPI_Basic_Wallet_RemoveNym(nymId);
		}
		public static boolean canDeleteAsset(String assetId) {
			return otapiJNI.OTAPI_Basic_Wallet_CanRemoveAssetType(assetId);
		}
		public static boolean deleteAsset(String assetId) {
			return otapiJNI.OTAPI_Basic_Wallet_RemoveAssetType(assetId);
		}
		public static boolean canDeleteAccount(String accountId) {
			return otapiJNI.OTAPI_Basic_Wallet_CanRemoveAccount(accountId);
		}
		public static boolean importPurse(String serverId, String nymId, String assetId, String purse) {
			return otapiJNI.OTAPI_Basic_Wallet_ImportPurse(serverId, assetId, nymId, purse);
		}
	}

	public static class GetServer {
		public static String id(int index) {
			return otapiJNI.OTAPI_Basic_GetServer_ID(index);
		}
		public static String name(String serverId) {
			return otapiJNI.OTAPI_Basic_GetServer_Name(serverId);
		}
		public static String contract(String serverId) {
			return otapiJNI.OTAPI_Basic_GetServer_Contract(serverId);
		}
	}

	public static class SetServer {
		public static boolean name(String serverId, String serverName) {
			return otapiJNI.OTAPI_Basic_SetServer_Name(serverId, serverName);
		}
	}

	public static class GetNym {
		public static String id(int index) {
			return otapiJNI.OTAPI_Basic_GetNym_ID(index);
		}
		public static String name(String nymId) {
			return otapiJNI.OTAPI_Basic_GetNym_Name(nymId);
		}
		public static int transactionNumCount(String serverId, String nymId) {
			return otapiJNI.OTAPI_Basic_GetNym_TransactionNumCount(serverId, nymId);
		}
		public static int outpaymentsCount(String nymId) {
			return otapiJNI.OTAPI_Basic_GetNym_OutpaymentsCount(nymId);
		}
		public static String outpaymentsContentsByIndex(String nymId, int index) {
			return otapiJNI.OTAPI_Basic_GetNym_OutpaymentsContentsByIndex(nymId, index);
		}
		public static String outpaymentsRecipientNymIdByIndex(String nymId, int index) {
			return otapiJNI.OTAPI_Basic_GetNym_OutpaymentsRecipientIDByIndex(nymId, index);
		}
		public static String outpaymentsServerIdByIndex(String nymId, int index) {
			return otapiJNI.OTAPI_Basic_GetNym_OutpaymentsServerIDByIndex(nymId, index);
		}
		public static String inboxHashLocal(String nymId, String accountId) {
			return otapiJNI.OTAPI_Basic_GetNym_InboxHash(accountId, nymId);
		}
		public static String outboxHashLocal(String nymId, String accountId) {
			return otapiJNI.OTAPI_Basic_GetNym_OutboxHash(accountId, nymId);
		}
		public static String nymboxHashCached(String serverId, String nymId) {
			return otapiJNI.OTAPI_Basic_GetNym_RecentHash(serverId, nymId);
		}
		public static String nymboxHashLocal(String serverId, String nymId) {
			return otapiJNI.OTAPI_Basic_GetNym_NymboxHash(serverId, nymId);
		}
	}

	public static class SetNym {
		public static boolean name(String nymId, String nymName) {
			return otapiJNI.OTAPI_Basic_SetNym_Name(nymId, nymId, nymName);
		}
	}

	public static class GetAsset {
		public static String id(int index) {
			return otapiJNI.OTAPI_Basic_GetAssetType_ID(index);
		}
		public static String name(String assetId) {
			return otapiJNI.OTAPI_Basic_GetAssetType_Name(assetId);
		}
		public static String contract(String assetId) {
			return otapiJNI.OTAPI_Basic_GetAssetType_Contract(assetId);
		}
	}

	public static class SetAsset {
		public static boolean name(String assetId, String assetName) {
			return otapiJNI.OTAPI_Basic_SetAssetType_Name(assetId, assetName);
		}
	}

	public static class GetAccount {
		public static String id(int index) {
			return otapiJNI.OTAPI_Basic_GetAccountWallet_ID(index);
		}
		public static String name(String accountId) {
			return otapiJNI.OTAPI_Basic_GetAccountWallet_Name(accountId);
		}
		public static String nymId(String accountId) {
			return otapiJNI.OTAPI_Basic_GetAccountWallet_NymID(accountId);
		}
		public static String serverId(String accountId) {
			return otapiJNI.OTAPI_Basic_GetAccountWallet_ServerID(accountId);
		}
		public static String assetId(String accountId) {
			return otapiJNI.OTAPI_Basic_GetAccountWallet_AssetTypeID(accountId);
		}
		public static String type(String accountId) {
			return otapiJNI.OTAPI_Basic_GetAccountWallet_Type(accountId);
		}
		public static String balance(String accountId) {
			return otapiJNI.OTAPI_Basic_GetAccountWallet_Balance(accountId);
		}
		public static String inboxHashCached(String accountId) {
			return otapiJNI.OTAPI_Basic_GetAccountWallet_InboxHash(accountId);
		}
		public static String outboxHashCached(String accountId) {
			return otapiJNI.OTAPI_Basic_GetAccountWallet_OutboxHash(accountId);
		}
	}

	public static class SetAccount {
		// why is nymId needed?
		public static boolean name(String nymId, String accountId, String accountName) {
			return otapiJNI.OTAPI_Basic_SetAccountWallet_Name(accountId, nymId, accountName);
		}
	}
	
	public static class Ledger {
		public static int getCount(String serverId, String nymId, String accountId, String ledger) {
			return otapiJNI.OTAPI_Basic_Ledger_GetCount(serverId, nymId, accountId, ledger);
		}
		public static String getInstrumentbyIndex(String serverId, String nymId, String accountId, String ledger, int index) {
			return otapiJNI.OTAPI_Basic_Ledger_GetInstrument(serverId, nymId, accountId, ledger, index);
		}
		public static String getTransactionIdByIndex(String serverId, String nymId, String accountId, String ledger, int index) {
			return otapiJNI.OTAPI_Basic_Ledger_GetTransactionIDByIndex(serverId, nymId, accountId, ledger, index);
		}
		public static String getTransactionById(String serverId, String nymId, String accountId, String ledger, String transactionId) {
			return otapiJNI.OTAPI_Basic_Ledger_GetTransactionByID(serverId, nymId, accountId, ledger, transactionId);
		}
		public static String getTransactionByIndex(String serverId, String nymId, String accountId, String ledger, int index) {
			return otapiJNI.OTAPI_Basic_Ledger_GetTransactionByIndex(serverId, nymId, accountId, ledger, index);
		}
		public static String createResponse(String serverId, String nymId, String accountId, String originalLedger) {
			return otapiJNI.OTAPI_Basic_Ledger_CreateResponse(serverId, nymId, accountId, originalLedger);
		}
		public static String finalizeResponse(String serverId, String nymId, String accountId, String ledger) {
			return otapiJNI.OTAPI_Basic_Ledger_FinalizeResponse(serverId, nymId, accountId, ledger);
		}
	}

	public static class Transaction {
		public static String createResponse(String serverId, String nymId, String accountId, String responseLedger, String originalTransaction, boolean accept) {
			return otapiJNI.OTAPI_Basic_Transaction_CreateResponse(serverId, nymId, accountId, responseLedger, originalTransaction, accept);
		}
		public static String getType(String serverId, String nymId, String accountId, String transaction) {
			return otapiJNI.OTAPI_Basic_Transaction_GetType(serverId, nymId, accountId, transaction);
		}
		public static String getDisplayReferenceToNum(String serverId, String nymId, String accountId, String transaction) {
			return otapiJNI.OTAPI_Basic_Transaction_GetDisplayReferenceToNum(serverId, nymId, accountId, transaction);
		}
		public static String getAmount(String serverId, String nymId, String accountId, String transaction) {
			return otapiJNI.OTAPI_Basic_Transaction_GetAmount(serverId, nymId, accountId, transaction);
		}
		public static String getSenderNymId(String serverId, String nymId, String accountId, String transaction) {
			return otapiJNI.OTAPI_Basic_Transaction_GetSenderUserID(serverId, nymId, accountId, transaction);
		}
		public static String getSenderAccountId(String serverId, String nymId, String accountId, String transaction) {
			return otapiJNI.OTAPI_Basic_Transaction_GetSenderAcctID(serverId, nymId, accountId, transaction);
		}
		public static String getRecipientNymId(String serverId, String nymId, String accountId, String transaction) {
			return otapiJNI.OTAPI_Basic_Transaction_GetRecipientUserID(serverId, nymId, accountId, transaction);
		}
		public static String getRecipientAccountId(String serverId, String nymId, String accountId, String transaction) {
			return otapiJNI.OTAPI_Basic_Transaction_GetRecipientAcctID(serverId, nymId, accountId, transaction);
		}
		public static String getDateSigned(String serverId, String nymId, String accountId, String transaction) {
			return otapiJNI.OTAPI_Basic_Transaction_GetDateSigned(serverId, nymId, accountId, transaction);
		}
		public static String getVoucher(String serverId, String nymId, String accountId, String transaction) {
			return otapiJNI.OTAPI_Basic_Transaction_GetVoucher(serverId, nymId, accountId, transaction);
		}
	}

	public static class Pending {
		public static String getNote(String serverId, String nymId, String accountId, String transaction) {
			return otapiJNI.OTAPI_Basic_Pending_GetNote(serverId, nymId, accountId, transaction);
		}
	}

	public static class Instrument {
		public static String getAmount(String instrument) {
			return otapiJNI.OTAPI_Basic_Instrmnt_GetAmount(instrument);
		}
		public static String getTransactionNum(String instrument) {
			return otapiJNI.OTAPI_Basic_Instrmnt_GetTransNum(instrument);
		}
		public static String getValidFrom(String instrument) {
			return otapiJNI.OTAPI_Basic_Instrmnt_GetValidFrom(instrument);
		}
		public static String getValidTo(String instrument) {
			return otapiJNI.OTAPI_Basic_Instrmnt_GetValidTo(instrument);
		}
		public static String getNote(String instrument) {
			return otapiJNI.OTAPI_Basic_Instrmnt_GetMemo(instrument);
		}
		public static String getType(String instrument) {
			return otapiJNI.OTAPI_Basic_Instrmnt_GetType(instrument);
		}
		public static String getServerId(String instrument) {
			return otapiJNI.OTAPI_Basic_Instrmnt_GetServerID(instrument);
		}
		public static String getAssetId(String instrument) {
			return otapiJNI.OTAPI_Basic_Instrmnt_GetAssetID(instrument);
		}
		public static String getSenderNymId(String instrument) {
			return otapiJNI.OTAPI_Basic_Instrmnt_GetSenderUserID(instrument);
		}
		public static String getSenderAccountId(String instrument) {
			return otapiJNI.OTAPI_Basic_Instrmnt_GetSenderAcctID(instrument);
		}
		public static String getRecipientNymId(String instrument) {
			return otapiJNI.OTAPI_Basic_Instrmnt_GetRecipientUserID(instrument);
		}
		public static String getRecipientAccountId(String instrument) {
			return otapiJNI.OTAPI_Basic_Instrmnt_GetRecipientAcctID(instrument);
		}
		public static String getRemitterNymId(String instrument) {
			return otapiJNI.OTAPI_Basic_Instrmnt_GetRemitterUserID(instrument);
		}
		public static String getRemitterAccountId(String instrument) {
			return otapiJNI.OTAPI_Basic_Instrmnt_GetRemitterAcctID(instrument);
		}
	}

	public static class ReplyNotice {
		public static String getRequestId(String serverId, String nymId, String transaction) {
			return otapiJNI.OTAPI_Basic_ReplyNotice_GetRequestNum(serverId, nymId, transaction);
		}
	}

	public static class Mint {
		public static boolean isStillGood(String serverId, String assetId) {
			return otapiJNI.OTAPI_Basic_Mint_IsStillGood(serverId, assetId);
		}
	}

	public static class Nym {
		public static boolean verifyOutpaymentsByIndex(String nymId, int index) {
			return otapiJNI.OTAPI_Basic_Nym_VerifyOutpaymentsByIndex(nymId, index);
		}
		public static boolean removeOutpaymentsByIndex(String nymId, int index) {
			return otapiJNI.OTAPI_Basic_Nym_RemoveOutpaymentsByIndex(nymId, index);
		}
	}

	public static class Purse {
		public static String getBalance(String serverId, String assetId, String purse) {
			return otapiJNI.OTAPI_Basic_Purse_GetTotalValue(serverId, assetId, purse);
		}
		public static int getSize(String serverId, String assetId, String purse) {
			return otapiJNI.OTAPI_Basic_Purse_Count(serverId, assetId, purse);
		}
		public static String peek(String serverId, String nymId, String assetId, String purse) {
			return otapiJNI.OTAPI_Basic_Purse_Peek(serverId, assetId, nymId, purse);
		}
		public static String pop(String serverId, String nymId, String assetId, String purse) {
			return otapiJNI.OTAPI_Basic_Purse_Pop(serverId, assetId, nymId, purse);
		}
		public static String push(String serverId, String nymId, String assetId, String purse, String token, String hisNymId) {
			return otapiJNI.OTAPI_Basic_Purse_Push(serverId, assetId, nymId, hisNymId, purse, token);
		}
		public static boolean hasPassword(String serverId, String purse) {
			return otapiJNI.OTAPI_Basic_Purse_HasPassword(serverId, purse);
		}
	}

	public static class Token {
		public static String getDenomination(String serverId, String assetId, String token) {
			return otapiJNI.OTAPI_Basic_Token_GetDenomination(serverId, assetId, token);
		}
		public static int getSeries(String serverId, String assetId, String token) {
			return otapiJNI.OTAPI_Basic_Token_GetSeries(serverId, assetId, token);
		}
		public static String getValidFrom(String serverId, String assetId, String token) {
			return otapiJNI.OTAPI_Basic_Token_GetValidFrom(serverId, assetId, token);
		}
		public static String getValidTo(String serverId, String assetId, String token) {
			return otapiJNI.OTAPI_Basic_Token_GetValidTo(serverId, assetId, token);
		}
		public static String changeOwner(String serverId, String nymId, String assetId, String token, String hisNymId) {
			return otapiJNI.OTAPI_Basic_Token_ChangeOwner(serverId, assetId, token, nymId, nymId, hisNymId);
		}
		public static String getId(String serverId, String assetId, String token) {
			return otapiJNI.OTAPI_Basic_Token_GetID(serverId, assetId, token);
		}
	}

	public static boolean init() {
		return otapiJNI.OTAPI_Basic_Init();
	}
	public static boolean appStartup() {
		return otapiJNI.OTAPI_Basic_AppStartup();
	}
	public static boolean appShutdown() {
		return otapiJNI.OTAPI_Basic_AppShutdown();
	}

	public static boolean walletExists() {
		return otapiJNI.OTAPI_Basic_WalletExists();
	}
	public static boolean loadWallet() {
		return otapiJNI.OTAPI_Basic_LoadWallet();
	}
	public static boolean setWallet(String fileName) {
		return otapiJNI.OTAPI_Basic_SetWallet(fileName);
	}
	public static boolean switchWallet() {
		return otapiJNI.OTAPI_Basic_SwitchWallet();
	}

	public static String getTime() {
		return otapiJNI.OTAPI_Basic_GetTime();
	}

	public static String encode(String s, boolean lineBreaks) {
		return otapiJNI.OTAPI_Basic_Encode(s, lineBreaks);
	}
	public static String decode(String encoded, boolean lineBreaks) {
		return otapiJNI.OTAPI_Basic_Decode(encoded, lineBreaks);
	}
	
	/** "545" becomes "$5.45" */
	public static String formatAmount(String assetId, String amount) {
		return otapiJNI.OTAPI_Basic_FormatAmount(assetId, amount);
	}
	/** "$5.45" becomes "545" */
	public static String unformatAmount(String assetId, String amount) {
		return otapiJNI.OTAPI_Basic_StringToAmount(assetId, amount);
	}
	
	public static void flushMessageBuffer() {
		otapiJNI.OTAPI_Basic_FlushMessageBuffer();
	}
	public static String popMessageBuffer(String requestId, String serverId, String nymId) {
		return otapiJNI.OTAPI_Basic_PopMessageBuffer(requestId, serverId, nymId);
	}

	public static int synchronizeRequestNumber(String serverId, String nymId) {
		return otapiJNI.OTAPI_Basic_getRequest(serverId, nymId);
	}
	public static boolean verifyAccountReceipt(String serverId, String nymId, String accountId) {
		return otapiJNI.OTAPI_Basic_VerifyAccountReceipt(serverId, nymId, accountId);
	}

	public static int getNymCount() {
		return otapiJNI.OTAPI_Basic_GetNymCount();
	}
	public static int getServerCount() {
		return otapiJNI.OTAPI_Basic_GetServerCount();
	}
	public static int getAssetCount() {
		return otapiJNI.OTAPI_Basic_GetAssetTypeCount();
	}
	public static int getAccountCount() {
		return otapiJNI.OTAPI_Basic_GetAccountCount();
	}
	
	public static String createServer(String nymId, String definition) {
		return otapiJNI.OTAPI_Basic_CreateServerContract(nymId, definition);
	}
	public static int addServer(String contract) {
		return otapiJNI.OTAPI_Basic_AddServerContract(contract);
	}
	
	public static int pingServer(String serverId, String nymId) {
		return otapiJNI.OTAPI_Basic_checkServerID(serverId, nymId);
	}

	/** For emergency/testing use only - this call forces you to trust the server */
	public static boolean resyncNymWithServer(String serverId, String nymId, String message) {
		return otapiJNI.OTAPI_Basic_ResyncNymWithServer(serverId, nymId, message);
	}

	public static String createNym(int keySize, String nymIdSource, String altLocation) {
		return otapiJNI.OTAPI_Basic_CreateNym(keySize, nymIdSource, altLocation);
	}
	
	public static boolean isNymRegisteredAtServer(String serverId, String nymId) {
		return otapiJNI.OTAPI_Basic_IsNym_RegisteredAtServer(nymId, serverId);
	}
	public static int registerNymAtServer(String serverId, String nymId) {
		return otapiJNI.OTAPI_Basic_createUserAccount(serverId, nymId);
	}
	public static int removeNymFromServer(String serverId, String nymId) {
		return otapiJNI.OTAPI_Basic_deleteUserAccount(serverId, nymId);
	}

	public static String createAsset(String nymId, String definition) {
		return otapiJNI.OTAPI_Basic_CreateAssetContract(nymId, definition);
	}

	public static int issueAsset(String serverId, String nymId, String contract) {
		return otapiJNI.OTAPI_Basic_issueAssetType(serverId, nymId, contract);
	}
	
	// why is it not returning assetId?
	public static int addAsset(String contract) {
		return otapiJNI.OTAPI_Basic_AddAssetContract(contract);
	}

	// decoding does not work on server side
	public static int queryAssetTypes(String serverId, String nymId, String encodedMap) {
		return otapiJNI.OTAPI_Basic_queryAssetTypes(serverId, nymId, encodedMap);
	}

	// why is it not returning accountId?
	public static int createAccount(String serverId, String nymId, String assetId) {
		return otapiJNI.OTAPI_Basic_createAssetAccount(serverId, nymId, assetId);
	}
	
	// why is nymId needed?
	public static int deleteAccountFromServer(String serverId, String nymId, String accountId) {
		return otapiJNI.OTAPI_Basic_deleteAssetAccount(serverId, nymId, accountId);
	}

	/** Send a message to the server asking it to send you the latest copy of any of your asset account */
	public static int synchronizeAccount(String serverId, String nymId, String accountId) {
		return otapiJNI.OTAPI_Basic_getAccount(serverId, nymId, accountId);
	}

	public static int getInbox(String serverId, String nymId, String accountId) {
		return otapiJNI.OTAPI_Basic_getInbox(serverId, nymId, accountId);
	}
	public static int getOutbox(String serverId, String nymId, String accountId) {
		return otapiJNI.OTAPI_Basic_getOutbox(serverId, nymId, accountId);
	}
	public static int getNymbox(String serverId, String nymId) {
		return otapiJNI.OTAPI_Basic_getNymbox(serverId, nymId);
	}
	public static String loadRecordbox(String serverId, String nymId, String accountId) {
		return otapiJNI.OTAPI_Basic_LoadRecordBox(serverId, nymId, accountId);
	}
	public static String loadPayInbox(String serverId, String nymId) {
		return otapiJNI.OTAPI_Basic_LoadPaymentInbox(serverId, nymId);
	}

	public static String loadInboxNoVerify(String serverId, String nymId, String accountId) {
		return otapiJNI.OTAPI_Basic_LoadInboxNoVerify(serverId, nymId, accountId);
	}
	public static String loadOutboxNoVerify(String serverId, String nymId, String accountId) {
		return otapiJNI.OTAPI_Basic_LoadOutboxNoVerify(serverId, nymId, accountId);
	}
	public static String loadNymboxNoVerify(String serverId, String nymId) {
		return otapiJNI.OTAPI_Basic_LoadNymboxNoVerify(serverId, nymId);
	}
	public static String loadRecordboxNoVerify(String serverId, String nymId, String accountId) {
		return otapiJNI.OTAPI_Basic_LoadRecordBoxNoVerify(serverId, nymId, accountId);
	}
	public static String loadPayInboxNoVerify(String serverId, String nymId) {
		return otapiJNI.OTAPI_Basic_LoadPaymentInboxNoVerify(serverId, nymId);
	}

	public static boolean verifySignature(String nymId, String contract) {
		return otapiJNI.OTAPI_Basic_VerifySignature(nymId, contract);
	}

	public static boolean haveAlreadySeenReply(String serverId, String nymId, String requestId) {
		return otapiJNI.OTAPI_Basic_HaveAlreadySeenReply(serverId, nymId, requestId);
	}
	public static boolean doesBoxReceiptExist(String serverId, String nymId, String accountId, Box box, String transactionId) {
		return otapiJNI.OTAPI_Basic_DoesBoxReceiptExist(serverId, nymId, accountId, box.getIndex(), transactionId);
	}
	public static int getBoxReceipt(String serverId, String nymId, String accountId, Box box, String transactionId) {
		return otapiJNI.OTAPI_Basic_getBoxReceipt(serverId, nymId, accountId, box.getIndex(), transactionId);
	}
	
	public static int getTransactionNumbers(String serverId, String nymId) {
		return otapiJNI.OTAPI_Basic_getTransactionNumber(serverId, nymId);
	}

	public static String loadInbox(String serverId, String nymId, String accountId) {
		return otapiJNI.OTAPI_Basic_LoadInbox(serverId, nymId, accountId);
	}
	public static String loadOutbox(String serverId, String nymId, String accountId) {
		return otapiJNI.OTAPI_Basic_LoadOutbox(serverId, nymId, accountId);
	}
	public static String loadNymbox(String serverId, String nymId) {
		return otapiJNI.OTAPI_Basic_LoadNymbox(serverId, nymId);
	}

	public static int processInbox(String serverId, String nymId, String accountId, String ledger) {
		return otapiJNI.OTAPI_Basic_processInbox(serverId, nymId, accountId, ledger);
	}

	public static int processNymbox(String serverId, String nymId) {
		return otapiJNI.OTAPI_Basic_processNymbox(serverId, nymId);
	}

	public static int getMint(String serverId, String nymId, String assetId) {
		return otapiJNI.OTAPI_Basic_getMint(serverId, nymId, assetId);
	}
	public static String loadMint(String serverId, String assetId) {
		return otapiJNI.OTAPI_Basic_LoadMint(serverId, assetId);
	}

	public static String loadPurse(String serverId, String nymId, String assetId) {
		return otapiJNI.OTAPI_Basic_LoadPurse(serverId, assetId, nymId);
	}
	public static String createPurse(String serverId, String nymId, String assetId, String ownerNymId) {
		return otapiJNI.OTAPI_Basic_CreatePurse(serverId, assetId, ownerNymId, nymId);
	}
	public static String createPurseWithPassphrase(String serverId, String nymId, String assetId) {
		return otapiJNI.OTAPI_Basic_CreatePurse_Passphrase(serverId, assetId, nymId);
	}
	public static boolean savePurse(String serverId, String nymId, String assetId, String purse) {
		return otapiJNI.OTAPI_Basic_SavePurse(serverId, assetId, nymId, purse);
	}
	
	public static int notarizeTransfer(String serverId, String nymId, String accountId, String amount, String hisAccountId, String note) {
		return otapiJNI.OTAPI_Basic_notarizeTransfer(serverId, nymId, accountId, hisAccountId, amount, note);
	}
	public static int notarizeWithdrawal(String serverId, String nymId, String accountId, String amount) {
		return otapiJNI.OTAPI_Basic_notarizeWithdrawal(serverId, nymId, accountId, amount);
	}
	public static int notarizeDeposit(String serverId, String nymId, String accountId, String purse) {
		return otapiJNI.OTAPI_Basic_notarizeDeposit(serverId, nymId, accountId, purse);
	}

	public static int writeVoucher(String serverId, String nymId, String accountId, String amount, String hisNymId, String note) {
		return otapiJNI.OTAPI_Basic_withdrawVoucher(serverId, nymId, accountId, hisNymId, note, amount);
	}
	public static String writeCheque(String serverId, String nymId, String accountId, String amount, String hisNymId, String note, String validFrom, String validTo) {
		return otapiJNI.OTAPI_Basic_WriteCheque(serverId, amount, validFrom, validTo, accountId, nymId, note, hisNymId);
	}
	public static boolean cancelCheque(String serverId, String nymId, String accountId, String cheque) {
		return otapiJNI.OTAPI_Basic_DiscardCheque(serverId, nymId, accountId, cheque);
	}
	public static int executeCheque(String serverId, String nymId, String accountId, String cheque) {
		return otapiJNI.OTAPI_Basic_depositCheque(serverId, nymId, accountId, cheque);
	}

	public static int sendUserPayment(String serverId, String nymId, String hisNymId, String hisPublicKey, String payment) {
		return otapiJNI.OTAPI_Basic_sendUserInstrument(serverId, nymId, hisNymId, hisPublicKey, payment, "");
	}

	public static boolean recordPayment(String serverId, String nymId, boolean isInbox, int index, boolean saveCopy) {
		return otapiJNI.OTAPI_Basic_RecordPayment(serverId, nymId, isInbox, index, saveCopy);
	}

	public static boolean clearRecord(String serverId, String nymId, String accountId) {
		return otapiJNI.OTAPI_Basic_ClearRecord(serverId, nymId, accountId, 0, true);
	}
	public static boolean clearRecord(String serverId, String nymId, String accountId, int index) {
		return otapiJNI.OTAPI_Basic_ClearRecord(serverId, nymId, accountId, index, false);
	}

	public static int checkHisNymId(String serverId, String nymId, String hisNymId) {
		return otapiJNI.OTAPI_Basic_checkUser(serverId, nymId, hisNymId);
	}
	public static String loadUserPublicKeyEncryption(String nymId) {
		return otapiJNI.OTAPI_Basic_LoadUserPubkey_Encryption(nymId);
	}
	public static String loadPublicKeyEncryption(String nymId) {
		return otapiJNI.OTAPI_Basic_LoadPubkey_Encryption(nymId);
	}

	public static Storable createObject(int objectType) {
		return otapi.CreateObject(objectType);
	}
	public static String encodeObject(Storable object) {
		return otapi.EncodeObject(object);
	}
	public static Storable decodeObject(int objectType, String s) {
		return otapi.DecodeObject(objectType, s);
	}
	public static boolean exists(String folder, String first, String second) {
		return otapiJNI.Exists__SWIG_1(folder, first, second);
	}

}
