package eu.opentxs.bridge.core.dto;

import java.io.Serializable;
import java.util.List;

//import org.hibernate.Session;

public class ContactAccount implements Serializable {

	private static final long serialVersionUID = 1L;

	private String accountId;
	private String assetId;
	private String nymId;
	private String serverId;

	public String getAccountId() {
		return accountId;
	}
	public String getAssetId() {
		return assetId;
	}
	public String getNymId() {
		return nymId;
	}
	public String getServerId() {
		return serverId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public void setNymId(String nymId) {
		this.nymId = nymId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public static void main(String[] args) {
		{
//			add("aaa", "ggg", "xxx", "localhost");
//			add("aaa", "sss", "xxx", "localhost");
//			add("bbb", "sss", "xxx", "localhost");
//			add("ccc", "sss", "yyy", "localhost");
//			add("ddd", "sss", "zzz", "localhost");
//			delete("eee");
//			delete("ddd");
		}
		// {
		// ContactAccount contactAccount = get("xxx", "ggg");
		// System.out.println(contactAccount);
		// }
//		{
//			List<ContactAccount> contactAccounts = getList("xxx");
//			for (ContactAccount contactAccount : contactAccounts)
//				System.out.println(contactAccount);
//		}
		System.exit(0);
	}

//	public static ContactAccount add(String accountId, String assetId, String nymId, String serverId) {
//		ContactAccount contactAccount = null;
//		Session sess = null;
//		try {//fatal
//			sess = Contact.getDatabase().beginTransaction();
//			contactAccount = (ContactAccount) sess.get(ContactAccount.class, accountId);
//			if (contactAccount != null) {
//				contactAccount = null;
//			} else {
//				contactAccount = new ContactAccount();
//				contactAccount.accountId = accountId;
//				contactAccount.assetId = assetId;
//				contactAccount.nymId = nymId;
//				contactAccount.serverId = serverId;
//				sess.save(contactAccount);
//			}
//		} catch (Exception e) {
//			System.err.println(e);
//		} finally {
//			Contact.getDatabase().commitClose(sess);
//		}
//		return contactAccount;
//	}
//
//	public static boolean delete(String accountId) {
//		Session sess = null;
//		ContactAccount contactAccount = null;
//		try {//fatal
//			sess = Contact.getDatabase().beginTransaction();
//			contactAccount = (ContactAccount) sess.get(ContactAccount.class, accountId);
//			if (contactAccount != null)
//				sess.delete(contactAccount);
//		} catch (Exception e) {
//			System.err.println(e);
//		} finally {
//			Contact.getDatabase().commitClose(sess);
//		}
//		return (contactAccount != null);
//	}
//
//	public static ContactAccount get(String accountId) {
//		Session sess = null;
//		ContactAccount contactAccount = null;
//		try {//fatal
//			sess = Contact.getDatabase().beginTransaction();
//			contactAccount = (ContactAccount) sess.get(ContactAccount.class, accountId);
//		} catch (Exception e) {
//			System.err.println(e);
//		} finally {
//			Contact.getDatabase().commitClose(sess);
//		}
//		return contactAccount;
//	}
//
//	@SuppressWarnings("unchecked")
//	public static List<ContactAccount> getList() {
//		List<ContactAccount> contactAccounts = null;
//		Session sess = null;
//		try {//fatal
//			sess = Contact.getDatabase().beginTransaction();
//			String sql = "FROM ContactAccount";
//			contactAccounts = sess.createQuery(sql).list();
//		} catch (Exception e) {
//			System.err.println(e);
//		} finally {
//			Contact.getDatabase().commitClose(sess);
//		}
//		return contactAccounts;
//	}
//
//	@SuppressWarnings("unchecked")
//	public static List<ContactAccount> getList(String nymId) {
//		List<ContactAccount> contactAccounts = null;
//		Session sess = null;
//		try {//fatal
//			sess = Contact.getDatabase().beginTransaction();
//			String sql = "FROM ContactAccount";
//			sql += String.format(" WHERE nymId='%s'", nymId);
//			contactAccounts = sess.createQuery(sql).list();
//		} catch (Exception e) {
//			System.err.println(e);
//		} finally {
//			Contact.getDatabase().commitClose(sess);
//		}
//		return contactAccounts;
//	}
//
//	@SuppressWarnings("unchecked")
//	public static List<ContactAccount> getList(String serverId, String assetId) {
//		List<ContactAccount> contactAccounts = null;
//		Session sess = null;
//		try {//fatal
//			sess = Contact.getDatabase().beginTransaction();
//			String sql = "FROM ContactAccount";
//			sql += String.format(" WHERE serverId='%s'", serverId);
//			sql += String.format(" AND assetId='%s'", assetId);
//			contactAccounts = sess.createQuery(sql).list();
//		} catch (Exception e) {
//			System.err.println(e);
//		} finally {
//			Contact.getDatabase().commitClose(sess);
//		}
//		return contactAccounts;
//	}

	@Override
	public String toString() {
		return String.format("%s: %s %s", accountId, assetId, nymId);
	}
}