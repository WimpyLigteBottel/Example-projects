package com.example.demo.book

import com.example.demo.author.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepo : JpaRepository<Book, Long>, PagingAndSortingRepository<Book, Long>,
    JpaSpecificationExecutor<Book> {

    fun findAllByAuthor(author: Author): List<Book>
}