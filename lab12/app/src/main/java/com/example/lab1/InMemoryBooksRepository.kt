package com.example.lab1

import java.time.Year

class InMemoryBooksRepository : BooksRepository {

    private val books = listOf(
        Book("Книга 1", "автор 1", Year.of(2004), "Гарна книга про нічого)"),
        Book("Книга 2", "автор 1", Year.of(2006), "Гарна книга про нічого)"),
        Book("Книга 3", "автор 2", Year.of(2008), "Гарна книга про нічого)"),
        Book("Книга 4", "автор 2", Year.of(2008), "Гарна книга про нічого)"),
    );

    override fun getAllPossibleYears(): Set<Year> {
        return books.map { book -> book.publicationYear }.toSet()
    }

    override fun getBookNames() : List<String> {
        return books.map {book -> book.name}
    }

    override fun findBook(name: String, year: Year): Book? {
        return books.find { book -> book.name == name && book.publicationYear == year}
    }
}