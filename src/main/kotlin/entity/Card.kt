package entity

data class Card(val suit: CardSuit, val value: CardValue) {
    fun cardString() : String {
        return "$value$suit"
    }
}