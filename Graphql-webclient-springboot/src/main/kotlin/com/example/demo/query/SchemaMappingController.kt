package com.example.demo.query

import com.example.demo.advance.AdvanceSearchService
import com.example.demo.author.Author
import com.example.demo.author.AuthorRepo
import com.example.demo.book.Book
import com.example.demo.book.BookRepo
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class SchemaMappingController(
    val authorRepo: AuthorRepo,
    val bookRepo: BookRepo,
    val advanceSearchService: AdvanceSearchService
) {

    private val log = LoggerFactory.getLogger(this::class.java)


    /**
     * Example: The following will call this method
     * findBook(id: "book-1"){
     *     id,
     *     author{
     *       id
     *     }
     *   }
     *
     */

    @SchemaMapping(
        value = "author" // the field name in your query...
    )
    fun authors(
        // This the entity that contains the  @SchemaMapping value (aka book contains author) see the Type definition in book.graphqls
        book: Book
    ): Author? {
        log.info("finding author from ${book.id}")
        return  authorRepo.findByIdOrNull(book.author.id)
    }


    /**
     * Example: The following will call this method
     *  findAuthors{
     *     id,
     *     books{
     *       id
     *     }
     *   },
     */
    @SchemaMapping(
        value = "books" // the field name in your query...
    )
    fun book(
        // This the entity that contains the  @SchemaMapping value (aka book contains author) see the Type definition in book.graphqls
        author: Author
    ): List<Book> {
        log.info("finding the books from Author.id=${author.id}")

        return bookRepo.findAllByAuthor(author)
    }

}