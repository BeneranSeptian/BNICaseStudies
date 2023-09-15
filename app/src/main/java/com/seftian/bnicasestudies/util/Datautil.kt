package com.seftian.bnicasestudies.util

import ChartData
import DonutChartData
import TransactionData

object Datautil {
    fun generateStaticPieChartData(): List<ChartData> {
        return listOf(
            ChartData(
                type = "donutChart",
                data = listOf(
                    DonutChartData(
                        label = "Tarik Tunai",
                        percentage = 55f,
                        data = listOf(
                            TransactionData("21/01/2023", 1000000f),
                            TransactionData("20/01/2023", 500000f),
                            TransactionData("19/01/2023", 1000000f)
                        )
                    ),
                    DonutChartData(
                        label = "QRIS Payment",
                        percentage = 31f,
                        data = listOf(
                            TransactionData("21/01/2023", 159000f),
                            TransactionData("20/01/2023", 35000f),
                            TransactionData("19/01/2023", 1500f)
                        )
                    ),
                    DonutChartData(
                        label = "Top Up Gopay",
                        percentage = 7.7f,
                        data = listOf(
                            TransactionData("21/01/2023", 200000f),
                            TransactionData("20/01/2023", 195000f),
                            TransactionData("19/01/2023", 5000000f)
                        )
                    ),
                    DonutChartData(
                        label = "Lainnya",
                        percentage = 6.3f,
                        data = listOf(
                            TransactionData("21/01/2023", 1000000f),
                            TransactionData("20/01/2023", 500000f),
                            TransactionData("19/01/2023", 1000000f)
                        )
                    ),
                )
            )
        )
    }
}