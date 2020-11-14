/**
 *
 */
package com.corejsf.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * @author Yogesh Verma and Sung Na
 *
 */
public class CustomExceptionHandlerFactory extends ExceptionHandlerFactory {
    private final ExceptionHandlerFactory parent;

    // this injection handles jsf
    @SuppressWarnings("deprecation")
    public CustomExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    /**
     * Returns a newly created instance of the exception handler
     */
    public ExceptionHandler getExceptionHandler() {

        final ExceptionHandler handler = new CustomExceptionHandler(parent.getExceptionHandler());

        return handler;
    }

}