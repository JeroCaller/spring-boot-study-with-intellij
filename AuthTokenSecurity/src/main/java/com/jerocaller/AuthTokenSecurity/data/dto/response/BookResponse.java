package com.jerocaller.AuthTokenSecurity.data.dto.response;

import com.jerocaller.AuthTokenSecurity.data.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {
    private int id;
    private String name;
    private Integer price;

    public static BookResponse toDto(Book book) {
        return BookResponse.builder()
            .id(book.getId())
            .name(book.getName())
            .price(book.getPrice())
            .build();
    }
}
