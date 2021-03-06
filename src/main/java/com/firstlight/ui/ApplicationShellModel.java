package com.firstlight.ui;


import org.jrebirth.core.command.CommandBean;
import org.jrebirth.core.ui.fxml.*;
import org.jrebirth.core.wave.WaveBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firstlight.command.RegionCommand;
import com.firstlight.service.IMenuService;
import com.firstlight.service.MenuService;
import com.firstlight.ui.dashboard.DashboardModel;
import com.firstlight.ui.wallet.KnownWalletModel;
import com.firstlight.wave.ChangeMenuWaveBuilder;
import com.firstlight.wave.MenuAction;
import com.firstlight.wave.RegionAction;
import com.firstlight.wave.RegionWaveBean;
import com.fxexperience.javafx.scene.control.InputField;

import javafx.beans.property.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * The class <strong>ApplicationStateModel</strong>.
 * 
 */
public final class ApplicationShellModel extends DefaultFXMLModel<ApplicationShellModel> {

    /** The class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationShellModel.class);
    private IMenuService menuService = null;
    
    
    public ApplicationShellModel(){
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFXMLPath() {
        return "/fxml/Application.fxml";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fxmlPreInitialize() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initModel() {
    	this.menuService = (IMenuService)this.getService(MenuService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInnerModels() {
        // Put the code to initialize inner models here (if any)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bind() {
        // Put the code to manage model object binding (if any)
    }
    
    

    
    @SuppressWarnings("unchecked")
	@Override
    protected void showView() {
    	super.showView();

    	ChangeMenuWaveBuilder.create()
							.action(MenuAction.add)
    						.menuItemKey("HOME")
    						.text("Home")
    						.imageURL("target/classes/images/Home.png")
    						.commandClass((Class<? extends CommandBean<WaveBean>>)RegionCommand.class)
    						.commandWaveBean(new RegionWaveBean(RegionAction.show, DashboardModel.class, "activeScreenRegion"))
    						.callback(HOME_CLICKED)
    						.buildAndSend(this);
    }

    
    @Override
    protected void hideView() {
    	super.hideView();

    	ChangeMenuWaveBuilder.create()
    						.action(MenuAction.remove)
    						.menuItemKey("HOME")
    						.buildAndSend(this);
    }
    
    
    
    
    
    public static final Callback<MouseEvent, Boolean> HOME_CLICKED = new Callback<MouseEvent, Boolean>() {
        /**
         * Home link was clicked
         * 
         * @param event the mouse event triggered
         * 
         * @return true for single click
         */
        @Override
        public Boolean call(final MouseEvent event) {
        	return true;
        }
    };
    
    
    
}
