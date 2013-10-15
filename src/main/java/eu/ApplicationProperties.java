package eu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationProperties extends PropertiesFileBase {

	private static final long serialVersionUID = 1L;
	private static ApplicationProperties inst = null;
	public static Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);

	public static ApplicationProperties get() {
		if (inst == null) {
			inst = new ApplicationProperties();
			inst.setReadOnly(true);
			inst.init();
			if (inst.propertiesUrl.getPath().indexOf("jar!") != -1) {
				/* evidently, in production */
				if (get().getString("workingDir") == null || get().getString("workingDir").equals(""))
					inst.setString("workingDir", getUserDataPath());
				inst.setString("database.relpath", get().getString("workingDir"));
			} else {
				if (get().getString("workingDir") == null || get().getString("workingDir").equals(""))
					inst.setString("workingDir", getApplBasePath());
			}
			logger.info(String
					.format("Working directory is %s; user data path is %s; "
							+ "application base path is %s.",
							getWorkingDirectory(), getUserDataPath(),
							getApplBasePath()));
		}
		return inst;
	}

	public static String getWorkingDirectory() {
		return get().getString("workingDir");
	}

	public static String getUserDataPath() {
		return String.format("%s/%s/%s", getWorkingDirectory(), get()
				.getString("appData.app"), get()
				.getString("appData.clientData"));
	}

	public static void main(String[] args) {
	}

	protected ApplicationProperties() {
		super();
	}
}
