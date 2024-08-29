package br.com.itau.CasePix.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateConverter implements Converter<String, LocalDateTime> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public LocalDateTime convert(String source) {
        LocalDate date = LocalDate.parse(source, DATE_FORMATTER);
        return date.atStartOfDay();
    }
}
