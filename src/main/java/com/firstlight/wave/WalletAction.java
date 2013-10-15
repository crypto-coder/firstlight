
package com.firstlight.wave;

/**
 * The enumeration <strong>RegionAction</strong> list all action related to a "Region" (Nodes registered with the RegionService)
 * 
 * @author MrMoneyChanger
 */
public enum WalletAction {

    /** Open a wallet. */
    open,
    
    /** Close a wallet. */
    close,
    
    /** Create a new wallet. */
    createNew,

    /** Securely package the wallet so that it can be transferred (to removable disk/ storage / network).  */
    securePackage
    
}