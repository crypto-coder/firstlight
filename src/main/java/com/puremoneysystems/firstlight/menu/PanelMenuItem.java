/**
 * 
 */
package com.puremoneysystems.firstlight.menu;

import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.CustomMenuItem;

import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;

import javafx.fxml.FXML;


/**
 * @author chris
 *
 */
public class PanelMenuItem extends Control {

	private PanelMenuItem parentMenuItem;
	private List<PanelMenuItem> childMenuItems;
	
	@FXML 
	private ImageView panelMenuItemImage;
	@FXML 
	private Label panelMenuItemLabel;

	

	public PanelMenuItem getParentMenuItem(){
		return this.parentMenuItem;
	}
	private void setParentMenuItem(PanelMenuItem newParentMenuItem){
		this.parentMenuItem = newParentMenuItem;		
	}
	
	
	public List<PanelMenuItem> getChildMenuItems(){
		return this.childMenuItems;
	}
	private void setChildMenuItems(List<PanelMenuItem> newChildMenuItems){
		this.childMenuItems = newChildMenuItems;		
	}

	
	public Image getImage() {
		return this.panelMenuItemImage.getImage();
	}
	public void setImage(Image newImage) {
		this.panelMenuItemImage.setImage(newImage);
	}
	public void setImage(String resourceBundlePathToImage) {
		if(this.panelMenuItemImage != null){
			this.panelMenuItemImage.setImage(new Image(resourceBundlePathToImage));
		}
	}

	public String getLabelText() {
		return this.panelMenuItemLabel.getText();
	}
	public void setLabelText(String newLabelText) {
		this.panelMenuItemLabel.setText(newLabelText);
	}
	
	


//	public string Text
//	{
//		get;
//		set;
//	}
//	
//	
//	
//	public ICommand InvocationCommand
//	{
//		get;
//		private set;
//	}
//
//
//
//	//
//	// Constructors
//	//
//
//	public MenuItem (string text, ICommand invocationCommand, Image image) : this (text, invocationCommand)
//	{
//		this.Text = text;
//		this.Image = image;
//		this.InvocationCommand = invocationCommand;
//	}
//
//	public MenuItem (string text, ICommand invocationCommand) : this ()
//	{
//		this.Text = text;
//		this.InvocationCommand = invocationCommand;
//	}
//
//	public MenuItem ()
//	{
//		this.ChildMenuItems = new List<MenuItem> ();
//	}
//
//	//
//	// Methods
//	//
//
//	public void AddChildMenuItem (MenuItem item)
//	{
//		if (!this.ChildMenuItems.Contains (item))
//		{
//			this.ChildMenuItems.Add (item);
//			item.ParentMenuItem = this;
//		}
//	}
//
//	public void ClearChildMenuItems ()
//	{
//		this.ChildMenuItems.Clear ();
//	}
//
//	public void InsertChildMenuItem (int index, MenuItem item)
//	{
//		if (!this.ChildMenuItems.Contains (item))
//		{
//			this.ChildMenuItems.Insert (index, item);
//			item.ParentMenuItem = this;
//		}
//	}
//
//	public bool RemoveChildMenuItem (MenuItem item)
//	{
//		return this.ChildMenuItems.Contains (item) && this.ChildMenuItems.Remove (item);
//	}
//
//	public void RemoveChildMenuItemAt (int index)
//	{
//		this.ChildMenuItems.RemoveAt (index);
//	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
