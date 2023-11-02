package com.example.demo.book

import com.example.demo.author.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepo : JpaRepository<Book, String> {

    fun findAllByAuthor(author: Author): List<Book>
}