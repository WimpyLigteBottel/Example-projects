package com.example.demo.author

import com.example.demo.book.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepo : JpaRepository<Author, Long> {


}