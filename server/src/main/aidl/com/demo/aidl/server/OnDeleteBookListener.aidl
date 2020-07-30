package com.demo.aidl.server;

import com.demo.aidl.server.Book;

interface OnDeleteBookListener {
    void onDeleteBook(in Book book);
}
