package com.gushuley.me.ttp;

public class TTPTools {
	public static String getString(TypedTag t) throws TTCastException {
		if (t.getType() == TypedTagPacket.TYPE_STRING) {
			return ((StringTag)t).getData();
		} else if (t.getType() == TypedTagPacket.TYPE_INT) {
			return "" + ((IntTag)t).getData();
		} else if (t.getType() == TypedTagPacket.TYPE_BYTE) {
			return "" + ((ByteTag)t).getData();
		} else if (t.getType() == TypedTagPacket.TYPE_SHORT) {
			return "" + ((ShortTag)t).getData();
		} else if (t.getType() == TypedTagPacket.TYPE_SHORT) {
			return "" + ((ShortTag)t).getData();
		}
		throw new TTCastException("Can't cast to string type " + t.getType() + " of tag " + t.getTag());
	}

	public static int getInt(TypedTag t) throws TTCastException {
		if (t.getType() == TypedTagPacket.TYPE_INT) {
			return ((IntTag)t).getData();
		} else if (t.getType() == TypedTagPacket.TYPE_BYTE) {
			return ((ByteTag)t).getData();
		} else if (t.getType() == TypedTagPacket.TYPE_SHORT) {
			return ((ShortTag)t).getData();
		}
		throw new TTCastException("Can't cast to int type " + t.getType() + " of tag " + t.getTag());
	}

	public static short getShort(TypedTag t) throws TTCastException {
		if (t.getType() == TypedTagPacket.TYPE_BYTE) {
			return ((ByteTag)t).getData();
		} else if (t.getType() == TypedTagPacket.TYPE_SHORT) {
			return ((ShortTag)t).getData();
		}
		throw new TTCastException("Can't cast to short type " + t.getType() + " of tag " + t.getTag());
	}

	public static byte getByte(TypedTag t) throws TTCastException {
		if (t.getType() == TypedTagPacket.TYPE_BYTE) {
			return ((ByteTag)t).getData();
		} 
		throw new TTCastException("Can't cast to byte type " + t.getType() + " of tag " + t.getTag());
	}

}
