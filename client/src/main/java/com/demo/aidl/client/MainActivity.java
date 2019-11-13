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

import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private BookController bookController;
    private boolean isConnected;
    private List<Book> bookList;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookController = BookController.Stub.asInterface(service);
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.get_book_list_btn).setOnClickListener(this);
        findViewById(R.id.add_book_btn).setOnClickListener(this);

        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.demo.aidl.server", "com.demo.aidl.server.AidlService"));
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isConnected) {
            unbindService(serviceConnection);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_book_list_btn:
                if (isConnected) {
                    try {
                        bookList = bookController.getBookList();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    for (Book book : bookList) {
                        Log.d("gxd", "获取书籍列表-->" + book.getName());
                    }
                }
                break;
            case R.id.add_book_btn:
                if (isConnected) {
                    Book book = new Book("狼图腾");
                    try {
                        Log.d("gxd", "客户端向服务器添加了一本新书-->" + book.getName());
                        bookController.addBookInOut(book);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
