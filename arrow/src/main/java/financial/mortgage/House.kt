package financial.mortgage

data class House(val address: Address) {
}


data class Address(val streetName: String, val number: Number, val postalCode: String) {

}