package eu.opentxs.bridge.core.modules.act;

import java.util.List;

import eu.opentxs.bridge.Util;
import eu.opentxs.bridge.core.dto.Contact;
import eu.opentxs.bridge.core.dto.ContactAccount;
import eu.opentxs.bridge.core.dto.Transaction.InstrumentType;
import eu.opentxs.bridge.core.exceptions.OTException;
import eu.opentxs.bridge.core.modules.Module;
import eu.opentxs.bridge.core.modules.OTAPI;

public class ContactModule extends Module {

//	public static void createContact(String nymId, String name) throws OTException {
//		if (!Util.isValidString(name))
//			error("Name is empty");
//		Contact contact = Contact.add(nymId, name);
//		if (contact == null)
//			error("Contact already exists");
//		showContact(contact);
//		success("Contact successfully created");
//	}
//
//	public static void deleteContact(String nymId) throws OTException {
//		Contact contact = Contact.delete(nymId);
//		if (contact == null)
//			error("Contact does not exist");
//		List<ContactAccount> contactAccounts = getContactAccounts(contact);
//		for (ContactAccount contactAccount : contactAccounts)
//			ContactAccount.delete(contactAccount.getAccountId());
//		success("Contact is deleted");
//	}
//
//	public static void updateContact(String nymId, String name) throws OTException {
//		Contact contact = Contact.update(nymId, name);
//		if (contact == null)
//			error("Contact does not exist");
//		showContact(contact);
//		success("Contact successfully updated");
//	}
//
//	public static boolean verifyContact(String nymId) throws OTException {
//		return (Contact.get(nymId) != null);
//	}
//
//	public static boolean verifyContactAccount(String accountId) throws OTException {
//		return (ContactAccount.get(accountId) != null);
//	}
//
//	public static Contact getContact(String nymId) throws OTException {
//		Contact contact = Contact.get(nymId);
//		if (contact == null)
//			error("Contact does not exist");
//		return contact;
//	}
//
//	public static List<ContactAccount> getContactAccounts(Contact contact) {
//		return ContactAccount.getList(contact.getNymId());
//	}

//	public static void showContact(Contact contact) {
//		print(Util.repeat("-", 13));
//		print(String.format("%12s: %s", "Name", contact.getName()));
//		print(String.format("%12s: %s", "Nym", contact.getNymId()));
//		print(Util.repeat("-", 13));
//		List<ContactAccount> contactAccounts = getContactAccounts(contact);
//		if (contactAccounts.size() == 0) {
//			print(String.format("%12s: <%s>", "", "no accounts defined"));
//			print(Util.repeat("-", 13));
//			return;
//		}
//		for (ContactAccount contactAccount : contactAccounts) {
//			String assetId = contactAccount.getAssetId();
//			String serverId = contactAccount.getServerId();
//			print(String.format("%12s: %s", "Account", contactAccount.getAccountId()));
//			print(String.format("%12s: %s (%s)", "Asset", assetId, getAssetName(assetId)));
//			print(String.format("%12s: %s (%s)", "Server", serverId, getServerName(serverId)));
//			print(Util.repeat("-", 13));
//		}
//	}
//
//	public static void showContacts() throws OTException {
//		List<Contact> contacts = Contact.getList();
//		final int size = contacts.size();
//		if (size == 0) {
//			info("There are no contacts defined");
//			return;
//		}
//		for (Contact contact : contacts)
//			showContact(contact);
//	}
//
//	public static void createContactAccount(String accountId, String assetId, String nymId, String serverId)
//			throws OTException {
//		if (!Util.isValidString(nymId))
//			error("hisNymId is undefined");
//		Contact contact = getContact(nymId);
//		assetId = parseAssetId(assetId);
//		ContactAccount contactAccount = ContactAccount.add(accountId, assetId, nymId, serverId);
//		if (contactAccount == null)
//			error("Contact account already exists");
//		showContact(contact);
//		success("Contact account successfully created");
//	}
//
//	public static void deleteContactAccount(String accountId) throws OTException {
//		ContactAccount contactAccount = getContactAccount(accountId);
//		Contact contact = getContact(contactAccount.getNymId());
//		if (!ContactAccount.delete(accountId))
//			error("Contact account does not exist");
//		showContact(contact);
//		success("Contact account is deleted");
//	}
//
//	public static ContactAccount getContactAccount(String accountId) throws OTException {
//		ContactAccount contactAccount = ContactAccount.get(accountId);
//		if (contactAccount == null)
//			error("Contact account does not exist");
//		return contactAccount;
//	}
//
//	public static String getContactName(String nymId) {
//		Contact contact = Contact.get(nymId);
//		if (contact != null)
//			return contact.getName();
//		return getNymName(nymId);
//	}
//
//	public static String getContactAccountName(ContactAccount contactAccount) {
//		return String.format("%s's %s", Contact.get(contactAccount.getNymId()).getName(), 
//				getAssetName(contactAccount.getAssetId()));
//	}
//
//	public static String getContactAccountName(String accountId) {
//		ContactAccount contactAccount = ContactAccount.get(accountId);
//		if (contactAccount != null)
//			return getContactAccountName(contactAccount);
//		return getAccountName(accountId);
//	}
//	
//	public static String extractContactFromInstrument(String instrument) throws OTException {
//		InstrumentType instrumentType = getInstrumentType(instrument);
//		String nymId = null;
//		if (instrumentType.equals(InstrumentType.CHEQUE) 
//				|| instrumentType.equals(InstrumentType.INVOICE)) {
//			nymId = OTAPI.Instrument.getSenderNymId(instrument);
//			if (!Util.isValidString(nymId))
//				nymId = null;
//			else if (verifyContact(nymId)) {
//				String accountId = OTAPI.Instrument.getSenderAccountId(instrument);
//				if (Util.isValidString(accountId) && !verifyContactAccount(accountId)) {
//					String serverId = OTAPI.Instrument.getServerId(instrument);
//					String assetId = OTAPI.Instrument.getAssetId(instrument);
//					if (Util.isValidString(serverId) && Util.isValidString(assetId))
//						ContactAccount.add(accountId, assetId, nymId, serverId);
//				}
//				nymId = null;
//			}
//		} else if (instrumentType.equals(InstrumentType.VOUCHER)) {
//			nymId = OTAPI.Instrument.getRemitterNymId(instrument);
//			if (!Util.isValidString(nymId))
//				nymId = null;
//			else if (verifyContact(nymId)) {
//				String accountId = OTAPI.Instrument.getRemitterAccountId(instrument);
//				if (Util.isValidString(accountId) && !verifyContactAccount(accountId)) {
//					String serverId = OTAPI.Instrument.getServerId(instrument);
//					String assetId = OTAPI.Instrument.getAssetId(instrument);
//					if (Util.isValidString(serverId) && Util.isValidString(assetId))
//						ContactAccount.add(accountId, assetId, nymId, serverId);
//				}
//				nymId = null;
//			}
//		}
//		return nymId;
//	}
}
