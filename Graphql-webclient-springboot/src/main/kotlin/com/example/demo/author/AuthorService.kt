package com.example.demo.author

import org.springframework.stereotype.Service
import java.util.*


@Service
class AuthorService {

    private val authors = Arrays.asList(
        Author("author-1", "Joshua", "Bloch"),
        Author("author-2", "Douglas", "Adams"),
        Author("author-3", "Bill", "Bryson"),
        Author("author-4", "4", "44"),
        Author("author-5", "5", "55"),
        Author("author-6", "6", "66"),
    )

    fun findAll(
        id: String? = null,
        firstName: String? = null,
        lastName: String? = null,
        page: Int = 0,
        pageSize: Int = 100,
    ): List<Author> {
        val allAuthors = authors
            .filter { x -> isEqual(id, x.id) }
            .filter { x -> isEqual(firstName, x.firstName) }
            .filter { x -> isEqual(lastName, x.lastName) }
            .toMutableList()


        return paginationResult(pageSize, allAuthors, page)
    }

    private fun paginationResult(
        pageSize: Int,
        allAuthors: MutableList<Author>,
        page: Int
    ): List<Author> {
        val listOfAuthorList: MutableList<List<Author>> = mutableListOf()
        var maxSize = pageSize

        while (allAuthors.isNotEmpty()) {
            if (maxSize > allAuthors.size) {
                maxSize = allAuthors.size
            }
            listOfAuthorList.add(allAuthors.slice(0 until maxSize))
            allAuthors.removeAll(allAuthors.slice(0 until maxSize))
        }

        if (page >= listOfAuthorList.size)
            return emptyList()

        return listOfAuthorList[page]
    }

    private fun isEqual(queryParameter: Any?, x: Any) = queryParameter?.let { queryParameter == x } ?: true
}