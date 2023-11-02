package com.example.demo.book

import com.example.demo.author.Author
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepo : JpaRepository<Book, Long> {


    @Cacheable("books")
    override fun findAll(): MutableList<Book>

    @Cacheable("books")
    fun findAllByAuthor(author: Author): List<Book>
}