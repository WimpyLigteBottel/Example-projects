package com.example.demo.author

import com.example.demo.advance.AdvanceSearchService
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class AuthorGraphQLController(
    val authorService: AuthorService,
    val advanceSearchService: AdvanceSearchService
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @QueryMapping(
        name = "findAuthors" // This is not needed because if left out then take the function name as querymapping name
    )
     fun findAuthors(
        @Argument page: Int?,
        @Argument pageSize: Int?
    ): List<Author> {
        log.info("findAuthors [page=$page;pageSize=$pageSize]")

        return authorService.findAll(
            page = page ?: 0,
            pageSize = pageSize ?: 1
        )
    }

    @QueryMapping(
        name = "findAuthor" // This is not needed because if left out then take the function name as querymapping name
    )
     fun findAuthor(
        // The same applies to the argument name
        @Argument(name = "id") id: Long? = null,
        @Argument firstName: String? = null,
        @Argument lastName: String? = null
    ): Author? {
        log.info("findAuthor [id=$id;firstName=$firstName;lastName=$lastName]")
        return authorService.findAll(
            id = id,
            firstName = firstName,
            lastName = lastName
        ).firstOrNull()
    }

    @QueryMapping
     fun findAuthorsByBookIds(
        @Argument bookIds: List<Long> = emptyList(),
    ): List<Author> {
        log.info("findAuthorByBookIds [bookIds=$bookIds]")

        return advanceSearchService.findAuthors(bookIds)
    }

}