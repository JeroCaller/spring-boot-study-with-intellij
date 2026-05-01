package com.jerocaller.AuthTokenSecurity.business;

import com.jerocaller.AuthTokenSecurity.data.dto.request.BookRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.BookResponse;

public interface BookService {
    BookResponse getByBookName(String bookName);
    void register(BookRequest bookRequest);
    BookResponse updateBook(BookRequest bookRequest);
}
