import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.omlet.arcade.App

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Omlet Arcade") {
        App()
    }
}
