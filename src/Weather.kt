import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.lang.Math.*

class Weather (source: String) {
    private val specimens: ArrayList<ArrayList<String>> = arrayListOf()
    private val km = 6372.8
    private lateinit var csvHeader: String
    private lateinit var key: String

    init {
        setKey()
        try {
            // Get specimen records from .csv
            val fileReader = BufferedReader(FileReader(source))

            fileReader.use {
                // Stash headers for later use
                csvHeader = fileReader.readLine()

                // Read the file line by line starting from the second line
                fileReader.forEachLine { val tokens = it.split(",")
                    // Enhance specimen info and add to specimens
                    if (!tokens.isEmpty()) {
                        specimens.add(getWeather(tokens))
                    }
                }
            }
        } catch (e: Exception) {
            println("Reading CSV Error!")
            e.printStackTrace()
        }
    }


    // Enhances specimen data with weather at time of collection and related data
    private fun getWeather(tokens: List<String>): ArrayList<String> {

        val specimenRecord = arrayListOf(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4],
                tokens[5], tokens[6], tokens[7], tokens[8],tokens[9])

        // Set format of start date
        val startDate: LocalDate = LocalDate.parse(tokens[9], DateTimeFormatter.ofPattern("dd/MM/uuuu"))

        print("Getting weather for specimen ${specimenRecord[0]}... ")

        // Reformat start date to fit api requirements and get data for each row...
        val ibmFormatter = DateTimeFormatter.ofPattern("MM/dd/uuuu")
        val url = "http://cleanedobservations.wsi.com/CleanedObs.svc/premium?version=2" +
                "&interval=daily&units=metric&format=csv&station=cfsr&userKey=$key" +
                "&lat=${specimenRecord[5]}&long=${specimenRecord[6]}&startDate=${startDate.format(ibmFormatter)}" +
                "&endDate=${startDate.plusDays(1).format(ibmFormatter)}"

        // Add url to specimen record
        specimenRecord.add(url)
        // Add weather data to specimen record
        specimenRecord.addAll(URL(url).readText().substringAfter("\n").trim().split(",") as MutableList)
        // Add distance between input and output co-ordinates
        specimenRecord.add(haversine(specimenRecord[5].toDouble(), specimenRecord[6].toDouble(),
                specimenRecord[12].toDouble(), specimenRecord[13].toDouble()).toString())

        println("Weather got!")

        return specimenRecord
    }


    // Function to calculate distance (km) between sets of co-ordinates
    // From https://github.com/acmeism/RosettaCodeData/tree/master/Task/Haversine-formula/Kotlin
    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val diffRadians = toRadians(lat2 - lat1)
        val lonRadians = toRadians(lon2 - lon1)
        return 2 * km * asin(sqrt(pow(sin(diffRadians / 2), 2.0) + pow(sin(lonRadians / 2), 2.0) * cos(toRadians(lat1)) * cos(toRadians(lat2))))
    }


    // Write results out to new .csv
    fun writeOut() = try {
        val fileWriter = BufferedWriter(FileWriter("out.csv"))

        fileWriter.use {
            fileWriter.append(csvHeader)
            fileWriter.append("\n")

            specimens.forEach { s ->
                fileWriter.append(s.joinToString())
                fileWriter.append("\n")
            }
        }
    } catch (e: Exception) {
        println("Writing CSV error!")
        e.printStackTrace()
    }


    // Get key for API from text file
    private fun setKey() {
        try {
            val keyReader = BufferedReader(FileReader("src/apikey"))
            keyReader.use { key = keyReader.readLine() }
        } catch (e: Exception) {
            println("Couldn't retrieve key!")
            e.printStackTrace()
        }
    }

}

