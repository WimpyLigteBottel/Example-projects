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
class BookEndpointsTest {

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

        var document = """
        {
          findBook(id: 1){
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
}