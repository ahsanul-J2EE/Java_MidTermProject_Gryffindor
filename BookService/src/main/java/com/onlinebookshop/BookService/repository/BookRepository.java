package com.onlinebookshop.BookService.repository;

import com.onlinebookshop.BookService.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

}
