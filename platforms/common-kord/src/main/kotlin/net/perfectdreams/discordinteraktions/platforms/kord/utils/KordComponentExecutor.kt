package net.perfectdreams.discordinteraktions.platforms.kord.utils

import dev.kord.common.entity.ComponentType
import dev.kord.common.entity.DiscordInteraction
import kotlinx.coroutines.launch
import net.perfectdreams.discordinteraktions.common.commands.InteraKTions
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickWithDataExecutor
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickWithNoDataExecutor
import net.perfectdreams.discordinteraktions.common.components.selects.SelectMenuWithDataExecutor
import net.perfectdreams.discordinteraktions.common.components.selects.SelectMenuWithNoDataExecutor
import net.perfectdreams.discordinteraktions.common.context.components.ComponentContext
import net.perfectdreams.discordinteraktions.common.context.components.GuildComponentContext
import net.perfectdreams.discordinteraktions.common.context.manager.RequestManager
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordInteractionMember
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordPublicMessage

/**
 * Checks, matches and executes commands, this is a class because we share code between the `gateway-kord` and `webserver-ktor-kord` modules
 */
public class KordComponentExecutor(public val interaKTions: InteraKTions) {
    public fun checkAndExecute(interaction: DiscordInteraction, requests: RequestManager) {
        val componentType = interaction.data.componentType.value
            ?: error("Component Type is not present in Discord's request! Bug?")

        // If the component doesn't have a custom ID, we won't process it
        val componentCustomId = interaction.data.customId.value
            ?: return

        val executorId = componentCustomId.substringBefore(":")
        val data = componentCustomId.substringAfter(":")

        val kordUser = KordUser(interaction.member.value?.user?.value ?: interaction.user.value ?: error("oh no"))
        val kordPublicMessage = KordPublicMessage(interaction.message.value!!)

        val interactionData = InteractionData(interaction.data.resolved.value?.toDiscordInteraKTionsResolvedObjects())

        // If the guild ID is not null, then it means that the interaction happened in a guild!
        val componentContext = if (interaction.guildId.value != null) {
            val member = interaction.member.value!! // Should NEVER be null!
            val kordMember = KordInteractionMember(
                member,
                KordUser(member.user.value!!) // Also should NEVER be null!
            )

            GuildComponentContext(
                requests.bridge,
                kordUser,
                interaction.channelId,
                kordPublicMessage,
                interactionData,
                interaction.guildId.value!!,
                kordMember,
                interaction
            )
        } else {
            ComponentContext(
                requests.bridge,
                kordUser,
                interaction.channelId,
                kordPublicMessage,
                interactionData,
                interaction
            )
        }

        // Now this changes a bit depending on what we are trying to execute
        when (componentType) {
            is ComponentType.Unknown ->
                error("Unknown Component Type!")

            ComponentType.ActionRow ->
                error("Received a ActionRow component interaction... but that's impossible!")

            ComponentType.Button -> {
                val buttonExecutorDeclaration = interaKTions.buttonDeclarations
                    .firstOrNull { it.id == executorId } ?: return

                val executor = interaKTions.buttonExecutors.first {
                    it.signature() == buttonExecutorDeclaration.parent
                }

                interaKTions.launch {
                    if (executor is ButtonClickWithNoDataExecutor)
                        executor.onClick(kordUser, componentContext)
                    else if (executor is ButtonClickWithDataExecutor)
                        executor.onClick(kordUser, componentContext, data)
                }
            }

            ComponentType.SelectMenu -> {
                val executorDeclaration = interaKTions.selectMenusDeclarations
                    .firstOrNull { it.id == executorId } ?: return

                val executor = interaKTions.selectMenusExecutors.first {
                    it.signature() == executorDeclaration.parent
                }

                interaKTions.launch {
                    if (executor is SelectMenuWithNoDataExecutor)
                        executor.onSelect(kordUser, componentContext, interaction.data.values.value ?: error("Values list is null!"))
                    else if (executor is SelectMenuWithDataExecutor)
                        executor.onSelect(kordUser, componentContext, data, interaction.data.values.value ?: error("Values list is null!"))
                }
            }
        }
    }
}
