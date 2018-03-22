import java.io.BufferedReader
import java.io.FileReader
import java.util.*

data class Specimen(val irn: Int, val collection: String, val collected_day: Int, val collected_month: Int,
                    val collected_year: Int, val dec_lat: Double,  val dec_long: Double, val lat_precision: Int,
                    val long_precision: Int)

class Weather (source: String) {
    val specimens = ArrayList<Specimen>()
    lateinit var key: String

    init {
        try {
            val fileReader = BufferedReader(FileReader(source))
            fileReader.use {

                // Read header
                fileReader.readLine()

                // Read the file line by line starting from the second line
                var line = fileReader.readLine()
                while (line != null) {
                    val tokens = line.split(",")
                    // Use values to create specimen and add to list
                    if (!tokens.isEmpty()) {
                        specimens.add(Specimen(tokens[0].toInt(), tokens[1], tokens[2].toInt(), tokens[3].toInt(),
                                tokens[4].toInt(), tokens[5].toDouble(), tokens[6].toDouble(), tokens[7].toInt(),
                                tokens[8].toInt()))
                    }
                    line = fileReader.readLine()
                }
            }
        val keyReader = BufferedReader(FileReader("src/apikey"))
        keyReader.use {
            key = keyReader.readLine()
        }
        } catch (e: Exception) {
            println("Reading CSV Error!")
            e.printStackTrace()
        }
    }


}

