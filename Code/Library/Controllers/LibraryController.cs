﻿using System;
using System.Collections.Generic;
using Controllers.Connections;
using Models;

namespace Controllers
{
    public class LibraryController
    {
        private static LibraryController _instance;
        private IDatabaseProxy _proxy;

        private LibraryController ()
        {
            // Mock class used temporarilly
            // _proxy = new DatabaseProxy();
            _proxy = new MockDatabaseProxy();
        }

        public static LibraryController GetInstance()
        {
            return _instance ?? (_instance = new LibraryController());
        }

        public List<Book> Search(string searchTerm)
        {
            return _proxy.Search(searchTerm);
        }
        public List<Book> AdvancedSearch(string title, string author, int? year, string isbn, Category? category)
        {
            return _proxy.AdvancedSearch(title,author,year,isbn,category);
        }

        public void CreateBook(Book book) {
            _proxy.CreateBook(book);
        }

        public void DeleteBook(string id)
        {
         _proxy.DeleteBook(id);
 
        }
    }
}
