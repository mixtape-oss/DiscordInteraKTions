package net.perfectdreams.discordinteraktions.platforms.kord.commands

import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.interaction.*
import dev.kord.rest.service.RestClient
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.commands.CommandRegistry
import net.perfectdreams.discordinteraktions.common.commands.InteraKTions
import net.perfectdreams.discordinteraktions.declarations.commands.*
import net.perfectdreams.discordinteraktions.declarations.commands.slash.options.CommandOption
import net.perfectdreams.discordinteraktions.declarations.commands.slash.options.CommandOptionType

internal val kordCommandRegistryLogger = KotlinLogging.logger { }

public class KordCommandRegistry(
    private val applicationId: Snowflake,
    private val rest: RestClient,
    private val manager: InteraKTions
) : CommandRegistry {
    override suspend fun updateAllCommandsInGuild(guildId: Snowflake, deleteUnknownCommands: Boolean) {
        if (deleteUnknownCommands) {
            // Check commands that are already registered and remove the ones that aren't present in our command manager
            val commandsRegistered = rest.interaction.getGuildApplicationCommands(applicationId, guildId)
            val commandsKnown = manager.commandDeclarations.map { it.name }
            val commandsUnknown = commandsRegistered.filter { it.name !in commandsKnown }
            kordCommandRegistryLogger.debug { "Removing ${commandsUnknown.size} unknown commands for guild ${guildId.asString}" }

            for (it in commandsUnknown) {
                rest.interaction.deleteGuildApplicationCommand(applicationId, guildId, it.id)
            }
        }

        rest.interaction.createGuildApplicationCommands(
            applicationId,
            guildId,
            manager.commandDeclarations.map { convertCommandDeclarationToKord(it).toRequest() }
        )
    }

    override suspend fun updateAllGlobalCommands(deleteUnknownCommands: Boolean) {
        val kordApplicationId = applicationId

        if (deleteUnknownCommands) {
            // Check commands that are already registered and remove the ones that aren't present in our command manager
            val commandsRegistered = rest.interaction.getGlobalApplicationCommands(kordApplicationId)
            val commandsKnown = manager.commandDeclarations.map { it.name }
            val commandsUnknown = commandsRegistered.filterNot { it.name in commandsKnown }
            kordCommandRegistryLogger.debug { "Removing ${commandsUnknown.size} unknown global commands." }

            for (it in commandsUnknown) {
                rest.interaction.deleteGlobalApplicationCommand(kordApplicationId, it.id)
            }
        }

        rest.interaction.createGlobalApplicationCommands(
            kordApplicationId,
            manager.commandDeclarations.map { convertCommandDeclarationToKord(it).toRequest() }
        )
    }

    private fun convertCommandDeclarationToKord(declaration: InteractionCommandDeclaration): ApplicationCommandCreateBuilder {
        when (declaration) {
            is UserCommandDeclaration -> {
                return UserCommandCreateBuilder(declaration.name)
            }

            is MessageCommandDeclaration -> {
                return MessageCommandCreateBuilder(declaration.name)
            }

            is SlashCommandDeclaration -> {
                val commandData = ChatInputCreateBuilder(declaration.name, declaration.description)
                commandData.options = mutableListOf() // Initialize an empty list so we can use it

                // We can only have (subcommands OR subcommand groups) OR arguments
                if (declaration.subcommands.isNotEmpty() || declaration.subcommandGroups.isNotEmpty()) {
                    declaration.subcommands.forEach { commandData.options?.add(convertSubcommandDeclarationToKord(it)) }
                    declaration.subcommandGroups.forEach {
                        commandData.options?.add(
                            convertSubcommandGroupDeclarationToKord(it)
                        )
                    }
                } else {
                    val executor = declaration.executor ?: error("Root command without an executor!")
                    val options = executor.options
                    options.arguments.forEach { convertCommandOptionToKord(it, commandData) }
                }

                return commandData
            }

            is SlashCommandGroupDeclaration -> {
                val commandData = ChatInputCreateBuilder(declaration.name, declaration.description)
                commandData.options = mutableListOf() // Initialize an empty list so we can use it

                declaration.subcommands.forEach { commandData.options?.add(convertSubcommandDeclarationToKord(it)) }

                return commandData
            }
        }
    }

    private fun convertSubcommandDeclarationToKord(declaration: SlashCommandDeclaration): SubCommandBuilder {
        val commandData = SubCommandBuilder(declaration.name, declaration.description)

        // This is a subcommand, so we only have an executor anyway
        val executor = declaration.executor ?: error("Subcommand without an executor!")
        val options = executor.options

        options.arguments.forEach {
            convertCommandOptionToKord(it, commandData)
        }

        return commandData
    }

    private fun convertSubcommandGroupDeclarationToKord(declaration: SlashCommandGroupDeclaration): GroupCommandBuilder {
        val commandData = GroupCommandBuilder(declaration.name, declaration.description)
        commandData.options = mutableListOf() // Initialize an empty list so we can use it

        declaration.subcommands.forEach {
            commandData.options?.add(convertSubcommandDeclarationToKord(it))
        }

        return commandData
    }

    private fun convertCommandOptionToKord(cmdOption: CommandOption<*>, builder: BaseInputChatBuilder) {
        when (cmdOption.type) {
            // TODO: Add all possible types
            CommandOptionType.Integer, CommandOptionType.NullableInteger ->
                builder.int(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable

                    for (choice in cmdOption.choices) {
                        choice(choice.name, choice.value as Long)
                    }
                }
            CommandOptionType.Number, CommandOptionType.NullableNumber ->
                builder.number(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable

                    for (choice in cmdOption.choices) {
                        choice(choice.name, choice.value as Double)
                    }
                }
            CommandOptionType.String, CommandOptionType.NullableString ->
                builder.string(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable

                    for (choice in cmdOption.choices) {
                        choice(choice.name, choice.value as String)
                    }
                }
            CommandOptionType.Bool, CommandOptionType.NullableBool ->
                builder.boolean(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable
                }
            CommandOptionType.User, CommandOptionType.NullableUser ->
                builder.user(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable
                }
            CommandOptionType.Channel, CommandOptionType.NullableChannel ->
                builder.channel(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable
                }
            CommandOptionType.Role, CommandOptionType.NullableRole ->
                builder.role(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable
                }
            CommandOptionType.Mentionable, CommandOptionType.NullableMentionable ->
                builder.mentionable(cmdOption.name, cmdOption.description) {
                    this.required = !cmdOption.type.isNullable
                }
            else -> error("Unsupported type ${cmdOption.type}")
        }
    }
}
