package ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Composable DatePicker for Compose Desktop and other platforms.
 * URL: https://gist.github.com/erdevasconcellos/8d38a6124b27151f87bdbb4edf4e5cf3
 */
@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun DatePicker(
    initDate: Date = Date(),
    onDateSelect: (Date) -> Unit,
    onDismissRequest: () -> Unit,
    minYear: Int = GregorianCalendar().get(Calendar.YEAR) - 10,
    maxYear: Int = GregorianCalendar().get(Calendar.YEAR) + 10,
) {
    val calendar = GregorianCalendar().apply {
        time = initDate
    }

    var year by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var month by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var day by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    EmptyBaseDialog(
        onDismissRequest = {}
        //onDismissRequest
    ) {
        MaterialTheme(
            colors = lightColors()
        ) {
            Card(
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.width(IntrinsicSize.Min)
                ) {

                    Box(
                        modifier = Modifier.background(MaterialTheme.colors.primary)
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = SimpleDateFormat("EEEE, d  MMMM  yyyy").format(
                                GregorianCalendar(
                                    year,
                                    month,
                                    day
                                ).time
                            ),
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.h6
                        )
                    }

                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {


                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .padding(8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {

                            MonthSelector(
                                month = month,
                                onValueChange = { month = it }
                            )

                            YearSelector(
                                year = year,
                                onValueChange = { year = it },
                                minYear = minYear,
                                maxYear = maxYear,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )

                        }

                        Header("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС")
                        Divider(Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colors.primary))

                        val startDay = GregorianCalendar(year, month, 1)
                            .apply {
                                firstDayOfWeek = Calendar.SUNDAY
                            }.get(Calendar.DAY_OF_WEEK)

                        var render = false
                        var dayCounter = 1
                        val maxDay = GregorianCalendar(year, month, 1).daysCount()

                        if (day > maxDay) {
                            day = maxDay
                        }

                        for (i in 1..6) {
                            Row {
                                for (j in 1..7) {
                                    if (j == startDay) {
                                        render = true
                                    }
                                    if (!render || dayCounter > maxDay) {
                                        Day(0, false) { }
                                    } else {
                                        Day(
                                            day = dayCounter,
                                            selected = (day == dayCounter),
                                            onChangeValue = { day = it }
                                        )
                                        dayCounter++
                                    }
                                }
                            }
                        }

                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { onDismissRequest() }
                        ) {
                            Text(
                                text = "Отмена"
                            )
                        }

                        TextButton(
                            onClick = {
                                onDateSelect(GregorianCalendar(year, month, day).time)
                            }
                        ) {
                            Text(
                                text = "Подтвердить"
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun YearSelector(
    year: Int,
    onValueChange: (Int) -> Unit,
    minYear: Int,
    maxYear: Int,
    modifier: Modifier = Modifier,
) {
    var expandYearList by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.clickable { expandYearList = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = year.toString())
        Spacer(Modifier.width(4.dp))
        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)

        DropdownMenu(
            expanded = expandYearList,
            onDismissRequest = { expandYearList = false }
        ) {
            for (y in minYear..maxYear) {
                DropdownMenuItem(onClick = {
                    onValueChange(y)
                    expandYearList = false
                }) {
                    Text(text = y.toString())
                }
            }
        }
    }
}

@Composable
fun MonthSelector(
    month: Int,
    onValueChange: (Int) -> Unit,
) {
    var expandMonthList by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.clickable { expandMonthList = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = monthName(month).uppercase())
        Spacer(Modifier.width(4.dp))
        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)

        DropdownMenu(
            expanded = expandMonthList,
            onDismissRequest = { expandMonthList = false }
        ) {
            val months = listOf(
                Calendar.JANUARY,
                Calendar.FEBRUARY,
                Calendar.MARCH,
                Calendar.APRIL,
                Calendar.MAY,
                Calendar.JUNE,
                Calendar.JULY,
                Calendar.AUGUST,
                Calendar.SEPTEMBER,
                Calendar.OCTOBER,
                Calendar.NOVEMBER,
                Calendar.DECEMBER
            )
            for (m in months) {
                DropdownMenuItem(onClick = {
                    onValueChange(m)
                    expandMonthList = false
                }) {
                    Text(text = monthName(m).uppercase())
                }
            }
        }
    }
}

@Composable
fun Header(vararg daysNames: String) {
    Row {
        for (dayName in daysNames) {
            DayName(dayName)
        }
    }
}

@Composable
fun Day(
    day: Int,
    selected: Boolean,
    onChangeValue: (Int) -> Unit,
) {
    val clickable = day != 0

    Box(
        modifier = Modifier.size(48.dp)
            .padding(3.dp)
            .clip(RoundedCornerShape(50))
            .background(
                if (selected) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.surface
                }
            ).clickable(enabled = clickable) { onChangeValue(day) },
        contentAlignment = Alignment.Center
    ) {
        if (day != 0) {
            Text(
                text = day.toString(),
                color = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
            )
        }
    }
}

@Composable
fun DayName(day: String) {
    Box(
        modifier = Modifier.size(48.dp)
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day,
            color = MaterialTheme.colors.primary
        )
    }
}

fun monthName(month: Int): String {
    return when (month) {
        GregorianCalendar.JANUARY -> "Январь"
        GregorianCalendar.FEBRUARY -> "Февраль"
        GregorianCalendar.MARCH -> "Март"
        GregorianCalendar.APRIL -> "Апель"
        GregorianCalendar.MAY -> "Май"
        GregorianCalendar.JUNE -> "Июнь"
        GregorianCalendar.JULY -> "Июль"
        GregorianCalendar.AUGUST -> "Август"
        GregorianCalendar.SEPTEMBER -> "Сентябрь"
        GregorianCalendar.OCTOBER -> "Октябрь"
        GregorianCalendar.NOVEMBER -> "Ноябрь"
        GregorianCalendar.DECEMBER -> "Декабрь"
        else -> "Undefined"
    }
}

fun GregorianCalendar.daysCount(): Int {
    return when (get(Calendar.MONTH)) {
        Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> 31
        Calendar.FEBRUARY -> if (isLeapYear(get(Calendar.YEAR))) 29 else 28
        Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> 30
        else -> 0
    }
}

private val TextPadding = Modifier.padding(0.dp)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EmptyBaseDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    dialogProvider: AlertDialogProvider = PopupAlertDialogProvider,
    content: @Composable () -> Unit,
) {
    with(dialogProvider) {
        AlertDialog(onDismissRequest = onDismissRequest) {
            AlertDialogContent2(
                modifier = modifier.width(IntrinsicSize.Min),
                content = content,
                shape = shape,
                backgroundColor = backgroundColor,
                contentColor = contentColor
            )
        }
    }
}

@Composable
internal fun AlertDialogContent2(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = backgroundColor,
        contentColor = contentColor
    ) {
        Column {
            AlertDialogBaselineLayout2(
                content = content
            )
        }
    }
}

@Composable
internal fun ColumnScope.AlertDialogBaselineLayout2(
    content: @Composable (() -> Unit),
) {
    Layout(
        {
            Box(TextPadding.layoutId("content").align(Alignment.Start)) {
                content()
            }
        },
        Modifier.weight(1f, false)
    ) { measurables, constraints ->

        val textPlaceable = measurables.firstOrNull { it.layoutId == "content" }?.measure(
            constraints.copy(minHeight = 0)
        )

        val layoutWidth = textPlaceable?.width ?: 0

        val layoutHeight = textPlaceable?.let {
            textPlaceable.height
        } ?: 0

        layout(layoutWidth, layoutHeight) {
            textPlaceable?.place(0, 0)
        }
    }
}