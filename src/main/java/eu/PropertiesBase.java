package eu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Properties are kept in file pairs: one file for default properties, second
 * for actualizations. Default properties extension is '.properties"; other
 * files are extended with '.user.properties'.
 * 
 * The name of files may be passed by the class constructor, or it is derived
 * from the name of the supper class.
 * 
 * Properties files are looked for in the class-path by their name. Preferred
 * are those that are not jarred.
 * 
 * @author cartman
 * 
 */
public class PropertiesBase implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger logger = LoggerFactory.getLogger(PropertiesBase.class);
	private static final String extension = ".properties";
	private static final String actualizingExtension = ".user.properties";

	public static String getApplBasePath() {
		String path = null;
		try {
			File file = new File(PropertiesBase.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI());
			if (file.isFile())/* executable jar */
				path = file.getParent();
			else
				path = file.getPath();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return path;
	}

	public static void main(String[] args) {
		String path = "E:/Workspaces/OpenTransactions/FellowTravelerBridge/ft-0.0.1-jar-with-dependencies.jar";
		File file = new File(path);
		file.getName();
		file.getParent();

		PropertiesBase instance = new PropertiesBase();
		instance.init();
		System.out.println();
		System.out.println(instance.toString());
	}

	protected Properties properties;
	private boolean isReadOnly = false;
	transient private String propertiesFileName;
	transient protected URL propertiesDefaultUrl;

	transient protected URL propertiesUrl;

	/**
	 * The default properties file name is made of the super class' name,
	 * appended with '.properties' extension. The actualizing properties file
	 * takes the super class' name appended with '.user.properties' extension.
	 */
	public PropertiesBase() {
		propertiesFileName = this.getClass().getSimpleName();
	}

	public String[] getArray(String key, String[] defaultValue) {
		if (properties.getProperty(key) != null) {
			String[] retval = new String[0];
			String property = properties.getProperty(key);
			{
				retval = property.split(",");
			}
			return retval;
		} else
			return defaultValue;
	}

	public Boolean getBoolean(String key) {
		return Boolean.valueOf(properties.getProperty(key));
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		if (properties.getProperty(key) != null)
			return Boolean.valueOf(properties.getProperty(key));
		else
			return defaultValue;
	}

	public Double getDouble(String key) {
		if (properties.getProperty(key) != null)
			return Double.valueOf(properties.getProperty(key));
		return null;
	}

	public double getDouble(String key, double defaultValue) {
		if (properties.getProperty(key) != null)
			return Double.valueOf(properties.getProperty(key));
		else
			return defaultValue;
	}

	public Float getFloat(String key) {
		if (properties.getProperty(key) != null)
			return Float.valueOf(properties.getProperty(key));
		return null;
	}

	public Integer getInteger(String key) {
		if (properties.getProperty(key) != null)
			return Integer.decode(properties.getProperty(key));
		return null;
	}

	public int getInteger(String key, int defaultValue) {
		if (properties.getProperty(key) != null)
			return Integer.decode(properties.getProperty(key));
		else
			return defaultValue;
	}

	public String getKeyForValue(String value) {
		Set<String> entries = properties.stringPropertyNames();
		String key = null;
		for (String entry : entries)
			if (getString(entry).equals(value)) {
				key = entry;
				break;
			}
		return key;
	}

	public long getLong(String key) {
		return Long.valueOf(properties.getProperty(key));
	}

	public long getLong(String key, long defaultValue) {
		if (properties.getProperty(key) != null)
			return Long.valueOf(properties.getProperty(key));
		else
			return defaultValue;
	}

	private URL getPropertiesUrl(String propertiesFileName, boolean isReadOnly) {
		Enumeration<URL> urls = null;
		URL url = null;
		if (isReadOnly) {
			try {/* Read only property files are expected in the class-path. */
				for (urls = ClassLoader.getSystemResources(propertiesFileName); urls
						.hasMoreElements();) {
					url = urls.nextElement();
					if (url.toString().indexOf("jar!") == -1)
						break;
				}
				if (url != null)
					logger.info(String.format(
							"Properties file in the class-path: %s",
							url.getPath()));
			} catch (Exception e) {
				throw new ApplicationRuntimeException(e);
			}
		} else {/*
				 * Re-writable property files are expected within the working
				 * directory.
				 */
			File file = new File(ApplicationProperties.getWorkingDirectory(),
					propertiesFileName);
			if (file.exists())
				try {
					url = file.toURI().toURL();
				} catch (MalformedURLException e) {
					throw new ApplicationRuntimeException(e);
				}
		}

		if (url == null) {/* Create a new property file in the working directory */
			PrintWriter out = null;
			File file = new File(ApplicationProperties.getWorkingDirectory(),
					propertiesFileName);
			logger.info(String
					.format("Properties file '%s' is not in the class-path. Attempting to open new one: %s",
							propertiesFileName, file.getAbsolutePath()));
			try {
				out = new PrintWriter(file);
				out.println("#---No Comment---");
				url = file.toURI().toURL();
			} catch (Exception e) {
				throw new ApplicationRuntimeException(e);
			} finally {
				if (out != null)
					try {
						out.close();
					} catch (Exception e) {
					}
			}
		}

		InputStream in = null;
		try {
			in = url.openStream();
			properties.load(in);
		} catch (Exception e) {
			throw new ApplicationRuntimeException(e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}
		return url;
	}

	public String getString(String key) {
		return properties.getProperty(key);
	}

	public String getString(String key, String defaultValue) {
		if (properties.getProperty(key) != null)
			return properties.getProperty(key);
		else
			return defaultValue;
	}

	public void init() {
		properties = new Properties();
		propertiesDefaultUrl = getPropertiesUrl(propertiesFileName + extension,
				true);
		propertiesUrl = getPropertiesUrl(propertiesFileName
				+ actualizingExtension, isReadOnly);
	}

	public void save() {
		FileOutputStream out = null;
		File file = null;
		boolean ok = false;
		try {
			file = new File(propertiesUrl.toURI());
			if (file.exists()) {
				ok = true;
				out = new FileOutputStream(file);
				properties.store(out, "---No Comment---");
			}
		} catch (Exception e) {
			throw new ApplicationRuntimeException(e);
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
				}
		}
		if (!ok)
			throw new ApplicationRuntimeException(String.format(
					"cannot write to file: %s", file != null ? file.getPath()
							: "null"));
	}

	public String[] setArray(String key, String[] value) {
		String property = "";
		for (String item : value)
			property += item + ",";
		properties.put(key, property);
		return value;
	}

	public void setBoolean(String key, boolean value) {
		properties.put(key, Boolean.valueOf(value).toString());
	}

	public void setDouble(String key, double value) {
		properties.put(key, Double.valueOf(value).toString());
	}

	public int setInteger(String key, int value) {
		properties.put(key, Integer.valueOf(value).toString());
		return value;
	}

	public long setLong(String key, long value) {
		properties.put(key, Long.valueOf(value).toString());
		return value;
	}

	public void setPropertiesFileName(String propertiesFileName) {
		this.propertiesFileName = propertiesFileName;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public String setString(String key, String value) {
		properties.put(key, value);
		return value;
	}

}
