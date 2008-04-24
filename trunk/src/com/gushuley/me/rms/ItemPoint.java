package com.gushuley.me.rms;

import java.io.*;

public interface ItemPoint {
	void readData(DataInputStream is) throws IOException;
	void writeData(DataOutputStream os) throws IOException;
}
