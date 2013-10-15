package eu.opentxs.bridge.core.commands;

import java.util.ArrayList;
import java.util.List;

import eu.ApplicationProperties;
import eu.opentxs.bridge.Text;
import eu.opentxs.bridge.Localizer;
import eu.opentxs.bridge.CustomUTC;
import eu.opentxs.bridge.Util;
import eu.opentxs.bridge.core.exceptions.OTException;
import eu.opentxs.bridge.core.exceptions.OTUserException;
import eu.opentxs.bridge.core.modules.Module;

public abstract class Command implements Comparable<Command> {

	public static abstract class Validator {
		private boolean canBeEmpty = false;
		public void setCanBeEmpty(boolean canBeEmpty) {
			this.canBeEmpty = canBeEmpty;
		}
		public boolean validate(String s) {
			if (!Util.isValidString(s) && canBeEmpty)
				return true;
			return false;
		}
		protected static boolean error(Object e) {
			System.err.println(e);
			return false;
		}
	}

	public static class IsId extends Validator {
		@Override
		public boolean validate(String s) {
			if (super.validate(s))
				return true;
			if (!isValidId(s))
				return error(Text.VALIDATION_MUST_BE_ID);
			return true;
		}
	}

	public static class IsIntegerList extends Validator {
		private Integer min = null;
		private Integer max = null;
		public void setMinMax(Integer min, Integer max) {
			this.min = min;
			this.max = max;
		}
		@Override
		public boolean validate(String s) {
			if (super.validate(s))
				return true;
			if (!Util.isValidString(s))
				return false;
			String[] split = s.split(Text.INTEGER_LIST_SEPARATOR.toString());
			List<Integer> list = new ArrayList<Integer>();
			try {
				for (String n : split)
					list.add(new Integer(n));
			} catch (Exception e) {
				return error(Text.VALIDATION_MUST_BE_INTEGER_LIST);
			}
			if (min != null && max != null) {
				for (Integer i : list) {
					if (i < min || i > max)
						return error(String.format("%s [%d,%d]", Text.VALIDATION_MUST_BE_INTEGER_LIST_RANGE, min, max));
				}
			}
			return true;
		}
	}

	public static class IsBoolean extends Validator {
		@Override
		public boolean validate(String s) {
			if (super.validate(s))
				return true;
			boolean ok = s.equalsIgnoreCase("true") || s.equalsIgnoreCase("t") || s.equalsIgnoreCase("false") || s.equalsIgnoreCase("f");
			if (!ok)
				return error(Text.VALIDATION_MUST_BE_BOOLEAN);
			return true;
		}
	}

	public static class IsUTC extends Validator {
		@Override
		public boolean validate(String s) {
			if (super.validate(s))
				return true;
			CustomUTC u = null;
			try {
				u = new CustomUTC(s);
			} catch (Exception e) {
				return error(Text.VALIDATION_MUST_BE_DATE);
			}
			if (u.isBefore(Module.getTime()))
				return error(Text.VALIDATION_MUST_BE_DATE_FUTURE);
			return true;
		}
	}

	public static abstract class IsNumber<T> extends Validator {
		protected T min = null;
		protected T max = null;
		public void setMinMax(T min, T max) {
			this.min = min;
			this.max = max;
		}
		protected abstract T cast(String s);
		protected abstract boolean range(T t);
		protected abstract boolean onCastError(String s);
		protected abstract boolean onRangeError();
		@Override
		public boolean validate(String s) {
			if (super.validate(s))
				return true;
			T t = null;
			try {
				t = cast(s);
			} catch (Exception e) {
				return onCastError(s);
			}
			if (min != null && max != null && !range(t))
				return onRangeError();
			return true;
		}
	}

	public static class IsInteger extends IsNumber<Integer> {
		@Override
		public Integer cast(String s) {
			return new Integer(s);
		}
		@Override
		public boolean range(Integer n) {
			return (n >= min && n <= max);
		}
		@Override
		protected boolean onCastError(String s) {
			return error(Text.VALIDATION_MUST_BE_INTEGER);
		}
		@Override
		protected boolean onRangeError() {
			return error(String.format("%s [%d,%d]", Text.VALIDATION_MUST_BE_INTEGER_RANGE, min, max));
		}
	}

	public static class IsIntegerOrId extends IsInteger {
		@Override
		protected boolean onCastError(String s) {
			if (isValidId(s))
				return true;
			return error(Text.VALIDATION_MUST_BE_INTEGER_OR_ID);
		}
	}

	public static class IsDouble extends IsNumber<Double> {
		@Override
		public Double cast(String s) {
			return new Double(s);
		}
		@Override
		public boolean range(Double n) {
			return (n >= min && n <= max);
		}
		@Override
		protected boolean onCastError(String s) {
			return error(Text.VALIDATION_MUST_BE_NUMBER);
		}
		@Override
		protected boolean onRangeError() {
			return error(String.format("%s [%f,%f]", Text.VALIDATION_MUST_BE_NUMBER_RANGE, min, max));
		}
	}

	protected static boolean isValidId(String s) {
		return (Util.isValidString(s) && s.length() == ApplicationProperties.get().getInteger("ids.size"));
	}

	protected static Validator getListValidator(List<?> list) {
		return getListValidator(list, false);
	}

	protected static Validator getListValidator(List<?> list, String alt) {
		return getListValidator(list, Util.isValidString(alt));
	}

	protected static Validator getListValidator(final List<?> list, final boolean canBeEmpty) {
		if (list == null || list.size() == 0)
			return new IsId() {
				{
					setCanBeEmpty(canBeEmpty);
				}
			};
		return new IsIntegerOrId() {
			{
				setCanBeEmpty(canBeEmpty);
				setMinMax(1, list.size());
			}
		};
	}

	protected class PlainExtractor extends Extractor<String> {
		public PlainExtractor() {
		}
		@Override
		public String get(String s) {
			return s;
		}
	}

	protected abstract class Extractor<T> {
		public abstract String get(T t);
		public String eval(int index, List<T> list) throws OTException {
			return eval(index, list, null);
		}
		public String eval(int index, List<T> list, String alt) throws OTException {
			String s = args[index];
			if (!isValidId(s)) {
				if (list.size() == 1) {
					return get(list.get(0));
				} else if (Util.isValidString(s) && list.size() > 1) {
					return get(list.get(getInteger(index) - 1));
				}
			} else if (Util.isValidString(s)) {
				return s;
			}
			return alt;
		}
	}

	protected static abstract class PlainPresenter extends Presenter<String> {
		@Override
		protected String id(String s) {
			return s;
		}
		@Override
		protected abstract String name(String s);
	}

	protected static abstract class Presenter<T> {
		protected abstract String id(T t);
		protected abstract String name(T t);
		public boolean show(List<T> list) {
			return show(list, false);
		}
		public boolean show(List<T> list, boolean canBeSingle) {
			if (!canBeSingle && list.size() == 1)
				return false;
			int i = 0;
			for (T t : list) {
				String name = name(t);
				if (name != null)
					print(String.format("%3d: %s (%s)", ++i, id(t), name));
				else
					print(String.format("%3d: %s", ++i, id(t)));
			}
			return true;
		}
	}

	protected static void print(Object s) {
		System.out.println(s);
	}

	public static final Localizer local = Localizer.get();

	private String[] args;
	public Integer index;
	public Commands.Category category;
	public Commands.Sophistication sophistication;

	protected void sanity() throws OTException {
	}

	public Boolean sanityCheck() {
		Boolean retval = Boolean.TRUE;
		try {
			sanity();
		} catch (OTException e) {
			System.err.println(e.getMessage());
			retval = Boolean.FALSE;
		}
		return retval;
	}

	public boolean introduceArgument(int index) {
		return true;
	}

	public Validator getValidator(int index) {
		return null;
	}

	protected abstract void action(String[] args) throws OTException;

	public Boolean actionResult(String[] args) {
		this.args = args;
		Boolean retval = Boolean.TRUE;
		try {
			action(args);
		} catch (OTException e) {
			System.err.println(e.getMessage());
			retval = Boolean.FALSE;
		}
		return retval;
	}

	@Override
	public int compareTo(Command o) {
		int compare = this.category.compareTo(o.category);
		if (compare != 0)
			return compare;
		return this.index.compareTo(o.index);
	}

	public String[] getArguments() {
		String arguments = getArgumentsLocal();
		if (arguments.equals(""))
			return null;
		return arguments.split(",");
	}

	public boolean validate(int index, String s) {
		Validator validator = getValidator(index);
		if (validator != null)
			return validator.validate(s);
		return true;
	}

	public String getArgumentsLocal() {
		String arguments = local.getString(getName() + ".arguments");
		if (arguments == null)
			return "";
		return arguments;
	}

	public String getHelpLocal() {
		return local.getString(getName() + ".help");
	}

	public String getName() {
		return getClass().getSimpleName();
	}

	public String getNameLocal() {
		return local.getString(getName());
	}

	public String getPseudoLocal() {
		return local.getString(getName() + ".pseudo");
	}

	protected Boolean getBoolean(int index) throws OTException {
		if (!Util.isValidString(args[index]))
			error(Text.EMPTY_ARGUMENT_ERROR, getArguments()[index]);
		if (args[index].equals("t") || args[index].equals("true") || args[index].equals("1"))
			return Boolean.TRUE;
		return Boolean.FALSE;
	}

	protected String getString(int index, boolean canBeEmpty) throws OTException {
		if (canBeEmpty)
			return new String(args[index]);
		return getString(index);
	}

	protected String getString(int index, String alt) throws OTException {
		return (!Util.isValidString(args[index]) ? alt : getString(index));
	}

	protected String getString(int index) throws OTException {
		if (!Util.isValidString(args[index]))
			error(Text.EMPTY_ARGUMENT_ERROR, getArguments()[index]);
		return new String(args[index]);
	}

	protected Integer getInteger(int index, Integer alt) throws OTException {
		return (!Util.isValidString(args[index]) ? alt : getInteger(index));
	}

	protected Integer getInteger(int index) throws OTException {
		if (!Util.isValidString(args[index]))
			error(Text.EMPTY_ARGUMENT_ERROR, getArguments()[index]);
		Integer i = null;
		try {
			i = new Integer(args[index]);
		} catch (Exception e) {
			error(Text.INTEGER_EXPECTED_ERROR, getArguments()[index]);
		}
		return i;
	}

	protected List<Integer> getIntegerList(int index, boolean canBeEmpty) throws OTException {
		if (canBeEmpty && !Util.isValidString(args[index]))
			return new ArrayList<Integer>();
		return getIntegerList(index);
	}

	protected List<Integer> getIntegerList(int index) throws OTException {
		if (!Util.isValidString(args[index]))
			error(Text.EMPTY_ARGUMENT_ERROR, getArguments()[index]);
		String[] split = args[index].split(Text.INTEGER_LIST_SEPARATOR.toString());
		List<Integer> list = new ArrayList<Integer>();
		try {
			for (String s : split) {
				list.add(new Integer(s) - 1);
			}
		} catch (Exception e) {
			error(Text.INTEGER_LIST_EXPECTED_ERROR, getArguments()[index]);
		}
		return list;
	}

	protected Float getFloat(int index) throws OTException {
		if (!Util.isValidString(args[index]))
			error(Text.EMPTY_ARGUMENT_ERROR, getArguments()[index]);
		Float f = null;
		try {
			f = new Float(args[index]);
		} catch (Exception e) {
			error(Text.FLOAT_EXPECTED_ERROR, getArguments()[index]);
		}
		return f;
	}

	protected Double getDouble(int index) throws OTException {
		if (!Util.isValidString(args[index]))
			error(Text.EMPTY_ARGUMENT_ERROR, getArguments()[index]);
		Double d = null;
		try {
			d = new Double(args[index]);
		} catch (Exception e) {
			error(Text.DOUBLE_EXPECTED_ERROR, getArguments()[index]);
		}
		return d;
	}

	protected CustomUTC getUTC(int index, CustomUTC alt) throws OTException {
		return (!Util.isValidString(args[index]) ? alt : getUTC(index));
	}

	protected CustomUTC getUTC(int index, boolean canBeEmpty) throws OTException {
		if (canBeEmpty && !Util.isValidString(args[index]))
			return null;
		return getUTC(index);
	}

	protected CustomUTC getUTC(int index) throws OTException {
		if (!Util.isValidString(args[index]))
			error(Text.EMPTY_ARGUMENT_ERROR, getArguments()[index]);
		CustomUTC u = null;
		try {
			u = new CustomUTC(args[index]);
		} catch (Exception e) {
			error(Text.DATE_EXPECTED_ERROR, getArguments()[index]);
		}
		return u;
	}

	private static void error(Text text, String param) throws OTUserException {
		throw new OTUserException(text, param);
	}

}