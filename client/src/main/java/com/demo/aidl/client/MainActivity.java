package com.demo.aidl.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.demo.aidl.server.Book;
import com.demo.aidl.server.BookController;
import com.demo.aidl.server.OnDeleteBookListener;

import java.util.List;

public class MainActivity extends Activity {
    private BookController bookController;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookController = BookController.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bookController = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setClassName("com.demo.aidl.server", "com.demo.aidl.server.AidlService");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void getBookList(View view) throws RemoteException {
        List<Book> bookList = bookController.getBookList();
        Log.d("gxd", "bookList = " + bookList);
    }

    public void addBookIn(View view) throws RemoteException {
        Book book = new Book("狼图腾");
        Log.d("gxd", String.format("客户端调用BookController.addBookIn(), 添加书籍对象...%s", book));
        bookController.addBookIn(book);
        Log.d("gxd", String.format("使用定向标签in, 客户端书籍对象没有受到服务端影响...%s", book));
    }

    public void addBookOut(View view) throws RemoteException {
        Book book = new Book("狼图腾");
        Log.d("gxd", String.format("客户端调用BookController.addBookOut(), 添加书籍对象...%s", book));
        bookController.addBookOut(book);
        Log.d("gxd", String.format("使用定向标签out, 客户端书籍对象受到服务端影响...%s", book));
    }

    public void addBookInOut(View view) throws RemoteException {
        Book book = new Book("狼图腾");
        Log.d("gxd", String.format("客户端调用BookController.addBookInOut(), 添加书籍对象...%s", book));
        bookController.addBookInOut(book);
        Log.d("gxd", String.format("使用定向标签inout, 客户端书籍对象受到服务端影响...%s", book));
    }

    public void deleteBook(View view) throws RemoteException {
        bookController.deleteBook("朝花夕拾", new OnDeleteBookListener.Stub() {
            @Override
            public void onDeleteBook(Book book) {
                Log.d("gxd", "异步删除书籍回调...");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bookController != null) {
            unbindService(serviceConnection);
        }
    }
}
