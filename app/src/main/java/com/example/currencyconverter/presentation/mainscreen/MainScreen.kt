package com.example.currencyconverter.presentation.mainscreen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.R
import com.example.currencyconverter.data.Currencies.currencyList
import com.example.currencyconverter.ui.theme.poppins
import com.himanshoe.charty.common.axis.AxisConfig
import com.himanshoe.charty.line.CurveLineChart
import com.himanshoe.charty.line.model.LineData

@Composable
fun MainScreen() {
    Column(verticalArrangement = Arrangement.Top) {
        TopApp()
        MainContent()
    }
}

@Composable
fun TopApp() {
    TopAppBar(
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .border(
                            1.dp,
                            Color.LightGray,
                            CircleShape
                        ),
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile pic"
                )
                Text(
                    text = "Charts",
                    fontFamily = poppins,
                    fontWeight = FontWeight.ExtraBold
                )
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification"
                )
            }
            Divider()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainContent(viewModel: MainViewModel = hiltViewModel()) {
    var textFileSize by remember {
        mutableStateOf(Size.Zero)
    }
    val bicon = if (viewModel.bcexpanded.value) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }
    val ticon = if (viewModel.tcexpanded.value) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        val color = if (isSystemInDarkTheme()) {
            Color.LightGray
        } else {
            Color.Gray
        }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .onGloballyPositioned {
                            textFileSize = it.size.toSize()
                        }
                        .width(120.dp),
                    value = viewModel.baseCurrency.value,
                    onValueChange = { viewModel.onBaseCurrencyChange(it) },
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.clickable {
                                viewModel.onbcxpandedChange()
                                viewModel.show.value = false
                            },
                            imageVector = bicon,
                            contentDescription = "More or Less"
                        )
                    }
                )
                DropdownMenu(modifier = Modifier
                    .width(with(LocalDensity.current) { textFileSize.width.toDp() }),
                    expanded = viewModel.bcexpanded.value,
                    onDismissRequest = { viewModel.bcexpanded.value = false }) {
                    currencyList.forEach { currency ->
                        DropdownMenuItem(onClick = {
                            viewModel.baseCurrency.value = currency
                            viewModel.bcexpanded.value = false
                        }) {
                            Text(
                                text = currency,
                                fontFamily = poppins,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
            Icon(
                modifier = Modifier
                    .size(45.dp)
                    .align(CenterVertically),
                painter = painterResource(id = R.drawable.arrows),
                contentDescription = "Arrows",
                tint = color
            )
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .onGloballyPositioned {
                            textFileSize = it.size.toSize()
                        }
                        .width(120.dp),
                    value = viewModel.targetCurrency.value,
                    onValueChange = { viewModel.onTargetCurrencyChange(it) },
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.clickable {
                                viewModel.ontcxpandedChange()
                                viewModel.show.value = false
                            },
                            imageVector = ticon,
                            contentDescription = "More or Less"
                        )
                    }
                )
                DropdownMenu(modifier = Modifier
                    .width(with(LocalDensity.current) { textFileSize.width.toDp() }),
                    expanded = viewModel.tcexpanded.value,
                    onDismissRequest = { viewModel.tcexpanded.value = false }) {
                    currencyList.forEach { currency ->
                        DropdownMenuItem(onClick = {
                            viewModel.targetCurrency.value = currency
                            viewModel.tcexpanded.value = false
                        }) {
                            Text(
                                text = currency,
                                fontFamily = poppins,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Surface(
                modifier = Modifier.clickable {
                    if (viewModel.baseCurrency.value != "" && viewModel.targetCurrency.value != "") {
                        viewModel.show.value = true
                    }
                },
                shape = CircleShape
            ) {
                Icon(
                    modifier = Modifier
                        .size(35.dp)
                        .padding(0.dp),
                    painter = painterResource(id = R.drawable.exchange),
                    contentDescription = "Show Exchange Rate",
                    tint = color
                )
            }
        }
        viewModel.getConversionRate(
            viewModel.baseCurrency.value,
            viewModel.targetCurrency.value
        )
        AnimatedVisibility(viewModel.show.value) {
            if (viewModel.fetched.value.data != null) {
                Column() {
                    Text(
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 8.dp),
                        text = "1 ${viewModel.baseCurrency.value}= ${viewModel.fetched.value.data?.conversion_rate}" +
                                " ${viewModel.targetCurrency.value}",
                        fontFamily = poppins,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp
                    )


                }
            } else {
                LaunchedEffect(key1 = true) {
                    Toast.makeText(context, "Could not reach the server", Toast.LENGTH_SHORT).show()
                    viewModel.show.value = false
                }
            }
        }
        Column(modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom) {
            CurveLineChart(
                lineData = lineData,
                chartColor = Color.Transparent,
                lineColor = color,
                modifier = Modifier
                    .size(350.dp)
                    .padding(29.dp),
                axisConfig = AxisConfig(
                    showAxis = true,
                    isAxisDashed = false,
                    showUnitLabels = true,
                    textColor =color,
                    showXLabels = true
                )
            )
        }
    }
}

val lineData = listOf(
    LineData(1, 0.25f),
    LineData(2, 0.5f),
    LineData(3, 0.75f),
    LineData(4, 0.25f),
    )