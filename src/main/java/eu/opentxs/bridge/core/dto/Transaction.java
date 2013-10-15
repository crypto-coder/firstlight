package eu.opentxs.bridge.core.dto;

import java.util.Collections;
import java.util.List;

import eu.opentxs.bridge.CustomUTC;
import eu.opentxs.bridge.Util;
import eu.opentxs.bridge.core.exceptions.OTException;
import eu.opentxs.bridge.core.modules.Module;
import eu.opentxs.bridge.core.modules.OTAPI;
import eu.opentxs.bridge.core.modules.act.ContactModule;

public class Transaction implements Comparable<Transaction> {

	public enum TransactionType {
		PENDING("pending"), REPLY_NOTICE("replyNotice"), INSTRUMENT_NOTICE("instrumentNotice"), TRANSFER_RECEIPT("transferReceipt"), 
		VOUCHER_RECEIPT("voucherReceipt"), CHEQUE_RECEIPT("chequeReceipt"), INVOICE_RECEIPT("invoiceReceipt");
		private String value;
		private TransactionType(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
		public static TransactionType parse(String value) {
			if (value.equalsIgnoreCase(PENDING.getValue()))
				return PENDING;
			if (value.equalsIgnoreCase(REPLY_NOTICE.getValue()))
				return REPLY_NOTICE;
			if (value.equalsIgnoreCase(INSTRUMENT_NOTICE.getValue()))
				return INSTRUMENT_NOTICE;
			if (value.equalsIgnoreCase(TRANSFER_RECEIPT.getValue()))
				return TRANSFER_RECEIPT;
			if (value.equalsIgnoreCase(VOUCHER_RECEIPT.getValue()))
				return VOUCHER_RECEIPT;
			if (value.equalsIgnoreCase(CHEQUE_RECEIPT.getValue()))
				return CHEQUE_RECEIPT;
			if (value.equalsIgnoreCase(INVOICE_RECEIPT.getValue()))
				return INVOICE_RECEIPT;
			return null;
		}
	}

	public enum InstrumentType {
		TRANSFER(null, "transfer"), CHEQUE("cheque", "cheque"), VOUCHER("voucher", "voucher"), INVOICE("invoice", "invoice"), CASH("purse", "cash");
		private String value;
		private String label;
		private InstrumentType(String value, String label) {
			this.value = value;
			this.label = label;
		}
		public String getValue() {
			return value;
		}
		public static InstrumentType parse(String value) {
			if (value.equalsIgnoreCase(CHEQUE.getValue()))
				return CHEQUE;
			if (value.equalsIgnoreCase(VOUCHER.getValue()))
				return VOUCHER;
			if (value.equalsIgnoreCase(INVOICE.getValue()))
				return INVOICE;
			if (value.equalsIgnoreCase(CASH.getValue()))
				return CASH;
			return null;
		}
		@Override
		public String toString() {
			return label;
		}
	}

	public enum Side {
		SENT("sent"), RECEIVED("received");
		private String label;
		private Side(String label) {
			this.label = label;
		}
		@Override
		public String toString() {
			return label;
		}
	}

	private enum Status {
		NULL(""), CONFIRMED("ok");
		private String label;
		private Status(String label) {
			this.label = label;
		}
		@Override
		public String toString() {
			return label;
		}
	}

	private Transaction(String refNum, String dateSigned, String senderNymId, String senderAccountId, String recipientNymId,
			String recipientAccountId, String hisNymId, String trxnNum, String instrument, InstrumentType instrumentType, TransactionType trxnType,
			Side side, Status status, String assetId, Integer value, String note, String validFrom, String validTo) {
		super();
		this.refNum = refNum;
		this.dateSigned = dateSigned;
		this.senderNymId = senderNymId;
		this.senderAccountId = senderAccountId;
		this.recipientNymId = recipientNymId;
		this.recipientAccountId = recipientAccountId;
		this.hisNymId = hisNymId;
		this.trxnNum = trxnNum;
		this.instrument = instrument;
		this.instrumentType = instrumentType;
		this.trxnType = trxnType;
		this.side = side;
		this.status = status;
		this.assetId = assetId;
		this.value = value;
		this.note = note;
		this.validFrom = validFrom;
		this.validTo = validTo;
	}
	@SuppressWarnings("unused")
	private String refNum;
	private String dateSigned;

	private String senderNymId;
	private String senderAccountId;
	private String recipientNymId;
	private String recipientAccountId;
	private String hisNymId;

	@SuppressWarnings("unused")
	private String trxnNum;
	private String instrument;
	private InstrumentType instrumentType;
	private TransactionType trxnType;
	private Side side;
	private Status status;

	private String assetId;
	private Integer value;
	private String note;
	private String validFrom;
	private String validTo;

	@Override
	public int compareTo(Transaction o) {
		if (o.dateSigned != null && dateSigned != null)
			return o.dateSigned.compareTo(dateSigned);
		return 0;
	}

	public String getInstrument() {
		return instrument;
	}

	public InstrumentType getInstrumentType() {
		return instrumentType;
	}
	
	public TransactionType getTrxnType() {
		return trxnType;
	}

	public Side getSide() {
		return side;
	}
	
	public static Transaction getTransactionForOutpayments(String serverId, String nymId, String assetId, int index, InstrumentType[] instrumentTypes) throws OTException {
		if (!OTAPI.GetNym.outpaymentsServerIdByIndex(nymId, index).equals(serverId))
			return null;
		String instrument = OTAPI.GetNym.outpaymentsContentsByIndex(nymId, index);
		if (!Util.isValidString(instrument))
			Module.error("instrument is empty");
		InstrumentType instrumentType = Module.getInstrumentType(instrument);
		if (!verifyInstrumentType(instrumentTypes, instrumentType))
			return null;
		if (assetId != null && !OTAPI.Instrument.getAssetId(instrument).equals(assetId))
			return null;
		if (!OTAPI.Nym.verifyOutpaymentsByIndex(nymId, index))
			Module.error("outpayment failed to verify");
		String refNum = null;
		String dateSigned = new Integer(Module.getTime().getSeconds()).toString();// /how to get dateSigned???

		String senderNymId = nymId;
		String senderAccountId = null;
		String recipientNymId = OTAPI.GetNym.outpaymentsRecipientNymIdByIndex(nymId, index);
		String recipientAccountId = null;
		String hisNymId = getHisNymId(nymId, senderNymId, recipientNymId);

		String trxnNum = OTAPI.Instrument.getTransactionNum(instrument);
		TransactionType trxnType = null;
		Side side = Side.SENT;
		Status status = Status.NULL;
		Integer value = 0 - Module.convertAmountToValue(OTAPI.Instrument.getAmount(instrument));
		String note = OTAPI.Instrument.getNote(instrument);
		String validFrom = OTAPI.Instrument.getValidFrom(instrument);
		String validTo = OTAPI.Instrument.getValidTo(instrument);

		return new Transaction(refNum, dateSigned, senderNymId, senderAccountId, recipientNymId, recipientAccountId, hisNymId, trxnNum,
				instrument, instrumentType, trxnType, side, status, assetId, value, note, validFrom, validTo);
	}
	
	public static Transaction getTransactionForNym(String serverId, String nymId, String assetId, String ledger, int index) throws OTException {
		String instrument = OTAPI.Ledger.getInstrumentbyIndex(serverId, nymId, nymId, ledger, index);
		if (!Util.isValidString(instrument))
			Module.error("instrument is empty");
		if (assetId != null && !OTAPI.Instrument.getAssetId(instrument).equals(assetId))
			return null;

		String transaction = OTAPI.Ledger.getTransactionByIndex(serverId, nymId, nymId, ledger, index);
		if (!Util.isValidString(transaction))
			Module.error("transaction is empty");

		String refNum = OTAPI.Transaction.getDisplayReferenceToNum(serverId, nymId, nymId, transaction);
		String dateSigned = OTAPI.Transaction.getDateSigned(serverId, nymId, nymId, transaction);

		String senderNymId = OTAPI.Transaction.getSenderNymId(serverId, nymId, nymId, transaction);
		String senderAccountId = null;
		String recipientNymId = OTAPI.Transaction.getRecipientNymId(serverId, nymId, nymId, transaction);
		String recipientAccountId = null;
		String hisNymId = getHisNymId(nymId, senderNymId, recipientNymId);

		String trxnNum = OTAPI.Instrument.getTransactionNum(instrument);
		InstrumentType instrumentType = Module.getInstrumentType(instrument);
		TransactionType trxnType = TransactionType.parse(OTAPI.Transaction.getType(serverId, nymId, nymId, transaction));
		Side side;
		if (trxnType.equals(TransactionType.INSTRUMENT_NOTICE))
			side = Side.RECEIVED;
		else
			side = null;
		Status status = Status.NULL;
		Integer value = Module.convertAmountToValue(OTAPI.Instrument.getAmount(instrument));
		String note = OTAPI.Instrument.getNote(instrument);
		String validFrom = OTAPI.Instrument.getValidFrom(instrument);
		String validTo = OTAPI.Instrument.getValidTo(instrument);

		return new Transaction(refNum, dateSigned, senderNymId, senderAccountId, recipientNymId, recipientAccountId, hisNymId, trxnNum,
				instrument, instrumentType, trxnType, side, status, assetId, value, note, validFrom, validTo);
	}
	
	public static Transaction getTransactionForAccount(String serverId, String nymId, String accountId, String ledger, int index) throws OTException {
		String transaction = OTAPI.Ledger.getTransactionByIndex(serverId, nymId, accountId, ledger, index);
		if (!Util.isValidString(transaction))
			Module.error("transaction is empty");

		String refNum = OTAPI.Transaction.getDisplayReferenceToNum(serverId, nymId, accountId, transaction);
		String dateSigned = OTAPI.Transaction.getDateSigned(serverId, nymId, accountId, transaction);
		String senderNymId = OTAPI.Transaction.getSenderNymId(serverId, nymId, accountId, transaction);
		String senderAccountId = OTAPI.Transaction.getSenderAccountId(serverId, nymId, accountId, transaction);
		String recipientNymId = OTAPI.Transaction.getRecipientNymId(serverId, nymId, accountId, transaction);
		String recipientAccountId = OTAPI.Transaction.getRecipientAccountId(serverId, nymId, accountId, transaction);

		String hisNymId = getHisNymId(nymId, senderNymId, recipientNymId);
		if (!Util.isValidString(hisNymId))
			hisNymId = getHisNymId(getHisAccountId(accountId, senderAccountId, recipientAccountId));

		String trxnNum = null;
		TransactionType trxnType = TransactionType.parse(OTAPI.Transaction.getType(serverId, nymId, accountId, transaction));
		String instrument = null;
		InstrumentType instrumentType;
		Side side;
		Status status;
		String note;
		if (trxnType.equals(TransactionType.PENDING)) {
			instrumentType = InstrumentType.TRANSFER;
			side = Side.RECEIVED;
			status = Status.NULL;
			note = OTAPI.Pending.getNote(serverId, nymId, accountId, transaction);
		} else if (trxnType.equals(TransactionType.TRANSFER_RECEIPT)) {
			instrumentType = InstrumentType.TRANSFER;
			side = Side.SENT;
			status = Status.CONFIRMED;
			note = null;// /how to get the note??
		} else if (trxnType.equals(TransactionType.VOUCHER_RECEIPT)) {
			instrumentType = InstrumentType.VOUCHER;
			side = Side.SENT;
			status = Status.CONFIRMED;
			note = null;// /how to get the note??
		} else if (trxnType.equals(TransactionType.CHEQUE_RECEIPT)) {
			instrumentType = InstrumentType.CHEQUE;
			side = Side.SENT;
			status = Status.CONFIRMED;
			note = null;// /how to get the note??
		} else {
			instrumentType = null;
			side = null;
			status = null;
			note = null;
		}

		String assetId = Module.getAccountAssetId(accountId);
		Integer value = Module.convertAmountToValue(OTAPI.Transaction.getAmount(serverId, nymId, accountId, transaction));
		String validFrom = null;
		String validTo = null;

		return new Transaction(refNum, dateSigned, senderNymId, senderAccountId, recipientNymId, recipientAccountId, hisNymId, trxnNum,
				instrument, instrumentType, trxnType, side, status, assetId, value, note, validFrom, validTo);
	}
	
	public static Transaction getTransactionForOutbox(String serverId, String nymId, String accountId, String ledger, int index) throws OTException {
		String transaction = OTAPI.Ledger.getTransactionByIndex(serverId, nymId, accountId, ledger, index);
		if (!Util.isValidString(transaction))
			Module.error("transaction is empty");

		String refNum = OTAPI.Transaction.getDisplayReferenceToNum(serverId, nymId, accountId, transaction);
		String dateSigned = OTAPI.Transaction.getDateSigned(serverId, nymId, accountId, transaction);
		String senderNymId = OTAPI.Transaction.getSenderNymId(serverId, nymId, accountId, transaction);
		String senderAccountId = OTAPI.Transaction.getSenderAccountId(serverId, nymId, accountId, transaction);
		String recipientNymId = OTAPI.Transaction.getRecipientNymId(serverId, nymId, accountId, transaction);
		String recipientAccountId = OTAPI.Transaction.getRecipientAccountId(serverId, nymId, accountId, transaction);

		String hisNymId = getHisNymId(nymId, senderNymId, recipientNymId);
		if (!Util.isValidString(hisNymId))
			hisNymId = getHisNymId(getHisAccountId(accountId, senderAccountId, recipientAccountId));

		String trxnNum = null;
		TransactionType trxnType = TransactionType.parse(OTAPI.Transaction.getType(serverId, nymId, accountId, transaction));
		String instrument = null;
		InstrumentType instrumentType;
		Side side;
		Status status;
		if (trxnType.equals(TransactionType.PENDING)) {
			instrumentType = InstrumentType.TRANSFER;
			side = Side.SENT;
			status = Status.NULL;
		} else {
			instrumentType = null;
			side = null;
			status = null;
		}

		String assetId = Module.getAccountAssetId(accountId);
		Integer value = 0 - Module.convertAmountToValue(OTAPI.Transaction.getAmount(serverId, nymId, accountId, transaction));
		String note = OTAPI.Pending.getNote(serverId, nymId, accountId, transaction);
		String validFrom = null;
		String validTo = null;

		return new Transaction(refNum, dateSigned, senderNymId, senderAccountId, recipientNymId, recipientAccountId, hisNymId, trxnNum,
				instrument, instrumentType, trxnType, side, status, assetId, value, note, validFrom, validTo);
	}

	public static void showH(List<Transaction> list) {
		Collections.sort(list);
		for (Transaction transaction : list) {
			Module.print(String.format(
					"%18s : %-8s : %-8s : %9s : %2s : %-15s %s",
					// %5s | %5s |
					// transaction.refNum, transaction.trxnNum,
					CustomUTC.timeToString(transaction.dateSigned), transaction.instrumentType, transaction.side, 
					Module.convertValueToFormat(transaction.assetId, transaction.value), transaction.status,
					"Commented Out" /*Util.crop(ContactModule.getContactName(transaction.hisNymId), 15) */,
					(Util.isValidString(transaction.note) ? String.format(": %s", transaction.note) : "")));
		}
	}

	public static void present(List<Transaction> list) {
		Collections.sort(list);
		int i = 0;
		for (Transaction transaction : list) {
			Module.print(String.format("%3d: %18s | %-8s | %-8s | %9s | %2s | %s", ++i, 
					CustomUTC.timeToString(transaction.dateSigned), transaction.instrumentType, transaction.side,
					Module.convertValueToFormat(transaction.assetId, transaction.value), transaction.status,
					"Commented Out" /*Util.crop(ContactModule.getContactName(transaction.hisNymId), 15)*/ ));
		}
	}

	public static void showV(List<Transaction> list) {
		Collections.sort(list);
		for (Transaction transaction : list)
			transaction.print();
	}

	public void print() {
		Module.print(Util.repeat("-", 13));
		Module.print(String.format("%12s: %s", "InstrType", instrumentType));
		Module.print(String.format("%12s: %s", "Date", CustomUTC.timeToString(dateSigned)));
		Module.print(String.format("%12s: %s", "Volume", Module.convertValueToFormat(assetId, Math.abs(value))));
		// Module.print(String.format("%12s: %s", "RefNum", refNum));
		// Module.print(String.format("%12s: %s", "TrxnType", trxnType));
		// Module.print(String.format("%12s: %s", "TrxnNum", trxnNum));

		if (Util.isValidString(validFrom))
			Module.print(String.format("%12s: %s", "ValidFrom", CustomUTC.timeToString(validFrom)));
		else
			Module.print(String.format("%12s:", "ValidFrom"));

		if (Util.isValidString(validTo))
			Module.print(String.format("%12s: %s", "ValidTo", CustomUTC.timeToString(validTo)));
		else
			Module.print(String.format("%12s:", "ValidTo"));

//		if (Util.isValidString(senderNymId))
//			Module.print(String.format("%12s: %s (%s)", "NymFrom", senderNymId, ContactModule.getContactName(senderNymId)));
//		else
			Module.print(String.format("%12s:", "NymFrom"));

//		if (Util.isValidString(senderAccountId))
//			Module.print(String.format("%12s: %s (%s)", "AccountFrom", senderAccountId, ContactModule.getContactAccountName(senderAccountId)));
//		else
			Module.print(String.format("%12s:", "AccountFrom"));

//		if (Util.isValidString(recipientNymId))
//			Module.print(String.format("%12s: %s (%s)", "NymTo", recipientNymId, ContactModule.getContactName(recipientNymId)));
//		else
			Module.print(String.format("%12s:", "NymTo"));

//		if (Util.isValidString(recipientAccountId))
//			Module.print(String.format("%12s: %s (%s)", "AccountTo", recipientAccountId, ContactModule.getContactAccountName(recipientAccountId)));
//		else
			Module.print(String.format("%12s:", "AccountTo"));

		Module.print(String.format("%12s: %s", "Note", note));
		Module.print(Util.repeat("-", 13));
	}

	private static boolean verifyInstrumentType(InstrumentType[] instrumentTypes, InstrumentType instrumentType) {
		if (instrumentTypes == null)
			return true;
		for (InstrumentType t : instrumentTypes)
			if (instrumentType.equals(t))
				return true;
		return false;
	}

	private static String getHisNymId(String nymId, String senderNymId, String recipientNymId) {
		String hisNymId = null;
		if ((nymId.equals(senderNymId) || !Util.isValidString(senderNymId)) && !nymId.equals(recipientNymId))
			hisNymId = recipientNymId;
		else if ((nymId.equals(recipientNymId) || !Util.isValidString(recipientNymId)) && !nymId.equals(senderNymId))
			hisNymId = senderNymId;
		else if (nymId.equals(senderNymId) && nymId.equals(recipientNymId))
			hisNymId = nymId;
		return hisNymId;
	}

	private static String getHisAccountId(String accountId, String senderAccountId, String recipientAccountId) {
		String hisAccountId = null;
		if ((accountId.equals(senderAccountId) || !Util.isValidString(senderAccountId)) && !accountId.equals(recipientAccountId))
			hisAccountId = recipientAccountId;
		else if ((accountId.equals(recipientAccountId) || !Util.isValidString(recipientAccountId)) && !accountId.equals(senderAccountId))
			hisAccountId = senderAccountId;
		return hisAccountId;
	}

	private static String getHisNymId(String accountId) {
		if (!Util.isValidString(accountId))
			return null;
//		ContactAccount contactAccount = ContactAccount.get(accountId);
//		if (contactAccount != null)
//			return contactAccount.getNymId();
		String nymId = Module.getAccountNymId(accountId);
		if (Util.isValidString(nymId))
			return nymId;
		return null;
	}
}
