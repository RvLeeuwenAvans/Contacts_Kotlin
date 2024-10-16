package contacts.phonebook.contacts

import contacts.phonebook.contacts.exceptions.NoSuchPropertyException

data class Organisation(
    var name: String,
    var address: String,
    private var _number: String,
) : Contact(_number) {
    override fun getDisplayName() = name

    override fun listAvailableProperties(): Set<String> = setOf("name", "address", "number")

    @Throws(NoSuchPropertyException::class)
    override fun getProperty(propertyName: String) = when (propertyName) {
        "name" -> ::name
        "address" -> ::address
        "number" -> ::number
        else -> throw NoSuchPropertyException(propertyName)
    }

    override fun getDescription(): String {
        return """
            Organization name: $name
            Address: $address
            Number: $number
            Time created: $timeCreated
            Time last edit: $timeUpdated
            """.trimIndent()
    }
}