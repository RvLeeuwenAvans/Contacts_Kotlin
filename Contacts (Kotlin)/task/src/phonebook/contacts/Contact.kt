package contacts.phonebook.contacts

import contacts.phonebook.contacts.exceptions.NoSuchPropertyException
import java.time.LocalDateTime
import kotlin.reflect.KMutableProperty0

interface Contactable {
    fun getDisplayName(): String
    fun getDescription(): String
    fun getProperty(propertyName: String): KMutableProperty0<String>
    fun listAvailableProperties(): Set<String>
}

abstract class Contact(private var _number: String) : Contactable {
    var number: String
        get() = _number
        set(value) {
            _number = if (validateNumber(value)) value else "[no data]"
            timeUpdated = LocalDateTime.now()
        }

    protected val timeCreated: LocalDateTime = LocalDateTime.now()
    protected var timeUpdated: LocalDateTime = timeCreated

    abstract override fun listAvailableProperties(): Set<String>

    @Throws(NoSuchPropertyException::class)
    fun editProperty(propertyName: String, newValue: String, getProperty: (String) ->  KMutableProperty0<String>) {
        getProperty(propertyName).set(newValue)
        timeUpdated = LocalDateTime.now()
    }

    private fun validateNumber(phoneNumber: String): Boolean = Regex(
        "^\\+?(\\w+(?:[ |-]\\w{2,})*|\\(\\w+\\)" +
                "(?:[ |-]\\w{2,})*|\\w+[ |-]\\(\\w{2,}\\)" +
                "(?:[ |-]\\w{2,})*)$"
    ).matches(phoneNumber)
}