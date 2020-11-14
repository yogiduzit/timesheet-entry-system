/**
 *
 */
package com.corejsf.exception;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 * Helper class to handle exceptions
 *
 * @author Yogesh Verma and Sung Na
 *
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {
    private final ExceptionHandler wrapped;

    @SuppressWarnings("deprecation")
    CustomExceptionHandler(ExceptionHandler exception) {
        wrapped = exception;
    }

    @Override
    /**
     * Returns the exception handler
     */
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    /**
     * Handles the thrown exception
     */
    public void handle() throws FacesException {

        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        while (i.hasNext()) {
            final ExceptionQueuedEvent event = i.next();
            final ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            // get the exception from context
            final Throwable t = context.getException();

            final FacesContext fc = FacesContext.getCurrentInstance();

            // here you do what ever you want with exception
            try {

                fc.addMessage(null, new FacesMessage(t.getLocalizedMessage()));

            } finally {
                // remove it from queue
                i.remove();
            }
        }
        // parent hanle
        getWrapped().handle();
    }
}