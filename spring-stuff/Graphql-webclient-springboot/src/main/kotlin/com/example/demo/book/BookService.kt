package com.example.demo.book

import com.example.demo.author.Author
import jakarta.persistence.criteria.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class BookService {

    @Autowired
    lateinit var bookRepo: BookRepo

    @Cacheable("books")
    fun findAll(
        bookFilter: BookFilter,
        pageRequest: PageRequest
    ): List<Book> = bookRepo.findAll(
        BookSpecification(bookFilter),
        pageRequest
    ).toList()

    @Cacheable("books")
    fun findAllBooksByAuthor(author: Author) = bookRepo.findAllByAuthor(author)

}


data class BookFilter(
    val id: Long? = null,
    val name: String? = null,
    val pageCount: Int? = null,
    val authorId: String? = null,
)


class BookSpecification(val bookFilter: BookFilter) : Specification<Book> {
    override fun toPredicate(
        root: Root<Book>,
        query: CriteriaQuery<*>,
        cb: CriteriaBuilder
    ): Predicate {

        val predicates: MutableList<Predicate> = ArrayList()


        bookFilter.id?.let {
            val value: Path<Long> = root["id"]
            predicates.add(cb.equal(value, it))
        }


        bookFilter.name?.let {
            val value: Path<String> = root["name"]
            predicates.add(cb.equal(value, it))
        }


        bookFilter.pageCount?.let {
            val value: Path<Long> = root["pageCount"]
            predicates.add(cb.equal(value, it))

        }

        bookFilter.authorId?.let {
            val value: Path<String> = root["author_id"]
            predicates.add(cb.equal(value, it))
        }


        return cb.and(*predicates.toTypedArray())

    }

}