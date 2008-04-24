/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gushuley.me.ui;

import javax.microedition.lcdui.*;

/**
 *
 * @author andriy
 */
public abstract class CommonForm extends Form implements Showable, CommandListener {
    protected final Display display;
    protected final Showable backForm;
    
    public CommonForm(String title, Display display, Showable back) {
        super(title);
        
        this.display = display;
        this.backForm = back;
        
        setCommandListener(this);
    }

    public void show() {
    	System.gc();
        display.setCurrent(this);
    }

	public Displayable getDisplayable() {
		return this;
	}

	public abstract void commandAction(Command c, Displayable d);
}
