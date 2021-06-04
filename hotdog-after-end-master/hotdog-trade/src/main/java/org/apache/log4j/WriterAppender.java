package org.apache.log4j;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.QuietWriter;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.LoggingEvent;

import com.pmzhongguo.ex.core.utils.HelpUtils;


public class WriterAppender extends AppenderSkeleton {
	protected boolean immediateFlush;
	protected String encoding;
	protected QuietWriter qw;

	public WriterAppender() {
		this.immediateFlush = true;
	}

	public WriterAppender(Layout layout, OutputStream os) {
		this(layout, new OutputStreamWriter(os));
	}

	public WriterAppender(Layout layout, Writer writer) {
		this.immediateFlush = true;

		this.layout = layout;
		setWriter(writer);
	}

	public void setImmediateFlush(boolean value) {
		this.immediateFlush = value;
	}

	public boolean getImmediateFlush() {
		return this.immediateFlush;
	}

	public void activateOptions() {
	}

	public void append(LoggingEvent event) {
		if (!(checkEntryConditions())) {
			return;
		}
		subAppend(event);
	}

	protected boolean checkEntryConditions() {
		if (this.closed) {
			LogLog.warn("Not allowed to write to a closed appender.");
			return false;
		}

		if (this.qw == null) {
			this.errorHandler
					.error("No output stream or file set for the appender named ["
							+ this.name + "].");

			return false;
		}

		if (this.layout == null) {
			this.errorHandler.error("No layout set for the appender named ["
					+ this.name + "].");
			return false;
		}
		return true;
	}

	public synchronized void close() {
		if (this.closed)
			return;
		this.closed = true;
		writeFooter();
		reset();
	}

	protected void closeWriter() {
		if (this.qw == null)
			return;
		try {
			this.qw.close();
		} catch (IOException e) {
			LogLog.error("Could not close " + this.qw, e);
		}
	}

	protected OutputStreamWriter createWriter(OutputStream os) {
		OutputStreamWriter retval = null;

		String enc = getEncoding();
		if (enc != null) {
			try {
				retval = new OutputStreamWriter(os, enc);
			} catch (IOException e) {
				LogLog.warn("Error initializing output writer.");
				LogLog.warn("Unsupported encoding?");
			}
		}
		if (retval == null) {
			retval = new OutputStreamWriter(os);
		}
		return retval;
	}

	public String getEncoding() {
		return this.encoding;
	}

	public void setEncoding(String value) {
		this.encoding = value;
	}

	public synchronized void setErrorHandler(ErrorHandler eh) {
		if (eh == null) {
			LogLog.warn("You have tried to set a null error-handler.");
		} else {
			this.errorHandler = eh;
			if (this.qw != null)
				this.qw.setErrorHandler(eh);
		}
	}

	public synchronized void setWriter(Writer writer) {
		reset();
		this.qw = new QuietWriter(writer, this.errorHandler);

		writeHeader();
	}

	protected void subAppend(LoggingEvent event) {
		// 查询语句都不出来
		boolean isBigLogMode = true;
		try {
			isBigLogMode = (HelpUtils.getMgrConfig().getIs_use_big_log() == 1);
		} catch (Exception e) {
		}
		if (!isBigLogMode
				&& (event.getMessage().toString().toUpperCase().indexOf(
						"EXECUTING: SELECT") >= 0
						|| event.getMessage().toString().toUpperCase().indexOf(
								"COLUMNS:") >= 0
						|| event.getMessage().toString().toUpperCase().indexOf(
								"ROW:") >= 0
						|| event.getMessage().toString().toUpperCase().indexOf(
								"OOO CONNECTION OPENED") >= 0 || event
						.getMessage().toString().toUpperCase().startsWith(
								"SELECT"))) {
			return;
		}

		this.qw.write(this.layout.format(event));

		if (this.layout.ignoresThrowable()) {
			String[] s = event.getThrowableStrRep();
			if (s != null) {
				int len = s.length;
				for (int i = 0; i < len; ++i) {
					this.qw.write(s[i]);
					this.qw.write(Layout.LINE_SEP);
				}
			}
		}

		if (this.immediateFlush)
			this.qw.flush();
	}

	public boolean requiresLayout() {
		return true;
	}

	protected void reset() {
		closeWriter();
		this.qw = null;
	}

	protected void writeFooter() {
		if (this.layout != null) {
			String f = this.layout.getFooter();
			if ((f != null) && (this.qw != null)) {
				this.qw.write(f);
				this.qw.flush();
			}
		}
	}

	protected void writeHeader() {
		if (this.layout != null) {
			String h = this.layout.getHeader();
			if ((h != null) && (this.qw != null))
				this.qw.write(h);
		}
	}
}