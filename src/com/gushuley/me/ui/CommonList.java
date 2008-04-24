/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gushuley.me.ui;

import java.io.IOException;
import javax.microedition.lcdui.*;

import com.gushuley.me.Tools;

/**
 *
 * @author andriy
 */
public abstract class CommonList 
extends List 
implements CommandListener, Showable {
    protected final Display display;
    protected final Showable backForm;
    
    public CommonList(String title, Display display, Showable back) {
        super(title, List.IMPLICIT);
        this.display = display;
        this.backForm = back;
        
        this.setCommandListener(this);
        this.addCommand(SELECT_COMMAND);
    }

    public void commandAction(Command arg0, Displayable arg1) {
        if (arg0 == SELECT_COMMAND) {
            itemSelected(items[getSelectedIndex()]);
        }
    }
    
    private ListItem[] items = new ListItem[0];
    protected void setItems(ListItem[] newItems) {
        if (newItems == null) {
            throw new NullPointerException("Items list can't be null");
        }
        this.deleteAll();
        this.items = newItems;
        for (int i = 0; i < items.length; i++) {
            Image icon = null;
            if (!Tools.isEmpty(items[i].getIcon())) {
                try {
                    icon = Image.createImage(items[i].getIcon());
                } catch (IOException ex) { }
            }
            this.append(items[i].getCaption(), icon);
        }
    }

    protected abstract void itemSelected(ListItem listItem);
    public  void show() {
    	System.gc();
        display.setCurrent(this);
    }

	public Displayable getDisplayable() {
		return this;
	};
}
