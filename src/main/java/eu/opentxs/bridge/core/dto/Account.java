package eu.opentxs.bridge.core.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.opentxs.bridge.Util;
import eu.opentxs.bridge.core.modules.Module;
import eu.opentxs.bridge.core.modules.OTAPI;

public class Account implements Comparable<Account> {
	public String id;
	public String name;
	public String serverId;
	private Account(String id, String name, String serverId) {
		super();
		this.id = id;
		this.name = name;
		this.serverId = serverId;
	}
	@Override
	public int compareTo(Account o) {
		if (o.serverId.equals(serverId)) {
			if (o.name.equals(name)) {
				return o.id.compareTo(id);
			}
			return o.name.compareTo(name);
		}
		return o.serverId.compareTo(serverId);
	}
	public static List<Account> getList() {
		int count = OTAPI.getAccountCount();
		List<Account> list = new ArrayList<Account>();
		for (int index = 0; index < count; index++) {
			String accountId = OTAPI.GetAccount.id(index);
			Account account = new Account(accountId, Module.getAccountName(accountId), Module.getAccountServerId(accountId));
			list.add(account);
		}
		Collections.sort(list);
		return list;
	}
	public static void show() {
		int serverCount = OTAPI.getServerCount();
		List<Account> accounts = getList();
		String serverId = null;
		int i = 0;
		for (Account account : accounts) {
			if (serverId == null || !serverId.equals(account.serverId)) {
				if (serverId != null)
					Module.print(Util.repeat("-", 13));
				serverId = account.serverId;
				if (serverCount == 1)
					Module.print(String.format("%12s:", "ACCOUNTS"));
				else
					Module.print(String.format("%12s: (%s)", "ACCOUNTS", Module.getServerName(serverId)));
				Module.print(Util.repeat("-", 13));
			}
			Module.print(String.format("%12d: %s (%s)", ++i, account.id, account.name));
		}
		if (i > 0)
			Module.print(Util.repeat("-", 13));
	}
	public static void present() {
		int serverCount = OTAPI.getServerCount();
		List<Account> accounts = getList();
		String serverId = null;
		int i = 0;
		for (Account account : accounts) {
			if (serverId == null || !serverId.equals(account.serverId)) {
				serverId = account.serverId;
				if (serverCount > 1)
					Module.print(String.format("(%3s)", Module.getServerName(serverId)));
			}
			Module.print(String.format("%3d: %s (%s)", ++i, account.id, account.name));
		}
	}
}
