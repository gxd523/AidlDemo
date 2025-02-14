package com.gxd.demo.aidl.client

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.view.View
import com.gxd.demo.aidl.Book
import com.gxd.demo.aidl.IMyAidlInterface
import com.gxd.demo.aidl.OnDeleteBookListener
import kotlin.concurrent.thread

class MainActivity : Activity() {
    private var aidlInterface: IMyAidlInterface? = null
    private val serviceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                aidlInterface = IMyAidlInterface.Stub.asInterface(service)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                aidlInterface = null
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent().apply {
            val packageName = "com.gxd.demo.aidl.server"
            setClassName(packageName, "$packageName.MyAidlService")
            setPackage(packageName)
        }
        val bindResult = bindService(intent, serviceConnection, BIND_AUTO_CREATE)
        "bindResult = $bindResult".log()
    }

    @Throws(RemoteException::class)
    fun getBookList(view: View?) {
        val bookList = aidlInterface?.getBookList()
        val joinToString = bookList?.joinToString(", ", transform = { book -> book.string() })
        "bookList = $joinToString".log()
    }

    @Throws(RemoteException::class)
    fun addBookIn(view: View?) {
        val book = Book().apply {
            name = "d"
            author = "D"
            price = 4.0
        }
        "客户端调用「addBookIn」, 添加书籍对象...${book.string()}".log()
        aidlInterface?.addBookIn(book)
        "使用定向标签in, 客户端书籍对象没有受到服务端影响...${book.string()}".log()
    }

    @Throws(RemoteException::class)
    fun addBookOut(view: View?) {
        val book = Book().apply {
            name = "d"
            author = "D"
            price = 4.0
        }
        "客户端调用「addBookOut」, 添加书籍对象...${book.string()}".log()
        aidlInterface?.addBookOut(book)
        "使用定向标签out, 客户端书籍对象受到服务端影响...${book.string()}".log()
    }

    @Throws(RemoteException::class)
    fun addBookInOut(view: View?) {
        val book = Book().apply {
            name = "d"
            author = "D"
            price = 4.0
        }
        "客户端调用「addBookInOut」, 添加书籍对象...${book.string()}".log()
        aidlInterface?.addBookInOut(book)
        "使用定向标签inout, 客户端书籍对象受到服务端影响...${book.string()}".log()
    }

    fun deleteBook(view: View?) {
        thread {
            aidlInterface?.deleteBook("d", object : OnDeleteBookListener.Stub() {
                override fun onDeleteBook(book: Book?) {
                    "异步删除书籍回调...${book?.string()}".log()
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (aidlInterface != null) unbindService(serviceConnection)
    }

    private fun Book.string(): String =
        "${this.toString().substring(this.toString().lastIndexOf('@') + 1)}(name = $name, author = $author, price = $price)"
}
