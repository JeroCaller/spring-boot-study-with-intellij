package com.jerocaller.AuthTokenSecurity.business.impl;

import com.jerocaller.AuthTokenSecurity.business.BookService;
import com.jerocaller.AuthTokenSecurity.data.dto.request.BookRequest;
import com.jerocaller.AuthTokenSecurity.data.dto.response.BookResponse;
import com.jerocaller.AuthTokenSecurity.data.entity.Book;
import com.jerocaller.AuthTokenSecurity.data.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @AfterEach
    void clear() {
        bookRepository.deleteAll();
    }

    @Test
    @DisplayName("책 DB 등록 테스트")
    void shouldRegisterNewBook() {
        BookRequest bookRequest = BookRequest.builder()
            .name("정보처리기사 정복하기")
            .price(1000)
            .build();
        bookService.register(bookRequest);
        assertThat(bookRepository.existsByName(bookRequest.getName())).isTrue();

        Optional<Book> foundBookOpt = bookRepository.findByName(bookRequest.getName());
        assertThat(foundBookOpt.isPresent()).isTrue();

        Book foundBook = foundBookOpt.get();
        assertThat(foundBook.getName()).isEqualTo(bookRequest.getName());
        assertThat(foundBook.getPrice()).isEqualTo(bookRequest.getPrice());
    }

    /**
     * <p>
     *     dirty check를 이용한 업데이트 시 해당 서비스 메서드에
     *     {@code @Transactional} 부여 여부에 따른 업데이트 성공 여부를 확인하기 위한
     *     테스트 케이스.
     *     <code>BookServiceImpl#updateBook()</code> 메서드에 부여된 해당 어노테이션을 주석 처리 또는
     *     주석 해제하여 각각 테스트해본다.
     * </p>
     */
    @Test
    @DisplayName("이미 DB에 등록된 책 정보가 업데이트되어야 한다.")
    void shouldUpdateBook() {
        // 책 등록
        BookRequest bookRequest = BookRequest.builder()
            .name("정보처리기사 정복하기")
            .price(1000)
            .build();
        BookResponse registerResult =  bookService.register(bookRequest);
        assertThat(bookRepository.existsByName(bookRequest.getName())).isTrue();

        // 기존에 등록한 책 정보 업데이트.
        BookRequest updateRequest = BookRequest.builder()
            .name("SQLD 자격증 도전하기!")
            .build();
        bookService.updateBook(registerResult.getId(), updateRequest);
        assertThat(bookRepository.existsByName(bookRequest.getName())).isFalse();

        Optional<Book> targetBookOpt = bookRepository.findByName(updateRequest.getName());
        assertThat(targetBookOpt.isPresent()).isTrue();

        Book targetBook = targetBookOpt.get();
        assertThat(targetBook.getName()).isNotEqualTo(bookRequest.getName());
        assertThat(targetBook.getPrice()).isEqualTo(bookRequest.getPrice());
        assertThat(targetBook.getId()).isEqualTo(registerResult.getId());
    }
}