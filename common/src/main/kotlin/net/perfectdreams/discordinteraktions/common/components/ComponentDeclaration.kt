package net.perfectdreams.discordinteraktions.common.components

public open class ComponentDeclaration(
    /**
     * The "parent" is Any to avoid issues with anonymous classes
     *
     * When using anonymous classes, you can use another type to match declarations
     */
    public val parent: Any,

    /**
     * The executor's ID regex, this is used to figure out what executor a button should use.
     *
     * All button executors should be unique!
     */
    public val id: String
) {
    public companion object {
        public val ID_REGEX: Regex = Regex("[A-z0-9]+")
    }

    init {
        require(ID_REGEX.matches(id)) { "ID must respect the $ID_REGEX regular expression!" }
    }
}
