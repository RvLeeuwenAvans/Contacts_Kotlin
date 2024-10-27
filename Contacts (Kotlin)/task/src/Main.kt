package contacts

import phonebook.Phonebook
import java.nio.file.Path
import java.time.LocalDate
import java.time.format.DateTimeParseException
import kotlin.io.path.Path

fun main(vararg args: String) {
    var filePath: Path? = null
    if (args[0].isNotEmpty()) filePath = Path(args[0])


    val contactService = ContactService()
    contactService.run()
}

class ContactService(filePath: Path? = null) {
    private var running = false
    private var contactBook = Phonebook(filePath)

    fun run() {
        running = true

        while (running) {
            print("[menu] Enter action (${Action.values().joinToString(", ") { it.toString().lowercase() }}): ")
            val input = readln().trimIndent()

            try {
                when (Action.valueOf(input.uppercase())) {
                    Action.ADD -> contactBook.createContact()
                    Action.LIST -> contactBook.listRecords()
                    Action.SEARCH -> contactBook.searchContacts()
                    Action.COUNT -> contactBook.countRecords()
                    Action.EXIT -> running = false
                }
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }

            println()
        }
    }

    private enum class Action { ADD, LIST, SEARCH, COUNT, EXIT }
}

fun String.toLocalDate(): LocalDate? = try {
    LocalDate.parse(this)
} catch (e: DateTimeParseException) {
    null
}

fun promptAndRead(prompt: String): String {
    print("$prompt: ")
    return readln().trimIndent()
}
