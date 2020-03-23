package com.demo.aidl.server;

import com.demo.aidl.server.Book;
import com.demo.aidl.server.OnDeleteBookListener;

// 所有的非基本参数都需要一个定向tag来指出数据流通的方式(in、out、inout)
// 基本参数的定向tag默认是并且只能是in
// out:表示数据从服务端流到客户端
// in:表示数据从客户端流入服务端
interface BookController {
    List<Book> getBookList();

    void addBookInOut(inout Book book);

    void addBookIn(in Book book);

    void addBookOut(out Book book);

    void deleteBook(in String bookName, in OnDeleteBookListener onDeleteBookListener);
}