package contacts.phonebook.contacts.exceptions

class NoSuchPropertyException(property: String): Exception("Property: $property not found")