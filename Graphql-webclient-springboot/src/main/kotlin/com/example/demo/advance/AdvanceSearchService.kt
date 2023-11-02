package com.example.demo.advance

import com.example.demo.author.Author
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class AdvanceSearchService {

    @Autowired
    lateinit var entityManager: EntityManager

    fun findAuthors(bookIds: List<String>): List<Author> {

        var query = """
            select a
            FROM Author a
            JOIN Book b
            ON a.id = b.author.id
            where  b.id in (:bookIds)
        """.trimIndent()

        return entityManager
            .createQuery(query, Author::class.java)
            .setParameter("bookIds", bookIds)
            .resultList
    }

}