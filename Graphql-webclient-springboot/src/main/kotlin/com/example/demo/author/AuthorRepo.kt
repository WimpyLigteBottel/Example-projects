package com.example.demo.author

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthorRepo : JpaRepository<Author, Long> {

    @Cacheable("author")
    override fun findById(id: Long): Optional<Author>


    @Cacheable("authors")

    override fun findAll(): MutableList<Author>

}