package com.firstlight.ui;

import javafx.scene.layout.Pane;
import javafx.animation.Animation;

import org.jrebirth.core.ui.Model;

/**
 * The <strong>Region</strong> class associates a region of the active Stage / Scene, along with any load and unload animations
 * 
 * @author MrMoneyChanger
 */
public class Region {

	
	private boolean loaded;
	private Pane fxmlPane;

    /** The model class currently loaded in the fxmlPane */
    private Class<? extends Model> currentModelClass;
    private Model currentModelInstance;
	
	private Animation loadingAnimation;
	private Animation unloadingAnimation;
	
	
	public Region(){
		this(false, null);
	}
	public Region(boolean loaded, Pane fxmlPane){
		this(loaded, fxmlPane, null, null);
	}
	public Region(boolean loaded, Pane fxmlPane, Animation loadingAnimation, Animation unloadingAnimation){
		this.loaded = loaded;
		this.fxmlPane = fxmlPane;
		this.loadingAnimation = loadingAnimation;
		this.unloadingAnimation = unloadingAnimation;
	}
	
	
	
	/**
	 * @return Flag indicating if the Region is loaded
	 */
	public boolean isLoaded() {
		return loaded;
	}
	/**
	 * @param loaded Set the flag indicating if the Region is loaded
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	/**
	 * @return the FXML Pane
	 */
	public Pane getFxmlPane() {
		return fxmlPane;
	}
	/**
	 * @param fxmlPane the FXML Pane to set
	 */
	public void setFxmlPane(Pane fxmlPane) {
		this.fxmlPane = fxmlPane;
	}
	/**
	 * @return the loadingAnimation
	 */
	public Animation getLoadingAnimation() {
		return loadingAnimation;
	}
	/**
	 * @param loadingAnimation the loadingAnimation to set
	 */
	public void setLoadingAnimation(Animation loadingAnimation) {
		this.loadingAnimation = loadingAnimation;
	}
	/**
	 * @return the unloadingAnimation
	 */
	public Animation getUnloadingAnimation() {
		return unloadingAnimation;
	}
	/**
	 * @param unloadingAnimation the unloadingAnimation to set
	 */
	public void setUnloadingAnimation(Animation unloadingAnimation) {
		this.unloadingAnimation = unloadingAnimation;
	}
	/**
	 * @return the currentModelClass
	 */
	public Class<? extends Model> getCurrentModelClass() {
		return currentModelClass;
	}
	/**
	 * @param currentModelClass the currentModelClass to set
	 */
	public void setCurrentModelClass(Class<? extends Model> currentModelClass) {
		this.currentModelClass = currentModelClass;
	}
	/**
	 * @return the currentModelInstance
	 */
	public Model getCurrentModelInstance() {
		return this.currentModelInstance;
	}
	/**
	 * @param currentModelInstance the currentModelInstance to set
	 */
	public void setCurrentModelInstance(Model currentModelInstance) {
		this.currentModelInstance = currentModelInstance;
	}
	
	
}
