package net.perfectdreams.discordinteraktions.common.entities.messages

import net.perfectdreams.discordinteraktions.common.builder.message.modify.PersistentMessageModifyBuilder

public interface EditablePersistentMessage : Message {
    public suspend fun editMessage(block: PersistentMessageModifyBuilder.() -> (Unit)): EditablePersistentMessage

    public suspend fun editMessage(message: PersistentMessageModifyBuilder): EditablePersistentMessage
}
