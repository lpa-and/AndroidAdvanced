package com.example.exercisekotlin

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import java.security.SecureRandom

class MainActivity : AppCompatActivity() {

    // lateinit can be used when we know this will be initialized later in a callback
    // but don't want to declare it as nullable
    private lateinit var textView: TextView
    // The alternative would be to make it nullable, but then you have to deal with null checks
    private var button: Button? = null

    // When using by lazy { }, the val is only initialized when it is used for the first time.
    // This is called lazy initialization.
    private val random by lazy { SecureRandom() }

    private val mainScope = MainScope()

    private val randomBoolean: Boolean
        // By defining a val with get(), this getter is then called each time the val is accessed
        get() = random.nextBoolean()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.text_view)
        button = findViewById(R.id.button)

        // Since button is a nullable type, we have to use ?. to only execute the following code
        // when button is non-null
        button?.setOnClickListener {
            // Instead of creating a anonymous class for our click listener as we did in Java,
            // we can instead directly write our click listener as a function. This is possible
            // here, because View.OnClickListener interface only defined one function that needs
            // to be implemented.
            loadData()
        }
    }

    private fun loadData() {
        // Launch a Coroutine on a Scope (here MainScope, which means by default code inside
        // runs on the mainThread
        mainScope.launch {
            textView.text = "Loading ..."
            try {
                // Call loadNewestMessage() and since it is a suspend fun, suspend execution here
                // until the fun returns
                val message = loadNewestMessage()
                textView.text = message.fullInfo
            } catch (e: Exception) {
                // When loadNewestMessage() throws an exception, we catch it!
                textView.text = "Error loading newest message :("
            }
        }
    }

    // suspend means that this fun can suspend its execution when calling other suspend fun
    // by using withContext() we can switch threads, so when this suspend fun is called, the code
    // inside withContext() is run on a background thread
    private suspend fun loadNewestMessage(): Message =
        withContext(Dispatchers.Default) {
            // Just wait for 1000ms (1s). Notice that delay() is also a suspend fun!
            delay(1000)
            // Randomly throw an Exception here
            if (randomBoolean) throw Exception("This sadly did not work!")
            // If all works out, return the Message
            Message(text = "No news from my side.")
        }

    // This is how we would write the above code with an AsyncTask. It's way more complicated to
    // see what is going on here, if you compare it to our suspend fun.
    private fun loadDataAsyncTask() {
        textView.text = "Loading ..."

        object : AsyncTask<Void, Void, Message?>() {
            override fun doInBackground(vararg params: Void?): Message? =
                try {
                    Thread.sleep(1000)
                    if (randomBoolean) throw Exception("This sadly did not work!")
                    Message(text = "No news from my side.")
                } catch (e: Exception) {
                    null
                }

            override fun onPostExecute(message: Message?) {
                if (message != null) {
                    textView.text = message.fullInfo
                } else {
                    textView.text = "Error loading newest message :("
                }
            }
        }.execute()
    }

    // We can extend other classes with additional vals and funs (this is called extension fun)
    private val Message.fullInfo: String
        // We can directly reference variables inside Strings
        get() = "$author wrote: $text"
}

// Data class are similar to AutoValue classes in Java. By using vals in the constructor
// we get immutable objects from this class.
data class Message(
    // We can set a default value for fun parameters and constructor arguments
    val author: String = "Anonymous",
    val text: String
)