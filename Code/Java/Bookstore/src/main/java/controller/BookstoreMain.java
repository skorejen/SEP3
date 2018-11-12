package controller;

import controller.connection.DatabaseConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookstoreMain {
    public static void main(String[] args) {
        SpringApplication.run(BookstoreMain.class, args);

        Controller controller = new Controller();
        System.out.println(controller.getBookDetails("978-83-8116-1"));
    }
}
