package eu.opentxs.bridge.core.commands.act;

import java.util.List;

import eu.opentxs.bridge.core.commands.Command;
import eu.opentxs.bridge.core.commands.Commands;
import eu.opentxs.bridge.core.dto.Contact;
import eu.opentxs.bridge.core.dto.ContactAccount;
import eu.opentxs.bridge.core.exceptions.OTException;
import eu.opentxs.bridge.core.modules.Module;
import eu.opentxs.bridge.core.modules.act.ContactModule;

public class ContactCommands extends Commands {

	public static void init() {
		addToCommands(new CreateContact(), Category.CONTACT, Sophistication.MINI);
		addToCommands(new EditContact(), Category.CONTACT, Sophistication.MINI);
		addToCommands(new DeleteContact(), Category.CONTACT, Sophistication.MINI);
		addToCommands(new CreateContactAccount(), Category.CONTACT, Sophistication.MINI);
		addToCommands(new DeleteContactAccount(), Category.CONTACT, Sophistication.MINI);
		addToCommands(new ShowContacts(), Category.CONTACT, Sophistication.MINI);
	}

	public static class ShowContacts extends Command {
		@Override
		protected void action(String[] args) throws OTException {
			execute();
		}
		public static void execute() throws OTException {
//			ContactModule.showContacts();
		}
	}

	public static class CreateContact extends Command {
		@Override
		protected void action(String[] args) throws OTException {
			String nymId = getString(0);
			String name = getString(1);
			execute(nymId, name);
		}
		public static void execute(String nymId, String name) throws OTException {
//			ContactModule.createContact(nymId, name);
		}
	}

	public static class DeleteContact extends Command {
		private List<Contact> contacts;
		@Override
		public void sanity() throws OTException {
//			contacts = Contact.getList();
//			if (contacts.size() == 0)
//				error("You have no contacts in your wallet");
		}
		@Override
		public boolean introduceArgument(int index) {
			return (new Presenter<Contact>() {
				@Override
				protected String id(Contact t) {
					return t.getNymId();
				}
				@Override
				protected String name(Contact t) {
					return t.getName();
				}
			}).show(contacts);
		}
		@Override
		public Validator getValidator(int index) {
			if (index == 0) {
				return getListValidator(contacts);
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String nymId = (new Extractor<Contact>() {
				@Override
				public String get(Contact contact) {
					return contact.getNymId();
				}
			}).eval(0, contacts);
			execute(nymId);
		}
		public static void execute(String nymId) throws OTException {
//			ContactModule.showContact(Contact.get(nymId));
//			if (readBooleanFromInput("Are you sure you want to delete this contact?"))
//				ContactModule.deleteContact(nymId);
		}
	}

	public static class EditContact extends Command {
		private List<Contact> contacts;
		@Override
		public void sanity() throws OTException {
//			contacts = Contact.getList();
//			if (contacts.size() == 0)
//				error("You have no contacts in your wallet");
		}
		@Override
		public boolean introduceArgument(int index) {
			if (index == 0) {
				return (new Presenter<Contact>() {
					@Override
					protected String id(Contact t) {
						return t.getNymId();
					}
					@Override
					protected String name(Contact t) {
						return t.getName();
					}
				}).show(contacts);
			}
			return true;
		}
		@Override
		public Validator getValidator(int index) {
			if (index == 0) {
				return getListValidator(contacts);
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String nymId = (new Extractor<Contact>() {
				@Override
				public String get(Contact contact) {
					return contact.getNymId();
				}
			}).eval(0, contacts);
			String name = getString(1);
			execute(nymId, name);
		}
		public static void execute(String nymId, String name) throws OTException {
//			ContactModule.updateContact(nymId, name);
		}
	}

	public static class CreateContactAccount extends Command {
		private List<Contact> contacts;
		private List<String> serverIds;
		private List<String> assetIds;
		@Override
		public void sanity() throws OTException {
			serverIds = Module.getServerIds();
			if (serverIds.size() == 0)
				error("You have no servers in your wallet");
//			contacts = Contact.getList();
//			if (contacts.size() == 0)
//				error("You have no contacts in your wallet");
			assetIds = Module.getAssetIds();
			if (assetIds.size() == 0)
				error("You have no assets in your wallet");
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
				return (new Presenter<Contact>() {
					@Override
					protected String id(Contact t) {
						return t.getNymId();
					}
					@Override
					protected String name(Contact t) {
						return t.getName();
					}
				}).show(contacts);
			}
			if (index == 2) {
				return (new PlainPresenter() {
					@Override
					protected String name(String s) {
						return Module.getAssetName(s);
					}
				}).show(assetIds);
			}
			if (index == 3) {
				return true;
			}
			return false;
		}
		@Override
		public Validator getValidator(int index) {
			if (index == 0) {
				return getListValidator(serverIds);
			} else if (index == 1) {
				return getListValidator(contacts);
			} else if (index == 2) {
				return getListValidator(assetIds);
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String serverId = new PlainExtractor().eval(0, serverIds);
			String nymId = (new Extractor<Contact>() {
				@Override
				public String get(Contact contact) {
					return contact.getNymId();
				}
			}).eval(1, contacts);
			String assetId = new PlainExtractor().eval(2, assetIds);
			String accountId = getString(3);
			execute(accountId, assetId, nymId, serverId);
		}
		public static void execute(String accountId, String assetId, String nymId, String serverId) throws OTException {
//			ContactModule.createContactAccount(accountId, assetId, nymId, serverId);
		}
	}

	public static class DeleteContactAccount extends Command {
		private List<ContactAccount> contactAccounts;
		@Override
		public void sanity() throws OTException {
//			contactAccounts = ContactAccount.getList();
//			if (contactAccounts.size() == 0)
//				error("You have no contacts in your wallet account");
		}
		@Override
		public boolean introduceArgument(int index) {
			return (new Presenter<ContactAccount>() {
				@Override
				protected String id(ContactAccount t) {
					return t.getAccountId();
				}
				@Override
				protected String name(ContactAccount t) {
					return "COMMENTED OUT"; //ContactModule.getContactAccountName(t);
				}
			}).show(contactAccounts);
		}
		@Override
		public Validator getValidator(int index) {
			if (index == 0) {
				return getListValidator(contactAccounts);
			}
			return null;
		}
		@Override
		protected void action(String[] args) throws OTException {
			String accountId = (new Extractor<ContactAccount>() {
				@Override
				public String get(ContactAccount contactAccount) {
					return contactAccount.getAccountId();
				}
			}).eval(0, contactAccounts);
			execute(accountId);
		}
		public static void execute(String accountId) throws OTException {
//			ContactModule.deleteContactAccount(accountId);
		}
	}
}
