package com.trujidev.literalura.presentation;

import com.trujidev.literalura.model.Author;
import com.trujidev.literalura.model.Book;
import com.trujidev.literalura.model.BookData;
import com.trujidev.literalura.model.ResultData;
import com.trujidev.literalura.repository.AuthorRepository;
import com.trujidev.literalura.repository.BookRepository;
import com.trujidev.literalura.service.ApiClient;
import com.trujidev.literalura.service.JsonParser;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {
  Scanner sc = new Scanner(System.in);
  JsonParser jsonParser = new JsonParser();
  ApiClient apiClient = new ApiClient();

  private final AuthorRepository authorRepository;
  private final BookRepository bookRepository;
  private final String url = "https://gutendex.com/books";

  public Menu(BookRepository bookRepository, AuthorRepository authorRepository) {
    this.bookRepository = bookRepository;
    this.authorRepository = authorRepository;
  }

  public void showMenu() {
    var option = -1;
    while (option != 0) {
      var menu = """
        *** Menú Principal ***
        -----------------------
        [Búsqueda]
        [1] Buscar libros por título
        [2] Buscar autores por nombre
        
        [Historial]
        [3] Ver historial de búsquedas por idioma
        [4] Ver historial completo de búsquedas
        [5] Ver historial de autores registrados
        [6] Ver historial de autores vivos en un año específico
        
        [Estadísticas]
        [7] Ver la cantidad de libros por idioma
        [8] Ver el top 10 de libros más descargados
        [9] Ver estadísticas de descargas
        [10] Ver promedio de vida de los autores
  
        [0] Salir del programa
        ------------------------
        Por favor, selecciona una opción (0-6):
        """;
      System.out.println(menu);

      option = getUserOption();

      switch (option) {
        case 1:
          System.out.println("Buscando libros por título...");
          searchByTitle();
          break;
        case 2:
          System.out.println("Buscando autores por nombre...");
          searchByName();
          break;
        case 3:
          System.out.println("Consultando historial de búsqueda por idioma...");
          searchHistoryByLanguage();
          break;
        case 4:
          System.out.println("Consultando historial de búsqueda...");
          searchHistory();
          break;
        case 5:
          System.out.println("Consultando historial de autores...");
          authorsHistory();
          break;
        case 6:
          System.out.println("Consultando historial de autores vivos...");
          livingAuthors();
          break;
        case 7:
          System.out.println("Consultando cantidad de libros por idioma...");
          bookCountByLanguage();
          break;
        case 8:
          System.out.println("Consultando top 10 de libros más descargados...");
          topBooks();
          break;
        case 9:
          System.out.println("Consultando estadísticas de descargas...");
          showDownloadStatistics();
          break;
        case 10:
          System.out.println("Consultando promedio de vida de los autores...");
          showAuthorLifeSpanStatistics();
          break;
        case 0:
          System.out.print("¿Estás seguro de que quieres salir? (sí/no): ");
          String confirm = sc.nextLine().toLowerCase();
          if (confirm.equals("sí") || confirm.equals("si")) {
            System.out.println("Cerrando...");
            break;
          } else {
            continue;
          }
        default:
          System.out.println("Opción inválida. Por favor, ingresa un número entre 0 y 10.");
      }
    }
  }

  private int getUserOption() {
    int option = -1;
    while (option < 0 || option > 10) {
      if (sc.hasNextInt()) {
        option = sc.nextInt();
        sc.nextLine();
        if (option < 0 || option > 10) {
          System.out.println("Opción fuera de rango. Por favor, elige entre 0 y 10.");
        }
      } else {
        System.out.println("Entrada no válida. Por favor, ingresa un número.");
        sc.nextLine();
      }
    }
    return option;
  }


  private Optional<BookData> fetchBookData(String input) {
    var json = apiClient.fetchData(url + "/?search=" + input);
    ResultData resultData = jsonParser.fromJson(json, ResultData.class);
    return resultData.results().stream().findFirst();
  }

  private void searchByTitle() {
    System.out.print("¿Qué título le interesa?: ");
    var title = sc.nextLine().replace(" ", "%20").toLowerCase();
    var result = fetchBookData(title);

    if (result.isPresent()) {
      var data = result.get();

      Author author = result.stream()
          .flatMap(r -> r.author().stream()
              .map(a -> new Author(a.name(), a.birth_year(), a.death_year())))
          .findFirst()
          .orElse(null);

      Book book = new Book(data, author);
      System.out.println(book);
      bookRepository.save(book);
    } else {
      System.out.println("No se encontraron resultados para el título: " + title);
    }
  }

  public void searchHistoryByLanguage() {
    System.out.print("Escribe el código del idioma en el que quieres ver los libros: ");
    var languageCode = sc.nextLine().toLowerCase().trim();

    List<Book> books = bookRepository.findByLanguage(languageCode);

    for (Book book : books) {
      System.out.println(
          "-------------------------------------------\n" +
              "Título: " + book.getTitle() + "\n" +
              "Autor: " + book.getAuthor().getName() +
              " (Año de nacimiento: " + book.getAuthor().getBirthYear() +
              ", Año de muerte: " + book.getAuthor().getDeathYear() + ")\n" +
              "Idioma: " + book.getLanguage() + "\n" +
              "Número de descargas: " + book.getDownloadCount() + "\n" +
              "-------------------------------------------"
      );
    }
  }

  public void searchHistory() {
    List<Book> books = bookRepository.findAll();

    if (books.isEmpty()) {
      System.out.print("No hay libros en la base de datos.");
    }

    for (Book book : books) {
      System.out.println(
          "-------------------------------------------\n" +
              "Título: " + book.getTitle() + "\n" +
              "Autor: " + book.getAuthor().getName() +
              " (Año de nacimiento: " + book.getAuthor().getBirthYear() +
              ", Año de muerte: " + book.getAuthor().getDeathYear() + ")\n" +
              "Idioma: " + book.getLanguage() + "\n" +
              "Número de descargas: " + book.getDownloadCount() + "\n" +
              "-------------------------------------------"
      );
    }
  }

  public void authorsHistory() {
    List<Author> authors = authorRepository.findAll();

    if (authors.isEmpty()) {
      System.out.println("No hay autores en la base de datos.");
      return;
    }

    System.out.println("Listado de Autores:");
    System.out.println("-------------------------------------------");
    for (Author author : authors) {
      System.out.printf("Nombre: %s\nAño de Nacimiento: %d\nAño de Fallecimiento: %d\n",
          author.getName(), author.getBirthYear(), author.getDeathYear());
      System.out.println("-------------------------------------------");
    }
  }

  public void livingAuthors() {
    System.out.print("Introduce el año para consultar autores vivos: ");
    int year = sc.nextInt();
    sc.nextLine();

    if (year < 0) {
      System.out.println("El año no puede ser negativo. Inténtalo de nuevo.");
      return;
    }

    List<Author> authors = authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqualOrDeathYearIsNull(year, year);

    if (authors.isEmpty()) {
      System.out.println("No hay autores vivos en el año " + year);
      return;
    }

    System.out.println("Listado de Autores Vivos en " + year + ":");
    System.out.println("-------------------------------------------");
    for (Author author : authors) {
      System.out.printf("Nombre: %s\nAño de Nacimiento: %d\nAño de Fallecimiento: %s\n",
          author.getName(), author.getBirthYear(), author.getDeathYear() != -1 ? author.getDeathYear() : "Aún vivo");
      System.out.println("-------------------------------------------");
    }
  }


  public void bookCountByLanguage() {
    List<String> languages = bookRepository.findDistinctLanguages();

    if (languages.isEmpty()) {
      System.out.println("No hay libros en la base de datos.");
      return;
    }

    System.out.println("Idiomas disponibles:");
    for (int i = 0; i < languages.size(); i++) {
      System.out.println((i + 1) + " - " + languages.get(i));
    }

    System.out.print("Selecciona el número del idioma para ver la cantidad de libros: ");
    int languageOption = sc.nextInt();
    sc.nextLine();

    if (languageOption < 1 || languageOption > languages.size()) {
      System.out.println("Opción inválida.");
      return;
    }

    String selectedLanguage = languages.get(languageOption - 1);

    long count = bookRepository.countByLanguage(selectedLanguage);

    System.out.println("Número total de libros en el idioma '" + selectedLanguage.toUpperCase() + "': " + count);
  }

  public void topBooks() {
    List<Book> bookList = bookRepository.findTop10ByOrderByDownloadCountDesc();

    if (bookList.isEmpty()) {
      System.out.println("No hay libros en la base de datos.");
      return;
    }

    System.out.println("===== Top 10 Libros Más Descargados =====");
    System.out.println("------------------------------------------");

    int rank = 1;
    for (Book book : bookList) {
      System.out.printf(
          "%d. Título: %s\n   Total de descargas: %d\n\n",
          rank++, book.getTitle(), book.getDownloadCount()
      );
    }

    System.out.println("------------------------------------------");
  }

  public void searchByName() {
    System.out.print("Escribe el nombre del autor: ");
    var name = sc.nextLine().replace(" ", "%20").toLowerCase().trim();

    var result = fetchBookData(name);

    if (result.isPresent()) {
      Author authorResult = result.stream()
          .flatMap(r -> r.author().stream()
              .map(a -> new Author(a.name(), a.birth_year(), a.death_year())))
          .findFirst()
          .orElse(null);

      if (authorResult != null) {
        System.out.println();
        System.out.println("=== Información del Autor ===");
        System.out.printf("%-20s: %s%n", "Nombre", authorResult.getName());
        System.out.printf("%-20s: %d%n", "Año de Nacimiento", authorResult.getBirthYear());
        System.out.printf("%-20s: %d%n", "Año de Fallecimiento", authorResult.getDeathYear());
        System.out.println("=============================");
      } else {
        System.out.println("No se encontró información del autor.");
      }
    } else {
      System.out.println("No se encontraron resultados para el nombre proporcionado.");
    }
  }

  public void showDownloadStatistics() {
    DoubleSummaryStatistics stats = bookRepository.findAll().stream()
        .mapToDouble(Book::getDownloadCount)
        .summaryStatistics();

    System.out.println("=== Estadísticas de Descargas ===");
    System.out.printf("Total de Descargas: %.0f%n", stats.getSum());
    System.out.printf("Descargas Promedio: %.2f%n", stats.getAverage());
    System.out.printf("Máximas Descargas: %.0f%n", stats.getMax());
    System.out.printf("Mínimas Descargas: %.0f%n", stats.getMin());
    System.out.println("==============================");
  }

  public void showAuthorLifeSpanStatistics() {
    DoubleSummaryStatistics stats = authorRepository.findAll().stream()
        .filter(author -> author.getDeathYear() > 0)
        .mapToDouble(author -> author.getDeathYear() - author.getBirthYear())
        .summaryStatistics();

    System.out.println("=== Estadísticas de Longevidad de Autores ===");
    System.out.printf("Longevidad Promedio: %.2f años%n", stats.getAverage());
    System.out.printf("Vida más Corta: %.0f años%n", stats.getMin());
    System.out.printf("Vida más Larga: %.0f años%n", stats.getMax());
    System.out.println("==============================");
  }

}