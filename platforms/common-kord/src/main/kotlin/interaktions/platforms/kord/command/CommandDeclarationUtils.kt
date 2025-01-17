package interaktions.platforms.kord.command

import dev.kord.common.entity.*
import interaktions.declaration.command.InteractionCommandDeclaration
import interaktions.declaration.command.SlashCommandDeclaration
import interaktions.declaration.command.slash.SlashCommandExecutorDeclaration
import interaktions.declaration.command.slash.options.CommandOption
import interaktions.platforms.kord.command.CommandDeclarationUtils.findAllSubcommandDeclarationNames

public object CommandDeclarationUtils {
    /**
     * Finds all command declaration names in the [request]
     *
     * If a command only has one single command (example: `/loritta`) then the list will only have a single [CommandLabel]
     *
     * If a command has multiple subcommands and subgroups (example: `/loritta morenitta/cute`, where `morenitta/cute` is two subcommands) then the list
     * will have a [CommandLabel] and a [SubCommandLabel]
     *
     * @see getLabelsConnectedToCommandDeclaration
     *
     * @param request the command interaction
     * @return a list with all the labels
     */
    public fun findAllSubcommandDeclarationNames(request: DiscordInteraction): List<interaktions.platforms.kord.command.CommandDeclarationUtils.CommandLabel> {
        val commandLabels = mutableListOf<interaktions.platforms.kord.command.CommandDeclarationUtils.CommandLabel>(
            interaktions.platforms.kord.command.CommandDeclarationUtils.RootCommandLabel(request.data.name.value!!)
        )
        return findAllSubcommandDeclarationNames(commandLabels, request.data.options.value)
    }

    private fun findAllSubcommandDeclarationNames(
        commandLabels: MutableList<interaktions.platforms.kord.command.CommandDeclarationUtils.CommandLabel>,
        options: List<Option>?
    ): List<interaktions.platforms.kord.command.CommandDeclarationUtils.CommandLabel> {
        when (val firstOption = options?.firstOrNull()) {
            is SubCommand -> {
                commandLabels.add(
                    interaktions.platforms.kord.command.CommandDeclarationUtils.SubCommandLabel(
                        firstOption.name
                    )
                )

                findAllSubcommandDeclarationNames(commandLabels, firstOption.options.value)
            }

            is CommandGroup -> {
                commandLabels.add(
                    interaktions.platforms.kord.command.CommandDeclarationUtils.CommandGroupLabel(
                        firstOption.name
                    )
                )

                findAllSubcommandDeclarationNames(commandLabels, firstOption.options.value)
            }

            else -> return commandLabels
        }

        return commandLabels
    }

    /**
     * Gets the nested options in the [options]
     *
     * If the options are nested in `subcommand group` -> `subcommand` -> `command values`, this will return only the `command values`
     *
     * @param options the interaction options
     * @return the nested options
     */
    public fun getNestedOptions(options: List<Option>?): List<Option>? {
        val firstOption = options?.firstOrNull()

        if (firstOption is SubCommand) {
            return interaktions.platforms.kord.command.CommandDeclarationUtils.getNestedOptions(firstOption.options.value)
        } else if (firstOption is CommandGroup) {
            return interaktions.platforms.kord.command.CommandDeclarationUtils.getNestedOptions(firstOption.options.value)
        }

        return options
    }

    /**
     * Checks if the [labels] are connected from the [rootDeclaration] to the [declaration], by checking the [rootDeclaration] and its children until
     * the [declaration] is found.
     *
     * @see findAllSubcommandDeclarationNames
     *
     * @param labels          the request labels in order
     * @param declaration     the declaration that must be found
     * @return the matched declaration
     */
    public fun getLabelsConnectedToCommandDeclaration(
        labels: List<interaktions.platforms.kord.command.CommandDeclarationUtils.CommandLabel>,
        declaration: InteractionCommandDeclaration
    ): InteractionCommandDeclaration? {
        /* Let's not overcomplicate this, we already know that Discord only supports one level deep of nesting
           (so group -> subcommand)
           So let's do easy and quick checks */
        if (labels.first() is interaktions.platforms.kord.command.CommandDeclarationUtils.RootCommandLabel && labels.first().label == declaration.name) {
            /* Matches the root label! Yay! */
            if (labels.size == 1) {
                /* If there is only a Root Label, then it means we found our root declaration! */
                return declaration
            }
        }

        return null
    }

    /**
     * Checks if the [labels] are connected from the [rootDeclaration] to the [declaration], by checking the [rootDeclaration] and its children until
     * the [declaration] is found.
     *
     * @see findAllSubcommandDeclarationNames
     *
     * @param labels          the request labels in order
     * @param declaration     the declaration that must be found
     * @return the matched declaration
     */
    public fun getLabelsConnectedToCommandDeclaration(
        labels: List<interaktions.platforms.kord.command.CommandDeclarationUtils.CommandLabel>,
        declaration: SlashCommandDeclaration
    ): SlashCommandDeclaration? {
        /* Let's not overcomplicate this, we already know that Discord only supports one level deep of nesting
           (so group -> subcommand)
           So let's do easy and quick checks */
        if (labels.first() is interaktions.platforms.kord.command.CommandDeclarationUtils.RootCommandLabel && labels.first().label == declaration.name) {
            /* Matches the root label! Yay! */
            if (labels.size == 1) {
                /* If there is only a Root Label, then it means we found our root declaration! */
                return declaration
            } else {
                val secondLabel = labels[1]

                /* If not, let's check subcommand groups and subcommands */
                /* Thankfully we know when a label is a subcommand or a group! */
                if (secondLabel is interaktions.platforms.kord.command.CommandDeclarationUtils.SubCommandLabel) {
                    for (subcommand in declaration.subcommands) {
                        if (secondLabel.label == subcommand.name) {
                            /* Matches, then return this! */
                            return subcommand
                        }
                    }

                    /* Nothing found, return... */
                    return null
                } else {
                    val thirdLabel = labels[2]
                    for (group in declaration.subcommandGroups) {
                        if (group.name == secondLabel.label) {
                            for (subcommand in group.subcommands) {
                                if (thirdLabel.label == subcommand.name) {
                                    /* Matches, then return this! */
                                    return subcommand
                                }
                            }
                        }
                    }

                    return null
                }
            }
        }
        return null
    }

    public open class CommandLabel(public val label: String)
    public class RootCommandLabel(label: String) : interaktions.platforms.kord.command.CommandDeclarationUtils.CommandLabel(label)
    public class SubCommandLabel(label: String) : interaktions.platforms.kord.command.CommandDeclarationUtils.CommandLabel(label)
    public class CommandGroupLabel(label: String) : interaktions.platforms.kord.command.CommandDeclarationUtils.CommandLabel(label)

    public fun convertOptions(
        request: DiscordInteraction,
        executorDeclaration: SlashCommandExecutorDeclaration,
        relativeOptions: List<Option>
    ): Map<CommandOption<*>, Any?> {
        val arguments = mutableMapOf<CommandOption<*>, Any?>()

        for (option in relativeOptions) {
            val interaKTionOption = executorDeclaration.options.arguments.firstOrNull { it.name == option.name }
                ?: continue

            val argument = option as CommandArgument<*>
            arguments[interaKTionOption] = interaktions.platforms.kord.command.CommandDeclarationUtils.convertOption(
                interaKTionOption,
                argument,
                request
            )
        }

        return arguments
    }

    private fun convertOption(
        option: CommandOption<*>,
        argument: CommandArgument<*>,
        request: DiscordInteraction
    ): Any? {
        val converter = interaktions.platforms.kord.command.optionConverters[option.type]
            ?: return argument.value

        return converter.convert(option, argument, request)
    }
}
