package com.example.demo.book

import com.example.demo.author.Author
import com.example.demo.author.AuthorRepo
import com.example.demo.util.PaginateUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookService {

    @Autowired
    lateinit var bookRepo: BookRepo

    @Autowired
    lateinit var paginateUtil: PaginateUtil

    fun findAll(
        id: Long? = null,
        name: String? = null,
        pageCount: Int? = null,
        authorId: String? = null,
        page: Int = 0,
        pageSize: Int = 100
    ): List<Book> {

        val books = bookRepo.findAll()
            .filter { x -> isEqual(id, x.id) }
            .filter { x -> isEqual(name, x.name) }
            .filter { x -> isEqual(pageCount, x.pageCount) }
            .toMutableList()

        return paginateUtil.paginationResult(books, page, pageSize)
    }


    private fun isEqual(fieldA: Any?, x: Any) = fieldA?.let { fieldA == x } ?: true

}