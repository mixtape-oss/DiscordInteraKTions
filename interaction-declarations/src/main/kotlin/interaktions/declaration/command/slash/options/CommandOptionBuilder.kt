package interaktions.declaration.command.slash.options

public open class CommandOptionBuilder<T>(
    // We need to store the command option type due to type erasure
    public val type: CommandOptionType,
    public val name: String,
    public val description: String,
    public val choices: MutableList<CommandChoice<T>>
) {
    public fun choice(value: T, name: String): CommandOptionBuilder<T> {
        choices.add(CommandChoice(type, name, value))
        return this
    }
}
