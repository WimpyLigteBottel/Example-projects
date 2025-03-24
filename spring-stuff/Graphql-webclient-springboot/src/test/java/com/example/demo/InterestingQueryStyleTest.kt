package com.example.demo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class InterestingQueryStyleTest {

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


        assertThat(authorWithBooks).hasSize(4)
    }

    @Test
    fun testShowcasingVariables() {
        val client = buildGQLClient()

        // Note: you will need to Add <""> if you want to inject multiple fields if they are strings
        val bookIds = listOf("1", "3", "4", "5")
        var document = """
            query findAuthorsByBookIds(variable: [ID]){
               findAuthorsByBookIds(bookIds: variable) {
                id
                books{
                  id
                }
              }
            }
        """.trimMargin().replace("variable", "\$variable")

        val authorWithBooks = client
            .document(document)
            .variable("variable", bookIds)
            .retrieve("findAuthorsByBookIds")
            .toEntityList(AuthorWithBooks::class.java)
            .block() ?: emptyList()


        assertThat(authorWithBooks).hasSize(4)
    }

    @Test
    fun testShowcasingFragements() {
        val client = buildGQLClient()
        var document = """
                {
                  findAuthorsByBookIds(bookIds: [1,2,3,4,5]){
                    ...author
                  }
                }
                
                fragment author on Author{
                  id
                  firstName
                  lastName
                  books {
                    ...book
                  }
                }
                
                fragment book on Book{
                  id
                  name
                  pageCount
                }
        """.trimMargin()

        val authorWithBooks = client
            .document(document)
            .retrieve("findAuthorsByBookIds")
            .toEntityList(AuthorWithBooks::class.java)
            .block() ?: emptyList()


        assertThat(authorWithBooks).hasSize(4)
    }


}