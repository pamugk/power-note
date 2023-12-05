import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.repository.DbNoteRepository
import ui.App
import ui.SplashScreen
import java.awt.Dimension

fun main() = application {

    val repository = remember { DbNoteRepository() }
    var repositoryInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            repository.initialize()
            repositoryInitialized = true
        } catch (_: Exception) {}
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "PowerNote",
        icon = painterResource("icon_main.svg"),
    ) {
        this.window.minimumSize = Dimension(300, 500)
        if (repositoryInitialized) {
            App(repository)
        } else {
            SplashScreen(Modifier.fillMaxSize())
        }
    }
}
