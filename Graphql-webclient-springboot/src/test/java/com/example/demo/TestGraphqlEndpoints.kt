package com.example.demo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import kotlin.test.assertContains
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


        assertEquals(
            expected = listOf(
                AuthorWithBooks(
                    id = "author-1",
                    firstName = "Joshua",
                    lastName = "Bloch",
                )
            ),
            actual = authorWithBooks
        )

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
    fun testInputList() {
        val client = buildGQLClient()

        // Note: you will need to Add <""> if you want to inject multiple fields
        val bookIds = listOf("\"book-1\"", "\"book-8\"")
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


        assertEquals(
            expected = 2,
            actual = authorWithBooks.size
        )

        assertContains(authorWithBooks, AuthorWithBooks(id = "author-3", books = listOf(TestBook(id = "book-8"))))
    }


}