package com.example.demo


/**
These models is constructed from the *.graphqls files
 */
data class BookWithAuthor(
    override val id: String? = null,
    override val name: String? = null,
    override val pageCount: Int? = null,
    override val author: TestAuthor? = null,
) : BookI {

}

data class AuthorWithBooks(
    override val id: String? = null,
    override val firstName: String? = null,
    override val lastName: String? = null,
    override val books: List<TestBook> = emptyList()
) : AuthorI {

}

data class TestBook(
    override val id: String? = null,
    override val name: String? = null,
    override val pageCount: Int? = null,
    override val author: TestAuthor? = null
) : BookI

interface BookI {
    val id: String?
    val name: String?
    val pageCount: Int?
    val author: TestAuthor?
}


data class TestAuthor(
    override val id: String = "",
    override val firstName: String = "",
    override val lastName: String = "",
    override val books: List<TestBook> = emptyList()
) : AuthorI

interface AuthorI {
    val id: String?
    val firstName: String?
    val lastName: String?
    val books: List<TestBook>
}

