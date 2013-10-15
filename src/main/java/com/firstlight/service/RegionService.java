package com.firstlight.service;

import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import org.jrebirth.core.command.basic.showmodel.ShowModelWaveBuilder;
import org.jrebirth.core.concurrent.AbstractJrbRunnable;
import org.jrebirth.core.concurrent.JRebirth;
import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.exception.JRebirthThreadException;
import org.jrebirth.core.key.UniqueKey;
import org.jrebirth.core.service.DefaultService;
import org.jrebirth.core.ui.Model;
import org.jrebirth.core.wave.Wave;
import org.jrebirth.core.wave.Wave.Status;
import org.jrebirth.core.wave.WaveListener;
import org.jrebirth.core.wave.WaveTypeBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.ui.Region;
import com.firstlight.wave.RegionWaveBean;


/**
 * The class <strong>RegionService</strong>.
 * 
 * @author MrMoneyChanger
 */
public class RegionService extends DefaultService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegionService.class);
    
    public static final WaveTypeBase DO_RETRIEVE_REGION = WaveTypeBase.build("RETRIEVE_REGION");
    public static final WaveTypeBase RE_REGION_RETRIEVED = WaveTypeBase.build("REGION_RETRIEVED");

    public static final WaveTypeBase DO_REGISTER_REGION = WaveTypeBase.build("REGISTER_REGION");
    public static final WaveTypeBase RE_REGION_REGISTERED = WaveTypeBase.build("REGION_REGISTERED");
    
    public static final WaveTypeBase DO_UNREGISTER_REGION = WaveTypeBase.build("UNREGISTER_REGION");
    public static final WaveTypeBase RE_REGION_UNREGISTERED = WaveTypeBase.build("REGION_UNREGISTERED");

    public static final WaveTypeBase DO_SHOW_REGION = WaveTypeBase.build("SHOW_REGION");
    public static final WaveTypeBase RE_REGION_SHOWN = WaveTypeBase.build("REGION_SHOWN");

    public static final WaveTypeBase DO_HIDE_REGION = WaveTypeBase.build("HIDE_REGION");
    public static final WaveTypeBase RE_REGION_HIDDEN = WaveTypeBase.build("REGION_HIDDEN");
    
    
	private final HashMap<String, Region> regionMap = new HashMap<>();

	
	

    /**
     * {@inheritDoc}
     */
    @Override
    public void ready() throws CoreException {
        super.ready(); // Can be omitted but it's a bad practice

        registerCallback(DO_RETRIEVE_REGION, RE_REGION_RETRIEVED);
        registerCallback(DO_REGISTER_REGION, RE_REGION_REGISTERED);
        registerCallback(DO_UNREGISTER_REGION, RE_REGION_UNREGISTERED);
        registerCallback(DO_SHOW_REGION, RE_REGION_SHOWN);        
        registerCallback(DO_HIDE_REGION, RE_REGION_HIDDEN);
    }
	
	
    
    
    
    
    

    
    public Region doRetrieveRegion(final Wave wave) {
        LOGGER.trace("Retrieving a region.");

        final RegionWaveBean rwb = getWaveBean(wave);
        final String regionKey = rwb.getRegionKey();
        
        // Retrieve the requested region with the specified key
        return this.regionMap.get(regionKey);
    }
    
    
    
    
    public void doRegisterRegion(final Wave wave) {
        LOGGER.trace("Register a region.");
        
        // Register the requested region with the specified key
        final RegionWaveBean rwb = getWaveBean(wave);
        this.regionMap.put(rwb.getRegionKey(), rwb.getRegion());
    }
    
    
    public void doUnregisterRegion(final Wave wave) {
        LOGGER.trace("Unregister a region.");

        // Unregister the requested region with the specified key
        final RegionWaveBean rwb = getWaveBean(wave);
        final String regionKey = rwb.getRegionKey();
        if (this.regionMap.containsKey(regionKey)) {
        	if (this.regionMap.get(regionKey).isLoaded()){
        		this.doHideRegion(wave);
        	}
        	
        	this.regionMap.remove(regionKey);
        }
    }
    
    
    
    
    
    

    /**
     * Show a region with the provided regionKey.  Load the model if that was provided.
     * 
     * @param wave the source wave
     */
    @SuppressWarnings("unchecked")
	public void doShowRegion(final Wave regionWave) {
        LOGGER.trace("Showing a region.");

        final RegionWaveBean rwb = getWaveBean(regionWave);
        final String regionKey = rwb.getRegionKey();
        final Region region = rwb.getRegion();
                
        //If the Region is not registered, then register it
        if (!this.regionMap.containsKey(regionKey) || this.regionMap.get(regionKey) == null) {
        	this.doRegisterRegion(regionWave);
        }
        
        if(this.regionMap.get(regionKey) != null){
        	
            //If the Region is loaded, and the requested Wave Model is not the current loaded Model, then hide the Region
            if(this.regionMap.get(regionKey).isLoaded() && rwb.getModelClass() != this.regionMap.get(regionKey).getCurrentModelClass()){
            	this.doHideRegion(regionWave, new Callback<Wave, Wave>(){
					@Override
					public Wave call(Wave originalShowWave) {
						doShowRegion(originalShowWave);
						return originalShowWave;
					}            		
            	});
            	return;
            }
        	
            //If the Model class has been provided, then attach this UI
            if(rwb.getModelClass() != null){	            	        	
            	final UniqueKey<Model> modelKey = (UniqueKey<Model>) this.getLocalFacade().getGlobalFacade().getUiFacade().buildKey(rwb.getModelClass());                
                ShowModelWaveBuilder builder = ShowModelWaveBuilder.create()
            														.childrenPlaceHolder(this.regionMap.get(regionKey).getFxmlPane().getChildren())
            														.showModelKey(modelKey);                
                if(rwb.getModelInstance() != null){                	
                	builder = builder.modelInstance(rwb.getModelInstance());                	
                }                
                final Wave showRegionWave = builder.build();
                                
                showRegionWave.addWaveListener(new WaveListener(){
            		@Override public void waveCreated(Wave showWave) {}
            		@Override public void waveSent(Wave showWave) {}
            		@Override public void waveProcessed(Wave showWave) {}
            		@Override public void waveCancelled(Wave showWave) {}
            		@Override public void waveConsumed(Wave showWave) {
            			regionMap.get(regionKey).setCurrentModelClass(((RegionWaveBean)regionWave.getWaveBean()).getModelClass());
            			regionMap.get(regionKey).setCurrentModelInstance(((RegionWaveBean)regionWave.getWaveBean()).getModelInstance());
            			if(regionMap.get(regionKey).getLoadingAnimation() != null){
            				regionMap.get(regionKey).getLoadingAnimation().setOnFinished(new EventHandler<ActionEvent>() {					
	        					@Override
	        					public void handle(ActionEvent arg0) {
	        						regionMap.get(regionKey).getLoadingAnimation().setOnFinished(null);	
	        					}
	        				});
            				regionMap.get(regionKey).getLoadingAnimation().play();
            			}
            			regionMap.get(regionKey).setLoaded(true);
            			//showRegionWave.removeWaveListener(this);
    	    		}
            		@Override public void waveFailed(Wave showWave) {}
            		@Override public void waveDestroyed(Wave showWave) {}		        			        	
                });
                
                showRegionWave.setStatus(Status.Sent);
            
                // Use the JRebirth Thread to manage Waves
                JRebirth.runIntoJIT(new AbstractJrbRunnable("Send Wave " + showRegionWave.toString()) {
                    @Override
                    public void runInto() throws JRebirthThreadException {
                    	getLocalFacade().getGlobalFacade().getNotifier().sendWave(showRegionWave);
                    }
                });        	
            	
            }else if(this.regionMap.get(regionKey) != null){
            	try{
					AnchorPane.setLeftAnchor(regionMap.get(regionKey).getFxmlPane().getChildren().get(0), Double.valueOf(0));
					AnchorPane.setTopAnchor(regionMap.get(regionKey).getFxmlPane().getChildren().get(0), Double.valueOf(0));
					AnchorPane.setRightAnchor(regionMap.get(regionKey).getFxmlPane().getChildren().get(0), Double.valueOf(0));
					AnchorPane.setBottomAnchor(regionMap.get(regionKey).getFxmlPane().getChildren().get(0), Double.valueOf(0));
            	}catch(Exception e){
				 	LOGGER.error("Failed to anchor the new Region FXML pane to its parent.  Parent must be an AnchorPane.", e);				
            	}
    			if(region.getLoadingAnimation() != null){
    				this.regionMap.get(regionKey).getLoadingAnimation().play();
    			}
            }		
        }
    }

    /**
     * Hide a region with the provided regionKey.
     * 
     * @param wave the source wave
     */
    public void doHideRegion(final Wave wave) {
        LOGGER.trace("Hiding a region.");

        final RegionWaveBean rwb = getWaveBean(wave);
        final String regionKey = rwb.getRegionKey();
        
        //If the Region is loaded, then run the hide animation
        if(this.regionMap.get(regionKey) != null){
			if(this.regionMap.get(regionKey).isLoaded() && this.regionMap.get(regionKey).getUnloadingAnimation() != null){
				this.regionMap.get(regionKey).getUnloadingAnimation().play();
			}			
			this.regionMap.get(regionKey).setLoaded(false);
        }   
    }
    /**
     * Hide a region with the provided regionKey.
     * 
     * @param wave the source wave
     */
    private void doHideRegion(final Wave wave, final Callback<Wave, Wave> callback) {
        LOGGER.trace("Hiding a region.");

        final RegionWaveBean rwb = getWaveBean(wave);
        final String regionKey = rwb.getRegionKey();
        
        //If the Region is loaded, then run the hide animation
        if(this.regionMap.get(regionKey) != null){
			if(this.regionMap.get(regionKey).isLoaded() && this.regionMap.get(regionKey).getUnloadingAnimation() != null){
				this.regionMap.get(regionKey).getUnloadingAnimation().setOnFinished(new EventHandler<ActionEvent>() {					
					@Override
					public void handle(ActionEvent arg0) {
						regionMap.get(regionKey).setLoaded(false);	
						regionMap.get(regionKey).getFxmlPane().getChildren().clear();
						regionMap.get(regionKey).getUnloadingAnimation().setOnFinished(null);
						
						if(callback != null){
							callback.call(wave);
						}
					}
				});
				this.regionMap.get(regionKey).getUnloadingAnimation().play();
			}			
        }   
    }
    
    
    
    
    
    
    
    
 
    /**
     * Get the wave bean and cast it.
     * 
     * @param wave the wave that hold the bean
     * 
     * @return the casted wave bean
     */
    private RegionWaveBean getWaveBean(final Wave wave) {
        return (RegionWaveBean) wave.getWaveBean();
    }

    
	
}
