package com.example.demo.author

import jakarta.persistence.criteria.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service


@Service
class AuthorService {

    @Autowired
    lateinit var authorRepo: AuthorRepo


    @Cacheable("author")

    fun findAuthorById(id: Long) = authorRepo.findById(id).orElse(null)


    @Cacheable("authors")
    fun findAll(
        authorFilter: AuthorFilter,
        pageRequest: PageRequest,
    ): List<Author> {

        val findAll = authorRepo.findAll(
            AuthorSpecification(authorFilter),
            pageRequest
        )

        return findAll.toList()
    }


    @Cacheable("authors")
    fun findAuthorsByBookIds(ids: List<Long>): List<Author> = authorRepo.findAuthorsByBookIds(ids)

}


data class AuthorFilter(
    val id: Long? = null,
    val firstName: String? = null,
    val lastName: String? = null
)


class AuthorSpecification(val filter: AuthorFilter) : Specification<Author> {
    override fun toPredicate(
        root: Root<Author>,
        query: CriteriaQuery<*>,
        cb: CriteriaBuilder
    ): Predicate {

        val predicates: MutableList<Predicate> = mutableListOf()


        filter.id?.let {
            val value: Path<Long> = root["id"]
            predicates.add(cb.equal(value, it))
        }


        filter.firstName?.let {
            val value: Path<String> = root["firstName"]
            predicates.add(cb.equal(value, it))
        }


        filter.lastName?.let {
            val value: Path<String> = root["lastName"]
            predicates.add(cb.equal(value, it))

        }



        return cb.and(*predicates.toTypedArray())

    }

}