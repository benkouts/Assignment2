//Ben Koutsoumbaris
//COSC 3352

package com.example.assignment2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

// This main class will be making extensive use of the exp4j library's expression builder.
// It can take a string and evaluate it as if it were an arithmetic expression.
// I found it to be the easiest solution to working on the "math" portion of this project.
class MainActivity : AppCompatActivity() {

    // The input/output is displayed in a TextView window
    lateinit var textIn: TextView

    // True if the last entry is numeric
    var numLast: Boolean = false

    // Prevents addition of another "." if true
    var dotLast: Boolean = false

    // Represent that current state is in error or not
    var errorFlag: Boolean = false

    // Create a new instance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textIn = findViewById(R.id.txtInput)
    }

    // When a user touches a number button, it is appended and displayed
    // NOTE: Because of how ext4j expressions are structured, the sqrt button
    // also uses this function (it CANNOT be entered last)
    fun numberButton(view: View) {
        if (errorFlag) {
            // This is to lock the TextView to displaying "Error" to prevent further entry
            textIn.text = (view as Button).text
            errorFlag = false
        } else {
            textIn.append((view as Button).text)
        }
        numLast = true
    }

    // When a user touches the "." button, it is appended and displayed
    fun decimalButton(view: View) {
        if (numLast && !errorFlag && !dotLast) {
            textIn.append(".")
            numLast = false
            dotLast = true
        }
    }

    // When a user touches an operator button, it is appended and displayed
    // Operators include +,-,*,/, and ^
    fun operatorButton(view: View) {
        if (numLast && !errorFlag) {
            textIn.append((view as Button).text)
            numLast = false
            dotLast = false    // Reset the DOT flag
        }
    }

    // When a user touches the clear button, TextView is cleared
    fun clearButton(view: View) {
        this.textIn.text = ""
        numLast = false
        errorFlag = false
        dotLast = false
    }

    // Here we use exp4j to create and calculate the whole expression entered.
    // This also handles arithmetic errors (i.e. divide by zero)
    fun onEqual(view: View) {
        // The expression can be solved if the last entry was a number.
        // If any error occurred previously, do nothing.
        if (numLast && !errorFlag) {
            // Read the expression
            val txt = textIn.text.toString()
            // Use exp4j library to create an expression using TextView
            val expression = ExpressionBuilder(txt).build()
            try {
                // Calculate the result and display in TextView
                val result = expression.evaluate()
                textIn.text = result.toString()
                dotLast = true
            } catch (ex: ArithmeticException) {
                // If the user attempts a zero-division or similar
                textIn.text = "Error"
                errorFlag = true
                numLast = false
            }
        }
    }
}