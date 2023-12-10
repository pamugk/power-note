package environment

import java.nio.file.FileSystems
import java.nio.file.Files
import kotlin.io.path.Path

private const val APP_ID = "com.github.pamugk.PowerNote"
private const val APP_NAME = "PowerNote"

private fun getDefaultDataPath(): String {
    val s = FileSystems.getDefault().separator
    return when (System.getProperty("os.name")) {
        "Linux" -> "${System.getProperty("user.home")}$s.local${s}share$s${APP_NAME.lowercase()}$s"
        "Mac", "Mac OS X" -> "${System.getProperty("user.home")}${s}Library$s$APP_NAME$s"
        else -> "${System.getProperty("user.home")}$s.${APP_NAME.lowercase()}$s"
    }
}

fun getDataPath(): String {
    val s = FileSystems.getDefault().separator
    val path = System.getenv("XDG_DATA_HOME")?.let { dataPath ->
        "$dataPath$s$APP_ID$s"
    } ?: getDefaultDataPath()
    Files.createDirectories(Path(path))
    return path
}