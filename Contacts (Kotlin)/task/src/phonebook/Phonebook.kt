package phonebook

import contacts.phonebook.contacts.Contact
import contacts.phonebook.contacts.Organisation
import contacts.phonebook.contacts.Person
import contacts.promptAndRead
import contacts.toLocalDate
import java.nio.file.Path

class Phonebook(filePath: Path? = null) {
    private val contactList: MutableList<Contact> = mutableListOf<Contact>()

    fun createContact() {
        print("Enter the type (${ContactType.values().joinToString(", ") { it.toString().lowercase() }}): ")
        val input = readln().trimIndent()
        try {
            val newContact = when (ContactType.valueOf(input.uppercase())) {
                ContactType.PERSON -> {
                    Person(
                        promptAndRead("Enter the name")
                            .let { it.ifBlank { return println("Name cannot be empty") } },
                        promptAndRead("Enter the surname")
                            .let { it.ifBlank { return println("Surname cannot be empty") } },
                        _birthDate = promptAndRead("Enter the birthdate").toLocalDate()
                            ?: null.also { println("Bad birth date!") },
                        _gender = promptAndRead(
                            "Enter the gender (${Person.supportedGenders.joinToString(", ")})"
                        ).let { if (Person.isSupportedGender(it)) it.uppercase() else null.also { println("Bad gender!") } },
                        _number = promptAndRead("Enter the number"),
                    )
                }

                ContactType.ORGANIZATION -> {
                    Organisation(
                        promptAndRead("Enter the organization name").let
                        { it.ifBlank { throw Exception("Company name cannot be empty") } },
                        promptAndRead("Enter the address")
                            .let { it.ifBlank { throw Exception("Address cannot be empty") } },
                        promptAndRead("Enter the number"),
                    )
                }
            }

            contactList.add(newContact)
            println("The record added.")
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    fun searchContacts() {
        val search = promptAndRead("Enter search query")
        val matches = contactList.filter { contact ->
            contact.listAvailableProperties().any { contact.getProperty(it).get().contains(search, true) }
        }
        println("Found ${matches.size} results")
        listRecords(matches)

        val action = promptAndRead("\n[search] Enter action ([number], back, again)")
        when (true) {
            (action.toIntOrNull() != null) -> getRecord(action.toInt())
            (action.lowercase() == "again") -> searchContacts()
            (action.lowercase() == "back") -> return
            else -> println("Unsupported action")
        }
    }

    fun countRecords() = println("The Phone Book has ${contactList.size} records.")

    fun listRecords() {
        listRecords(contactList)

        val action = promptAndRead("\n[list] Enter action ([number], back)")

        when (true) {
            (action.toIntOrNull() != null) -> getRecord(action.toInt())
            (action.lowercase() == "back") -> return
            else -> println("Unsupported action")
        }
    }

    private fun listRecords(contacts: List<Contact>) {
        contacts.forEachIndexed { index, contact ->
            println("${index + 1}. ${contact.getDisplayName()}")
        }
    }

    private fun getRecord(recordId: Int) {
        try {
            val record = contactList[validateRecordId(recordId)]
            println(record.getDescription())

            val action = promptAndRead("\n[record] Enter action (edit, delete, menu)").lowercase()
            when (action) {
                "edit" -> editContact(record)
                "delete" -> removeRecord(record)
                "menu" -> return
                else -> println("Unsupported action")
            }
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    private fun editContact(contact: Contact) {
        try {
            contact.listAvailableProperties()
            val property = promptAndRead("Select a field)")
            val newValue = promptAndRead("Enter $property")
            contact.editProperty(property, newValue, contact::getProperty)
            println("Saved")
        } catch (e: NoSuchElementException) {
            println(e.message)
        }
    }

    private fun removeRecord(record: Contact) = contactList.remove(record)

    private fun validateRecordId(id: Int): Int {
        return if (id - 1 in contactList.indices) id - 1 else throw Exception("Record does not exist.")
    }

    private enum class ContactType { PERSON, ORGANIZATION }
}