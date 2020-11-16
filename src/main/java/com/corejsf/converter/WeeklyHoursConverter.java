package com.corejsf.converter;

import java.math.BigDecimal;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("weeklyHours")
/**
 * Converts BigDecimal to a valid string
 *
 * @author yogeshverma
 *
 */
public class WeeklyHoursConverter implements Converter<BigDecimal> {

    @Override
    public BigDecimal getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, BigDecimal value) {
        // TODO Auto-generated method stub
        return value.toPlainString();
    }

}
