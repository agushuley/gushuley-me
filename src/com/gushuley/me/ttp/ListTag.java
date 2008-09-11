package com.gushuley.me.ttp;

import com.gushuley.java.utils.ArrayList;
import com.gushuley.java.utils.List;

public class ListTag 
extends TypedTag 
{
	final List list = new ArrayList();
	
	public ListTag(short tag) {
		super(TypedTagPacket.TYPE_LIST, tag);		 
	}

	public List getList() {
		return list;
	}
}
