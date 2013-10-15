package eu.opentxs.bridge.core.exceptions;

import eu.opentxs.bridge.Text;


public class OTUserException extends OTException {
	private static final long serialVersionUID = 1L;
	
	private Text text;
	
	public Text getText() {
		return text;
	}
	
	public OTUserException(String message) {
		super(message);
	}
	public OTUserException(Text text) {
		super(text.toString());
		this.text = text;
	}
	public OTUserException(Text text, String param) {
		super(String.format("%s [%s]", text, param.trim()));
		this.text = text;
	}
	
}
