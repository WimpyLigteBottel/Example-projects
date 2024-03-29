package com.example.demo

import com.example.demo.book.BookRepo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.data.domain.Pageable
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

    @Autowired
    lateinit var bookRepo: BookRepo

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
    fun findBookAndDoUniqueFilterSearch() {

        val bookInRepo = bookRepo.findAll(Pageable.ofSize(2)).last()

        val client = buildGQLClient()

        var document = """
        {
          findBook(id: ${bookInRepo.id}, name: "${bookInRepo.name}", pageCount: ${bookInRepo.pageCount}){
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


        assertThat(bookWithAuthor.id).isEqualTo(bookInRepo.id.toString())
        assertThat(bookWithAuthor.name).isEqualTo(bookInRepo.name)
        assertThat(bookWithAuthor.pageCount).isEqualTo(bookInRepo.pageCount)

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