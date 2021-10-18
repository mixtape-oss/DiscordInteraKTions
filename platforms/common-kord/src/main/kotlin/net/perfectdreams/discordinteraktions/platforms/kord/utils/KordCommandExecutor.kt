package net.perfectdreams.discordinteraktions.platforms.kord.utils

import dev.kord.common.entity.ApplicationCommandType
import dev.kord.common.entity.DiscordInteraction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
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
class KordCommandExecutor @OptIn(DelicateCoroutinesApi::class) constructor(
    val commands: CommandManager,
    val scope: CoroutineScope = GlobalScope
) {
    fun checkAndExecute(request: DiscordInteraction, requestManager: RequestManager) {
        val bridge = requestManager.bridge

        // Processing subcommands is kinda hard, but not impossible!
        val commandLabels = CommandDeclarationUtils.findAllSubcommandDeclarationNames(request)
        val relativeOptions = CommandDeclarationUtils.getNestedOptions(request.data.options.value)

        val applicationCommandType = request.data.type.value
            ?: error("Application Command Type is null, so we don't know what it is going to be used for!")

        val kordUser = KordUser(request.member.value?.user?.value ?: request.user.value ?: error("oh no"))
        val guildId = request.guildId.value

        val interactionData = InteractionData(request.data.resolved.value?.toDiscordInteraKTionsResolvedObjects())

        // If the guild ID is not null, then it means that the interaction happened in a guild!
        val commandContext = if (guildId != null) {
            val member = request.member.value!! // Should NEVER be null!
            val kordMember = KordInteractionMember(
                member,
                KordUser(member.user.value!!) // Also should NEVER be null!
            )

            GuildApplicationCommandContext(bridge, kordUser, request.channelId, interactionData, guildId, kordMember)
        } else {
            ApplicationCommandContext(bridge, kordUser, request.channelId, interactionData)
        }

        when (applicationCommandType) {
            is ApplicationCommandType.Unknown -> {
                error("Received unknown command type! ID: ${applicationCommandType.value}")
            }

            ApplicationCommandType.ChatInput -> {
                val command = commands.declarations
                    .asSequence()
                    .filterIsInstance<SlashCommandDeclaration>() // We only care about Slash Command Declarations here
                    .mapNotNull { CommandDeclarationUtils.getLabelsConnectedToCommandDeclaration(commandLabels, it) }
                    .firstOrNull() ?: return

                val executorDeclaration = command.executor ?: return
                val executor =
                    commands.executors.first { it.signature() == executorDeclaration.parent } as SlashCommandExecutor

                scope.launch {
                    if (executor.conditions.isNotEmpty()) {
                        val execute = executor.conditions.all { it.execute(commandContext) }
                        if (!execute) {
                            /* a condition has failed, return */
                            return@launch
                        }
                    }

                    /* Convert the Nested Options into a map, then we can access them with our Discord InteraKTion options! */
                    val arguments = CommandDeclarationUtils.convertOptions(
                        request,
                        executorDeclaration,
                        relativeOptions ?: listOf()
                    )

                    executor.execute(commandContext, SlashCommandArguments(arguments))
                }
            }

            ApplicationCommandType.User -> {
                val command = commands.declarations
                    .asSequence()
                    .filterIsInstance<UserCommandDeclaration>()
                    .mapNotNull { CommandDeclarationUtils.getLabelsConnectedToCommandDeclaration(commandLabels, it) }
                    .filterIsInstance<UserCommandDeclaration>()
                    .firstOrNull() ?: return

                val executorDeclaration = command.executor
                val executor =
                    commands.executors.first { it.signature() == executorDeclaration.parent } as UserCommandExecutor

                val targetUserId = request.data.targetId.value
                    ?: error("Target User ID is null in a User Command! Bug?")

                val targetUser = interactionData.resolved?.users?.get(targetUserId)
                    ?: error("Target User is null in a User Command! Bug?")

                val targetMember = interactionData.resolved?.members?.get(targetUserId)
                    ?: error("Target Member is null in a User Command! Bug?")

                scope.launch {
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
                val command = commands.declarations
                    .asSequence()
                    .filterIsInstance<MessageCommandDeclaration>()
                    .mapNotNull {
                        CommandDeclarationUtils.getLabelsConnectedToCommandDeclaration(
                            commandLabels,
                            it
                        )
                    }
                    .filterIsInstance<MessageCommandDeclaration>()
                    .firstOrNull() ?: return

                val executorDeclaration = command.executor
                val executor =
                    commands.executors.first { it.signature() == executorDeclaration.parent } as MessageCommandExecutor

                val targetMessageId = request.data.targetId.value
                    ?: error("Target Message ID is null in a Message Command! Bug?")

                val targetMessage = interactionData.resolved?.messages?.get(targetMessageId)
                    ?: error("Target Message is null in a Message Command! Bug?")

                scope.launch {
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
