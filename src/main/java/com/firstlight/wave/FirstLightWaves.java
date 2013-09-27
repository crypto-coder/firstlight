package com.firstlight.wave;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import org.jrebirth.core.wave.WaveItem;
import org.jrebirth.core.wave.WaveType;
import org.jrebirth.core.wave.WaveTypeBase;

public interface FirstLightWaves {


    
    
    
    
    /** The waveType used to show a region (start or reload). */
    WaveType SHOW_REGION = WaveTypeBase.build("SHOW_REGION");

    /** The waveType used to hide a region. */
    WaveType HIDE_REGION = WaveTypeBase.build("HIDE_REGION");
	
	
	
}
