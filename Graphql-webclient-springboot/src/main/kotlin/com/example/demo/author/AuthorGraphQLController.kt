package com.example.demo.author

import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class AuthorGraphQLController(
    val authorService: AuthorService
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @QueryMapping(
        name = "findAuthors" // This is not needed because if left out then take the function name as querymapping name
    )
    suspend fun findAuthors(
        @Argument page: Int? = 0,
        @Argument pageSize: Int? = 1
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
    suspend fun findAuthor(
        // The same applies to the argument name
        @Argument(name = "id") id: String? = null,
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

}