/**
 * 
 */
package com.firstlight.service;



import jfx.messagebox.*;

/**
 * @author chris
 *
 */
public class DialogService implements IDialogService {

	
	
	
	/* (non-Javadoc)
	 * @see com.puremoneysystems.firstlight.service.IDialogService#ShowConfirmation(java.lang.String, java.lang.String, int)
	 */
	@Override
	public int ShowConfirmation(String title, String message, int buttons) {
		 //MessageBox.show(primaryStage, message, title, buttons);
		return 0;
	}

	
	
	
	/* (non-Javadoc)
	 * @see com.puremoneysystems.firstlight.service.IDialogService#ShowMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public void ShowMessage(String title, String message) {
		 //MessageBox.show(primaryStage, message, title, MessageBox.ICON_INFORMATION);

	}

}
