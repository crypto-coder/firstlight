package com.firstlight.ui;


import java.util.ResourceBundle;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleObjectProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.FirstLightApplication;
import com.firstlight.region.Region;
import com.firstlight.region.RegionAction;
import com.firstlight.region.RegionWaveBean;
import com.firstlight.region.command.RegionCommand;
import com.firstlight.ui.ApplicationShellModel;
import com.firstlight.ui.dashboard.DashboardModel;
import com.firstlight.ui.wallet.KnownWalletModel;
import com.fxexperience.javafx.animation.*;

import org.jrebirth.core.ui.fxml.AbstractFXMLController;
import org.jrebirth.core.ui.View;

import jfx.messagebox.*;



public class ApplicationShellController extends AbstractFXMLController<ApplicationShellModel, View<ApplicationShellModel,?,?>> {
	
    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationShellController.class);
    
    @FXML
    private AnchorPane fxmlShell;    
    @FXML
    private Pane navigationRegion;
    @FXML
    private Pane notificationRegion;
    @FXML
    private Pane activeScreenRegion;
    private SimpleObjectProperty<Node> activeScreenRegionProperty = new SimpleObjectProperty<Node>(this.activeScreenRegion, "activeScreenRegion");
    public final SimpleObjectProperty<Node> ActiveScreenRegionProperty() { return activeScreenRegionProperty; }
    public final Node getActiveScreenRegion() { return activeScreenRegionProperty.get(); }

    
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {			
			FirstLightApplication app = FirstLightApplication.getInstance();
			
			//Load the FXML for the Application and set it as the Root node		
			app.getRootNode().setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			app.getRootNode().setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
			AnchorPane.setBottomAnchor(this.fxmlShell, Double.valueOf(0));
			AnchorPane.setLeftAnchor(this.fxmlShell, Double.valueOf(0));
			AnchorPane.setTopAnchor(this.fxmlShell, Double.valueOf(0));
			AnchorPane.setRightAnchor(this.fxmlShell, Double.valueOf(0));
			
			//Find the Navigation Region (id=navigationRegion) and create a fade in and down animation for it
			if(this.navigationRegion == null){
		        LOGGER.trace("Could not locate a Node with fx:id=navigationRegion in the /fxml/Application.fxml file.");				
			}else{				
				(new FadeInDownTransition(this.navigationRegion)).play();
			}
			
			//Find the Notification Region (id=notificationRegion) and create a fade in and up animation for it
			if(this.notificationRegion == null){
		        LOGGER.trace("Could not locate a Node with fx:id=notificationRegion in the /fxml/Application.fxml file.");				
			}else{				
				(new FadeInUpTransition(this.notificationRegion)).play();
			}
			
			//Find the Active Screen Region (id=activeScreenRegion) and create a fade in and fill animation for it
			if(this.activeScreenRegion == null){
		        LOGGER.trace("Could not locate a Node with fx:id=activeScreenRegion in the /fxml/Application.fxml file.");		
		        throw new NullPointerException("Could not locate a Node with id=activeScreenRegion in the /fxml/Application.fxml file.");
			}else{		
				//Update the property binding since FXML injection of the activeScreenRegion variable has occurred
		        this.activeScreenRegionProperty  = new SimpleObjectProperty<Node>(this.activeScreenRegion, "activeScreenRegion");
		        		        
		        //Build a RegionWave and call the RegionCommand
		        RegionWaveBean waveBean = new RegionWaveBean(RegionAction.show, DashboardModel.class, "activeScreenRegion");
		        waveBean.setRegion(new Region(false, this.activeScreenRegion, this.createActiveScreenShowAnimation(), this.createActiveScreenHideAnimation()));
		        this.getModel().callCommand(RegionCommand.class, waveBean);     
			}			
			
		} catch(Exception e) { 
			LOGGER.error("Error while customizing the main scene for the application : ", e);
			e.printStackTrace();
		}
	}
	
	
	
	

	private Animation createActiveScreenShowAnimation() {
    	try {
    		FadeTransition fade = new FadeTransition(Duration.millis(1000), this.activeScreenRegion);
    		fade.setFromValue(0.0);
    		fade.setToValue(1.0);
    		fade.setCycleCount(1);
    		
    		ScaleTransition scale = new ScaleTransition(Duration.millis(1000), this.activeScreenRegion);
    		scale.setFromX(0.1);
    		scale.setToX(1.0);
    		scale.setFromY(0.1);
    		scale.setToY(1.0);
    		scale.setCycleCount(1);
    		
    		ParallelTransition parallel = new ParallelTransition();
    		parallel.getChildren().addAll(fade, scale);
    		parallel.setCycleCount(1);
    		
    		return parallel;		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}    	
	}
	private Animation createActiveScreenHideAnimation() {
    	try {
    		FadeTransition fade = new FadeTransition(Duration.millis(1000), this.activeScreenRegion);
    		fade.setFromValue(1.0);
    		fade.setToValue(0.0);
    		fade.setCycleCount(1);
    		
    		ScaleTransition scale = new ScaleTransition(Duration.millis(1000), this.activeScreenRegion);
    		scale.setFromX(1.0);
    		scale.setToX(0.1);
    		scale.setFromY(1.0);
    		scale.setToY(0.1);
    		scale.setCycleCount(1);
    		
    		ParallelTransition parallel = new ParallelTransition();
    		parallel.getChildren().addAll(fade, scale);
    		parallel.setCycleCount(1);
    		
    		return parallel;		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}    	
	}
	
	
	
	
	
	
	
		
    
    @FXML
    public void openWalletFromFile() {   
        //RegionWaveBean hideWaveBean = new RegionWaveBean(RegionAction.hide, "activeScreenRegion");
        //this.getModel().callCommand(RegionCommand.class, hideWaveBean);      	
    	
        //Build a RegionWave and call the RegionCommand
        RegionWaveBean showWaveBean = new RegionWaveBean(RegionAction.show, KnownWalletModel.class, "activeScreenRegion");
        this.getModel().callCommand(RegionCommand.class, showWaveBean);      	
    }
	
    
    @FXML
    public void openCurrencySummary() {   
    	MessageBox.show(FirstLightApplication.getInstance().getScene().getWindow(), "Clicked It", "Item clicked", MessageBox.ICON_INFORMATION);
    }
	
    
    @FXML
    public void openConnectionSummary() {   
    	MessageBox.show(FirstLightApplication.getInstance().getScene().getWindow(), "Clicked It", "Item clicked", MessageBox.ICON_INFORMATION);
    }
	
    
    @FXML
    public void navigationBack() {   
    	MessageBox.show(FirstLightApplication.getInstance().getScene().getWindow(), "Clicked It", "Item clicked", MessageBox.ICON_INFORMATION);
    }
    

	
	
	
	
    
    
    
    
    
    
    
    
    

	//Load the FXML for the Dashboard and set it as the first child node of the activeScreenRegion
//    List<Node> activeScreenWrapper = new ArrayList<Node>();
//    activeScreenWrapper.add(this.activeScreenRegion);
//    final ObservableList<Node> dashboardUIViewBindPoint = FXCollections.observableList(activeScreenWrapper);
//    final Wave dashboardWave = this.getModel().attachUi(DashboardModel.class, WaveData.build(JRebirthWaves.ATTACH_UI_NODE_PLACEHOLDER, this.activeScreenRegionProperty));
//	
//    dashboardWave.addWaveListener(new WaveListener(){
//		@Override public void waveCreated(Wave wave) {}
//		@Override public void waveSent(Wave wave) {}
//		@Override public void waveProcessed(Wave wave) {
//			FirstLightApplication.PrimaryStage.show();	}
//		@Override public void waveCancelled(Wave wave) {}
//		@Override public void waveConsumed(Wave wave) {}
//		@Override public void waveFailed(Wave wave) {}
//		@Override public void waveDestroyed(Wave wave) {
//			FirstLightApplication.PrimaryStage.show();						
//		}		        			        	
//    });
    
    
//	this.getModel().sendWave(
//			WaveBuilder.create()
//					   .waveGroup(WaveGroup.CALL_COMMAND)
//					   .relatedClass(ShowHomeCommand.class)
//					   .build()						
//			);
//	this.getModel().sendWave(StackWaves.SHOW_PAGE_ENUM, WaveData.build(StackWaves.PAGE_ENUM, FXMLPages.DashboardFXML));
//	
    

    //List<Node> activeScreenWrapper = new ArrayList<Node>();
    //activeScreenWrapper.add(this.activeScreenRegion);
    //final ObservableList<Node> dashboardUIViewBindPoint = FXCollections.observableList(activeScreenWrapper);
    
//    final List<Wave> chainedWaveList = new ArrayList<>();
//	Wave loadDashboardWave = ShowModelWaveBuilder.create()
//												.uniquePlaceHolder(this.activeScreenRegionProperty)
//												.showModelKey(this.getModel().getLocalFacade().buildKey(dashboardModelClass))
//												.build();
//				
//	
//	chainedWaveList.add(loadDashboardWave);
//	
//	final Wave loadDashboardCommandWave = WaveBuilder.create()
//						                        .waveGroup(WaveGroup.CALL_COMMAND)
//						                        .relatedClass(ChainWaveCommand.class)
//						                        .data(WaveData.build(JRebirthWaves.CHAINED_WAVES, chainedWaveList))
//						                        .build();
	
    
    
//	JRebirth.runIntoJIT(new Runnable() {
//        @Override
//        public void run() {
//        	try {
//				Wave loadDashboardWave = ShowModelWaveBuilder.create()
//															.childrenPlaceHolder(dashboardUIViewBindPoint)
//															.showModelKey(dashboardModelKey)
//															.build();
//				
//				JRebirthThread.getThread().getFacade().getNotifier().sendWave(loadDashboardWave);							
//			} catch (JRebirthThreadException e) {   //(Exception e) {
//				LOGGER.error("Error while sending the LoadDashboardCommand Wave : ", e);
//				e.printStackTrace();
//			}
//        }
//    });
	
	
	
	//this.loadNewActiveScreen("/fxml/Dashboard.fxml");
	
	
//	linkWave(getView().getShowIncluded(), ActionEvent.ACTION, StackWaves.SHOW_PAGE_ENUM,
//            WaveData.build(StackWaves.PAGE_ENUM, FXMLPage.IncludedFxml)/* , stackName */);
    
    
    
    
    
    
	
//	Class<Model> dashboardModelClass = (Class<Model>) (DashboardModel.class).asSubclass(Model.class);
//	final UniqueKey<Model> dashboardModelKey = this.getModel().getLocalFacade().buildKey(dashboardModelClass);	        
//    
//    final Wave loadDashboardWave = ShowModelWaveBuilder.create()
//														.childrenPlaceHolder(this.activeScreenRegion.getChildren())
//														.showModelKey(dashboardModelKey)
//														.build();
//    
//    loadDashboardWave.addWaveListener(new WaveListener(){
//		@Override public void waveCreated(Wave wave) {}
//		@Override public void waveSent(Wave wave) {}
//		@Override public void waveProcessed(Wave wave) {}
//		@Override public void waveCancelled(Wave wave) {}
//		@Override public void waveConsumed(Wave wave) {
//			//FirstLightApplication.PrimaryStage.show();
//		}	
//		@Override public void waveFailed(Wave wave) {
//			MessageBox.show(FirstLightApplication.PrimaryStage.getScene().getWindow(), "Wave Operation", "Failed", MessageBox.ICON_INFORMATION);}
//		@Override public void waveDestroyed(Wave wave) {
//			MessageBox.show(FirstLightApplication.PrimaryStage.getScene().getWindow(), "Wave Operation", "Destroyed", MessageBox.ICON_INFORMATION);					
//		}		        			        	
//    });
//    
//    loadDashboardWave.setStatus(Status.Sent);
//
//    // Use the JRebirth Thread to manage Waves
//    JRebirth.runIntoJIT(new AbstractJrbRunnable("Send Wave " + loadDashboardWave.toString()) {
//        @Override
//        public void runInto() throws JRebirthThreadException {
//        	getModel().getLocalFacade().getGlobalFacade().getNotifier().sendWave(loadDashboardWave);
//        }
//    });
    
    
    
    
    
    
    
    
    
    
	
	
	
	
	
	

}
