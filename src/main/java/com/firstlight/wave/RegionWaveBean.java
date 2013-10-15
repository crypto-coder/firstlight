package com.firstlight.wave;

import com.firstlight.ui.Region;

import javafx.scene.layout.Pane;

import org.jrebirth.core.ui.Model;
import org.jrebirth.core.wave.WaveBean;

/**
 * The class <strong>RegionWaveBean</strong> used for configurations used by the RegionWave.  Region specific details 
 * including FXML Pane and animations are stored in the RegionWaveBean.Region property.
 * 
 * @author MrMoneyChanger
 */
public class RegionWaveBean implements WaveBean {

    /** The action. */
    private RegionAction action;

    /** The model class to load into a region */
    private Class<? extends Model> modelClass;

    /** The model instance to load into a region */
    private Model modelInstance;

    /** The FirstLight Region specifying the FXML Pane, loading, and unloading animations to use for operations */
    private Region region;

    /** The unique identifier for this region. */
    private String regionKey;

       
    
    public RegionWaveBean(){
    	super();
    }

    public RegionWaveBean(RegionAction action, String regionKey){
    	this();
    	this.setAction(action);
    	this.setRegionKey(regionKey);
    }
    
    public RegionWaveBean(RegionAction action, Class<? extends Model> modelClass, String regionKey){
    	this();
    	this.setAction(action);
    	this.setModelClass(modelClass);
    	this.setRegionKey(regionKey);
    }
    
    public RegionWaveBean(RegionAction action, Class<? extends Model> modelClass, String regionKey, Pane fxmlPane){
    	this(action, modelClass, regionKey);
    	this.setRegion(new Region(false, fxmlPane));
    }
    
    
    
    
    /**
     * Gets the action.
     * 
     * @return the action
     */
    public RegionAction getAction() {
        return this.action;
    }

    /**
     * Sets the action.
     * 
     * @param action the new action
     */
    public void setAction(final RegionAction action) {
        this.action = action;
    }
    
    
    
    /**
     * Gets the model class to load into a region.
     * 
     * @return the model class
     */
    public Class<? extends Model> getModelClass() {
        return this.modelClass;
    }
    
    /**
     * Sets the model class to load into a region.
     * 
     * @param modelClass the model class
     */
    public void setModelClass(final Class<? extends Model> modelClass) {
        this.modelClass = modelClass;
    }

    
    
    /**
     * Gets the model instance to load into a region.
     * 
     * @return the model instance
     */
    public Model getModelInstance() {
        return this.modelInstance;
    }
    
    /**
     * Sets the model instance to load into a region.
     * 
     * @param modelInstance the model instance
     */
    public void setModelInstance(final Model modelInstance) {
        this.modelInstance = modelInstance;
    }
    
    
    
    /**
     * Gets the FirstLight Region specifying the FXML Pane, loading, and unloading animations to use for operations.
     * 
     * @return the region
     */
    public Region getRegion() {
        return this.region;
    }

    /**
     * Sets the FirstLight Region specifying the FXML Pane, loading, and unloading animations to use for operations.
     * 
     * @param region the new region
     */
    public void setRegion(final Region region) {
        this.region = region;
    }
    
    
    
    /**
     * Gets the region key.
     * 
     * @return the region key
     */
    public String getRegionKey() {
        return this.regionKey;
    }

    /**
     * Sets the region key.
     * 
     * @param regionKey the new region key
     */
    public void setRegionKey(final String regionKey) {
        this.regionKey = regionKey;
    }

    

}
