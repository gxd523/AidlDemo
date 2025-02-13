package com.gxd.demo.aidl;

import com.gxd.demo.aidl.Book;
import com.gxd.demo.aidl.OnDeleteBookListener;

/**
* 所有的「非基本类型参数」都需要一个「定向tag」来指出数据流通的方式(in、out、inout)
* 「基本类型参数」的「定向tag」默认是并且只能是「in」
* 「out」：表示数据从「服务端」流到「客户端」
* 「in」：表示数据从「客户端」流入「服务端」
*/
interface IMyAidlInterface {
    int add(int a, int b);

    List<Book> getBookList();

    void addBookInOut(inout Book book);

    void addBookIn(in Book book);

    // 单写out服务端收到的数据有问题
    void addBookOut(out Book book);

    void deleteBook(in String bookName, in OnDeleteBookListener onDeleteBookListener);
}