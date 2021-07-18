package hu.szikorazoltan.validation;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class DateFormatHandler extends StdDeserializer<LocalDate> {

  @Autowired
  private MessageSource messages;

  public DateFormatHandler() {
    this(null);
  }

  public DateFormatHandler(Class<?> clazz) {
    super(clazz);
  }

  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Override
  public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    LocalDate localDate = null;
    try {
      localDate = LocalDate.parse(p.getText(), formatter);
      return localDate;
    } catch (Exception e) {
      final Locale locale = LocaleContextHolder.getLocale();
      throw new MyException(messages.getMessage("validation.dateOfBirth.dateFormat", null, locale));
    }
  }

}
