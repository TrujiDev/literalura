package com.trujidev.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthorData(
    @JsonProperty("name") String name,
    @JsonProperty("birth_year") int birth_year,
    @JsonProperty("death_year") int death_year
) {
}
