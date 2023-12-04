import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.App
import java.awt.Dimension

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "PowerNote",
        icon = painterResource("icon_main.svg"),
    ) {
        this.window.minimumSize = Dimension(300, 500)
        App()
    }
}
