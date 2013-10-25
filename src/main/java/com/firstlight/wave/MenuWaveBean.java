/**
 * 
 */
package com.firstlight.wave;

import org.jrebirth.core.wave.WaveBean;

import com.firstlight.ui.menu.MenuItemModel;

/**
 * @author MrMoneyChanger
 *
 */
public class MenuWaveBean implements WaveBean {

		private String key = null;
		private MenuItemModel menuItem = null;
		private MenuAction action = null;

		public MenuWaveBean(){}
		public MenuWaveBean(String key, MenuAction action, MenuItemModel menuItem){
			this.setKey(key);
			this.setAction(action);
			this.setMenuItem(menuItem);
		}
		
		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @param key the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}
		
		
		/**
		 * @return the menuItem
		 */
		public MenuItemModel getMenuItem() {
			return menuItem;
		}

		/**
		 * @param menuItem the menuItem to set
		 */
		public void setMenuItem(MenuItemModel menuItem) {
			this.menuItem = menuItem;
		}

		/**
		 * @return the action
		 */
		public MenuAction getAction() {
			return action;
		}

		/**
		 * @param action the action to set
		 */
		public void setAction(MenuAction action) {
			this.action = action;
		}

		
		
}
