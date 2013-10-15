package eu.opentxs.bridge;

import eu.ApplicationProperties;
import eu.PropertiesBase;

public class Localizer extends PropertiesBase {
	private static final long serialVersionUID = 1L;

	private static Localizer instance;

	public static Localizer get() {
		ApplicationProperties.get();
		if (instance == null){
			instance = new Localizer();
			instance.setReadOnly(true);
			instance.init();
		}
		return instance;
	}

	private Localizer() {
		super();
	}
}
