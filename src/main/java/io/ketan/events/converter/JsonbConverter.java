package io.ketan.events.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Collections;
import java.util.List;

@Converter
public class JsonbConverter implements AttributeConverter<List<?>, String> {

  private static final ObjectMapper mapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<?> attribute) {
    try {
      return mapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Failed to convert JSONB attribute to String", e);
    }
  }

  @Override
  public List<?> convertToEntityAttribute(String dbData) {
    try {
      if (dbData == null || dbData.isBlank()) return Collections.emptyList();
      return mapper.readValue(dbData, List.class);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Failed to parse JSONB String to attribute", e);
    }
  }
}