package contacts.phonebook.contacts

import contacts.phonebook.contacts.exceptions.NoSuchPropertyException
import contacts.toLocalDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Person(
    var name: String,
    var surname: String,
    private var _number: String,
    private var _birthDate: LocalDate? = null,
    private var _gender: String? = null
) : Contact(_number) {
    var birthDate: String
        get() = _birthDate?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "[no data]"
        set(value) {
            _birthDate = value.toLocalDate()
        }
    var gender: String
        get() = _gender ?: "[no data]"
        set(value) {
            _gender = if (isSupportedGender(value)) value else null
        }

    override fun getDisplayName() = "$name $surname"

    override fun listAvailableProperties(): Set<String> =
        setOf("name", "surname", "number", "birthDate", "gender")

    @Throws(NoSuchPropertyException::class)
    override fun getProperty(propertyName: String) = when (propertyName) {
        "name" -> ::name
        "surname" -> ::surname
        "number" -> ::number
        "birthDate" -> ::birthDate
        "gender" -> ::gender
        else -> throw NoSuchPropertyException(propertyName)
    }

    override fun getDescription(): String {
        return """
            Name: $name
            Surname: $surname
            Birth date: $birthDate
            Gender: $gender
            Number: $number
            Time created: ${timeCreated.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}
            Time last edit: ${timeUpdated.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}
            """.trimIndent()
    }

    companion object {
        val supportedGenders = setOf("M", "F")

        fun isSupportedGender(genderAbbreviation: String?): Boolean = try {
            if (genderAbbreviation == null || genderAbbreviation.length > 1) false
            else supportedGenders.contains(genderAbbreviation.uppercase())
        } catch (e: NoSuchElementException) {
            false
        }
    }
}