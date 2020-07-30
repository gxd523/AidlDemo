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

    private void bindService() {
        Intent intent = new Intent();
        intent.setClassName("com.demo.aidl.server", "com.demo.aidl.server.AidlService");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void getBookList(View view) throws RemoteException {
        List<Book> bookList = bookController.getBookList();
        for (Book book : bookList) {
            Log.d("gxd", "获取书籍列表-->" + book.name);
        }
    }

    public void addBookIn(View view) throws RemoteException {
        Book book = new Book("狼图腾");
        Log.d("gxd", "in客户端向服务器添加了一本新书-->" + book.name);
        bookController.addBookIn(book);
        Log.d("gxd", "in客户端对象没有受到服务端对象修改的影响-->" + book.name);
    }

    public void addBookOut(View view) throws RemoteException {
        Book book = new Book("狼图腾");
        Log.d("gxd", "out客户端向服务器添加了一本新书-->" + book.name);
        bookController.addBookOut(book);
        Log.d("gxd", "out客户端对象受到服务端对象修改的影响-->" + book.name);
    }

    public void addBookInOut(View view) throws RemoteException {
        Book book = new Book("狼图腾");
        Log.d("gxd", "inout客户端向服务器添加了一本新书-->" + book.name);
        bookController.addBookInOut(book);
        Log.d("gxd", "inout客户端对象受到服务端对象修改的影响-->" + book.name);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bookController != null) {
            unbindService(serviceConnection);
        }
    }
}
