package controller.connection;

import model.Book;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MockDatabase implements DatabaseProxy {
    @Override
    public List<Book> search(String searchTerm) throws DatabaseConnection.ServerOfflineException, DatabaseConnection.SearchException {
        LinkedList<Book> books = new LinkedList<>();
        books.add(new Book("123", "Hello", "Don Bell", 1998, Book.Category.Science));
        return books;
    }

    @Override
    public List<Book> advancedSearch(String title, String author, int year, String isbn, Book.Category category) throws DatabaseConnection.ServerOfflineException, DatabaseConnection.SearchException {
        return new LinkedList<>(Arrays.asList(new Book("123", "Hello", "Don Bell", 1998, Book.Category.Science)));
    }
}