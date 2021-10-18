package net.perfectdreams.discordinteraktions.common.builder.message.modify

/**
 * Message builder for a message that does not persists between client reloads.
 */
public interface EphemeralMessageModifyBuilder : MessageModifyBuilder {
    // We need to access the delegated stuff ourselves
    public var state: MessageModifyStateHolder
}
