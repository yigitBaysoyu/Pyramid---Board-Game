package entity

import kotlin.test.*

/**
 * PyramidGameTest is a test class for PyramidGame.
 *
 * It includes unit tests to validate the correct initialization and behavior of the PyramidGame class.
 */
class PyramidGameTest {

    /**
     * Tests the initial state of the PyramidGame upon creation.
     *
     * This test ensures that all properties and elements of the PyramidGame are correctly
     * initialized to their default states, including player turn, card counts, player details,
     * and the state of various card stacks.
     */
    @Test
    fun testInitialization() {
        // Create a new instance of PyramidGame with player names "Alice" and "Bob"
        val game = PyramidGame("Alice", "Bob")

        // Assert that player one's turn is initialized to true
        assertEquals(true, game.playerOnesTurn, "playerOnesTurn should be initialized to true")

        // Assert that the flipped card count starts at 0
        assertEquals(0, game.flippedCardCount, "flippedCardCount should be initialized to 0")

        // Assert that the pass counter starts at 0
        assertEquals(0, game.passCounter, "passCounter should be initialized to 0")

        // Assert that the pyramid is initialized as an empty mutable list
        assertTrue(game.pyramid.isEmpty(), "pyramid should be initialized as an empty MutableList")

        // Assert that the draw pile is initialized as an empty stack
        assertTrue(game.drawPile.isEmpty(), "drawPile should be initialized as an empty Stack")

        // Assert that the collected storage pile is initialized as an empty stack
        assertTrue(game.collectedStoragePile.isEmpty(), "collectedStoragePile should be initialized as an empty Stack")

        // Assert that the storage pile is initialized as an empty stack
        assertTrue(game.storagePile.isEmpty(), "storagePile should be initialized as an empty Stack")

        // Assert that player1 is correctly initialized with the name "Alice" and marked as the first player
        assertEquals("Alice", game.player1.name, "player1 should be initialized with the correct name")
        assertTrue(game.player1.isFirstPlayer, "player1 should be marked as the first player")

        // Assert that player2 is correctly initialized with the name "Bob" and not marked as the first player
        assertEquals("Bob", game.player2.name, "player2 should be initialized with the correct name")
        assertFalse(game.player2.isFirstPlayer, "player2 should not be marked as the first player")
    }

}
