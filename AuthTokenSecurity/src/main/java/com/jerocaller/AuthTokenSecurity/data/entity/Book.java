package com.jerocaller.AuthTokenSecurity.data.entity;

import com.jerocaller.AuthTokenSecurity.data.dto.request.BookRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    @Column(nullable = false)
    private Integer price;

    public void setName(String name) {
        if (name == null) {
            return;
        }

        this.name = name;
    }

    public void setPrice(Integer price) {
        if (price == null) {
            return;
        }

        this.price = price;
    }

    public static Book toEntity(BookRequest bookRequest) {
        Book book = new Book();
        book.setName(bookRequest.getName());
        book.setPrice(bookRequest.getPrice());
        return book;
    }
}
