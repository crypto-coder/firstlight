package eu.opentxs.bridge.core;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.opentransactions.otapi.NativeLoader;
import org.opentransactions.otapi.OTCallback;
import org.opentransactions.otapi.OTCaller;
import org.opentransactions.otapi.OTPassword;
import org.opentransactions.otjavalib.Load.IPasswordImage;
import org.opentransactions.otjavalib.Load.LoadingOpenTransactionsFailure;

import eu.ApplicationProperties;
import eu.opentxs.bridge.Text;
import eu.opentxs.bridge.core.Console.ConsoleApplication;
import eu.opentxs.bridge.core.commands.Commands;
import eu.opentxs.bridge.core.commands.Commands.Quit;
import eu.opentxs.bridge.core.commands.act.AccountCommands;
import eu.opentxs.bridge.core.commands.act.AssetCommands;
import eu.opentxs.bridge.core.commands.act.BusinessCommands;
import eu.opentxs.bridge.core.commands.act.ConfigCommands;
import eu.opentxs.bridge.core.commands.act.ContactCommands;
import eu.opentxs.bridge.core.commands.act.HackCommands;
import eu.opentxs.bridge.core.commands.act.MetaCommands;
import eu.opentxs.bridge.core.commands.act.NymCommands;
import eu.opentxs.bridge.core.commands.act.ServerCommands;
import eu.opentxs.bridge.core.commands.act.WalletCommands;
import eu.opentxs.bridge.core.modules.Module;
import eu.opentxs.bridge.core.modules.OTAPI;

public class OpenTransactions extends Interpreter {

	private static Logger logger = Logger.getLogger(OpenTransactions.class.getName());

	public static void main(String[] args) {
		new OpenTransactions().run(null);
		System.exit(0);
	}

//	static {
//		Thread cleanupThread = new Thread() {
//			@Override
//			public void run() {
//				OTAPI.appShutdown();
//				System.out.println("SUCCESS: shutdown hook done");
//			}
//		};
//		Runtime.getRuntime().addShutdownHook(cleanupThread);
//		int loadingResult = load();
//		if (loadingResult <= 0) {
//			if (loadingResult == 0)
//				Runtime.getRuntime().removeShutdownHook(cleanupThread);
//			System.err.println("ERROR: failed to load libs");
//			System.exit(0);
//		}
//		System.out.println("SUCCESS: native libs loaded");
//	}

	private static File getLockFile() {
		return new File(String.format("%s/%s",
				ApplicationProperties.getUserDataPath(), "ot.pid"));
	}

	private static int load() {
		getLockFile().delete();
		try {
			if (!NativeLoader.getInstance().initNative())
				return 0;

			if (NativeLoader.getInstance().init()) {
				IPasswordImage passwordImageMgmt = new PasswordImageMgmt();
				if (NativeLoader.getInstance().setupPasswordImage(
						passwordImageMgmt)) {
					OTCaller javaPasswordCaller = new OTCaller();
					OTCallback javaPasswordCallback = new OTCallback() {
						@Override
						public void runOne(String prompt, OTPassword output) {
							String password = getPasswordFromUser(prompt);
							output.setPassword(password, password.length());
						}

						@Override
						public void runTwo(String prompt, OTPassword output) {
							String password = getPasswordFromUser(prompt);
							output.setPassword(password, password.length());
						}
					};
					if (NativeLoader.getInstance().setupPasswordCallback(
							javaPasswordCaller, javaPasswordCallback)) {
						try {
							new FileInputStream(getLockFile());
						} catch (FileNotFoundException e) {
							e.getMessage();
						}
						// if (NativeLoader.getInstance().loadWallet())
						return 1;
					}
				}

			}
			if (NativeLoader.getInstance().getInitialized())
				return -1;
			return 0;

		} catch (LoadingOpenTransactionsFailure ex) {
			logger.log(Level.SEVERE, null, ex);
			if (NativeLoader.getInstance().getInitialized())
				return -1;
			return 0;
		}
	}

	public static class PasswordImageMgmt implements IPasswordImage {
		@Override
		public String GetPasswordImageFromUser(String value) {
			File file = new File(ApplicationProperties.getUserDataPath(),
					ApplicationProperties.get().getString("password.image"));
			if (file.exists())
				return file.getAbsolutePath();

			file = new File(ApplicationProperties.getApplBasePath(), ApplicationProperties
					.get().getString("password.image"));
			if (file.exists())
				return file.getAbsolutePath();
			return null;
		}

		@Override
		public boolean SetPasswordImage(String value) {
			return false;
		}

		@Override
		public Boolean GetIfUserCancelled() {
			return false;
		}
	}

	private static String getPasswordFromUser(String prompt) {
		System.out.println(String.format("%s", prompt));
		String password = null;
		password = ConsoleApplication.readLineFromConsole();
		return password;
	}

	@Override
	public String getTitle() {
		return Text.CONSOLE_TITLE.toString();
	}

	@Override
	public void run(ConsoleFrame console) {
		if (console != null) {
			try {
				/** Application icon */
				console.setIconImage(ImageIO.read(ClassLoader
						.getSystemResource(ApplicationProperties.get().getString(
								"icon.image"))));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (ApplicationProperties.get().getBoolean("password.image.show")) {
			JLabel passwordLabel = null;
			try {
				passwordLabel = new JLabel(new ImageIcon(
						ImageIO.read(ClassLoader
								.getSystemResource(ApplicationProperties.get()
										.getString("password.image")))));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			JDialog passwordImage = new JDialog();
			passwordImage.add(passwordLabel);
			passwordImage.setModal(false);
			passwordImage.setAlwaysOnTop(true);
			try {
				passwordImage.setIconImage(ImageIO.read(ClassLoader
						.getSystemResource(ApplicationProperties.get().getString(
								"icon.image"))));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			passwordImage.setBounds(
					ApplicationProperties.get().getInteger("password.image.insetW"),
					ApplicationProperties.get().getInteger("password.image.insetH"),
					screenSize.width
							- ApplicationProperties.get().getInteger(
									"password.image.insetW") * 2,
					screenSize.height
							- ApplicationProperties.get().getInteger(
									"password.image.insetH") * 2);
			passwordImage.pack();
			passwordImage.setVisible(true);
		}

		System.out.println(Text.WELCOME_HEADER);
		System.out.println();
		reset();
		super.run(console);
	}

	public static void reset() {
		Commands.reset();
		MetaCommands.init();
		ConfigCommands.init();
		WalletCommands.init();
		ServerCommands.init();
		AssetCommands.init();
		NymCommands.init();
		AccountCommands.init();
		ContactCommands.init();
		BusinessCommands.init();
		HackCommands.init();
		Module.init();
	}

	@Override
	public void close() {
		Quit.execute();
	}
}
