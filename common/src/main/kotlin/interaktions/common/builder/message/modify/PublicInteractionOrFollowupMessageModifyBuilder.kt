package interaktions.common.builder.message.modify

import dev.kord.common.entity.DiscordAttachment
import dev.kord.common.entity.optional.delegate.delegate
import dev.kord.rest.NamedFile
import dev.kord.rest.builder.component.MessageComponentBuilder
import dev.kord.rest.builder.message.AllowedMentionsBuilder
import dev.kord.rest.builder.message.EmbedBuilder

// From Kord, however this is an interaction OR followup modify builder
public class PublicInteractionOrFollowupMessageModifyBuilder : PersistentMessageModifyBuilder {
    // We need to access the delegated stuff ourselves
    override var state: MessageModifyStateHolder = MessageModifyStateHolder()

    override var files: MutableList<NamedFile>? by state::files.delegate()

    override var attachments: MutableList<DiscordAttachment>? by state::attachments.delegate()

    override var content: String? by state::content.delegate()

    override var embeds: MutableList<EmbedBuilder>? by state::embeds.delegate()

    override var allowedMentions: AllowedMentionsBuilder? by state::allowedMentions.delegate()

    override var components: MutableList<MessageComponentBuilder>? by state::components.delegate()
}
