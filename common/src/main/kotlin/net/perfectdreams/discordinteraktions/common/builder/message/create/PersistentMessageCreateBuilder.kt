package net.perfectdreams.discordinteraktions.common.builder.message.create

import dev.kord.rest.NamedFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path

/**
 * Message builder for a message that persists between client reloads.
 */
// From Kord
public interface PersistentMessageCreateBuilder : MessageCreateBuilder {

    /**
     * The files to include as attachments.
     */
    public val files: MutableList<NamedFile>

    /**
     * Adds a file with the [name] and [content] to the attachments.
     */
    public fun addFile(name: String, content: InputStream) {
        files += NamedFile(name, content)
    }

    /**
     * Adds a file with the given [path] to the attachments.
     */
    public suspend fun addFile(path: Path): Unit = withContext(Dispatchers.IO) {
        addFile(path.fileName.toString(), Files.newInputStream(path))
    }
}
