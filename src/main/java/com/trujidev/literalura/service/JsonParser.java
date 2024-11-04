package com.trujidev.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trujidev.literalura.contracts.IJsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonParser implements IJsonParser {
  private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);
  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public <T> T fromJson(String json, Class<T> tClass) {
    try {
      return objectMapper.readValue(json, tClass);
    } catch (JsonMappingException e) {
      logger.error("Error al procesar el JSON: el mapeo falló. Verifique que el JSON esté bien formado y "
          + "cumpla con la estructura esperada. Detalles del error: {}", e.getMessage(), e);
    } catch (JsonProcessingException e) {
      logger.error("Error al procesar el JSON: se produjo un error en el procesamiento. "
          + "Detalles del error: {}", e.getMessage(), e);
    }
    return null;
  }
}
