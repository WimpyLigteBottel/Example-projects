package com.example.demo.advance

import com.example.demo.author.Author
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class AdvanceSearchService {

    @Autowired
    lateinit var entityManager: EntityManager

    fun findAuthors(bookIds: List<Long>): List<Author> {

        var query = """
            SELECT a FROM Author a
            LEFT JOIN Book b
            ON b.author.id = a.id
            where b.id in (:bookIds)
        """.trimIndent()

        return entityManager
            .createQuery(query, Author::class.java)
            .setParameter("bookIds", bookIds.toList())
            .resultList
    }

}