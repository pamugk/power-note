import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "PowerNote",
        icon = painterResource("icon_main.svg"),
    ) {
        App()
    }
}
