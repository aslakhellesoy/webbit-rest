package org.webbitserver.rest;

public class ResourceException extends Throwable {
    public static final ResourceException METHOD_NOT_ALLOWED = new ResourceException(405);

    public final int status;

    public ResourceException(int status) {
        this.status = status;
    }
}
