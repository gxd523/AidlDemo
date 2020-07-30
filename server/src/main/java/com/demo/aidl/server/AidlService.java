package com.demo.aidl.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AidlService extends Service {
    private List<Book> bookList = new ArrayList<Book>() {{
        add(new Book("平凡的世界"));
        add(new Book("海底两万里"));
        add(new Book("红楼梦"));
    }};

    @Override
    public IBinder onBind(Intent intent) {
        return new BookController.Stub() {
            @Override
            public List<Book> getBookList() {
                return bookList;
            }

            @Override
            public void addBookInOut(Book book) {
                addBook(book);
            }

            @Override
            public void addBookIn(Book book) {
                addBook(book);
            }

            @Override
            public void addBookOut(Book book) {
                addBook(book);
            }

            @Override
            public void deleteBook(String bookName, OnDeleteBookListener onDeleteBookListener) throws RemoteException {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (Book book : bookList) {
                    if (TextUtils.equals(book.name, bookName)) {
                        onDeleteBookListener.onDeleteBook(book);
                        bookList.remove(book);
                        break;
                    }
                }
            }
        };
    }

    private void addBook(Book book) {
        String rename = "朝花夕拾";
        Log.d("gxd", String.format("服务端接收书籍对象...%s(%s), 改名为...%s", book.name, book.hashCode() % 1000, rename));
        book.name = rename;
        bookList.add(book);
    }
}
