package eu.opentxs.bridge.core.exceptions;

public class OTSystemException extends OTException {
	private static final long serialVersionUID = 1L;
	
	public enum Event {
		REQUEST_ID_ZERO,
		REQUEST_ID_NEGATIVE,
			
		MESSAGE_IS_INVALID,
		MESSAGE_VERIFICATION_ERROR,
		MESSAGE_VERIFICATION_FAILED_WITH_ZERO,

		TRANSACTION_MESSAGE_IS_INVALID,
		TRANSACTION_MESSAGE_VERIFICATION_ERROR,

		BALANCE_AGREEMENT_ERROR,
			
		STRING_TO_INTEGER_CONVERSION_ERROR,
		STRING_TO_DOUBLE_CONVERSION_ERROR,

		PARSE_SERVER_ID_ERROR,
		PARSE_NYM_ID_ERROR,
		PARSE_ASSET_ID_ERROR,
		PARSE_ACCOUNT_ID_ERROR,
	}
	
	private Event event;
	
	public Event getEvent() {
		return event;
	}
	
	public OTSystemException(String message) {
		super(message);
	}
	public OTSystemException(String message, int result) {
		super(String.format("%s (%d)", message, result));
	}
	public OTSystemException(Event event) {
		super(event.toString());
		this.event = event;
	}
	public OTSystemException(Event event, int result) {
		this(event.toString(), result);
		this.event = event;
	}
	
}
