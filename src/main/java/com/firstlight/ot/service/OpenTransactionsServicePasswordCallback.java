package com.firstlight.ot.service;


import java.awt.Dialog;

import javax.swing.JDialog;

import jfx.messagebox.MessageBox;

import org.opentransactions.otapi.OTCallback;
import org.opentransactions.otapi.OTPassword;
import org.opentransactions.otjavalib.util.Utility;

import com.firstlight.FirstLightApplication;


public class OpenTransactionsServicePasswordCallback extends OTCallback {

    public OpenTransactionsServicePasswordCallback() {
        super();
    }

    @Override
    public void runOne(String szDisplay, OTPassword theOutput) {
        if (null == theOutput) {
            System.out.println("OpenTransactionsServicePasswordCallback.runOne: Failure: theOutput variable (for password to be returned) is null!");
            return;
        }
        if (!Utility.VerifyStringVal(szDisplay)) {
            System.out.println("OpenTransactionsServicePasswordCallback.runOne: Failure: strDisplay string (telling you what password to type) is null!");
            return;
        }
        

    	MessageBox.show(FirstLightApplication.getInstance().getScene().getWindow(), "he clicked me", "Clicked It", MessageBox.ICON_INFORMATION);
        
        
//        JDialog j = new OTPasswordDialog(null, true, szDisplay);
//        j.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
//        j.setAlwaysOnTop(true);
//        j.setVisible(true);
        

//        OTPasswordDialog.getPassword(theOutput);
    }

    @Override
    public void runTwo(String szDisplay, OTPassword theOutput) {
        if (null == theOutput) {
            System.out.println("OpenTransactionsServicePasswordCallback.runTwo: Failure: theOutput variable (for password to be returned) is null!");
            return;
        }
        if (!Utility.VerifyStringVal(szDisplay)) {
            System.out.println("OpenTransactionsServicePasswordCallback.runTwo: Failure: strDisplay string (telling you what password to type) is null!");
            return;
        }


    	MessageBox.show(FirstLightApplication.getInstance().getScene().getWindow(), "he clicked me", "Clicked It", MessageBox.ICON_INFORMATION);
        
        
//        JDialog j = new OTPwdConfirmDialog(null, true, szDisplay);
//        j.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
//        j.setAlwaysOnTop(true);
//        j.setVisible(true);
        
//        OTPwdConfirmDialog.getPassword(theOutput);
    }
}


