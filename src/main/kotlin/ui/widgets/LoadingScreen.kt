package ui.widgets

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.AppTheme

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Surface {
        Column(modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(modifier = Modifier.width(64.dp))
            Spacer(Modifier.height(48.dp))
            Text("Пожалуйста, подождите…")
        }
    }}

@Composable
@Preview
private fun SplashScreenPreview() {
    AppTheme {
        LoadingScreen(Modifier.fillMaxSize())
    }
}