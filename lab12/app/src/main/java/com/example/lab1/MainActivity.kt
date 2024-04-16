package com.example.lab1

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import java.time.Year


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val books = getBooksRepository();

        // Setup book names
        val bookNames = findViewById<Spinner>(R.id.bookNames)
        bookNames.adapter = ArrayAdapter(
            this,
            R.layout.book_selection_item,
            books.getBookNames()
        )

        // Setup years selection
        val yearsRadioGroup = findViewById<RadioGroup>(R.id.yearsRadioGroup)
        books.getAllPossibleYears().map { year ->
            val button = RadioButton(this)
            button.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            button.textSize=23f
            button.text = year.toString()
            button.id = year.value
            button
        }.forEach {
            yearsRadioGroup.addView(it)
        }

        findViewById<Button>(R.id.okButton).setOnClickListener(View.OnClickListener {
            if (yearsRadioGroup.checkedRadioButtonId == 0 || bookNames.selectedItem == null) {
                Toast.makeText(
                    this@MainActivity,
                    "Будь ласка, виберіть рік і книгу",
                    Toast.LENGTH_LONG
                ).show()
                return@OnClickListener
            }

            val name = bookNames.selectedItem as String;
            val year = yearsRadioGroup.checkedRadioButtonId;

            val book = books.findBook(name, Year.of(year));
            if (book == null) {
                Toast.makeText(
                    this@MainActivity,
                    "Такої книги немає",
                    Toast.LENGTH_LONG
                ).show()
                return@OnClickListener
            }

            BookInfoDialog(book).show(supportFragmentManager, "BOOK_INFO")
        })
    }

    private fun getBooksRepository(): BooksRepository {
    // for lab 2
     return InMemoryBooksRepository()
    }
}

class BookInfoDialog(private val book: Book) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity).apply {
            setTitle(book.name)
            setMessage(
                "Автор: ${book.author}\nРік: ${book.publicationYear}\n" +
                        book.description
            )
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        }.create()
    }
}
