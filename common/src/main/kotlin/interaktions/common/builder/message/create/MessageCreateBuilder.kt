package interaktions.common.builder.message.create

import interaktions.common.builder.message.MessageBuilder

/**
 * The base builder for creating a new message.
 */
// From Kord
public sealed interface MessageCreateBuilder : MessageBuilder {
    /**
     * Whether this message should be played as a text-to-speech message.
     */
    public var tts: Boolean?
}
