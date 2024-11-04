package com.trujidev.literalura.contracts;

public interface IJsonParser {
  <T> T fromJson(String json, Class<T> tClass);
}
