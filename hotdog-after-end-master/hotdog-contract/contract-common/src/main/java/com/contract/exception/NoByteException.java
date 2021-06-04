package com.contract.exception;

/**
 * <p>File：NewsService.java</p>
 * <p>Description: ${description}</p>
 * <p>Copyright: Copyright (c) 2019/3/14 14:06</p>
 * <p>Company: zzex</p>
 *
 * @author yukai
 * @Version: $version
 */
public class NoByteException extends Throwable {
    private static final long serialVersionUID = -8601145084304417269L;

    public NoByteException()
    {
        super();
    }

    public NoByteException(String msg)
    {
        super(msg);
    }

    public NoByteException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

    public NoByteException(Throwable cause)
    {
        super(cause);
    }
}
