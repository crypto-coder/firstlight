package org.opentransactions.otapi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.opentransactions.otjavalib.Load.IPasswordImage;
import org.opentransactions.otjavalib.Load.LoadingOpenTransactionsFailure;
import org.opentransactions.otjavalib.Load.LoadingOpenTransactionsFailure.LoadErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ApplicationProperties;
import eu.opentxs.bridge.core.modules.OTAPI;

public class NativeLoader {

	private static NativeLoader instance = null;
	public static Logger logger = LoggerFactory
			.getLogger(NativeLoader.class);

	public synchronized static NativeLoader getInstance() {
		if (null == instance)
			instance = new NativeLoader();
		return instance;
	}

	private boolean isNativeLoaded = false;
	private boolean isInitialized = false;
	private boolean isPasswordImageSet = false;
	private boolean isPasswordCallbackSet = false;

	private NativeLoader() {
	}

	public boolean getInitialized() {
		return isInitialized;
	}

	private static Map<String, String> nativeLibMap;
	static {
		nativeLibMap = new HashMap<>();
		String[] entries = ApplicationProperties.get().getString("nativeLib.map")
				.split(",");
		for (int i = 0; i < entries.length; i++) {
			String entry = entries[i];
			String[] keyValue = entry.split(":");
			String[] key = keyValue[0].split(ApplicationProperties.get().getString(
					"nativeLib.keySeparator"));
			nativeLibMap.put(String.format("%s%s%s", key[0].trim(),
					ApplicationProperties.get().getString("nativeLib.keySeparator"),
					key[1].trim()), keyValue[1].trim());
		}
	}

	/**
	 * First, external folders are searched. They are defined with
	 * 'nativeLib.dir' key in the application properties file, extending any of
	 * two paths. One path is the location of the application source. The other
	 * is an operating system-specific user application data folder plus a path
	 * defined with 'appData.clientData' and 'appData.app' keys in the
	 * application properties file.
	 * 
	 * If this search fails, library is expected in the class-path.
	 * 
	 * @param libName
	 *            library name, bare, without extension.
	 */
	private static void loadDll(String libName) {
		String nativeLibKey = String.format("%s%s%s",
				System.getProperty("os.name"),
				ApplicationProperties.get().getString("nativeLib.keySeparator"),
				System.getProperty("os.arch"));

		String libId = nativeLibMap.get(nativeLibKey);
		String folder = libId.substring(0, libId.indexOf("."));
		String ext = libId.substring(libId.indexOf("."));

		File file = new File(String.format("%s/%s/%s/%s%s", ApplicationProperties
				.getUserDataPath(),
				ApplicationProperties.get().getString("nativeLib.dir"), folder,
				libName, ext));
		if (file.exists()) {
			System.load(file.getAbsolutePath());
			logger.info(String.format(
					"%s library loaded from the user data path which is %s",
					libName, file.getAbsolutePath()));
			return;
		}

		file = new File(String.format("%s/%s/%s/%s%s", ApplicationProperties
				.getApplBasePath(),
				ApplicationProperties.get().getString("nativeLib.dir"), folder,
				libName, ext));
		if (file.exists()) {
			System.load(file.getAbsolutePath());
			logger.info(String
					.format("%s library loaded from the application base path which is %s",
							libName, file.getAbsolutePath()));
			return;
		}

		Enumeration<URL> urls = null;
		URL url = null;
		File temp = null;
		try {
			for (urls = ClassLoader.getSystemResources(String.format("%s%s",
					libName, ext)); urls.hasMoreElements();) {
				url = urls.nextElement();
				if (url.toString().indexOf(folder) != -1)
					break;
			}
			if (url != null) {
				if (url.getPath().indexOf("jar!") != -1) {
					/* library is jarred */
					InputStream in = url.openStream();
					temp = new File(String.format("%s/%s/%s/%s%s",
							ApplicationProperties.getApplBasePath(), ApplicationProperties
									.get().getString("nativeLib.dir"), folder,
							libName, ext));
					FileOutputStream out = new FileOutputStream(temp);
					byte[] buffer = new byte[1024];
					int len;
					while ((len = in.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}
					in.close();
					out.close();
				} else
					temp = new File(url.toURI());
				System.load(temp.getAbsolutePath());

				if (url.getPath().indexOf("jar!") != -1)
					logger.info(String
							.format("%s library loaded from the class-path which is %s",
									libName, url.toString()));
				else
					logger.info(String
							.format("%s library loaded from the class-path which is %s",
									libName, temp.getAbsolutePath()));
			} else
				logger.error(String
						.format("%s LIBRARY NOT LOADED. Not found in the user data path "
								+ "which is %s, "
								+ "nor in the application base path which is %s, "
								+ "nor in the class-path.",
								libName, ApplicationProperties.getUserDataPath(),
								ApplicationProperties.getApplBasePath()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean initNative() throws LoadingOpenTransactionsFailure {
		if (isNativeLoaded)
			throw new RuntimeException("Native already loaded");
		try {
			String[] libNames = ApplicationProperties.get()
					.getString("nativeLib.libs").split(",");
			for (String libName : libNames)
				loadDll(libName);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return isNativeLoaded = true;
	}

	public boolean init() throws LoadingOpenTransactionsFailure {
		if (!isNativeLoaded) {
			throw new LoadingOpenTransactionsFailure(
					LoadErrorType.NATIVE_NOT_LOADED, "Native libs not loaded");
		}
		if (isInitialized) {
			throw new LoadingOpenTransactionsFailure(
					LoadErrorType.OTAPI_ALREADY_INSTIGATED,
					"Is Already Initialized");
		}
		boolean isSuccess = false;
		if (OTAPI.appStartup())
			isSuccess = OTAPI.init();
		if (isSuccess)
			System.out
					.println("Load.initOTAPI: SUCCESS invoking OTAPI_Basic_AppStartup and OTAPI_Basic_Init");
		else
			throw new LoadingOpenTransactionsFailure(
					LoadErrorType.OTAPI_FAILED_TO_INSTIGATE,
					"Load.initOTAPI: Failed calling OTAPI_Basic_AppStartup or OTAPI_Basic_Init");
		isInitialized = true;
		return true;
	}

	public boolean setupPasswordImage(IPasswordImage passwordImage)
			throws LoadingOpenTransactionsFailure {
		if (!isInitialized) {
			throw new LoadingOpenTransactionsFailure(
					LoadErrorType.OTAPI_NOT_INSTIGATED, "Is not initialized");
		}
		if (isPasswordImageSet) {
			throw new LoadingOpenTransactionsFailure(
					LoadErrorType.PASSWORD_IMAGE_ALREADY_SET,
					"Password image already set");
		}
		String imagePath = "";
		boolean haveImage = false;
		if (otapi.Exists("moneychanger", "settings.dat")) {
			Storable storable = null;
			StringMap stringMap = null;
			storable = otapi.QueryObject(
					StoredObjectType.STORED_OBJ_STRING_MAP, "moneychanger",
					"settings.dat");
			if (null != storable) {
				stringMap = StringMap.ot_dynamic_cast(storable);
				imagePath = stringMap.GetValue("ImagePath");
				File f = new File(imagePath);
				if (f.exists())
					haveImage = true;
			}
		}
		if (!haveImage) {
			for (;;) {
				imagePath = passwordImage
						.GetPasswordImageFromUser("passwordImage");

				if (passwordImage.GetIfUserCancelled()) {
					haveImage = false;
					break;
				}
				File f = new File(imagePath);
				if (f.exists()) {
					haveImage = true;
					break;
				}
			}
			if (!haveImage) {
				return false;
			}
			StringMap stringMap = null;
			Storable storable = otapi
					.CreateObject(StoredObjectType.STORED_OBJ_STRING_MAP);
			if (storable != null) {
				stringMap = StringMap.ot_dynamic_cast(storable);
				System.out.println(String.format("%s: %s", "stringMap",
						stringMap));
				if (stringMap != null) {
					stringMap.SetValue("ImagePath", imagePath);
					haveImage = otapi.StoreObject(stringMap, "moneychanger",
							"settings.dat");
				}
			}
		}
		if (haveImage) {
			passwordImage.SetPasswordImage(imagePath);
		} else {
			throw new LoadingOpenTransactionsFailure(
					LoadErrorType.PASSWORD_IMAGE_FAILED_TO_SET,
					"Password image not set");
		}
		isPasswordImageSet = true;
		return true;
	}

	public boolean setupPasswordCallback(OTCaller passwordCaller,
			OTCallback passwordCallback) throws LoadingOpenTransactionsFailure {
		if (!isPasswordImageSet) {
			throw new LoadingOpenTransactionsFailure(
					LoadErrorType.PASSWORD_IMAGE_NOT_SET,
					"Must set password image first");
		}
		if (isPasswordCallbackSet) {
			throw new LoadingOpenTransactionsFailure(
					LoadErrorType.PASSWORD_CALLBACK_ALREADY_SET,
					"Already have set password callback");
		}
		if (null == passwordCallback) {
			throw new IllegalArgumentException("password callback is null");
		}
		try {
			passwordCaller.setCallback(passwordCallback);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LoadingOpenTransactionsFailure(
					LoadErrorType.PASSWORD_CALLBACK_FAILED_TO_SET,
					"Unable to set password callback");
		}
		Boolean isSuccess = otapi.OT_API_Set_PasswordCallback(passwordCaller);
		if (!isSuccess) {
			passwordCaller = null;
			passwordCallback = null;
			throw new LoadingOpenTransactionsFailure(
					LoadErrorType.PASSWORD_CALLBACK_FAILED_TO_SET,
					"Unable to set password callback");
		}
		isPasswordCallbackSet = true;
		return true;
	}
}
