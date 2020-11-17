package com.corejsf.messages;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;

@ApplicationScoped
/**
 * Provides access to messages from the message bundle
 *
 * @author yogeshverma
 *
 */
public class MessageProvider {

    /**
     * Bundle containing the message strings
     */
    private ResourceBundle bundle;

    /**
     * Get the resource bundle
     *
     * @return resource bundle containing strings
     */
    public ResourceBundle getBundle() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final Locale locale = context.getViewRoot().getLocale();
        if (bundle == null || !bundle.getLocale().equals(locale)) {
            bundle = ResourceBundle.getBundle("messages", locale);
        }
        return bundle;
    }

    /**
     * Gets the value of a translatable string
     *
     * @param key, the unique identifier of the translatable string
     * @return a string
     */
    public String getValue(String key) {

        String result = null;
        try {
            result = getBundle().getString(key);
        } catch (final MissingResourceException e) {
            result = "???" + key + "??? not found";
        }
        return result;
    }

    /**
     * Gets the value of a translatable string with variable parts
     *
     * @param key,  the unique identifier of the translatable string
     * @param args, an array containing the variable placeholders
     * @return a string
     */
    public String getValue(String key, Object... args) {
        final MessageFormat formatter = new MessageFormat(" ");
        formatter.setLocale(FacesContext.getCurrentInstance().getViewRoot().getLocale());
        formatter.applyPattern(getValue(key));
        return formatter.format(args);
    }

}
