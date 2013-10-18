package com.firstlight.service;


public interface IMenuItemRequestor {
	
	String getMenuItemKey();
	void setMenuItemKey(String menuItemKey);
	
	void requestMenuItems(IMenuService menuService);
	void withdrawMenuItems(IMenuService menuService);

}
