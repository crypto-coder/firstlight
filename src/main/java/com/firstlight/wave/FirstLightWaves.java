package com.firstlight.wave;

import org.jrebirth.core.ui.Model;
import org.jrebirth.core.wave.WaveItem;
import org.jrebirth.core.wave.WaveType;
import org.jrebirth.core.wave.WaveTypeBase;

public interface FirstLightWaves {


    
    
    
    
    /** The waveType used to show a region (start or reload). */
    WaveType SHOW_REGION = WaveTypeBase.build("SHOW_REGION");

    /** The waveType used to hide a region. */
    WaveType HIDE_REGION = WaveTypeBase.build("HIDE_REGION");
	
    

    /** An instance of a Model to be used in a region. */
    WaveItem<Model> MODEL_INSTANCE = new WaveItem<Model>(false) {};
    
    

    WaveTypeBase DO_CHANGE_MENU_ITEMS = WaveTypeBase.build("CHANGE_MENU_ITEMS");
    WaveTypeBase DO_MENU_ITEMS_CHANGED = WaveTypeBase.build("MENU_ITEMS_CHANGED");
    
    
	
}
