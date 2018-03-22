import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*

data class Specimen(val irn: String, val collection: String, val collected_day: Int, val collected_month: Int,
                    val collected_year: Int, val dec_lat: Double,  val dec_long: Double, val lat_precision: Int,
                    val long_precision: Int)

fun main(args: Array<String>?) {
    var fileReader: BufferedReader? = null

    try {
        val specimens = ArrayList<Specimen>()
        var line: String?

        fileReader = BufferedReader(FileReader("src/portal_data_subset.csv"))

        // Read CSV header
        fileReader.readLine()

        // Read the file line by line starting from the second line
        line = fileReader.readLine()
        while (line != null) {
            val tokens = line.split(",")
            if (tokens.isNotEmpty()) {
                val specimen = Specimen(
                        tokens[0],
                        tokens[1],
                        tokens[2].toInt(),
                        tokens[3].toInt(),
                        tokens[4].toInt(),
                        tokens[5].toDouble(),
                        tokens[6].toDouble(),
                        tokens[7].toInt(),
                        tokens[8].toInt()
                        )
                specimens.add(specimen)
            }

            line = fileReader.readLine()
            }

        // Print the new customer list
        for (s in specimens) {
            println(s)
        }
    } catch (e: Exception) {
        println("Reading CSV Error!")
        e.printStackTrace()
    } finally {
        try {
            fileReader!!.close()
        } catch (e: IOException) {
            println("Closing fileReader Error!")
            e.printStackTrace()
        }
    }
}