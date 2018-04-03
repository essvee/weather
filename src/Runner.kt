import java.time.Duration
import java.time.LocalDateTime

fun main(args: Array<String>) {
    val start = LocalDateTime.now()
    val weather = Weather("src/portal_data_subset.csv")
    weather.writeOut()
    val end = LocalDateTime.now()
    println(Duration.between(start, end).toMinutes())
}