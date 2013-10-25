package com.firstlight.ui.menu;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class CustomControlTest extends AnchorPane {

	
	
	public CustomControlTest(){
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CustomControlTest.fxml"));

		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
		
		try { 			
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		} 		
	}
	
	
}
