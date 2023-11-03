package com.example.demo.author

import com.example.demo.util.PaginateUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service


@Service
class AuthorService {

    @Autowired
    lateinit var authorRepo: AuthorRepo

    @Autowired
    lateinit var paginateUtil: PaginateUtil

    fun findAll(
        id: Long? = null,
        firstName: String? = null,
        lastName: String? = null,
        page: Int = 0,
        pageSize: Int = 100,
    ): List<Author> {
        var allAuthors = authorRepo.findAll();

        allAuthors = allAuthors
            .filter { x -> isEqual(id, x.id) }
            .filter { x -> isEqual(firstName, x.firstName) }
            .filter { x -> isEqual(lastName, x.lastName) }
            .toMutableList()


        return paginateUtil.paginationResult(allAuthors, page, pageSize)
    }


    fun findAuthorsByBookIds(ids: List<Long>): List<Author> {
        return authorRepo.findAuthorsByBookIds(ids)
    }

    private fun isEqual(queryParameter: Any?, x: Any) = queryParameter?.let { queryParameter == x } ?: true
}