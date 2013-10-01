package com.firstlight;

import static org.jrebirth.core.resource.Resources.create;

import org.jrebirth.core.resource.parameter.ObjectParameter;
import org.jrebirth.core.resource.parameter.ParameterItem;

/**
 * The class <strong>FirstLightParameters</strong>.
 * 
 * @author MrMoneyChanger
 */
public interface FirstLightParameters {

    /**************************************************************************************/
    /** ______________________________Wallet Parameters._________________________________ */
    /**************************************************************************************/

	ParameterItem<String> DefaultWalletStorageLocation = create("defaultWalletStorageLocation", "~/firstLight/wallets");
    ParameterItem<Integer> MaxKnownWalletLocationCount = create("maxKnownWalletLocationCount", 10);

    /**************************************************************************************/
    /** ______________________________Integer Parameters.________________________________ */
    /**************************************************************************************/


    /**************************************************************************************/
    /** ______________________________Overridabl Parameters.________________________________ */
    /**************************************************************************************/


}
