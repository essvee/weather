import java.io.BufferedReader
import java.io.FileReader
import java.net.URL
import java.time.LocalDate
import java.util.*

data class Specimen(val irn: Int, val collection: String, val collected_year: Int, val collected_month: Int,
                    val collected_day: Int, val dec_lat: Double,  val dec_long: Double, val lat_precision: Int,
                    val long_precision: Int, val date: LocalDate)

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
                                tokens[8].toInt(), LocalDate.parse(tokens[9])))
                    }
                    line = fileReader.readLine()
                }
            }
        } catch (e: Exception) {
            println("Reading CSV Error!")
            e.printStackTrace()
        }
    }

    fun readKey(): String {
        try {
            val keyReader = BufferedReader(FileReader("src/apikey"))
            keyReader.use { key = keyReader.readLine() }
        } catch (e: Exception) {
            println("Reading key Error!")
            e.printStackTrace()
        }
        return key
    }

    fun getWeather(lat: Long, long: Long, start_date: String) {
        val result = URL("http://cleanedobservations.wsi.com/CleanedObs.svc/premium?version=2" +
                "&interval=daily&units=metric&format=csv&station=cfsr&userKey=$key" +
                "&lat=39.6932&long=27.800399&startDate=01/01/2018&endDate=01/02/2018").readText()
        print(result)
    }

}

