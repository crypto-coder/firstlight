
package com.firstlight.region;

/**
 * The enumeration <strong>RegionAction</strong> list all action related to a "Region" (Nodes registered with the RegionService)
 * 
 * @author MrMoneyChanger
 */
public enum RegionAction {

    /** Register a region. */
    register,
    
    /** Unregister a region. */
    unregister,
    
    /** Show a model in a specific region. */
    show,

    /** Hide a region.  */
    hide
    
}