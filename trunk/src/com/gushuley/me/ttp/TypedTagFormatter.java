package com.gushuley.me.ttp;

import java.io.*;
import java.util.*;

import com.gushuley.java.utils.*;
import com.gushuley.me.Tools;

public final class TypedTagFormatter 
{
	public static TypedTagPacket readStream(InputStream is) 
	throws TTPacketException 
	{
		final TypedTagPacket packet = new TypedTagPacket();
		
		final List tags = readTags(is);
		for (int i = 0; i < tags.size(); i++) {
			final TypedTag tag = (TypedTag) tags.get(i);
			if (tag.getTag() == TypedTagPacket.RESULT_CODE_TAG) {
				try {
					final ShortTag intTag = (ShortTag) tag;
					packet.setResultCode(intTag.getData());
				} catch (ClassCastException e) {
					throw new TTPacketException("ResultCode tag is not short");
				} 
			} else if (tag.getTag() == TypedTagPacket.MESSAGE_TAG) {
				try {
					final ShortTag intTag = (ShortTag) tag;
					packet.setMessage(intTag.getData());
				} catch (ClassCastException e) {
					throw new TTPacketException("Message tag is not short");
				} 
			} else if (tag.getTag() == TypedTagPacket.ERROR_TEXT_TAG) {
				try {
					final StringTag intTag = (StringTag) tag;
					packet.setErrorText(intTag.getData());
				} catch (ClassCastException e) {
					throw new TTPacketException("ErrorText tag is not string");
				}
			} else {
				packet.getTags().add(tag);
			}
		}
		
		return packet;
	}

	private static List readTags(InputStream is) throws TTPacketException {
		final List tags = new ArrayList();
		do {
			final short tag;
			try {
				byte[] tagByte = readSize(is, 2);
				tag = getShort(tagByte);
			} catch (EOFException e) { 
				break;
			} catch (IOException e) { 
				throw new TTPacketException("Error while reading tag header", e);
			}
			
			final byte type;
			try {
				byte[] tagByte = readSize(is, 1);
				type = tagByte[0];
			} catch (IOException e) { 
				throw new TTPacketException("Error while reading type of tag", e);
			}
			
			int length = readLen(is);
			
			final byte[] data;
			try {
				data = readSize(is, length);
			} catch (IOException e) { 
				throw new TTPacketException("Error while reading data of tag", e);
			}
			
			final TypedTag tagValue;
			switch (type) {
		    case TypedTagPacket.TYPE_BYTE:
				tagValue = new ByteTag(tag, getByte(data));
				break;
			case TypedTagPacket.TYPE_SHORT:
				tagValue = new ShortTag(tag, getShort(data));
				break;
			case TypedTagPacket.TYPE_INT:
				tagValue = new IntTag(tag, getInt(data));
				break;
			case TypedTagPacket.TYPE_BINARY:
				tagValue = new BinaryTag(tag, data);
				break;
			case TypedTagPacket.TYPE_DATE_TIME:
				tagValue = new DateTimeTag(tag, getTimeDate(data));
				break;
			case TypedTagPacket.TYPE_STRING:
				try {
					tagValue = new StringTag(tag, new String(data, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw new TTPacketException("Unsupported text encoding UTF-8");				
				}
				break;
			case TypedTagPacket.TYPE_LIST:
				final ListTag lt = new ListTag(tag);
				lt.getList().addAll(readTags(new ByteArrayInputStream(data)));
				tagValue = lt;
				break;
			default:
				throw new TTPacketException("Unknown packet type " + type);
			} 
			tags.add(tagValue);
		} while (true);
		return tags;
	}

	private static int readLen(InputStream is) throws TTPacketException {
		int length = 0;
		byte[] len;
		do {
			try {
				len = readSize(is, 1);
			} catch (IOException e) { 
				throw new TTPacketException("Error while reading size of tag", e);
			}
			if (len[0] < 0) {
				length  = length * 127 - len[0];
			} else {
				length = length * 127 + len[0];					
			}
		} while (len[0] < 0);
		return length;
	}

	private static Date getTimeDate(byte[] data) throws TTPacketException {
		final ByteArrayInputStream stm = new ByteArrayInputStream(data);
		int size = readLen(stm);
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		{
			int date;
			try {
				date = getInt(readSize(stm, size));
			} catch (IOException e) {
				throw new TTPacketException("Error while reading date part tag", e);
			}
			final byte yearCoef;
			if (date < 0) {
				yearCoef = -1;
				date = -date;
			} else {
				yearCoef = 1;
			}
			cal.set(Calendar.DAY_OF_MONTH, date % 32);
			date = date / 32;
			cal.set(Calendar.MONTH, date % 12);
			date = date / 12;
			cal.set(Calendar.YEAR, 2000 + yearCoef * date);
		}
		{
			size = readLen(stm);
			int time;
			try {
				time = getInt(readSize(stm, size));
			} catch (IOException e) {
				throw new TTPacketException("Error while reading time part tag", e);
			}
			final int hour;
			if (time < 0) {
				time = -time;
				hour = 12 - time % 12;
				time = time / 12;
			} else {
				hour = 12 + time % 12;			
				time = time / 12;
			}
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, time % 60);
			time = time / 60;
			cal.set(Calendar.SECOND, time % 60);
		}	
		return cal.getTime();
	}

	private static int getInt(byte[] data) throws TTPacketException {
		if (data.length == 0) {
			return 0;
		} else if (data.length == 1) {
			return getByte(data);
		} else if (data.length == 2) {
	        return getShort(data);		
	    } else if (data.length == 4) {
	        long val = 0;
	        val = byteToLong(data[0]);
	        val = byteToLong(data[1]) << 8 | val;
	        val = byteToLong(data[2]) << 16 | val;
	        val = byteToLong(data[3]) << 24 | val;
	        return (int)val;
	    } else {
	    	throw new TTPacketException("Unresolved int size " + data.length);
	    }
	}

	private static short getShort(byte[] data) throws TTPacketException {
		if (data.length == 0) {
			return 0;
		} else if (data.length == 1) {
			return getByte(data);
		} else if (data.length == 2) {
	        long val = 0;
	        val = byteToLong(data[0]);
	        val = byteToLong(data[1]) << 8 | val;
	        return (short)val;		
	    } else {
	    	throw new TTPacketException("Unresolved short size " + data.length);
	    }
	}

	private static byte getByte(byte[] data) throws TTPacketException {
		if (data.length == 0) {
			return 0;
		} else if (data.length == 1) {
			return data[0];
		} else {
			throw new TTPacketException("Unresolved byte size " + data.length);			
		}
	}

    private static long byteToLong(byte b) {
        if (b < 0) {
            return 0xff + b + 1;
        }
        else {
            return b;
        }
    }
    
    private static byte longToByte(long i) throws TTPacketException {
        if (i > 0xff) {
            throw new TTPacketException("Vaue to long for byte" + i);
        }
        if (i > 0x7f) {
            i =  i - 0xff - 1;
        }
        return (byte)i;
    }

	private static byte[] readSize(InputStream is, int i) throws IOException {
		final byte[] buff = new byte[i];
		if (i == 0) {
			return buff;
		}
		int pos = 0;
		int r = 0;
		do {
			r = is.read(buff, 0, i - pos);
			if (r >= 0) {
				pos += r;
			} else {
				throw new EOFException();
			}
		} while (pos < i);
		return buff;
	}
	
	public static void writeToStream(TypedTagPacket packet, OutputStream os) 
	throws TTPacketException 
	{
		final List tags = new ArrayList();
		tags.add(new ShortTag(TypedTagPacket.RESULT_CODE_TAG, packet.getResultCode()));
		if (packet.getMessage() != 0) {
			tags.add(new ShortTag(TypedTagPacket.MESSAGE_TAG, packet.getMessage()));
		}
		if (!Tools.isEmpty(packet.getErrorText()) && packet.getResultCode() != 0) {
			tags.add(new StringTag(TypedTagPacket.ERROR_TEXT_TAG, packet.getErrorText()));			
		}
		tags.addAll(packet.getTags());
		writeTags(os, tags);
	}

	private static void writeTags(OutputStream os, List tags) throws TTPacketException {		
		for (int i = 0; i < tags.size(); i++) {
			final TypedTag tag = (TypedTag) tags.get(i);
			if (tag.getType() == TypedTagPacket.TYPE_BYTE) {
				writeInt(os, tag.getTag(), tag.getType(), ((ByteTag)tag).getData());
			} else if (tag.getType() == TypedTagPacket.TYPE_SHORT) {
				writeInt(os, tag.getTag(), tag.getType(), ((ShortTag)tag).getData());
			} else if (tag.getType() == TypedTagPacket.TYPE_INT) {
				writeInt(os, tag.getTag(), tag.getType(), ((IntTag)tag).getData());
			} else if (tag.getType() == TypedTagPacket.TYPE_BINARY) {
				writeBinary(os, tag.getTag(), tag.getType(), ((BinaryTag)tag).getData());
			} else if (tag.getType() == TypedTagPacket.TYPE_DATE_TIME) {
				writeDateTime(os, tag.getTag(), tag.getType(), ((DateTimeTag)tag).getData());
			} else if (tag.getType() == TypedTagPacket.TYPE_LIST) {
				final ByteArrayOutputStream stm = new ByteArrayOutputStream();
				writeTags(stm, ((ListTag)tag).getList());
				writeBinary(os, tag.getTag(), tag.getType(), stm.toByteArray());
			} else if (tag.getType() == TypedTagPacket.TYPE_STRING) {
				try {
					writeBinary(os, tag.getTag(), tag.getType(), ((StringTag)tag).getData().getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw new TTPacketException("Unsupported text encoding UTF-8");	
				}
			} else {
				throw new TTPacketException("Unknown tag type for serialize " + tag.getType());				
			}
		}		
	}

	private static void writeDateTime(OutputStream os1, short tag, byte type, Date data) throws TTPacketException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		{
			int date = cal.get(Calendar.YEAR) - 2000;
			final byte coef;
			if (date < 0) {
				coef = -1;
				date = -date;
			} else {
				coef = 1;
			}
			date = date * 12 + cal.get(Calendar.MONTH);
			date = date * 32 + cal.get(Calendar.DAY_OF_MONTH); 
			date = date * coef;
			try {
				writeIntWithLen(os, date);
			} catch (IOException e) {
				throw new TTPacketException("Error writing date part tag", e);
			}
		}
		{
			int time = cal.get(Calendar.SECOND);
			time = time * 60 + cal.get(Calendar.MINUTE); 
			time = time * 12; 
			final int hour = cal.get(Calendar.HOUR_OF_DAY) - 12;
			final byte coef;
			if (hour < 0) {
				coef = -1;
			} else {
				coef = 1;
			}
			time = coef * (time + coef * hour);
			try {
				writeIntWithLen(os, time);
			} catch (IOException e) {
				throw new TTPacketException("Error writing time part tag", e);
			}
		}
		writeBinary(os1, tag, type, os.toByteArray());
	}

	private static void writeBinary(OutputStream os, short tag, byte type, byte[] data) throws TTPacketException {
		try {
			os.write(shortToBytes(tag));
			os.write(type);
			writeLen(os, data.length);
			os.write(data);
		} catch (IOException e) {
			throw new TTPacketException("Error writing int tag", e);
		}
	}

	private static void writeInt(OutputStream os, short tag, byte type, int data) throws TTPacketException {
		try {
			os.write(shortToBytes(tag));
			os.write(type);
			writeIntWithLen(os, data);
		} catch (IOException e) {
			throw new TTPacketException("Error writing int tag", e);
		}
	}

	private static void writeIntWithLen(OutputStream os, int data)
			throws TTPacketException, IOException {
		if (data == 0) {
			writeLen(os, 0);
		} else if (data >= Byte.MIN_VALUE && data <= Byte.MAX_VALUE) {
			writeLen(os, 1);
			os.write(data);
		} else if (data >= Short.MIN_VALUE && data <= Short.MAX_VALUE) {
			writeLen(os, 2);
			os.write(shortToBytes((short)data));
		} else {
			writeLen(os, 4);			
			os.write(intToBytes(data));
		}
	}

	private static byte[] shortToBytes(short val) throws TTPacketException {
		byte[] stream = new byte[2];
        stream[0] = longToByte(val & 0xff);
        stream[1] = longToByte((val >> 8) & 0xff);		
		return stream;
	}

	private static byte[] intToBytes(int val) throws TTPacketException {
		byte[] stream = new byte[4];
        stream[0] = longToByte(val & 0xff);
        stream[1] = longToByte((val >> 8) & 0xff);
        stream[2] = longToByte((val >> 16) & 0xff);
        stream[3] = longToByte((val >> 24) & 0xff);
        return stream;
	}

	private static void writeLen(OutputStream os, int i) throws TTPacketException {
		if (i < 0) {
			throw new TTPacketException("Len can't be negative");
		}
		int writen;
		do {
			writen = i % 127;
			i = i / 127;
			if (i > 0) {
				writen = -writen;
			}
			try {
				os.write(writen);
			} catch (IOException e) {
				throw new TTPacketException("Error writting len: ", e);
			}
		} while(writen < 0);
	}
}
