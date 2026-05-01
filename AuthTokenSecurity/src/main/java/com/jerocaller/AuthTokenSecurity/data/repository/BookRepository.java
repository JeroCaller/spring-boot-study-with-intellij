package com.jerocaller.AuthTokenSecurity.data.repository;

import com.jerocaller.AuthTokenSecurity.data.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    boolean existsByName(String name);
    Optional<Book> findByName(String name);
}
