package dev.codethat.moneyplant.core.spring.coverter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;

import javax.inject.Inject;
import javax.inject.Named;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Named
@ConfigurationPropertiesBinding
public class DateConverter implements Converter<String, Date> {
    private static DateFormat sdf = new SimpleDateFormat("dd-MM-yy");
    @Override
    public Date convert(final String s) {
        Date date = null;
        try {
            date = sdf.parse(s);
        } catch (ParseException e) {
            log.error("Exception occurred. Message={}", e.getMessage());
        }
        return date;
    }
}
