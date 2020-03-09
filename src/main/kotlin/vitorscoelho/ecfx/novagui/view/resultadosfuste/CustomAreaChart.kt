package vitorscoelho.ecfx.novagui.view.resultadosfuste

import javafx.beans.property.DoubleProperty
import javafx.scene.Group
import javafx.scene.chart.AreaChart
import javafx.scene.chart.Axis
import javafx.scene.chart.LineChart.SortingPolicy
import javafx.scene.chart.XYChart
import javafx.scene.shape.ClosePath
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import java.util.*

/**
 * [AreaChart] que preenche o grafico utilizando o eixo y como origem ao invés do eixo x (como faz o [AreaChart])
 */
class CustomAreaChart<X, Y>(xAxis: Axis<X>, yAxis: Axis<Y>) : AreaChart<X, Y>(xAxis, yAxis) {
    private fun <X, Y> getDisplayedDataIterator(chart: XYChart<X, Y>, series: Series<X, Y>?): Iterator<Data<X, Y>> {
        val metodo = XYChart::class.java.getDeclaredMethod("getDisplayedDataIterator", Series::class.java)
        metodo.isAccessible = true
        return metodo.invoke(chart, series) as Iterator<Data<X, Y>>
    }

    private fun <X, Y> getCurrentX(data: XYChart.Data<X, Y>): X {
        val metodo = XYChart.Data::class.java.getDeclaredMethod("getCurrentX")
        metodo.isAccessible = true
        return metodo.invoke(data) as X
    }

    private fun <X, Y> getCurrentY(data: XYChart.Data<X, Y>): Y {
        val metodo = XYChart.Data::class.java.getDeclaredMethod("getCurrentY")
        metodo.isAccessible = true
        return metodo.invoke(data) as Y
    }

    //private Map<Series<X,Y>, DoubleProperty> seriesYMultiplierMap = new HashMap<>();
    private fun getSeriesYMultiplierMap(series: Series<X, Y>): DoubleProperty? {
        val field = AreaChart::class.java.getDeclaredField("seriesYMultiplierMap")
        field.isAccessible = true
        val fieldMap = field.get(this) as Map<Series<X, Y>, DoubleProperty>
        return fieldMap.get(series)
    }

    fun getDataSize(): Int {
        val data = data
        return data?.size ?: 0
    }

    fun <X, Y> myMakePaths(
        chart: XYChart<X, Y>, series: Series<X, Y>?,
        constructedPath: MutableList<LineTo>,
        fillPath: Path?, linePath: Path,
        yAnimMultiplier: Double, sortAxis: SortingPolicy
    ) {
        val axisX = chart.xAxis
        val axisY = chart.yAxis
        val hlw = linePath.strokeWidth / 2.0
        val sortX = sortAxis == SortingPolicy.Y_AXIS
        val sortY = sortAxis == SortingPolicy.X_AXIS
        val dataXMin = if (sortX) -hlw else Double.NEGATIVE_INFINITY
        val dataXMax = if (sortX) axisX.width + hlw else Double.POSITIVE_INFINITY
        val dataYMin = if (sortY) -hlw else Double.NEGATIVE_INFINITY
        val dataYMax = if (sortY) axisY.height + hlw else Double.POSITIVE_INFINITY
        var prevDataPoint: LineTo? = null
        var nextDataPoint: LineTo? = null
        constructedPath.clear()
        val it = getDisplayedDataIterator(chart, series)
        while (it.hasNext()) {
            val item = it.next()
            val x = axisX.getDisplayPosition(getCurrentX(item))
            val y = axisY.getDisplayPosition(
                axisY.toRealValue(axisY.toNumericValue(getCurrentY(item)) * yAnimMultiplier)
            )
            val skip = java.lang.Double.isNaN(x) || java.lang.Double.isNaN(y)
            val symbol = item.node
            if (symbol != null) {
                val w = symbol.prefWidth(-1.0)
                val h = symbol.prefHeight(-1.0)
                if (skip) {
                    symbol.resizeRelocate(-w * 2, -h * 2, w, h)
                } else {
                    symbol.resizeRelocate(x - w / 2, y - h / 2, w, h)
                }
            }
            if (skip) {
                continue
            }
            if (x < dataXMin || y < dataYMin) {
                if (prevDataPoint == null) {
                    prevDataPoint = LineTo(x, y)
                } else if (sortX && prevDataPoint.x <= x ||
                    sortY && prevDataPoint.y <= y
                ) {
                    prevDataPoint.x = x
                    prevDataPoint.y = y
                }
            } else if (x <= dataXMax && y <= dataYMax) {
                constructedPath.add(LineTo(x, y))
            } else {
                if (nextDataPoint == null) {
                    nextDataPoint = LineTo(x, y)
                } else if (sortX && x <= nextDataPoint.x ||
                    sortY && y <= nextDataPoint.y
                ) {
                    nextDataPoint.x = x
                    nextDataPoint.y = y
                }
            }
        }
        if (!constructedPath.isEmpty() || prevDataPoint != null || nextDataPoint != null) {
            if (sortX) {
                Collections.sort(
                    constructedPath
                ) { e1: LineTo, e2: LineTo ->
                    java.lang.Double.compare(
                        e1.x,
                        e2.x
                    )
                }
            } else if (sortY) {
                Collections.sort(
                    constructedPath
                ) { e1: LineTo, e2: LineTo ->
                    java.lang.Double.compare(
                        e1.y,
                        e2.y
                    )
                }
            } else {
                // assert prevDataPoint == null && nextDataPoint == null
            }
            if (prevDataPoint != null) {
                constructedPath.add(0, prevDataPoint)
            }
            if (nextDataPoint != null) {
                constructedPath.add(nextDataPoint)
            }

            // assert !constructedPath.isEmpty()
            val first = constructedPath[0]
            val last = constructedPath[constructedPath.size - 1]
            val displayYPos = first.y
            val lineElements = linePath.elements
            lineElements.clear()
            lineElements.add(MoveTo(first.x, displayYPos))
            lineElements.addAll(constructedPath)
            if (fillPath != null) {
                val fillElements = fillPath.elements
                fillElements.clear()
//                val yOrigin = axisY.getDisplayPosition(axisY.toRealValue(0.0))
//                fillElements.add(MoveTo(first.x, yOrigin))
//                fillElements.addAll(constructedPath)
//                fillElements.add(LineTo(last.x, yOrigin))
//                fillElements.add(ClosePath())
                val xOrigin = axisX.getDisplayPosition(axisX.toRealValue(0.0))
                with(fillElements) {
                    add(MoveTo(xOrigin, first.y))
                    addAll(constructedPath)
                    add(LineTo(xOrigin, last.y))
                    add(ClosePath())
                }
            }
        }
    }

    override fun layoutPlotChildren() {
        val constructedPath: MutableList<LineTo> = ArrayList(getDataSize())
        for (seriesIndex in 0 until getDataSize()) {
            val series = data[seriesIndex]
            val seriesYAnimMultiplier = getSeriesYMultiplierMap(series)
            val children =
                (series.node as Group).children
            val fillPath = children[0] as Path
            val linePath = children[1] as Path
            myMakePaths(
                this, series, constructedPath, fillPath, linePath,
                seriesYAnimMultiplier!!.get(), SortingPolicy.X_AXIS
            )
        }
    }
}
//    /**
//     * Gets the size of the data returning 0 if the data is null
//     * @return The number of items in data, or null if data is null
//     */
//    fun getDataSize(): Int {
//        val data = data
//        return data?.size ?: 0
//    }
//
//    override fun layoutPlotChildren() {
//        val constructedPath: List<LineTo> =
//            ArrayList(getDataSize())
//        for (seriesIndex in 0 until getDataSize()) {
//            val series = data[seriesIndex]
//            val seriesYAnimMultiplier = seriesYMultiplierMap[series]
//            val children =
//                (series.node as Group).children
//            val fillPath = children[0] as Path
//            val linePath = children[1] as Path
//            makePaths(
//                this, series, constructedPath, fillPath, linePath,
//                seriesYAnimMultiplier!!.get(), SortingPolicy.X_AXIS
//            )
//        }
//    }
//
//    companion object {
//        fun <X, Y> makePaths(
//            chart: XYChart<X, Y>, series: Series<X, Y>?,
//            constructedPath: MutableList<LineTo>,
//            fillPath: Path?, linePath: Path,
//            yAnimMultiplier: Double, sortAxis: SortingPolicy
//        ) {
//            val axisX = chart.xAxis
//            val axisY = chart.yAxis
//            val hlw = linePath.strokeWidth / 2.0
//            val sortX = sortAxis == SortingPolicy.X_AXIS
//            val sortY = sortAxis == SortingPolicy.Y_AXIS
//            val dataXMin = if (sortX) -hlw else Double.NEGATIVE_INFINITY
//            val dataXMax = if (sortX) axisX.width + hlw else Double.POSITIVE_INFINITY
//            val dataYMin = if (sortY) -hlw else Double.NEGATIVE_INFINITY
//            val dataYMax = if (sortY) axisY.height + hlw else Double.POSITIVE_INFINITY
//            var prevDataPoint: LineTo? = null
//            var nextDataPoint: LineTo? = null
//            constructedPath.clear()
//            val it = chart.getDisplayedDataIterator(series)
//            while (it.hasNext()) {
//                val item = it.next()
//                val x = axisX.getDisplayPosition(item.getCurrentX())
//                val y = axisY.getDisplayPosition(
//                    axisY.toRealValue(axisY.toNumericValue(item.getCurrentY()) * yAnimMultiplier)
//                )
//                val skip = java.lang.Double.isNaN(x) || java.lang.Double.isNaN(y)
//                val symbol = item.node
//                if (symbol != null) {
//                    val w = symbol.prefWidth(-1.0)
//                    val h = symbol.prefHeight(-1.0)
//                    if (skip) {
//                        symbol.resizeRelocate(-w * 2, -h * 2, w, h)
//                    } else {
//                        symbol.resizeRelocate(x - w / 2, y - h / 2, w, h)
//                    }
//                }
//                if (skip) {
//                    continue
//                }
//                if (x < dataXMin || y < dataYMin) {
//                    if (prevDataPoint == null) {
//                        prevDataPoint = LineTo(x, y)
//                    } else if (sortX && prevDataPoint.x <= x ||
//                        sortY && prevDataPoint.y <= y
//                    ) {
//                        prevDataPoint.x = x
//                        prevDataPoint.y = y
//                    }
//                } else if (x <= dataXMax && y <= dataYMax) {
//                    constructedPath.add(LineTo(x, y))
//                } else {
//                    if (nextDataPoint == null) {
//                        nextDataPoint = LineTo(x, y)
//                    } else if (sortX && x <= nextDataPoint.x ||
//                        sortY && y <= nextDataPoint.y
//                    ) {
//                        nextDataPoint.x = x
//                        nextDataPoint.y = y
//                    }
//                }
//            }
//            if (!constructedPath.isEmpty() || prevDataPoint != null || nextDataPoint != null) {
//                if (sortX) {
//                    constructedPath.sortWith(Comparator { e1: LineTo, e2: LineTo ->
//                        java.lang.Double.compare(
//                            e1.x,
//                            e2.x
//                        )
//                    })
//                } else if (sortY) {
//                    constructedPath.sortWith(Comparator { e1: LineTo, e2: LineTo ->
//                        java.lang.Double.compare(
//                            e1.y,
//                            e2.y
//                        )
//                    })
//                } else {
//                    // assert prevDataPoint == null && nextDataPoint == null
//                }
//                if (prevDataPoint != null) {
//                    constructedPath.add(0, prevDataPoint)
//                }
//                if (nextDataPoint != null) {
//                    constructedPath.add(nextDataPoint)
//                }
//
//                // assert !constructedPath.isEmpty()
//                val first = constructedPath[0]
//                val last = constructedPath[constructedPath.size - 1]
//                val displayYPos = first.y
//                val lineElements = linePath.elements
//                lineElements.clear()
//                lineElements.add(MoveTo(first.x, displayYPos))
//                lineElements.addAll(constructedPath)
//                if (fillPath != null) {
//                    val fillElements =
//                        fillPath.elements
//                    fillElements.clear()
//                    val yOrigin = axisY.getDisplayPosition(axisY.toRealValue(0.0))
//                    fillElements.add(MoveTo(first.x, yOrigin))
//                    fillElements.addAll(constructedPath)
//                    fillElements.add(LineTo(last.x, yOrigin))
//                    fillElements.add(ClosePath())
//                }
//            }
//        }
//    }
//}