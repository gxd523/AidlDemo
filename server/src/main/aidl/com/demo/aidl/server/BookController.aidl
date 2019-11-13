package com.demo.aidl.server;

import com.demo.aidl.server.Book;

interface BookController {
    List<Book> getBookList();

    void addBookInOut(inout Book book);

    void addBookIn(in Book book);

    void addBookOut(out Book book);
}