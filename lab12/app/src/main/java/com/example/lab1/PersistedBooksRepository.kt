package com.example.lab1

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import java.time.Year

class PersistedBooksRepository(private val context: Context) : BooksRepository {

    override fun getAllPossibleYears(): Set<Year> {
        val db = BooksDbHelper(context).readableDatabase;

        val cursor = db.query(
            true,
            BookEntry.TABLE_NAME,
            arrayOf(BookEntry.BOOK_YEAR),
            null, null,
            null,
            null,
            null, null,
        );

        val result = mutableSetOf<Year>();
        while (cursor.moveToNext()) {
            result += Year.of(cursor.getInt(0))
        }
        return result
    }

    override fun getBookNames(): List<String> {
        val db = BooksDbHelper(context).readableDatabase;

        val cursor = db.query(
            true,
            BookEntry.TABLE_NAME,
            arrayOf(BookEntry.BOOK_NAME),
            "1=1", arrayOf(),
            null,
            null,
            null, null,
        );

        val result = mutableListOf<String>()
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
        }
        return result
    }

    override fun findBook(name: String, year: Year): Book? {
        val db = BooksDbHelper(context).readableDatabase;
        // select name, year from books
        // where name = 'Test' And year = '1998"

        val selection = "${BookEntry.BOOK_NAME} = ? AND ${BookEntry.BOOK_YEAR} = ? ";
        val cursor = db.query(
            true,
            BookEntry.TABLE_NAME,
            arrayOf(
                BookEntry.BOOK_NAME,
                BookEntry.BOOK_YEAR,
                BookEntry.BOOK_DESCRIPTION,
                BookEntry.BOOK_AUTHOR
            ),
            selection,
            arrayOf(name, year.toString()),
            null,
            null,
            null, null,
        );

        while (cursor.moveToNext()) {
            return Book(
                name = cursor.getString(0),
                author = cursor.getString(3),
                publicationYear = Year.of(cursor.getInt(1)),
                description = cursor.getString(2)
            )
        }

        return null
    }

    object BookEntry : BaseColumns {
        const val TABLE_NAME = "books";
        const val BOOK_NAME = "name";
        const val BOOK_YEAR = "year";
        const val BOOK_AUTHOR = "author";
        const val BOOK_DESCRIPTION = "description";
    }

    class BooksDbHelper(context: Context) : SQLiteOpenHelper(context, "Lab3.db", null, 0) {
        private val SQL_CREATE_BOOKS_TABLE = "" +
                "CREATE TABLE ${BookEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${BookEntry.BOOK_NAME} TEXT," +
                "${BookEntry.BOOK_YEAR} INTEGER," +
                "${BookEntry.BOOK_AUTHOR} TEXT," +
                "${BookEntry.BOOK_DESCRIPTION} TEXT)"

        private val SQL_DELETE_BOOKS_TABLE = "DROP TABLE IF EXISTS ${BookEntry.TABLE_NAME}";

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(SQL_CREATE_BOOKS_TABLE);
            db?.insert(BookEntry.TABLE_NAME, null, ContentValues().apply {
                put(BookEntry.BOOK_NAME, "Name 1")
                put(BookEntry.BOOK_YEAR, 1992)
                put(BookEntry.BOOK_AUTHOR, "Author 1")
                put(BookEntry.BOOK_DESCRIPTION, "Nice book")
            })
            db?.insert(BookEntry.TABLE_NAME, null, ContentValues().apply {
                put(BookEntry.BOOK_NAME, "Name 2")
                put(BookEntry.BOOK_YEAR, 1993)
                put(BookEntry.BOOK_AUTHOR, "Author 2")
                put(BookEntry.BOOK_DESCRIPTION, "Nice book")
            })

        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL(SQL_DELETE_BOOKS_TABLE);
            onCreate(db);
        }
    }
}