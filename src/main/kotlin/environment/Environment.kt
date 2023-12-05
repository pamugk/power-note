package environment

import java.nio.file.Files
import kotlin.io.path.Path

private const val APP_ID = "com.github.pamugk.PowerNote"

fun getDataPath(): String {
    val s = System.getProperty("file.separator")
    val path = System.getProperty("XDG_DATA_HOME")?.let { dataPath ->
        "$dataPath$s$APP_ID$s"
    } ?: "${System.getProperty("user.home")}$s.powernote$s"
    Files.createDirectories(Path(path))
    return path
}