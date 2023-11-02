package com.example.demo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import kotlin.test.assertEquals

@SpringBootTest
class TestEndpointThroughput {


    private fun buildGQLClient(path: String): HttpGraphQlClient =
        HttpGraphQlClient
            .builder(buildClient(path))
            .build()


    private fun buildClient(path: String): WebClient =
        WebClient.builder().baseUrl(path)
            .clientConnector(ReactorClientHttpConnector())
            .build()

    @Test
    fun findBook() {
        val client = buildGQLClient("http://localhost:8080/graphql")

        var id = "book-1"
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


        assertEquals(
            expected = BookWithAuthor(
                id = "book-1",
                name = "Effective Java",
                pageCount = 416,
                author = TestAuthor(
                    "author-1",
                    firstName = "Joshua",
                    lastName = "Bloch",
                    books = emptyList()
                )
            ),
            actual = bookWithAuthor
        )

    }


    @Test
    fun findBooks() {
        val client = buildGQLClient("http://localhost:8080/graphql")

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
            expected = 8,
            actual = booksWithAuhtorsList.size
        )

    }


    @Test
    fun findAuthor() {
        val client = buildGQLClient("http://localhost:8080/graphql")

        val id = "author-1"
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


        assertEquals(
            expected = AuthorWithBooks(
                id = "author-1",
                firstName = "Joshua",
                lastName = "Bloch",
                books = listOf(
                    TestBook("book-1"),
                    TestBook("book-2"),
                    TestBook("book-3"),
                    TestBook("book-4"),
                    TestBook("book-5"),
                    TestBook("book-6"),
                )
            ),
            actual = authorWithBooks
        )

    }

    @Test
    fun findAuthors() {
        val client = buildGQLClient("http://localhost:8080/graphql")

        var document = """
            {
              findAuthors(page: 0,pageSize: 100){
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


        assertEquals(
            expected = listOf(
                AuthorWithBooks(
                    id = "author-1",
                    firstName = "Joshua",
                    lastName = "Bloch",
                ),
                AuthorWithBooks(
                    id = "author-2",
                    firstName = "Douglas",
                    lastName = "Adams",
                ), AuthorWithBooks(
                    id = "author-3",
                    firstName = "Bill",
                    lastName = "Bryson",
                )
            ),
            actual = authorWithBooks
        )

    }


    @Test
    fun testingPagination() {
        val client = buildGQLClient("http://localhost:8080/graphql")

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


        assertEquals(
            expected = listOf(
                AuthorWithBooks(
                    id = "author-1",
                    firstName = "Joshua",
                    lastName = "Bloch",
                ),
                AuthorWithBooks(
                    id = "author-2",
                    firstName = "Douglas",
                    lastName = "Adams",
                ), AuthorWithBooks(
                    id = "author-3",
                    firstName = "Bill",
                    lastName = "Bryson",
                )
            ),
            actual = authorWithBooks
        )

    }


}