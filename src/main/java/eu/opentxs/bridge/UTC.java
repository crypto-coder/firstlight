package eu.opentxs.bridge;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class UTC extends GregorianCalendar {

	private static final long serialVersionUID = 1L;
	public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("UTC");
	public static final int SECOND_MILS = 1000;
	public static final int MINUTE_SECONDS = 60;
	public static final int HOUR_SECONDS = MINUTE_SECONDS * 60;
	public static final int DAY_SECONDS = HOUR_SECONDS * 24;
	public static final int WEEK_DAYS = 7;
	public static final int WEEK_SECONDS = DAY_SECONDS * WEEK_DAYS;
	public static final int YEAR_SECONDS = DAY_SECONDS * 365;
	
	public static void main(String[] args) {
	}
	
	public static String getTimestamp() {
		return String.format(timeFormat(1), new Date());
	}

	public static String dateToString(UTC date) {
		return String.format(dateFormat(1), date);
	}

	public static UTC getDateUTC(int seconds) {
		return getDateUTC(Long.valueOf(seconds) * SECOND_MILS);
	}
	
	public static UTC getDateUTC(long mils) {
		UTC date = new UTC();
		date.clear();
		date.setTimeInMillis(mils);
		return date;
	}

	public int getSeconds() {
		int seconds = Long.valueOf(getTimeInMillis() / SECOND_MILS).intValue();
		return seconds;
	}

	public static int getSeconds(UTC date) {
		int seconds = Long.valueOf(date.getTimeInMillis() / SECOND_MILS)
				.intValue();
		return seconds;
	}

	public static int getSeconds(String time) {
		UTC date = new UTC();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		sdf.setTimeZone(TIME_ZONE);
		try {
			date.setTime(sdf.parse(time));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return Long.valueOf(date.getTimeInMillis() / SECOND_MILS).intValue();
	}

	public static String timeFormat(Integer n) {
		String t = n.toString();
		return "%" + t + "$tY" + "." + "%" + t
				+ "$tm" + "." + "%" + t + "$td" + " " + "%"
				+ t + "$tH" + ":" + "%" + t + "$tM";
	}
	
	private static String dateFormat(Integer n) {
		String t = n.toString();
		return "%" + t + "$tY" + "." + "%" + t	+ "$tm" + "." + "%" + t + "$td";
	}

	public static String timeToString(UTC date) {
		return String.format(timeFormat(1), date);
	}

	public static String timeToString(int seconds) {
		return String.format(timeFormat(1), getDateUTC(seconds));
	}
	
	public static String timeToString(long mils) {
		return String.format(timeFormat(1), getDateUTC(mils));
	}

	public UTC() {
		super();
		setTimeZone(TIME_ZONE);
	}

	public UTC(int year, int month, int dayOfMonth) {
		super(year, month, dayOfMonth);
		setTimeZone(TIME_ZONE);
	}

	public UTC(int year, int month, int dayOfMonth, int hourOfDay,
			int minute) {
		super(year, month, dayOfMonth, hourOfDay, minute);
		setTimeZone(TIME_ZONE);
	}

	/**
	 * @param time
	 *            yyyy.MM.dd HH:mm
	 */
	public UTC(String time) {
		this();
		SimpleDateFormat sdf;
		sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		sdf.setTimeZone(TIME_ZONE);
		try {
			setTime(sdf.parse(time));
		} catch (ParseException e) {
			sdf = new SimpleDateFormat("yyyy.MM.dd");
			sdf.setTimeZone(TIME_ZONE);
			try {
				setTime(sdf.parse(time));
			} catch (ParseException ee) {
				throw new RuntimeException(ee);
			}
		}
	}

	@Override
	public String toString() {
		return String.format("%1$tY.%1$tm.%1$td %1$tH:%1$tM", this);
	}
}
