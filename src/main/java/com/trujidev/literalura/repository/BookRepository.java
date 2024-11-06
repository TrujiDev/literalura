package com.trujidev.literalura.repository;

import com.trujidev.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
  List<Book> findByLanguage(String language);
  Long countByLanguage(String language);

  @Query("SELECT DISTINCT b.language FROM Book b")
  List<String> findDistinctLanguages();
}
