package com.fixiu.jdbc.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

/**
 * 封装运行异常为CommonException.
 *
 * @author dongyushuai
 */
public class CommonException extends RuntimeException {
	private static final Logger logger = LoggerFactory.getLogger(CommonException.class);
	private static final long serialVersionUID = 5044938065901970022L;

	private final transient Object[] parameters;

	private String msg;

	/**
	 * 构造器
	 *
	 * @param msg        异常msg
	 * @param parameters parameters
	 */
	public CommonException(String msg, Object... parameters) {
		super(msg);
		this.parameters = parameters;
		this.msg = parameters == null ? MessageFormatter.format(msg, parameters).getMessage() : msg;
	}

	public CommonException(String msg, Throwable cause, Object... parameters) {
		super(msg, cause);
		this.parameters = parameters;
		this.msg = parameters == null ? MessageFormatter.format(msg, parameters).getMessage() : msg;
	}

	public CommonException(String msg, Throwable cause) {
		super(msg, cause);
		this.msg = msg;
		this.parameters = new Object[] {};
	}

	public CommonException(Throwable cause, Object... parameters) {
		super(cause);
		this.parameters = parameters;
	}

	public Object[] getParameters() {
		return parameters;
	}

	@Override
	public String getMessage() {
		return msg;
	}

	public String getTrace() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = null;
		try {
			ps = new PrintStream(baos, false, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			logger.error("Error get trace, unsupported encoding.", e);
			return null;
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
		this.printStackTrace(ps);
		ps.flush();
		return new String(baos.toByteArray(), StandardCharsets.UTF_8);
	}
}
