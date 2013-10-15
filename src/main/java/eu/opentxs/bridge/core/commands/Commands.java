package eu.opentxs.bridge.core.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import eu.ApplicationProperties;
import eu.opentxs.bridge.Text;
import eu.opentxs.bridge.Util;
import eu.opentxs.bridge.core.Console.ConsoleApplication;
import eu.opentxs.bridge.core.dto.Contact;
import eu.opentxs.bridge.core.exceptions.OTException;
import eu.opentxs.bridge.core.modules.Module;

public abstract class Commands {

	public static void main(String[] args) {
		// Test1().execute(true, 8.6726f, 198);
		String s = "last living souls";
		@SuppressWarnings("unused")
		boolean test = s.contains("st li");
		System.exit(0);
	}

	public enum Category {
		META("META"), CONFIG("CONFIG"), WALLET("WALLET"), SERVER("SERVER"), NYM(
				"NYM"), ASSET("ASSET"), ACCOUNT("ACCOUNT"), CONTACT("CONTACT"), BUSINESS(
				"BUSINESS"), BUSINESS_EXTRA("BUSINESS EXTRA"), HACK("HACK"), TEST(
				"TEST");
		private String label;

		private Category(String label) {
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}
	}

	public enum Sophistication {
		MINI(0), SIMPLE(1), ADVANCED(2), ISSUER(3), ADMIN(4), TOP(9);
		private int value;

		private Sophistication(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static Sophistication parse(int value) {
			if (value == 0)
				return MINI;
			if (value == 1)
				return SIMPLE;
			if (value == 2)
				return ADVANCED;
			if (value == 3)
				return ISSUER;
			if (value == 4)
				return ADMIN;
			return TOP;
		}

		public boolean hasAccess(Sophistication sophistication) {
			return value >= sophistication.value;
		}
	}

	public enum Extension {
		DEFINITION(ApplicationProperties.get().getString("extension.definition")), CONTRACT(
				ApplicationProperties.get().getString("extension.contract"));
		private String value;

		private Extension(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public static Map<String, Command> commands = new HashMap<>();
	private static int index = 0;

	public static void reset() {
		commands.clear();
		addToCommands(new Index(), Category.META);
		addToCommands(new Help(), Category.META);
		addToCommands(new Quit(), Category.META);
	}

	public static void addToCommands(Command command, Category category, Sophistication sophistication) {
		if (Module.hasAccess(sophistication))
			addToCommands(command, category);
	}

	public static void addToCommands(Command command, Category category) {
		command.index = index++;
		command.category = category;
		commands.put(command.getName(), command);
	}

	protected static void print(Object s) {
		System.out.println(s);
	}

	protected static void error(String message) throws OTException {
		throw new OTException(message);
	}

	protected static void error(Text text) throws OTException {
		error(text.toString());
	}

	protected static boolean isValidServerDefinition(String s) {
		return (!s.contains("BEGIN SIGNED CONTRACT") && s
				.contains("notaryProviderContract"));
	}

	protected static boolean isValidAssetDefinition(String s) {
		return (!s.contains("BEGIN SIGNED CONTRACT") && s
				.contains("digitalAssetContract"));
	}

	protected static boolean isValidServerContract(String s) {
		return (s.contains("BEGIN SIGNED CONTRACT") && s
				.contains("notaryProviderContract"));
	}

	protected static boolean isValidAssetContract(String s) {
		return (s.contains("BEGIN SIGNED CONTRACT") && s
				.contains("digitalAssetContract"));
	}

	protected static boolean isValidNymContract(String s) {
		return s.contains("BEGIN OT ARMORED EXPORTED NYM");
	}

	protected static boolean isValidPurseContract(String s) {
		return s.contains("BEGIN SIGNED PURSE");
	}

	protected static boolean isValidCashContract(String s) {
		return s.contains("BEGIN SIGNED PURSE");
	}

	protected static boolean isValidVoucherContract(String s) {
		return s.contains("BEGIN SIGNED VOUCHER");
	}

	protected static boolean isValidChequeContract(String s) {
		return s.contains("BEGIN SIGNED CHEQUE");
	}

	protected static boolean isValidInvoiceContract(String s) {
		return s.contains("BEGIN SIGNED INVOICE");
	}

	protected static List<String> getWalletIds() {
		File dir = new File(ApplicationProperties.getUserDataPath());
		File[] files = dir.listFiles();
		List<String> ids = new ArrayList<String>();
		for (File file : files) {
			if (file.isFile()) {
				String name = file.getName();
				if (Extension.DEFINITION.getValue().equals(
						getFileExtension(name)))
					ids.add(name.substring(0, name.lastIndexOf('.')));
			}
		}
		return ids;
	}

	protected static Boolean readBooleanFromInput(String prompt) {
		print(String.format("%s (%s/%s)", prompt, "Y", "N"));
		String input = ConsoleApplication.readLineFromConsole();
		return (Util.isValidString(input) && (input.equalsIgnoreCase("Y")));
	}

	protected static String readStringFromInput(String prompt) {
		print(String.format("%s:", prompt));
		String input = ConsoleApplication.readLineFromConsole();
		return input;
	}

	protected static String readStringFromFile(Text folder, Extension extension) {
		Path path = null;
		while (path == null) {
			path = openFileChooser(folder, extension, Text.OPEN_FILE_TITLE,
					Text.OPEN_FILE);
			if (path == null)
				return null;
			if (!path.toFile().exists()) {
				JOptionPane.showMessageDialog(null,
						Text.FILE_DOES_NOT_EXIST.toString(),
						Text.FILE_DOES_NOT_EXIST_TITLE.toString(),
						JOptionPane.ERROR_MESSAGE);
				path = null;
			}
		}
		byte[] encoded = null;
		try {// fatal
			encoded = Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Charset encoding = Charset.defaultCharset();
		String content = encoding.decode(ByteBuffer.wrap(encoded)).toString();
		content = content.replaceAll("\\r", "");
		return content;
	}

	protected static boolean writeStringToFile(Text folder,
			Extension extension, String content) {
		Path path = null;
		while (path == null) {
			path = openFileChooser(folder, extension, Text.SAVE_FILE_TITLE,
					Text.SAVE_FILE);
			if (path == null)
				return false;
			if (path.toFile().exists()) {
				int choice = JOptionPane.showOptionDialog(null,
						Text.FILE_OVERWRITE.toString(),
						Text.FILE_OVERWRITE_TITLE.toString(),
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (choice != JOptionPane.YES_OPTION)
					path = null;
			}
		}
		String fileName = path.toString();
		FileWriter fw;
		try {// fatal
			fw = new FileWriter(fileName);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		print(String.format("%s:", Text.FILE_SAVED_AS));
		print(fileName);
		return true;
	}

	private static Path openFileChooser(Text folder, final Extension extension,
			Text title, Text approveButtonText) {
		String startDir = String.format("%s/%s", ApplicationProperties.get().getString("workingDir"),
				ApplicationProperties.get().getString("contracts.dir"));
		if (folder != null)
			startDir = String.format("%s/%s", startDir, folder);
		return openFileChooser(startDir, extension, title, approveButtonText);
	}

	protected static Path openFileChooser(String startDir,
			final Extension extension, Text title, Text approveButtonText) {
		{
			File file = new File(startDir);
			if (!file.exists())
				file.mkdirs();
		}
		JFileChooser fileChooser = new JFileChooser(startDir);
		fileChooser.setDialogTitle(title.toString());
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				if (f.isDirectory())
					return true;
				return extension.getValue().equals(
						getFileExtension(f.getName()));
			}

			@Override
			public String getDescription() {
				return String.format("*.%s", extension.getValue());
			}
		});
		if (fileChooser.showDialog(null, approveButtonText.toString()) != JFileChooser.APPROVE_OPTION)
			return null;
		Path path = FileSystems.getDefault().getPath(
				fileChooser.getSelectedFile().getPath());
		String fileName = path.toString();
		if (!extension.getValue().equals(getFileExtension(fileName)))
			fileName = String.format("%s.%s", fileName, extension.getValue());
		path = path.resolve(fileName);
		return path;
	}

	public static String getFileExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		if (i != -1)
			return fileName.substring(i + 1).toLowerCase();
		return null;
	}

	public static class Index extends Command {
		@Override
		protected void action(String[] args) throws OTException {
			execute();
		}

		public static void execute() {
			List<Command> index = new ArrayList<>(commands.values());
			Collections.sort(index);
			Category category = null;
			for (Command command : index) {
				if (!command.category.equals(category)) {
					if (category != null)
						System.out.println();
					category = command.category;
					System.out.println(String.format("%21s", category));
				}
				System.out.println(String.format("%21s (%9s): %s",
						command.getNameLocal(), command.getPseudoLocal(),
						command.getArgumentsLocal()));
			}
		}
	}

	public static class Help extends Command {
		@Override
		protected void action(String[] args) throws OTException {
			execute();
		}

		public static void execute() {
			List<Command> index = new ArrayList<>(commands.values());
			Collections.sort(index);
			Category category = null;
			for (Command command : index) {
				if (!command.category.equals(category)) {
					if (category != null)
						System.out.println();
					category = command.category;
					System.out.println(String.format("%21s", category));
				}
				System.out.println(String.format("%21s: %s",
						command.getNameLocal(), command.getHelpLocal()));
			}
		}
	}

	public static class Quit extends Command {
		@Override
		protected void action(String[] args) throws OTException {
			execute();
		}

		public static void execute() {
			//Contact.closeDatabase();
			System.exit(0);
		}
	}

}
