package com.trujidev.literalura.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "author_id")
  private Author author;
  private String[] language;
  private int download_count;

  public Book() {}

  public Book(BookData bookData, Author author) {
    this.title = bookData.title();
    this.author = author;
    this.language = bookData.language();
    this.download_count = bookData.download_count();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public String[] getLanguage() {
    return language;
  }

  public void setLanguage(String[] language) {
    this.language = language;
  }

  public int getDownload_count() {
    return download_count;
  }

  public void setDownload_count(int download_count) {
    this.download_count = download_count;
  }

  @Override
  public String toString() {
    return "Book{" +
        "language=" + Arrays.toString(language) +
        ", download_count=" + download_count +
        ", author=" + author +
        ", title='" + title + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Book book = (Book) o;
    return download_count == book.download_count && Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.deepEquals(language, book.language);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, author, Arrays.hashCode(language), download_count);
  }
}
