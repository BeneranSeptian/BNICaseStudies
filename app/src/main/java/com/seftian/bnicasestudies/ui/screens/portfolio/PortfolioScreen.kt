package com.seftian.bnicasestudies.ui.screens.portfolio

import ChartData
import DonutChartData
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.seftian.bnicasestudies.util.Datautil
import com.seftian.bnicasestudies.util.Helper


@Composable
fun PortfolioScreen(
    navController: NavController
) {

    var selectedDonutChart by remember {
        mutableStateOf<DonutChartData?>(null)
    }

    val sampleData = Datautil.generateStaticPieChartData()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ComposablePieChart(
            data = sampleData,
            modifier = Modifier.weight(0.4f),
            onClickSection = { selectedDonutChart = it }
        )

        selectedDonutChart?.let {
            Text(selectedDonutChart!!.label, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            LazyColumn(
                modifier = Modifier.weight(0.6f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(it.data) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                        ,
                    ) {
                        Text(text = it.trx_date)
                        Text(text = (Helper.convertLongToCurrencyString(it.nominal.toLong())))
                    }
                }
            }
        }
    }
}

@Composable
fun ComposablePieChart(
    data: List<ChartData>,
    modifier: Modifier = Modifier,
    onClickSection: (DonutChartData?) -> Unit
) {
    var showLabel by remember {
        mutableStateOf(false)
    }
    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        factory = { context ->
            PieChart(context).apply {
                description.text = ""
            }
        },
        update = { pieChart ->
            val entries = mutableListOf<PieEntry>()

            for (chartData in data) {
                for (donutChartData in chartData.data) {
                    entries.add(PieEntry(donutChartData.percentage, donutChartData.label))
                }
            }

            val dataSet = PieDataSet(entries, "Transaction Types")
            dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()


            pieChart.legend.isEnabled = false
            dataSet.setDrawValues(false)

            pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    val pe = e as PieEntry
                    pieChart.centerText = "${pe.label}: ${pe.value}%"

                    val selectedLabel = pe.label
                    val selectedData = data.flatMap { it.data }.firstOrNull { it.label == selectedLabel }
                    if (selectedData != null) {
                        onClickSection(selectedData)
                        pieChart.setDrawEntryLabels(false)
                    }
                }

                override fun onNothingSelected() {
                    pieChart.centerText = ""
                    pieChart.setDrawEntryLabels(true)
                    onClickSection(null)
                }
            })


            val pieData = PieData(dataSet)
            pieChart.data = pieData
            pieChart.invalidate()
        }
    )
}

class DefaultValueFormatter(private val decimalPlaces: Int, val modify: (Float) -> String) :
    ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return modify(value)
    }
}