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

import com.firstlight.command.RegionCommand;
import com.firstlight.ui.wallet.KnownWalletModel;
import com.firstlight.wave.RegionAction;
import com.firstlight.wave.RegionWaveBean;

/**
 * @author MrMoneyChanger
 *
 */
public class ShowDashboardCommand extends DefaultCommand {

	@Override
	public void ready() throws CoreException {
		super.ready();
	}

	@Override
	protected void execute(Wave wave) throws CommandException {
		super.execute(wave);

        //Build a RegionWave and call the RegionCommand
        RegionWaveBean showWaveBean = new RegionWaveBean(RegionAction.show, KnownWalletModel.class, "activeScreenRegion");
        this.callCommand(RegionCommand.class, showWaveBean);  		
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
