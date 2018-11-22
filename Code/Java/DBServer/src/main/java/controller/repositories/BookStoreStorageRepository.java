package controller.repositories;

import controller.HibernateAdapter;
import model.Book;
import model.BookStore;
import model.BookStoreStorage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class BookStoreStorageRepository implements BookStoreStorageRepo {


    private static BookStoreStorageRepository instance;
    private SessionFactory sessionFactory;
    private BookStoreRepo bookStoreRepo;
    private BookRepo bookRepo;


    public BookStoreStorageRepository() {
        this.sessionFactory = HibernateAdapter.getSessionFactory();
        this.bookRepo = BookRepository.getInstance();
        this.bookStoreRepo = BookStoreRepository.getInstance();
    }

    public static BookStoreStorageRepository getInstance() {
        if (instance == null) {
            instance = new BookStoreStorageRepository();
        }
        return instance;
    }

    @Override
    public void addBookToBookStore(Book book, String bookStoreId) throws BookStoreRepository.BookStoreNotFoundException, BookAlreadyInBookStoreException {
        //Save book to Book table
        bookRepo.saveOrUpdate(book);

        BookStore bookStore = bookStoreRepo.get(bookStoreId);

        BookStoreStorage bookStoreStorage = new BookStoreStorage(bookStore, book);
        try {
            HibernateAdapter.addObject(bookStoreStorage);
        } catch (javax.persistence.PersistenceException e) {
            throw new BookAlreadyInBookStoreException("Book is already in that bookstore");
        }
    }


    @Override
    public void deleteBookFromBookStore(String isbn, String bookStoreId) throws BookRepository.BookNotFoundException, BookStoreRepository.BookStoreNotFoundException {
        Book book = bookRepo.get(isbn);

        BookStore bookStore = bookStoreRepo.get(bookStoreId);
        BookStoreStorage bookStoreStorage = new BookStoreStorage(bookStore, book);
        HibernateAdapter.deleteObject(bookStoreStorage);
    }

    @Override
    public List<Book> search(String searchTerm) {
        throw new NotImplementedException();
    }

    @Override
    public List<Book> advancedSearch(String bookStoreId, String isbn, String title, String author, int year, Book.Category category) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List<Book> searchedBooks = session.createQuery("select new model.Book(s.book.isbn, s.book.title, s.book.author, s.book.year, s.book.category) from BookStoreStorage as s where " +
                    "s.book.isbn like :isbn or " +
                    "lower(s.book.title) like :title or " +
                    "lower(s.book.author) like :author or " +
                    "s.book.year = :year or " +
                    "s.book.category like :category or " +
                    "s.bookstore.bookstoreid like :bookStoreId")
                    .setParameter("isbn", "%" + isbn + "%")
                    .setParameter("title", "%" + title.toLowerCase() + "%")
                    .setParameter("author", "%" + author.toLowerCase() + "%")
                    .setParameter("year", year)
                    .setParameter("category", category)
                    .setParameter("bookStoreId", bookStoreId)
                    .list();
            tx.commit();
            return searchedBooks;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

    @Override
    public List<BookStoreStorage> getBookStoresStorageByIsbn(String isbn) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List<BookStoreStorage> storages = session.createQuery("FROM BookStoreStorage as s where s.book.isbn like :isbn")
                    .setParameter("isbn", isbn)
                    .list();
            tx.commit();
            return storages;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

    public class BookAlreadyInBookStoreException extends Exception {
        public BookAlreadyInBookStoreException(String msg) {
            super(msg);
        }
    }
}
