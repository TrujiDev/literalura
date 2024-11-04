package com.trujidev.literalura.presentation;

import com.trujidev.literalura.model.Book;
import com.trujidev.literalura.model.BookData;
import com.trujidev.literalura.service.ApiClient;
import com.trujidev.literalura.service.JsonParser;

import java.util.List;
import java.util.Scanner;

public class Menu {
  Scanner sc = new Scanner(System.in);
  JsonParser jsonParser = new JsonParser();
  ApiClient apiClient = new ApiClient();

  private final String url = "https://gutendex.com/books";

  public void showMenu() {
    var option = -1;
    while (option != 0) {
      var menu = """
          
          1 - Buscar por título
          
          0 - Salir
          """;
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

  private List<Book> fetchBookData(String title) {
    var json = apiClient.fetchData(url + "/?search=" + title);
    BookData bookData = jsonParser.fromJson(json, BookData.class);
    return bookData.results();
  }

  private void searchByTitle() {
    System.out.println("¿Qué título le interesa?: ");
    var title = sc.nextLine().replace(" ", "%20").toLowerCase();

    var result = fetchBookData(title);
    System.out.println(result);
  }
}
