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

public class MainActivity extends Activity implements View.OnClickListener {
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
        intent.setComponent(new ComponentName("com.demo.aidl.server", "com.demo.aidl.server.AidlService"));
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void onBtnClicked(int id) throws RemoteException {
        switch (id) {
            case R.id.get_book_list_btn: {
                List<Book> bookList = bookController.getBookList();
                for (Book book : bookList) {
                    Log.d("gxd", "获取书籍列表-->" + book.name);
                }
                break;
            }
            case R.id.add_book_inout_btn: {
                Book book = new Book("狼图腾");
                Log.d("gxd", "inout客户端向服务器添加了一本新书-->" + book.name);
                bookController.addBookInOut(book);
                Log.d("gxd", "inout客户端对象受到服务端对象修改的影响-->" + book.name);
                break;
            }
            case R.id.add_book_in_btn: {
                Book book = new Book("狼图腾");
                Log.d("gxd", "in客户端向服务器添加了一本新书-->" + book.name);
                bookController.addBookIn(book);
                Log.d("gxd", "in客户端对象没有受到服务端对象修改的影响-->" + book.name);
                break;
            }
            case R.id.add_book_out_btn: {
                Book book = new Book("狼图腾");
                Log.d("gxd", "out客户端向服务器添加了一本新书-->" + book.name);
                bookController.addBookOut(book);
                Log.d("gxd", "out客户端对象受到服务端对象修改的影响-->" + book.name);
                break;
            }
            case R.id.add_book_delete_btn: {
                bookController.deleteBook("朝花夕拾", new OnDeleteBookListener.Stub() {
                    @Override
                    public void onDeleteBook() {
                        Log.d("gxd", "异步删除书籍回调...");
                    }
                });
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.get_book_list_btn).setOnClickListener(this);
        findViewById(R.id.add_book_inout_btn).setOnClickListener(this);
        findViewById(R.id.add_book_in_btn).setOnClickListener(this);
        findViewById(R.id.add_book_out_btn).setOnClickListener(this);
        findViewById(R.id.add_book_delete_btn).setOnClickListener(this);

        bindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bookController != null) {
            unbindService(serviceConnection);
        }
    }

    @Override
    public void onClick(View view) {
        if (bookController != null) {
            try {
                onBtnClicked(view.getId());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
