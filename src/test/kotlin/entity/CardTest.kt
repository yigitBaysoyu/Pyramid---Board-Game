package entity

import kotlin.test.*

/**
 * Test cases for [Card]
 */
class CardTest {

    // Some cards to perform the tests with
    private val aceOfSpades = Card(CardSuit.SPADES, CardValue.ACE)
    private val jackOfClubs = Card(CardSuit.CLUBS, CardValue.JACK)
    private val queenOfHearts = Card(CardSuit.HEARTS, CardValue.QUEEN)
    private val otherQueenOfHearts = Card(CardSuit.HEARTS, CardValue.QUEEN)
    private val jackOfDiamonds = Card(CardSuit.DIAMONDS, CardValue.JACK)

    // unicode characters for the suits, as those should be used by [WarCard.toString]
    private val heartsChar = '\u2665' // ♥
    private val diamondsChar = '\u2666' // ♦
    private val spadesChar = '\u2660' // ♠
    private val clubsChar = '\u2663' // ♣

    /**
     * Check if to String produces the correct strings for some test cards
     * of all four suits.
     */
    @Test
    fun testToString() {
        assertEquals(spadesChar + "A", aceOfSpades.toString())
        assertEquals(clubsChar + "J", jackOfClubs.toString())
        assertEquals(heartsChar + "Q", queenOfHearts.toString())
        assertEquals(diamondsChar + "J", jackOfDiamonds.toString())
    }

    /**
     * Check if toString produces a 2 character string for every possible card
     * except the 10 (for which length=3 is ensured)
     */
    @Test
    fun testToStringLength() {
        CardSuit.values().forEach { suit ->
            CardValue.values().forEach { value ->
                if (value == CardValue.TEN)
                    assertEquals(3, Card(suit, value).toString().length)
                else
                    assertEquals(2, Card(suit, value).toString().length)
            }
        }
    }

    /**
     * Test suite for the getIntValue method in the Card class.
     *
     * This test ensures that the getIntValue method returns the correct integer value
     * for each card in the deck, covering number cards, face cards, and the Ace card.
     * It checks the following scenarios:
     * - Number cards (Two through Ten) should return their respective numeric values.
     * - Face cards (Jack, Queen, King) should return values 11, 12, and 13 respectively.
     * - The Ace card should return a default value of 1 and also supports a custom value, which is also tested.
     */
    @Test
    fun testGetIntValue() {
        // Testing number cards
        assertEquals(2, Card(CardSuit.SPADES, CardValue.TWO).getIntValue())
        assertEquals(3, Card(CardSuit.CLUBS, CardValue.THREE).getIntValue())
        assertEquals(4, Card(CardSuit.HEARTS, CardValue.FOUR).getIntValue())
        assertEquals(5, Card(CardSuit.DIAMONDS, CardValue.FIVE).getIntValue())
        assertEquals(6, Card(CardSuit.SPADES, CardValue.SIX).getIntValue())
        assertEquals(7, Card(CardSuit.CLUBS, CardValue.SEVEN).getIntValue())
        assertEquals(8, Card(CardSuit.HEARTS, CardValue.EIGHT).getIntValue())
        assertEquals(9, Card(CardSuit.DIAMONDS, CardValue.NINE).getIntValue())
        assertEquals(10, Card(CardSuit.SPADES, CardValue.TEN).getIntValue())

        // Testing face cards
        assertEquals(11, jackOfClubs.getIntValue())
        assertEquals(12, queenOfHearts.getIntValue())
        assertEquals(13, Card(CardSuit.DIAMONDS, CardValue.KING).getIntValue())

        // Testing Ace with default value
        assertEquals(1, aceOfSpades.getIntValue())

        // Testing Ace with a custom value
        assertEquals(14, aceOfSpades.getIntValue(14))
    }

    /**
     * Check if two cards with the same CardSuit/CardValue combination are equal
     * in the sense of the `==` operator, but not the same in the sense of
     * the `===` operator.
     */
    @Test
    fun testEquals() {
        assertEquals(queenOfHearts, otherQueenOfHearts)
        assertNotSame(queenOfHearts, otherQueenOfHearts)
    }
}