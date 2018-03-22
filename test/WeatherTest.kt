/**
 * Created by svince04 on 22/03/2018 for weather.
 */
import org.junit.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class WeatherTest {
    lateinit var weather: Weather

    @Before
    fun initial() {
        weather = Weather("src/portal_data_subset.csv")
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
}