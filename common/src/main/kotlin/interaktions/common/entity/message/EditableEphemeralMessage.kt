package interaktions.common.entity.message

import interaktions.common.builder.message.modify.EphemeralMessageModifyBuilder

public interface EditableEphemeralMessage : Message {
    public suspend fun editMessage(message: EphemeralMessageModifyBuilder): EditableEphemeralMessage

    public suspend fun editMessage(block: EphemeralMessageModifyBuilder.() -> (Unit)): EditableEphemeralMessage
}
