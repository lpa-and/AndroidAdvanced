package at.technikumwien.maps.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object CoroutineDownloader {

    /**
     * Downloads [url] and parses it with [jsonParser]
     *
     * @return The downloaded and parsed object as specified by type parameter
     */
    suspend fun <T> download(url: String, jsonParser: (String) -> T): T {
        val downloadedString: String = withContext(Dispatchers.IO) {
            var connection: HttpsURLConnection? = null
            var result = ""

            try {
                connection = (URL(url).openConnection() as HttpsURLConnection)
                    .apply {
                        readTimeout = 30000
                        connectTimeout = 3000
                        requestMethod = "GET"
                        doInput = true
                        connect()

                        // Check whether response was HTTP status 200 OK
                        // throw IOException if not
                        responseCode.takeIf { it == HttpsURLConnection.HTTP_OK }
                            ?: throw IOException("HTTP error code $responseCode")

                        // Read the whole InputStream of Connection as String
                        result = inputStream.use { it.bufferedReader().readText() }
                    }
            } finally {
                connection?.disconnect()
            }
            result
        }

        return withContext(Dispatchers.Default) { jsonParser(downloadedString) }
    }
}