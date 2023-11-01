package service

import entity.*
import org.junit.jupiter.api.BeforeEach
import kotlin.test.*

/**
 * This test class focuses on the functionality and behavior of the PyramidGame entity.
 */
class PyramidGameTest {

    private lateinit var game: PyramidGame

    /**
     * Set up a basic game environment before each test.
     */
    @BeforeEach
    fun setup() {
        game = PyramidGame(player1Name = "Alice", player2Name = "Bob")
    }

    /**
     * Test to ensure the game initializes with the turn set to player one.
     */
    @Test
    fun testInitialPlayerTurn() {
        assertTrue(game.playerOnesTurn)
    }

    /**
     * Test to ensure the game initializes with no flipped cards.
     */
    @Test
    fun testInitialFlippedCardCount() {
        assertEquals(0, game.flippedCardCount)
    }

    /**
     * Test to ensure the game initializes with no consecutive passes.
     */
    @Test
    fun testInitialPassCounter() {
        assertEquals(0, game.passCounter)
    }

    /**
     * Test to ensure players are correctly initialized when a new game is started.
     */
    @Test
    fun testPlayersInitializedCorrectly() {
        assertEquals("Alice", game.player1.name)
        assertTrue(game.player1.isFirstPlayer)
        assertEquals(0, game.player1.score)

        assertEquals("Bob", game.player2.name)
        assertFalse(game.player2.isFirstPlayer)
        assertEquals(0, game.player2.score)
    }

    /**
     * Test to ensure the initial state of all card piles in the game is empty.
     */
    @Test
    fun testInitialPileSizes() {
        assertTrue(game.drawPile.isEmpty())
        assertTrue(game.pyramid.isEmpty())
        assertTrue(game.collectedStoragePile.isEmpty())
        assertTrue(game.storagePile.isEmpty())
    }
}