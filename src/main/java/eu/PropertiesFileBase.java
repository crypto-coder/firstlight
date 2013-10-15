package eu;

import java.io.File;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesFileBase extends PropertiesBase implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger logger = LoggerFactory.getLogger(PropertiesFileBase.class);
	private static String propertiesFile = "ApplicationProperties";

	private static PropertiesFileBase instance = null;

	public static File findAvailableFile(String pathList) {
		String[] paths = pathList.split(";");
		for (int i = 0; i < paths.length; i++) {
			File f = new File(paths[i].trim());
			if (f.exists()) {
				return f;
			}
		}
		return null;
	}

	public static PropertiesFileBase get() {
		if (instance == null){
			instance = new PropertiesFileBase();
			instance.setReadOnly(true);
			instance.init();
		}
		return instance;
	}

	public static File getApplBaseFile() {
		return new File(getApplBasePath());
	}

	public static File getApplBaseFilePlus(String relPath) {
		if (relPath.contains(":"))
			return new File(relPath);
		else
			return new File(getApplBaseFile(), relPath);

	}

	public static String getSvn() {
		return get().getString("svn");
	}

	public static String getApplBasePathPlus(String relPath) {
		if (relPath.contains(":"))
			return relPath;
		else
			return getApplBaseFilePlus(relPath).getPath();
	}

	public static File resolveFileProperty(String key) {
		return resolveFilePropertyPlus(key, "");
	}

	public static File resolveFilePropertyPlus(String key, String plusPath) {
		plusPath = plusPath.trim();
		if (!plusPath.equals(""))
			plusPath = "/" + plusPath;

		File file = new File(getApplBaseFile(), get().getString(key) + plusPath);

		if (!file.exists())
			throw new ApplicationRuntimeException("FILE_PATH_DOES_NOT_EXIST "
					+ file.getPath());
		return file;
	}

	protected PropertiesFileBase() {
		super();
		setPropertiesFileName(propertiesFile);
	}
}
