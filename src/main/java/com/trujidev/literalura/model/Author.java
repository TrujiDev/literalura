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
  private int birthYear;
  private int deathYear;
  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
  private List<Book> book;

  public Author() {}

  public Author(String name, int birthYear, int deathYear) {
    this.name = name != null ? name : "Autor Desconocido";
    this.birthYear = Optional.ofNullable(birthYear).orElse(0);
    this.deathYear = Optional.ofNullable(deathYear).orElse(0);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getBirthYear() {
    return birthYear;
  }

  public void setBirthYear(int birthYear) {
    this.birthYear = birthYear;
  }

  public int getDeathYear() {
    return deathYear;
  }

  public void setDeathYear(int deathYear) {
    this.deathYear = deathYear;
  }

  @Override
  public String toString() {
    return "{" +
        "deathYear=" + deathYear +
        ", birthYear=" + birthYear +
        ", name='" + name + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Author author = (Author) o;
    return birthYear == author.birthYear && deathYear == author.deathYear && Objects.equals(id, author.id) && Objects.equals(name, author.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, birthYear, deathYear);
  }
}
