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
                String rename = "朝花夕拾";
                Log.d("gxd", "服务器将客户端添加的新书-->" + book.name + ", 改名为-->" + rename);
                book.name = rename;
                bookList.add(book);
            }

            @Override
            public void addBookIn(Book book) {
                String rename = "朝花夕拾";
                Log.d("gxd", "服务器将客户端添加的新书-->" + book.name + ", 改名为-->" + rename);
                book.name = rename;
                bookList.add(book);
            }

            @Override
            public void addBookOut(Book book) {
                String rename = "朝花夕拾";
                Log.d("gxd", "服务器将客户端添加的新书-->" + book.name + ", 改名为-->" + rename);
                book.name = rename;
                bookList.add(book);
            }

            @Override
            public void deleteBook(String bookName, OnDeleteBookListener onDeleteBookListener) throws RemoteException {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Book deleteBook = null;
                for (Book book : bookList) {
                    if (TextUtils.equals(book.name, bookName)) {
                        deleteBook = book;
                        break;
                    }
                }
                if (deleteBook != null) {
                    bookList.remove(deleteBook);
                }
                onDeleteBookListener.onDeleteBook(deleteBook);
            }
        };
    }
}
