package com.example.lab1

import java.time.Year

interface BooksRepository {

    fun getAllPossibleYears(): Set<Year>

    fun getBookNames() : List<String>

    fun findBook(name: String, year: Year): Book?
}

data class Book(
    val name: String,
    val author: String,
    val publicationYear: Year,
    val description: String,
)