package com.example.demo.author

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthorRepo : JpaRepository<Author, Long> {

    @Cacheable("author")
    override fun findById(id: Long): Optional<Author>


    @Cacheable("authors")
    override fun findAll(): MutableList<Author>


    @Query("""
            SELECT a FROM Author a
            LEFT JOIN Book b
            ON b.author.id = a.id
            where b.id in (:bookIds)
    """)
    fun findAuthorsByBookIds(bookIds: List<Long>): List<Author>

}