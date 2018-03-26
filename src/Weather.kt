import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Weather (source: String) {
    val specimens: ArrayList<ArrayList<String>> = arrayListOf()
    lateinit var csvHeader: String
    lateinit var key: String

    init {
        setKey()
        try {
            // Get specimen records
            val fileReader = BufferedReader(FileReader(source))

            fileReader.use {
                // Stash headers for later use
                csvHeader = fileReader.readLine()

                // Read the file line by line starting from the second line
                fileReader.forEachLine {

                    val tokens = it.split(",")
                    val startDate: LocalDate = LocalDate.parse(tokens[9], DateTimeFormatter.ofPattern("dd/MM/uuuu"))
                    val endDate:LocalDate = startDate.plusDays(1)

                    // Create specimen record and add to list
                    if (!tokens.isEmpty()) {
                        val specimenRecord = arrayListOf(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4],
                                tokens[5], tokens[6], tokens[7],tokens[8], tokens[9])

                        // Date formatter for api call
                        val ibmFormatter = DateTimeFormatter.ofPattern("MM/dd/uuuu")
                        val resultList = URL("http://cleanedobservations.wsi.com/CleanedObs.svc/premium?version=2" +
                                "&interval=daily&units=metric&format=csv&station=cfsr&userKey=$key" +
                                "&lat=${tokens[5]}&long=${tokens[5]}&startDate=${startDate.format(ibmFormatter)}" +
                                "&endDate=${endDate.format(ibmFormatter)}").readText().substringAfter("\n").split(",")

                        // Add results onto end of specimen arraylist + add into main list
                        specimenRecord.addAll(resultList)
                        specimens.add(specimenRecord)
                    }
                }
            }
        } catch (e: Exception) {
            println("Reading CSV Error!")
            e.printStackTrace()
        }
        writeOut()
    }

    fun writeOut() {
        try {
            val fileWriter = BufferedWriter(FileWriter("out.csv"))

            fileWriter.use {
                fileWriter.append(csvHeader)
                fileWriter.append("\n")

                for (s in specimens) {
                    fileWriter.append(s.joinToString())
                    fileWriter.append("\t")
                }
            }
        } catch (e: Exception) {
            println("Writing CSV error!")
            e.printStackTrace()
        }
    }


    fun setKey() {
        try {
            // Get key for API
            val keyReader = BufferedReader(FileReader("src/apikey"))
            keyReader.use { key = keyReader.readLine() }
        } catch (e: Exception) {
            println("Couldn't retrieve key!")
            e.printStackTrace()
        }
    }

}

