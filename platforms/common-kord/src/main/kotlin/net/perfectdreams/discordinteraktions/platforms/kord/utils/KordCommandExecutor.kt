package net.perfectdreams.discordinteraktions.platforms.kord.utils

import dev.kord.common.entity.ApplicationCommandType
import dev.kord.common.entity.DiscordInteraction
import kotlinx.coroutines.launch
import net.perfectdreams.discordinteraktions.common.commands.InteraKTions
import net.perfectdreams.discordinteraktions.common.commands.message.MessageCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.slash.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.user.UserCommandExecutor
import net.perfectdreams.discordinteraktions.common.context.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.context.commands.GuildApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.context.commands.slash.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.context.manager.RequestManager
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.declarations.commands.MessageCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.UserCommandDeclaration
import net.perfectdreams.discordinteraktions.platforms.kord.commands.CommandDeclarationUtils
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordInteractionMember
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser

/**
 * Checks, matches and executes commands, this is a class because we share code between the `gateway-kord` and `webserver-ktor-kord` modules
 */
public class KordCommandExecutor(public val interaKTions: InteraKTions) {
    public fun checkAndExecute(interaction: DiscordInteraction, requests: RequestManager) {
        // Processing subcommands is kinda hard, but not impossible!
        val commandLabels = CommandDeclarationUtils.findAllSubcommandDeclarationNames(interaction)
        val relativeOptions = CommandDeclarationUtils.getNestedOptions(interaction.data.options.value)

        val kordUser = KordUser(interaction.member.value?.user?.value
            ?: interaction.user.value
            ?: error("oh no"))

        val data = InteractionData(interaction.data.resolved.value?.toDiscordInteraKTionsResolvedObjects())

        // If the guild ID is not null, then it means that the interaction happened in a guild!
        val commandContext = if (interaction.guildId.value != null) {
            val member = interaction.member.value?.let {
                KordInteractionMember(it, KordUser(it.user.value!!))
            }

            GuildApplicationCommandContext(requests.bridge, kordUser, interaction.channelId, data, interaction.guildId.value!!, member!!, interaction)
        } else {
            ApplicationCommandContext(requests.bridge, kordUser, interaction.channelId, data, interaction)
        }

        when (val applicationCommandType = interaction.data.type.value) {
            null ->
                error("Application Command Type is null, so we don't know what it is going to be used for!")

            is ApplicationCommandType.Unknown -> {
                error("Received unknown command type! ID: ${applicationCommandType.value}")
            }

            ApplicationCommandType.ChatInput -> {
                val command = interaKTions.commandDeclarations
                    .asSequence()
                    .filterIsInstance<SlashCommandDeclaration>() // We only care about Slash Command Declarations here
                    .firstNotNullOfOrNull { CommandDeclarationUtils.getLabelsConnectedToCommandDeclaration(commandLabels, it) }
                    ?: return

                val executorDeclaration = command.executor ?: return
                val executor = interaKTions.commandExecutors.first { it.signature() == executorDeclaration.parent } as SlashCommandExecutor

                interaKTions.launch {
                    if (executor.conditions.isNotEmpty()) {
                        val execute = executor.conditions.all { it.execute(commandContext) }
                        if (!execute) {
                            /* a condition has failed, return */
                            return@launch
                        }
                    }

                    /* Convert the Nested Options into a map, then we can access them with our Discord InteraKTion options! */
                    val arguments = CommandDeclarationUtils.convertOptions(
                        interaction,
                        executorDeclaration,
                        relativeOptions ?: listOf()
                    )

                    executor.execute(commandContext, SlashCommandArguments(arguments))
                }
            }

            ApplicationCommandType.User -> {
                val command = interaKTions.commandDeclarations
                    .asSequence()
                    .filterIsInstance<UserCommandDeclaration>()
                    .mapNotNull { CommandDeclarationUtils.getLabelsConnectedToCommandDeclaration(commandLabels, it) }
                    .filterIsInstance<UserCommandDeclaration>()
                    .firstOrNull() ?: return

                val executorDeclaration = command.executor
                val executor =
                    interaKTions.commandExecutors.first { it.signature() == executorDeclaration.parent } as UserCommandExecutor

                val targetUserId = interaction.data.targetId.value
                    ?: error("Target User ID is null in a User Command! Bug?")

                val targetUser = data.resolved?.users?.get(targetUserId)
                    ?: error("Target User is null in a User Command! Bug?")

                val targetMember = data.resolved?.members?.get(targetUserId)
                    ?: error("Target Member is null in a User Command! Bug?")

                interaKTions.launch {
                    if (executor.conditions.isNotEmpty()) {
                        val execute = executor.conditions.all { it.execute(commandContext, targetUser, targetMember) }
                        if (!execute) {
                            /* a condition has failed, return */
                            return@launch
                        }
                    }

                    executor.execute(commandContext, targetUser, targetMember)
                }
            }

            ApplicationCommandType.Message -> {
                val command = interaKTions.commandDeclarations
                    .asSequence()
                    .filterIsInstance<MessageCommandDeclaration>()
                    .mapNotNull { CommandDeclarationUtils.getLabelsConnectedToCommandDeclaration(commandLabels, it) }
                    .filterIsInstance<MessageCommandDeclaration>()
                    .firstOrNull() ?: return

                val executorDeclaration = command.executor
                val executor =
                    interaKTions.commandExecutors.first { it.signature() == executorDeclaration.parent } as MessageCommandExecutor

                val targetMessageId = interaction.data.targetId.value
                    ?: error("Target Message ID is null in a Message Command! Bug?")

                val targetMessage = data.resolved?.messages?.get(targetMessageId)
                    ?: error("Target Message is null in a Message Command! Bug?")

                interaKTions.launch {
                    if (executor.conditions.isNotEmpty()) {
                        val execute = executor.conditions.all { it.execute(commandContext, targetMessage) }
                        if (!execute) {
                            /* a condition has failed, return */
                            return@launch
                        }
                    }

                    executor.execute(commandContext, targetMessage)
                }
            }
        }
    }
}
