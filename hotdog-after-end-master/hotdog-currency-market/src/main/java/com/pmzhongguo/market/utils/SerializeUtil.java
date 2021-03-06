package com.pmzhongguo.market.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class SerializeUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(SerializeUtil.class);

	public static byte[] serialize(Object value) {

		if (value == null) {
			throw new NullPointerException("Can't serialize null");
		}
		byte[] rv = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;
		try {
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			os.writeObject(value);
			os.close();
			bos.close();
			rv = bos.toByteArray();
		} catch (IOException e) {
			throw new IllegalArgumentException("Non-serializable object", e);
		} finally {
			close(os);
			close(bos);
		}
		return rv;
	}

	public static Object deserialize(byte[] in) {
		Object rv = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream is = null;
		try {
			if (in != null) {
				bis = new ByteArrayInputStream(in);
				is = new ObjectInputStream(bis);
				rv = is.readObject();
				is.close();
				bis.close();
			}
		} catch (IOException e) {
			logger.warn("Caught IOException decoding %d bytes of data",
					in == null ? 0 : in.length, e);
		} catch (ClassNotFoundException e) {
			logger.warn("Caught CNFE decoding %d bytes of data", in == null ? 0
					: in.length, e);
		} finally {
			close(is);
			close(bis);
		}
		return rv;
	}

	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				logger.info("Unable to close %s", closeable, e);
			}
		}
	}
}