package interaktions.common.context.manager

import interaktions.common.builder.message.create.EphemeralInteractionOrFollowupMessageCreateBuilder
import interaktions.common.builder.message.create.PublicInteractionOrFollowupMessageCreateBuilder
import interaktions.common.builder.message.modify.EphemeralInteractionMessageModifyBuilder
import interaktions.common.builder.message.modify.PublicInteractionMessageModifyBuilder
import interaktions.common.context.RequestBridge
import interaktions.common.entity.message.EditableEphemeralMessage
import interaktions.common.entity.message.EditablePersistentMessage

public abstract class RequestManager(public val bridge: RequestBridge) {
    /**
     * A deferred response is the one that you can use to
     * be able to edit the original message for 15 minutes since it was sent.
     *
     * The user will just see a loading status for the interaction.
     */
    public abstract suspend fun deferChannelMessage()

    /**
     * A deferred response is the one that you can use to
     * be able to edit the original message for 15 minutes since it was sent.
     *
     * The user will just see a loading status for the interaction.
     */
    public abstract suspend fun deferChannelMessageEphemerally()

    /**
     * The usual way of sending messages to a specific channel/user.
     */
    public abstract suspend fun createPublicMessage(message: PublicInteractionOrFollowupMessageCreateBuilder): EditablePersistentMessage

    /**
     * The usual way of sending messages to a specific channel/user.
     */
    public abstract suspend fun createEphemeralMessage(message: EphemeralInteractionOrFollowupMessageCreateBuilder): EditableEphemeralMessage

    /**
     * A deferred response is the one that you can use to
     * be able to edit the original message for 15 minutes since it was sent.
     *
     * The user will not see a loading status for the interaction.
     */
    public abstract suspend fun deferUpdateMessage()

    /**
     * The usual way of editing a message to a specific channel/user.
     */
    public abstract suspend fun updateMessage(message: PublicInteractionMessageModifyBuilder): EditablePersistentMessage

    /**
     * The usual way of editing a message to a specific channel/user.
     */
    public abstract suspend fun updateEphemeralMessage(message: EphemeralInteractionMessageModifyBuilder): EditableEphemeralMessage
}
