package entity

/**
 * Data class for the single typ of game elements that the game "Pyramid" knows: cards.
 *
 * It is characterized by a [CardSuit] and a [CardValue]
 */
data class Card(val suit: CardSuit, val value: CardValue) {
    override fun toString() : String {
        return "$suit$value"
    }

    // Function to get the integer value of the card
    fun getIntValue(aceValue: Int = 1): Int {
        return when (value) {
            CardValue.TWO -> 2
            CardValue.THREE -> 3
            CardValue.FOUR -> 4
            CardValue.FIVE -> 5
            CardValue.SIX -> 6
            CardValue.SEVEN -> 7
            CardValue.EIGHT -> 8
            CardValue.NINE -> 9
            CardValue.TEN -> 10
            CardValue.JACK -> 11
            CardValue.QUEEN -> 12
            CardValue.KING -> 13
            CardValue.ACE -> aceValue // By default, it will be 1 unless specified
        }
    }

}