package com.corejsf.messages;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;

@ApplicationScoped
public class MessageProvider {

    private ResourceBundle bundle;

    public ResourceBundle getBundle() {
        if (bundle == null) {
            final FacesContext context = FacesContext.getCurrentInstance();
            bundle = context.getApplication().getResourceBundle(context, "msgs");
        }
        return bundle;
    }

    public String getValue(String key) {

        String result = null;
        try {
            result = getBundle().getString(key);
        } catch (final MissingResourceException e) {
            result = "???" + key + "??? not found";
        }
        return result;
    }

    public String getValue(String key, Object... args) {
        final MessageFormat formatter = new MessageFormat(" ");
        formatter.setLocale(FacesContext.getCurrentInstance().getViewRoot().getLocale());
        formatter.applyPattern(getValue(key));
        return formatter.format(args);
    }

}
