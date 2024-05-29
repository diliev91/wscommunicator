package com.example.client;

import org.springframework.stereotype.Component;

@Component
public class DataFormatterUtil {
    public String format(String value) {
        if (value == null) {
            return "0";
        }
        String formattedPrice = value.replace(".", "");
        formattedPrice = formattedPrice.replaceFirst("^0+", "");
        return formattedPrice.isEmpty() ? "0" : formattedPrice;
    }
}
