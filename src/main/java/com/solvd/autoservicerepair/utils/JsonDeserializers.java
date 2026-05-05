package com.solvd.autoservicerepair.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonDeserializers {

    public static class LocalDateDeserializer extends StdDeserializer<LocalDate> {

        public LocalDateDeserializer() {
            super(LocalDate.class);
        }

        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctx)
                throws IOException {
            return LocalDate.parse(p.getText());
        }

    }

    public static class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

        public LocalDateTimeDeserializer() {
            super(LocalDateTime.class);
        }
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            return null;
        }

    }

}


