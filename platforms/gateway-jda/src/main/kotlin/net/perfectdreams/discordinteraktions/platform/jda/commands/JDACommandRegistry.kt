package net.perfectdreams.discordinteraktions.platform.jda.commands

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData
import interaktions.api.entity.Snowflake
import interaktions.common.commands.CommandManager
import interaktions.common.commands.CommandRegistry
import interaktions.declaration.application.ApplicationCommandDeclarationBuilder
import interaktions.declaration.application.ApplicationCommandGroupDeclarationBuilder
import interaktions.declaration.slash.options.CommandOption
import interaktions.declaration.slash.options.CommandOptionType
import net.perfectdreams.discordinteraktions.platform.jda.utils.await

class JDACommandRegistry(private val jda: JDA, private val manager: CommandManager) : CommandRegistry {
    override suspend fun updateAllCommandsInGuild(guildId: Snowflake, deleteUnknownCommands: Boolean) = updateAllCommandsInGuild(jda.getGuildById(guildId.value) ?: error("There isn't a guild with the ID $guildId!"), deleteUnknownCommands)

    suspend fun updateAllCommandsInGuild(guild: Guild, deleteUnknownCommands: Boolean) {
        // TODO: Remove unknown commands
        val updateCommandRequest = guild.updateCommands()

        for (command in manager.declarations) {
            // Convert to JDA
            updateCommandRequest.addCommands(convertCommandDeclarationToJDA(command))
        }

        updateCommandRequest.await()
    }

    override suspend fun updateAllGlobalCommands(deleteUnknownCommands: Boolean) {
        // TODO: Remove unknown commands
        val updateCommandRequest = jda.updateCommands()

        for (command in manager.declarations) {
            // Convert to JDA
            updateCommandRequest.addCommands(convertCommandDeclarationToJDA(command))
        }

        updateCommandRequest.await()
    }

    private fun convertCommandDeclarationToJDA(declaration: ApplicationCommandDeclarationBuilder): CommandData {
        val commandData = CommandData(declaration.name, declaration.description)

        // We can only have (subcommands OR subcommand groups) OR arguments
        if (declaration.subcommands.isNotEmpty() || declaration.subcommandGroups.isNotEmpty()) {
            declaration.subcommands.forEach {
                commandData.addSubcommands(
                    convertSubcommandDeclarationToJDA(it)
                )
            }

            declaration.subcommandGroups.forEach {
                commandData.addSubcommandGroups(
                    convertSubcommandGroupDeclarationToJDA(it)
                )
            }
        } else {
            val executor = declaration.executor ?: error("Root command without a executor!")

            val options = executor.options
            options.arguments.forEach {
                commandData.addOptions(convertCommandOptionToJDA(it))
            }
        }

        return commandData
    }

    private fun convertSubcommandDeclarationToJDA(declaration: ApplicationCommandDeclarationBuilder): SubcommandData {
        val commandData = SubcommandData(declaration.name, declaration.description)

        // This is a subcommand, so we only have a executor anyway
        val executor = declaration.executor ?: error("Subcommand without a executor!")

        val options = executor.options
        options.arguments.forEach {
            commandData.addOptions(convertCommandOptionToJDA(it))
        }

        return commandData
    }

    private fun convertSubcommandGroupDeclarationToJDA(declaration: ApplicationCommandGroupDeclarationBuilder): SubcommandGroupData {
        val commandData = SubcommandGroupData(declaration.name, declaration.description)

        declaration.subcommands.forEach {
            commandData.addSubcommands(convertSubcommandDeclarationToJDA(it))
        }

        return commandData
    }

    private fun convertCommandOptionToJDA(cmdOption: CommandOption<*>): OptionData {
        val optionData = OptionData(
            when (cmdOption.type) {
                // TODO: Add all possible types
                CommandOptionType.Integer, CommandOptionType.NullableInteger -> OptionType.INTEGER
                CommandOptionType.String, CommandOptionType.NullableString -> OptionType.STRING
                CommandOptionType.Bool, CommandOptionType.NullableBool -> OptionType.BOOLEAN
                CommandOptionType.User, CommandOptionType.NullableUser -> OptionType.USER
                else -> error("Unsupported type ${cmdOption.type}")
            },
            cmdOption.name,
            cmdOption.description,
            !cmdOption.type.isNullable
        )

        // This will automatically register the choices in our optionData
        convertChoicesToJDA(cmdOption, optionData)
        return optionData
    }

    private fun convertChoicesToJDA(cmdOption: CommandOption<*>, optionData: OptionData) {
        // Register the options' choices
        cmdOption.choices.forEach {
            when (val optionValue = it.value) {
                is Int -> {
                    optionData.addChoice(it.name, optionValue)
                }
                is String -> {
                    optionData.addChoice(it.name, optionValue)
                }
                else -> error("I don't know how to handle a $optionValue choice!")
            }
        }
    }
}
