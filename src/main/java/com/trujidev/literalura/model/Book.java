package com.trujidev.literalura.model;

import jakarta.persistence.*;

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
  private String language;
  private int downloadCount;

  public Book() {}

  public Book(BookData bookData, Author author) {
    this.title = bookData.title();
    this.author = author;
    this.language = bookData.language() != null && bookData.language().length > 0 ? bookData.language()[0] : null;
    this.downloadCount = bookData.download_count();
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

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public int getDownloadCount() {
    return downloadCount;
  }

  public void setDownloadCount(int downloadCount) {
    this.downloadCount = downloadCount;
  }

  @Override
  public String toString() {
    return "Book{" +
        "language=" + language +
        ", downloadCount=" + downloadCount +
        ", author=" + author +
        ", title='" + title + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Book book = (Book) o;
    return downloadCount == book.downloadCount && Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.deepEquals(language, book.language);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, author, language, downloadCount);
  }
}
