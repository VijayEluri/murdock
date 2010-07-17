package hm.murdock.utils;

import hm.murdock.Murdock;
import hm.murdock.exceptions.ConfigurationException;
import hm.murdock.exceptions.ConfigurationException.ConfigurationExceptionType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Locale;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents app's context (both configuration and environment information).
 * 
 * @author Dario (i@dario.im)
 * 
 */
public final class Context {

	private final File appHome;

	private final Properties configuration;

	private final Boolean firstRun;

	private final File configurationFile;

	private final Logger logger;

	/**
	 * 
	 * This constructor set up application directory and loads user settings.
	 * 
	 * @throws IOException
	 */
	public Context() throws ConfigurationException {
		this.configuration = new Properties();
		// TODO LazyLogger?
		logger = LoggerFactory.getLogger(Murdock.NAME);

		String userHome = System.getProperty("user.home");
		appHome = new File(userHome + "/."
				+ Murdock.NAME.toLowerCase(Locale.US));

		/*
		 * If appHome doesn't exist, we assume that this is its first run ever
		 * for current user.
		 */
		firstRun = !appHome.exists();
		if (firstRun && !appHome.mkdirs()) {
			throw new ConfigurationException(
					ConfigurationExceptionType.UNABLE_CREATE_HOME,
					Murdock.NAME, appHome.getAbsolutePath());
		}

		configurationFile = new File(appHome.getAbsolutePath()
				+ "/configuration.xml");
		if (configurationFile.exists()) {
			try {
				this.configuration.loadFromXML(new FileInputStream(
						configurationFile));
			} catch (InvalidPropertiesFormatException e) {
				throw new ConfigurationException(
						ConfigurationExceptionType.INVALID_FORMAT_CONFIG, e);
			} catch (FileNotFoundException e) {
				logger.warn(ConfigurationExceptionType.UNABLE_ACCESS_CONFIG
						.getMessage(configurationFile.getAbsoluteFile()), e);
			} catch (IOException e) {
				throw new ConfigurationException(
						ConfigurationExceptionType.UNABLE_READ_CONFIG, e,
						configurationFile.getAbsolutePath());
			}
		} else {
			if (!firstRun) {
				logger.warn("Configuration file is missing at "
						+ configurationFile.getAbsolutePath());
			}
		}
	}

	/**
	 * 
	 * @return If current execution is first run, based in the existence of
	 *         appHome while initialization.
	 */
	public Boolean isFirstRun() {
		return this.firstRun;
	}

	/**
	 * 
	 * @return Current app's home located at user's home directory.
	 */
	public File getAppHome() {
		return this.appHome;
	}

	/**
	 * 
	 * @param <T>
	 * @param property
	 * @return Value associated to property key.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getProperty(ContextProperty<T> property) {
		return (T) this.configuration.get(property.toString());
	}

	/**
	 * 
	 * @param <T>
	 * @param <E>
	 * @param property
	 * @param value
	 * @return The previous value of the specified property in this context, or
	 *         null if it did not have one
	 * @throws ConfigurationException
	 */
	@SuppressWarnings("unchecked")
	public <T, E> E setProperty(ContextProperty<T> property, E value)
			throws ConfigurationException {
		E oldValue = (E) this.configuration.put(property.toString(), value);
		if (!property.isFlash()) {
			try {
				this.configuration.storeToXML(new FileOutputStream(
						configurationFile), null);
			} catch (FileNotFoundException e) {
				throw new ConfigurationException(
						ConfigurationExceptionType.UNABLE_WRITE_CONFIG, e,
						configurationFile.getAbsolutePath());
			} catch (IOException e) {
				throw new ConfigurationException(
						ConfigurationExceptionType.UNABLE_WRITE_CONFIG, e,
						configurationFile.getAbsolutePath());
			}
		}

		return oldValue;
	}

	public Logger getLogger() {
		return this.logger;
	}
}
