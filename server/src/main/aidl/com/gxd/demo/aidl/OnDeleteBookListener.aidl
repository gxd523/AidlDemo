package com.gxd.demo.aidl;

import com.gxd.demo.aidl.Book;

interface OnDeleteBookListener {
    void onDeleteBook(in Book book);
}
