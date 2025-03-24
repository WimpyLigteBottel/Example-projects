package nel.marco

import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.concurrent.ThreadLocalRandom


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    val chart: XYChart = XYChartBuilder().width(800).height(600)
        .title("Price Over Time")
        .xAxisTitle("Date")
        .yAxisTitle("Price")
        .build()

    chart.styler.datePattern = "yyyy-MM-dd"
    chart.styler.isXAxisTicksVisible = true // Ensure x-axis tick marks are visible
    chart.styler.xAxisLabelRotation = 65 // Rotate labels for better visibility (optional)

    val apples = generateData("Apple", 7)
    val banana = generateData("Banana", 3)
    val candy = generateData("Candy", 5)

    chart.addSeries(apples.first().name, apples.map { it.localDate }, apples.map { it.price })
    chart.addSeries(banana.first().name, banana.map { it.localDate }, banana.map { it.price })
    chart.addSeries(candy.first().name, candy.map { it.localDate }, candy.map { it.price })

    // Show it
    SwingWrapper(chart).displayChart();

}

fun LocalDate.toDate(): Date {
    return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

data class History(val id: String, val name: String, val price: Double, val localDate: Date)

fun generateData(name: String, pastDays: Long = 14): List<History> {
    return (1..pastDays)
        .map {
            History(
                id = UUID.randomUUID().toString(),
                name = name,
                price = ThreadLocalRandom.current().nextDouble(20.0, 80.0),
                localDate = LocalDate.now().minusDays(it).toDate()
            )
        }.sortedBy { it.localDate }
}