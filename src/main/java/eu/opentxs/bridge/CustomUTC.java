package eu.opentxs.bridge;

public class CustomUTC extends eu.opentxs.bridge.UTC {

	private static final long serialVersionUID = 1L;

	private CustomUTC() {
		super();
	}

	public CustomUTC(String s) {
		super(s);
	}

	public static CustomUTC getDateUTC(String s) {
		if (!Util.isValidString(s))
			return null;
		Long seconds = new Long(s);
		if (seconds > 0) {
			CustomUTC utc = new CustomUTC();
			utc.clear();
			utc.setTimeInMillis(seconds * SECOND_MILS);
			return utc;
		}
		return null;
	}

	public static String timeToString(String s) {
		CustomUTC utc = getDateUTC(s);
		if (utc != null)
			return timeToString(utc);
		return "";
	}

	public boolean isBefore(CustomUTC utc) {
		return (getSeconds() < utc.getSeconds());
	}

	public boolean isAfter(CustomUTC utc) {
		return (getSeconds() > utc.getSeconds());
	}
}
