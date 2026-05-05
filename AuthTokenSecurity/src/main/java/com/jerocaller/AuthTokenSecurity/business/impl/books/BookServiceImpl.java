package com.jerocaller.AuthTokenSecurity.business.impl.books;

import com.jerocaller.AuthTokenSecurity.aop.annotations.LogMethodBoundary;
import com.jerocaller.AuthTokenSecurity.business.BookService;
import com.jerocaller.AuthTokenSecurity.data.dto.request.BookRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.BookResponse;
import com.jerocaller.AuthTokenSecurity.data.entity.Book;
import com.jerocaller.AuthTokenSecurity.data.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service
@RequiredArgsConstructor
@LogMethodBoundary
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public BookResponse getByBookName(String bookName) {
        Optional<Book> targetBookOpt = bookRepository.findByName(bookName);
        return targetBookOpt.map(BookResponse::toDto).orElse(null);
    }

    @Override
    public BookResponse register(BookRequest bookRequest) {
        return BookResponse.toDto(bookRepository.save(Book.toEntity(bookRequest)));
    }

    @Override
    @Transactional
    public BookResponse updateBook(int id, BookRequest bookRequest) {
        Optional<Book> targetBookOpt = bookRepository.findById(id);

        if (targetBookOpt.isEmpty()) {
            return null;
        }

        Book targetBook = targetBookOpt.get();
        targetBook.setName(bookRequest.getName());
        targetBook.setPrice(bookRequest.getPrice());
        return BookResponse.toDto(targetBook);
    }
}
