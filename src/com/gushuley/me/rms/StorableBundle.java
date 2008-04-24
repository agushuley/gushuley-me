package com.gushuley.me.rms;

import java.io.*;

import javax.microedition.rms.*;

public abstract class StorableBundle {
	protected final RecordStore rs;
	private final String name;
	
	public StorableBundle(String name) throws StorableException {
		try {
			this.name = name;
			this.rs = RecordStore.openRecordStore(name, true);
		} catch (RecordStoreException e) {
			throw new StorableException(e.getMessage());
		}
	}
	
	public void close() throws StorableException {
		try {
			rs.closeRecordStore();
		} catch (RecordStoreException e) {
			throw new StorableException(e.getMessage());
		}
	}

    public Storable getRecord(int storeId) throws StorableException {
        return loadStorable(storeId);
    }
	
	private class StorableEnumerationInt implements StorableEnumeration {
		private RecordEnumeration re;

		public StorableEnumerationInt() throws StorableException {
			try {
				this.re = rs.enumerateRecords(null, null, false);				
			} catch (RecordStoreNotOpenException e) {
				throw new StorableException(e.getMessage());
			}
		}

		/* (non-Javadoc)
		 * @see ua.fenomen.osp.rms.StorableEnumeration#hasNext()
		 */
		public boolean hasNext() {
			return re.hasNextElement();
		}
		
		/* (non-Javadoc)
		 * @see ua.fenomen.osp.rms.StorableEnumeration#nextStorable()
		 */
		public Storable nextStorable() throws StorableException {
			if (!re.hasNextElement()) {
				throw new StorableException("Next element is not available");
			}
			try {
				return loadStorable(re.nextRecordId());
			} catch (RecordStoreException e) {
				throw new StorableException(e.getMessage());
			}
		}

		public void close() {
			re.destroy();
		}		
	}

	public StorableEnumeration enumerate() throws StorableException {
		return new StorableEnumerationInt();
	}
	
	private Storable loadStorable(int recordId) throws StorableException {
		final Storable st = createStorable();
		try {
			byte[] data = rs.getRecord(recordId);
			if (data == null) {
				return null;
			}
			ByteArrayInputStream is = new ByteArrayInputStream(data);
			try {
				DataInputStream s = new DataInputStream(is);
				try {
					int tag = s.read();
					while (tag > 0) {					
						for (int i = 0; i < st.getBindings().length; i++) {
							ItemPointBind b = st.getBindings()[i];
							if (b.getTag() == tag) {
								b.getPoint().readData(s);
							}
						}
						tag = s.read();
					}
				} finally {
					s.close();
				}
			}
			finally {
				is.close();
			}
			st.setRecordId(recordId);
		} catch (IOException e) {
			throw new StorableException(e.getMessage());
		} catch (RecordStoreNotFoundException e) {
			throw new StorableException(name + " Данные не найдены");
		} catch (RecordStoreException e) {
			throw new StorableException(e.getMessage());
		}
		return st;
	}

	public Storable find(Object key) throws StorableException {
		final StorableEnumeration e = enumerate();
		while (e.hasNext()) {
			final Storable s = e.nextStorable(); 
			if (s.isKey(key)) {
				return s;
			}
		}
		return null;
	}

	protected abstract Storable createStorable();

	public int getNumRecords() throws StorableException {
		try {
			return rs.getNumRecords();
		} catch (RecordStoreNotOpenException e) {
			throw new StorableException(e.getMessage());
		}
	}

	public void clearStore() throws StorableException {
		try {
		    RecordEnumeration e = rs.enumerateRecords(null, null, true);
		    while (e.hasNextElement()) {
				rs.deleteRecord(e.nextRecordId());
		    }
		} catch (RecordStoreException e1) {
			throw new StorableException(e1.getMessage());
		}
	}

	public void store(Storable item) throws StorableException {
		if (item.getClass() != createStorable().getClass()) {
			throw new StorableException("Store cant't contains items of class " + item.getClass().getName());
		}
		try {
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				final DataOutputStream ds = new DataOutputStream(os);				
				try {
					for (int i = 0; i < item.getBindings().length; i++) {						
						final ItemPointBind b = item.getBindings()[i];
						if (!b.isExpired()) {
							ds.writeByte(b.getTag());
							b.getPoint().writeData(ds);
						}
					}
					ds.flush();
					os.flush();
					final byte[] data = os.toByteArray();
					if (item.getRecordId() == 0) {
						item.setRecordId(rs.addRecord(data, 0, data.length));
					} else {
						rs.setRecord(item.getRecordId(), data, 0, data.length);
					}
				} finally {
					ds.close();
				}
			} finally {
				os.close();
			}
		} catch (IOException e) {
			throw new StorableException(name + " IO Error");
		} catch (RecordStoreNotOpenException e) {
			throw new StorableException(name + " store is not opened");
		} catch (InvalidRecordIDException e) {
			throw new StorableException(name + " referenced invalid ID");
		} catch (RecordStoreFullException e) {
			throw new StorableException(name + " not enought space");
		} catch (RecordStoreException e) {
			throw new StorableException(name + " undefined error");
		}
	}
}
