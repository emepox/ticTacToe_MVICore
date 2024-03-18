package com.sps.tictactoe.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TitleText(
    myTitle: String,
) {
    Text(
        modifier = Modifier.padding(20.dp),
        text = myTitle,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.primary,
        fontSize = 30.sp
    )
}