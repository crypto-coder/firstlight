package com.puremoneysystems.firstlight;

import java.lang.Class;

import javafx.util.Callback;

import com.google.inject.Injector;

/**
* A JavaFX controller factory for constructing controllers via Guice DI. To
* install this in the {@link FXMLLoader}, pass it as a parameter to
* {@link FXMLLoader#setControllerFactory(Callback)}.
* <p>
* Once set, make sure you do <b>not</b> use the static methods on
* {@link FXMLLoader} when creating your JavaFX node.
*/
public class GuiceControllerFactory implements Callback<Class<?>, Object> {
 
	private final Injector injector;
 
	public GuiceControllerFactory(Injector anInjector) {
		injector = anInjector;
	}
 
	@Override
	public Object call(Class<?> aClass) {
		return injector.getInstance(aClass);
	}
}