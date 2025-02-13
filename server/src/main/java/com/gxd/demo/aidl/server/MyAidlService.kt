package com.gxd.demo.aidl.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Process
import android.util.Log
import com.gxd.demo.aidl.Book
import com.gxd.demo.aidl.IMyAidlInterface
import com.gxd.demo.aidl.OnDeleteBookListener

class MyAidlService : Service() {
    private val bookList by lazy {
        mutableListOf(
            Book().apply {
                name = "a"
                author = "A"
                price = 1.0
            },
            Book().apply {
                name = "b"
                author = "B"
                price = 2.0
            }
        )
    }
    private val iBinder by lazy {
        object : IMyAidlInterface.Stub() {
            override fun add(a: Int, b: Int): Int = a + b

            override fun getBookList(): List<Book?>? = this@MyAidlService.bookList

            override fun addBookInOut(book: Book?) {
                book?.let(::addBook)
            }

            override fun addBookIn(book: Book?) {
                book?.let(::addBook)
            }

            override fun addBookOut(book: Book?) {
                book?.let(::addBook)
            }

            override fun deleteBook(bookName: String?, onDeleteBookListener: OnDeleteBookListener?) {
                for (book in this@MyAidlService.bookList.iterator()) {
                    if (book.name == bookName) {
                        onDeleteBookListener?.onDeleteBook(book)
                        this@MyAidlService.bookList.remove(book)
                        break
                    }
                }
            }
        }
    }

    override fun onBind(intent: Intent): IBinder = iBinder

    private fun addBook(book: Book) {
        "服务端接收书籍对象...${book.string()}".log()
        book.price = book.price + 0.1
        bookList += book
    }

    private val logPrefix: String
        get() = "Server...pid = ${Process.myPid()}, thread = ${Thread.currentThread().name}-${Thread.currentThread().id}"

    private fun String.log() {
        Log.d("ggg", "$logPrefix, $this")
    }

    private fun Book.string(): String =
        "${this.toString().substring(this.toString().lastIndexOf('@') + 1)}(name = $name, author = $author, price = $price)"
}