package com.trujidev.literalura.presentation;

import com.trujidev.literalura.model.Author;
import com.trujidev.literalura.model.Book;
import com.trujidev.literalura.model.BookData;
import com.trujidev.literalura.model.ResultData;
import com.trujidev.literalura.repository.AuthorRepository;
import com.trujidev.literalura.repository.BookRepository;
import com.trujidev.literalura.service.ApiClient;
import com.trujidev.literalura.service.JsonParser;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {
  Scanner sc = new Scanner(System.in);
  JsonParser jsonParser = new JsonParser();
  ApiClient apiClient = new ApiClient();

  private final AuthorRepository authorRepository;
  private final BookRepository bookRepository;

  public Menu(BookRepository bookRepository, AuthorRepository authorRepository) {
    this.bookRepository = bookRepository;
    this.authorRepository = authorRepository;
  }

  public void showMenu() {
    var option = -1;
    while (option != 0) {
      var menu = """
          
          *** Menu ***
          1 - Buscar por título
          2 - Historial de búsqueda por idioma
          3 - Historial de búsqueda
          4 - Historial de autores
          5 - Historial de autores vivos
          6 - Cantidad de libros por idioma
          
          0 - Salir
          
          ¿Qué quieres hacer?:\s""";
      System.out.println(menu);
      option = sc.nextInt();
      sc.nextLine();

      switch (option) {
        case 1:
          searchByTitle();
          break;
        case 2:
          searchHistoryByLanguage();
          break;
        case 3:
          searchHistory();
          break;
        case 4:
          authorsHistory();
          break;
        case 5:
          livingAuthors();
          break;
        case 6:
          bookCountByLanguage();
          break;
        case 0:
          System.out.println("Cerrando...");
          break;
        default:
          System.out.println("Opción inválida");
          showMenu();
      }
    }
  }

  private Optional<BookData> fetchBookData(String title) {
    String url = "https://gutendex.com/books";
    var json = apiClient.fetchData(url + "/?search=" + title);
    ResultData resultData = jsonParser.fromJson(json, ResultData.class);
    return resultData.results().stream().findFirst();
  }

  private void searchByTitle() {
    System.out.println("¿Qué título le interesa?: ");
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
              "Número de descargas: " + book.getDownload_count() + "\n" +
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
              "Número de descargas: " + book.getDownload_count() + "\n" +
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
}