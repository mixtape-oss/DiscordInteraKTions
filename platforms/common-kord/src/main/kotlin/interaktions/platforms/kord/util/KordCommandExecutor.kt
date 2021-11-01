package interaktions.platforms.kord.util

import dev.kord.common.entity.ApplicationCommandType
import dev.kord.common.entity.DiscordInteraction
import interaktions.common.command.InteraKTions
import interaktions.common.command.message.MessageCommandExecutor
import interaktions.common.command.slash.SlashCommandExecutor
import interaktions.common.command.user.UserCommandExecutor
import interaktions.common.context.commands.ApplicationCommandContext
import interaktions.common.context.commands.GuildApplicationCommandContext
import interaktions.common.context.commands.slash.SlashCommandArguments
import interaktions.common.context.manager.RequestManager
import interaktions.common.event.InteraKTionsExceptionEvent
import interaktions.common.event.MessageCommandFailedEvent
import interaktions.common.event.SlashCommandFailedEvent
import interaktions.common.event.UserCommandFailedEvent
import interaktions.common.interaction.InteractionData
import interaktions.declaration.command.MessageCommandDeclaration
import interaktions.declaration.command.SlashCommandDeclaration
import interaktions.declaration.command.UserCommandDeclaration
import interaktions.platforms.kord.entity.KordInteractionMember
import interaktions.platforms.kord.entity.KordUser
import kotlinx.coroutines.launch

/**
 * Checks, matches and executes commands, this is a class because we share code between the `gateway-kord` and `webserver-ktor-kord` modules
 */
public class KordCommandExecutor(public val interaKTions: InteraKTions) {
    public suspend fun handle(interaction: DiscordInteraction, requests: RequestManager) {
        try {
            // Processing subcommands is kinda hard, but not impossible!
            val commandLabels = interaktions.platforms.kord.command.CommandDeclarationUtils.findAllSubcommandDeclarationNames(interaction)
            val relativeOptions = interaktions.platforms.kord.command.CommandDeclarationUtils.getNestedOptions(interaction.data.options.value)

            val kordUser = KordUser(
                interaction.member.value?.user?.value
                    ?: interaction.user.value
                    ?: error("oh no, our kord member, it's null")
            )

            val data = InteractionData(interaction.data.resolved.value?.toDiscordInteraKTionsResolvedObjects())

            // If the guild ID is not null, then it means that the interaction happened in a guild!
            val commandContext = if (interaction.guildId.value != null) {
                val member = interaction.member.value?.let {
                    KordInteractionMember(it, KordUser(it.user.value!!))
                }

                GuildApplicationCommandContext(
                    requests.bridge,
                    kordUser,
                    interaction.channelId,
                    data,
                    interaction.guildId.value!!,
                    member!!,
                    interaction
                )
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
                        .filterIsInstance<SlashCommandDeclaration>() // We only care about Slash Command Declarations here
                        .firstNotNullOfOrNull { interaktions.platforms.kord.command.CommandDeclarationUtils.getLabelsConnectedToCommandDeclaration(commandLabels, it) }
                        ?: return

                    val executorDeclaration = command.executor ?: return
                    val executor = interaKTions.commandExecutors.first { it.signature() == executorDeclaration.parent } as SlashCommandExecutor

                    try {
                        interaKTions.launch {
                            if (executor.conditions.isNotEmpty()) {
                                val execute = executor.conditions.all { it.execute(commandContext) }
                                if (!execute) {
                                    /* a condition has failed, return */
                                    return@launch
                                }
                            }

                            /* Convert the Nested Options into a map, then we can access them with our Discord InteraKTion options! */
                            val arguments = interaktions.platforms.kord.command.CommandDeclarationUtils.convertOptions(
                                interaction,
                                executorDeclaration,
                                relativeOptions ?: listOf()
                            )

                            executor.execute(commandContext, SlashCommandArguments(arguments))
                        }
                    } catch (e: Throwable) {
                        val event = SlashCommandFailedEvent(command, executor, commandContext, e)
                        interaKTions.publishEvent(event)
                    }
                }

                ApplicationCommandType.User -> {
                    val command = interaKTions.commandDeclarations
                        .filterIsInstance<UserCommandDeclaration>()
                        .mapNotNull { interaktions.platforms.kord.command.CommandDeclarationUtils.getLabelsConnectedToCommandDeclaration(commandLabels, it) }
                        .filterIsInstance<UserCommandDeclaration>()
                        .firstOrNull() ?: return

                    val executorDeclaration = command.executor
                    val executor =
                        interaKTions.commandExecutors.first { it.signature() == executorDeclaration.parent } as UserCommandExecutor

                    try {
                        val targetUserId = interaction.data.targetId.value
                            ?: error("Target User ID is null in a User Command! Bug?")

                        val targetUser = data.resolved?.users?.get(targetUserId)
                            ?: error("Target User is null in a User Command! Bug?")

                        val targetMember = data.resolved?.members?.get(targetUserId)
                            ?: error("Target Member is null in a User Command! Bug?")

                        interaKTions.launch {
                            if (executor.conditions.isNotEmpty()) {
                                val execute =
                                    executor.conditions.all { it.execute(commandContext, targetUser, targetMember) }
                                if (!execute) {
                                    /* a condition has failed, return */
                                    return@launch
                                }
                            }

                            executor.execute(commandContext, targetUser, targetMember)
                        }
                    } catch (e: Throwable) {
                        val event = UserCommandFailedEvent(command, executor, commandContext, e)
                        interaKTions.publishEvent(event)
                    }
                }

                ApplicationCommandType.Message -> {
                    val command = interaKTions.commandDeclarations
                        .filterIsInstance<MessageCommandDeclaration>()
                        .mapNotNull { interaktions.platforms.kord.command.CommandDeclarationUtils.getLabelsConnectedToCommandDeclaration(commandLabels, it) }
                        .filterIsInstance<MessageCommandDeclaration>()
                        .firstOrNull() ?: return

                    val executorDeclaration = command.executor
                    val executor =
                        interaKTions.commandExecutors.first { it.signature() == executorDeclaration.parent } as MessageCommandExecutor

                    try {
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
                    } catch (e: Throwable) {
                        val event = MessageCommandFailedEvent(command, executor, commandContext, e)
                        interaKTions.publishEvent(event)
                    }
                }
            }
        } catch (e: Throwable) {
            interaKTions.publishEvent(InteraKTionsExceptionEvent(e))
        }
    }
}
