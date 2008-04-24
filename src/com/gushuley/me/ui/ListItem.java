package com.gushuley.me.ui;

/**
 *
 * @author andriy
 */
public class ListItem {
    public static final byte ITEM_TYPE_COMMAND = 1;
    public static final byte ITEM_TYPE_TRADE_MARK = 2;
    public static final byte ITEM_TYPE_PRODUCT = 3;
    
    private final int type;
    private final String caption;
    private final String icon;
    private final String data;
    
    public ListItem(byte type, String caption, String icon, String data) {
        this.type = type;
        this.caption = caption;
        this.icon = icon;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public String getCaption() {
        return caption;
    }

    public String getIcon() {
        return icon;
    }

    public String getData() {
        return data;
    }

	public static int copyItems(ListItem[] to, int from, ListItem[] arr) {
	    return copyItems(to, from, arr, arr.length);
	}

	public static int copyItems(ListItem[] to, int from, ListItem[] arr, int size) {
	    for (int i = 0; i < size; i++) {
	        to[from + i] = arr[i];
	    }
	    return from + size;
	}

	public static void reverse(ListItem[] oi, int i) {
		for (int j = 0; j < i / 2; j++) {
			ListItem t = oi[j];
			oi[j] = oi[i - 1 - j];
			oi[i - 1 - j] = t;
		}		
	}
}
