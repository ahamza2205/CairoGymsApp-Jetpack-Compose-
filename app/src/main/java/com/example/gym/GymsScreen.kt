package com.example.gym

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gym.retrofit.Gym

@Composable
fun GymsScreen() {
    val viewModel: GymsViewModel = viewModel()
    LazyColumn() {
        items(viewModel.state) { gym ->
            GymItem(gym) {
                viewModel.toggleFavorite(it)
            }
        }
    }
}

@Composable
fun GymItem(gym: Gym, onClick: (Int) -> Unit) {
    val favoriteIcon = if (gym.isFavorite) {
        Icons.Filled.Favorite
    } else {
        Icons.Filled.FavoriteBorder
    }

    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(Icons.Filled.Place, Modifier.weight(0.1f), "Place Icon")
            GymDetails(gym, Modifier.weight(0.70f))
            Icon(
                favoriteIcon,
                Modifier.weight(0.15f),
                "Favorite Icon"
            ) {
                onClick(gym.id)
            }
        }
    }
}


@Composable
fun Icon(
    favoriteIcon: ImageVector,
    modifier: Modifier,
    contentDescription: String,
    onClick: () -> Unit = {}
) {

    Image(
        imageVector = favoriteIcon,
        contentDescription = contentDescription,
        colorFilter = ColorFilter.tint(Color.Red),
        modifier = modifier
            .padding(8.dp)
            .clickable {
                onClick()
            }
    )
}

@Composable
fun GymDetails(gym: Gym, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = gym.name,
            style = TextStyle(
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        CompositionLocalProvider(LocalContentColor provides Color.Gray) {
            Text(
                text = gym.place,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GymsScreenPreview() {
    GymsScreen()
}
