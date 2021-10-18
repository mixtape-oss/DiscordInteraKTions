package net.perfectdreams.discordinteraktions.common.entities.messages

import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralMessageModifyBuilder

public interface EditableEphemeralMessage : Message {
    public suspend fun editMessage(message: EphemeralMessageModifyBuilder): EditableEphemeralMessage

    public suspend fun editMessage(block: EphemeralMessageModifyBuilder.() -> (Unit)): EditableEphemeralMessage
}
