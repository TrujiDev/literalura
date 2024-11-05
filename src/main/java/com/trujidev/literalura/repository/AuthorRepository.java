package com.trujidev.literalura.repository;

import com.trujidev.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
  List<Author> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(int birthYear, int deathYear);
}
