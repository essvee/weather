import java.io.BufferedReader
import java.io.FileReader
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Weather (source: String) {
    val specimens: ArrayList<ArrayList<String>> = arrayListOf<ArrayList<String>>()
    private val csvHeader = "irn,collection_code,collected_year,collected_month,collected_day," +
            "decimal_latitude,decimal_longitude,lat_precision,long_precision,date,SiteId,Latitude,Longitude," +
            "Day,Month,Year,MinSurfaceTemperatureCelsius,MaxSurfaceTemperatureCelsius,AvgSurfaceTemperatureCelsius," +
            "MinSurfaceDewpointTemperatureCelsius,MaxSurfaceDewpointTemperatureCelsius," +
            "AvgSurfaceDewpointTemperatureCelsius,MinSurfaceWetBulbTemperatureCelsius," +
            "MaxSurfaceWetBulbTemperatureCelsius,AvgSurfaceWetBulbTemperatureCelsius," +
            "MinRelativeHumidityPercent,MaxRelativeHumidityPercent,AvgRelativeHumidityPercent," +
            "MinSurfaceAirPressureKilopascals,MaxSurfaceAirPressureKilopascals,AvgSurfaceAirPressureKilopascals," +
            "MinCloudCoveragePercent,MaxCloudCoveragePercent,AvgCloudCoveragePercent,MinWindChillTemperatureCelsius," +
            "MaxWindChillTemperatureCelsius,AvgWindChillTemperatureCelsius,MinApparentTemperatureCelsius," +
            "MaxApparentTemperatureCelsius,AvgApparentTemperatureCelsius,MinWindSpeedKph,MaxWindSpeedKph," +
            "AvgWindSpeedKph,MinWindDirectionDegrees,MaxWindDirectionDegrees,AvgWindDirectionDegrees," +
            "MinPrecipitationPreviousHourCentimeters,MaxPrecipitationPreviousHourCentimeters," +
            "AvgPrecipitationPreviousHourCentimeters,SumPrecipitationPreviousHourCentimeters," +
            "MinDownwardSolarRadiationWsqm,MaxDownwardSolarRadiationWsqm,AvgDownwardSolarRadiationWsqm," +
            "SumDownwardSolarRadiationWsqm,MinDiffuseHorizontalRadiationWsqm,MaxDiffuseHorizontalRadiationWsqm," +
            "AvgDiffuseHorizontalRadiationWsqm,SumDiffuseHorizontalRadiationWsqm,MinDirectNormalIrradianceWsqm," +
            "MaxDirectNormalIrradianceWsqm,AvgDirectNormalIrradianceWsqm,SumDirectNormalIrradianceWsqm," +
            "MinMslPressureKilopascals,MaxMslPressureKilopascals,AvgMslPressureKilopascals,MinHeatIndexCelsius," +
            "MaxHeatIndexCelsius,AvgHeatIndexCelsius,MinSnowfallCentimeters,MaxSnowfallCentimeters," +
            "AvgSnowfallCentimeters,SumSnowfallCentimeters,MinSurfaceWindGustsKph,MaxSurfaceWindGustsKph," +
            "AvgSurfaceWindGustsKph,MinPotentialEvapotranspirationMicrometersPerHour," +
            "MaxPotentialEvapotranspirationMicrometersPerHour,AvgPotentialEvapotranspirationMicrometersPerHour," +
            "MinSurfaceWaterRunOffMillimeters,MaxSurfaceWaterRunOffMillimeters,AvgSurfaceWaterRunOffMillimeters," +
            "SumSurfaceWaterRunOffMillimeters,MinTenToFortyLiquidSoilMoisturePercent," +
            "MaxTenToFortyLiquidSoilMoisturePercent,AvgTenToFortyLiquidSoilMoisturePercent," +
            "MinTenToFortySoilTemperatureCelsius,MaxTenToFortySoilTemperatureCelsius," +
            "AvgTenToFortySoilTemperatureCelsius,MinZeroToTenLiquidSoilMoisturePercent," +
            "MaxZeroToTenLiquidSoilMoisturePercent,AvgZeroToTenLiquidSoilMoisturePercent," +
            "MinZeroToTenSoilTemperatureCelsius,MaxZeroToTenSoilTemperatureCelsius,AvgZeroToTenSoilTemperatureCelsius," +
            "MinFortyToOneHundredLiquidSoilMoisturePercent,MaxFortyToOneHundredLiquidSoilMoisturePercent," +
            "AvgFortyToOneHundredLiquidSoilMoisturePercent,MinFortyToOneHundredSoilTemperatureCelsius," +
            "MaxFortyToOneHundredSoilTemperatureCelsius,AvgFortyToOneHundredSoilTemperatureCelsius"
    lateinit var key: String

    init {
        setKey()
        try {
            // Get specimen records
            val fileReader = BufferedReader(FileReader(source))

            fileReader.use {
                // Read header
                fileReader.readLine()
                // Read the file line by line starting from the second line
                fileReader.forEachLine {

                    val tokens = it.split(",")
                    val startDate: LocalDate = LocalDate.parse(tokens[9], DateTimeFormatter.ofPattern("dd/MM/uuuu"))
                    val endDate:LocalDate = startDate.plusDays(1)

                    // Use read values to create specimen record and add to list
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

