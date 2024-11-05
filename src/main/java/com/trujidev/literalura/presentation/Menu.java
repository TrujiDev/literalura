package com.trujidev.literalura.presentation;

import com.trujidev.literalura.model.Author;
import com.trujidev.literalura.model.Book;
import com.trujidev.literalura.model.BookData;
import com.trujidev.literalura.model.ResultData;
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

  private final BookRepository repository;
  private final String url = "https://gutendex.com/books";

  public Menu(BookRepository repository) {
    this.repository = repository;
  }

  public void showMenu() {
    var option = -1;
    while (option != 0) {
      var menu = """
          
          *** Menu ***
          1 - Buscar por título
          
          0 - Salir
          
          ¿Qué quieres hacer?:\s""";
      System.out.println(menu);
      option = sc.nextInt();
      sc.nextLine();

      switch (option) {
        case 1:
          searchByTitle();
          break;

        case 0:
          System.out.println("Cerrando...");
          break;
        default:
          System.out.println("Opción inválida");
      }
    }
  }

  private Optional<BookData> fetchBookData(String title) {
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
      repository.save(book);
    } else {
      System.out.println("No se encontraron resultados para el título: " + title);
    }
  }
}
