package com.trujidev.literalura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "authors")
public class Author {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private int birth_year;
  private int death_year;
  @OneToMany(mappedBy = "author")
  private List<Book> book;

  public Author() {}

  public Author(String name, int birth_year, int death_year) {
    this.name = name != null ? name : "Autor Desconocido";
    this.birth_year = Optional.ofNullable(birth_year).orElse(0);
    this.death_year = Optional.ofNullable(death_year).orElse(0);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getBirth_year() {
    return birth_year;
  }

  public void setBirth_year(int birth_year) {
    this.birth_year = birth_year;
  }

  public int getDeath_year() {
    return death_year;
  }

  public void setDeath_year(int death_year) {
    this.death_year = death_year;
  }

  @Override
  public String toString() {
    return "{" +
        "death_year=" + death_year +
        ", birth_year=" + birth_year +
        ", name='" + name + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Author author = (Author) o;
    return birth_year == author.birth_year && death_year == author.death_year && Objects.equals(id, author.id) && Objects.equals(name, author.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, birth_year, death_year);
  }
}
