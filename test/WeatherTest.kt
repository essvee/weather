/**
 * Created by svince04 on 22/03/2018 for weather.
 */
import org.junit.*
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class WeatherTest {
    lateinit var weather: Weather
    lateinit var key: String

    @Before
    fun initial() {
        weather = Weather("src/portal_data_subset.csv")
        key = weather.readKey()
    }

    @Test
    fun readFile() {
        val expected = 1581
        assertEquals(expected, weather.specimens.size)
    }

    @Test
    fun testSpecimenIRN() {
        val expected = 4770704
        assertEquals(expected, weather.specimens[0].irn)
    }

    @Test
    fun testReadKey() {
        val expected = "cd"
        assertEquals(expected, weather.readKey().substring(0,2))
    }

    @Test
    fun testgetWeather() {
        val expected = "cd"
        val result = weather.getWeather(49.187720206553, 49.187720206553, LocalDate.parse("1980-03-12"))
        assertNotNull(result)
    }


}