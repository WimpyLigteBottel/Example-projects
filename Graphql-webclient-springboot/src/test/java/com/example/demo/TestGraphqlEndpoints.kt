package com.example.demo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import kotlin.test.assertEquals

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class TestGraphqlEndpoints {

    @LocalServerPort
    lateinit var port: String

    private fun buildGQLClient(): HttpGraphQlClient =
        HttpGraphQlClient
            .builder(
                WebClient.builder()
                    .baseUrl("http://localhost:$port/graphql")
                    .clientConnector(ReactorClientHttpConnector())
                    .build()
            )
            .build()


    @Test
    fun findBook() {
        val client = buildGQLClient()

        var id = "1"
        var document = """
        {
          findBook(id: "$id"){
            id
            name
            pageCount
            author {
              id
              firstName
              lastName
            }
          }
        }
        """.trimMargin()

        val bookWithAuthor = client
            .document(document)
            .retrieve("findBook")
            .toEntity(BookWithAuthor::class.java)
            .block()


        assertThat(bookWithAuthor).isNotNull

    }


    @Test
    fun findBooks() {
        val client = buildGQLClient()

        var document = """
            {
              findBooks{
                id
                name
                pageCount
                author{
                  id
                  firstName
                  lastName
                }
              }
            }
        """.trimMargin()

        val booksWithAuhtorsList = client
            .document(document)
            .retrieve("findBooks")
            .toEntityList(BookWithAuthor::class.java)
            .block()


        assertEquals(
            expected = 1,
            actual = booksWithAuhtorsList.size
        )

    }


    @Test
    fun findAuthor() {
        val client = buildGQLClient()

        val id = "1"
        var document = """
            {
              findAuthor(id: "$id"){
                id
                firstName
                lastName
                books {
                  id
                }
              }
            }
        """.trimMargin()

        val authorWithBooks = client
            .document(document)
            .retrieve("findAuthor")
            .toEntity(AuthorWithBooks::class.java)
            .block()


        assertThat(authorWithBooks).isNotNull
        assertThat(authorWithBooks.books).hasSize(2)

    }

    @Test
    fun findAuthors() {
        val client = buildGQLClient()

        var document = """
            {
              findAuthors{
                id
                firstName
                lastName
              }
            }
        """.trimMargin()

        val authorWithBooks = client
            .document(document)
            .retrieve("findAuthors")
            .toEntityList(AuthorWithBooks::class.java)
            .block()


        assertThat(authorWithBooks).hasSize(1)

    }


    @Test
    fun testingPagination() {
        val client = buildGQLClient()

        // Below is how you would do pagination
        var document = """
            {
              findAuthors(page: 0,pageSize: 3){
                id
                firstName
                lastName
              }
            }
        """.trimMargin()

        val authorWithBooks = client
            .document(document)
            .retrieve("findAuthors")
            .toEntityList(AuthorWithBooks::class.java)
            .block()


        assertThat(authorWithBooks).hasSize(3)
    }


    @Test
    fun testInputList() {
        val client = buildGQLClient()

        // Note: you will need to Add <""> if you want to inject multiple fields if they are strings
        val bookIds = listOf("1", "3", "4", "5")
        var document = """
            {
               findAuthorsByBookIds(bookIds: $bookIds) {
                id
                books{
                  id
                }
              }
            }
        """.trimMargin()

        val authorWithBooks = client
            .document(document)
            .retrieve("findAuthorsByBookIds")
            .toEntityList(AuthorWithBooks::class.java)
            .block() ?: emptyList()


        assertThat(authorWithBooks).hasSize(3)
    }


}