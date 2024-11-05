package com.trujidev.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookData(
  String title,
  @JsonProperty("authors") List<AuthorData> author,
  @JsonProperty("languages") String[] language,
  int download_count
) {
}
