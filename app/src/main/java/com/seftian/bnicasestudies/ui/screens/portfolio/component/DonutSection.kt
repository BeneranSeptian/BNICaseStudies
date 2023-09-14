data class DonutChartData(
    val label: String,
    val percentage: Float,
    val data: List<TransactionData>
)

data class TransactionData(
    val trx_date: String,
    val nominal: Float
)

data class ChartData(
    val type: String,
    val data: List<DonutChartData>
)