/**
 * 
 */
package com.firstlight.ui.dashboard;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import org.jrebirth.core.command.DefaultCommand;
import org.jrebirth.core.exception.CommandException;
import org.jrebirth.core.exception.CoreException;
import org.jrebirth.core.key.UniqueKey;
import org.jrebirth.core.ui.Model;
import org.jrebirth.core.wave.Wave;

/**
 * @author chris
 *
 */
public class ShowDashboardCommand extends DefaultCommand {

	@Override
	public void ready() throws CoreException {
		// TODO Auto-generated method stub
		super.ready();
	}

	@Override
	protected void execute(Wave wave) throws CommandException {
		// TODO Auto-generated method stub
		super.execute(wave);
		

//		Class<Model> dashboardModelClass = (Class<Model>) (DashboardModel.class).asSubclass(Model.class);
//		final UniqueKey<Model> dashboardModelKey = this.getModel().getLocalFacade().buildKey(dashboardModelClass);
//						
//        List<Node> activeScreenWrapper = new ArrayList<Node>();
//        activeScreenWrapper.add(this.activeScreenRegion);
//        final ObservableList<Node> dashboardUIViewBindPoint = FXCollections.observableList(activeScreenWrapper);
		
	}

	@Override
	public void preExecute(Wave wave) {
		// TODO Auto-generated method stub
		super.preExecute(wave);
	}

	@Override
	public void postExecute(Wave wave) {
		// TODO Auto-generated method stub
		super.postExecute(wave);
	}
	
	
	
	

}
