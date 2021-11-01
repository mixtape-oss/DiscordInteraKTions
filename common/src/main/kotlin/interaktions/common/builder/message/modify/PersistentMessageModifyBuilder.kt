package interaktions.common.builder.message.modify

import dev.kord.common.entity.DiscordAttachment
import dev.kord.rest.NamedFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path

/**
 * Message builder for a message that persists between client reloads.
 */
public interface PersistentMessageModifyBuilder : MessageModifyBuilder {
    // We need to access the delegated stuff ourselves
    public var state: MessageModifyStateHolder

    /**
     * The files to include as attachments
     */
    public var files: MutableList<NamedFile>?

    public var attachments: MutableList<DiscordAttachment>?

    public fun addFile(name: String, content: InputStream) {
        files = (files ?: mutableListOf()).also {
            it.add(NamedFile(name, content))
        }
    }

    public suspend fun addFile(path: Path): Unit = withContext(Dispatchers.IO) {
        addFile(path.fileName.toString(), Files.newInputStream(path))
    }
}
