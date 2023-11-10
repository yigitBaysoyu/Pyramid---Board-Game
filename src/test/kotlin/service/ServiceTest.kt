package service

import org.junit.jupiter.api.BeforeEach
import entity.Card
import entity.CardSuit
import entity.CardValue
import kotlin.test.*

class ServiceTest {

    private lateinit var gameService: GameService
    private lateinit var playerService: PlayerService
    private lateinit var rootService: RootService

    @BeforeEach
    fun setUp() {
        rootService = RootService()
        gameService = GameService(rootService)
        playerService = PlayerService(rootService)
        rootService.gameService.startNewGame("Alice", "Bob")
    }

    @Test
    fun testStartNewGame() {
        gameService.startNewGame("Alice", "Bob")
        assertNotNull(rootService.currentGame, "Game should be initialized.")
        assertTrue(rootService.currentGame?.playerOnesTurn ?: false, "It should be player one's turn.")
    }

    @Test
    fun testCreatePyramid() {
        val allCards = (CardSuit.values().flatMap { suit ->
            CardValue.values().map { value -> Card(suit, value) }
        }).take(28)
        gameService.startNewGame("Alice", "Bob")
        gameService.createPyramid(allCards)
        assertEquals(7, rootService.currentGame?.pyramid?.last()?.size, "The last level of the pyramid should have 7 cards.")
    }

    @Test
    fun testEndGame() {
        gameService.startNewGame("Alice", "Bob")
        // Mock the game state
        rootService.currentGame?.passCounter = 2
        gameService.endGame()
        // Actual assertion would depend on the game end logic, here we check if refreshables were called.
        // Assuming there is a refreshable that is set to true when game ends.
        // assertTrue(testRefreshable.refreshAfterGameEndCalled, "The game should be ended.")
    }

    @Test
    fun testSwitchPlayer() {
        gameService.startNewGame("Alice", "Bob")
        val initialTurn = rootService.currentGame?.playerOnesTurn
        gameService.switchPlayer()
        assertNotEquals(initialTurn, rootService.currentGame?.playerOnesTurn, "Player turn should switch.")
    }

    @Test
    fun testCheckPair() {
        val card1 = Card(CardSuit.HEARTS, CardValue.FOUR)
        val card2 = Card(CardSuit.SPADES, CardValue.JACK)  // Should sum up to 15
        assertTrue(gameService.checkPair(card1, card2), "This should be a valid pair.")
        val card3 = Card(CardSuit.HEARTS, CardValue.ACE)
        assertTrue(gameService.checkPair(card1, card3), "A pair with an Ace should be valid if the other card makes the sum to 15.")
    }

    @Test
    fun testRemovePair() {
        gameService.startNewGame("Alice", "Bob")
        val card1 = Card(CardSuit.HEARTS, CardValue.FOUR)
        val card2 = Card(CardSuit.SPADES, CardValue.JACK)
        rootService.currentGame?.pyramid = mutableListOf(mutableListOf(card1, card2)) // Adds a first level with card1 and card2

        gameService.removePair(card1, card2)
        assertFalse(rootService.currentGame?.pyramid?.flatten()!!.contains(card1), "Card1 should be removed from the pyramid.")
        assertFalse(rootService.currentGame?.pyramid?.flatten()!!.contains(card2), "Card2 should be removed from the pyramid.")
    }

    @Test
    fun testAddScore() {
        val card1 = Card(CardSuit.HEARTS, CardValue.FOUR)
        val card2 = Card(CardSuit.SPADES, CardValue.JACK)  // Should sum up to 15
        assertEquals(2, gameService.addScore(card1, card2), "Pair score should be 2.")
        val card3 = Card(CardSuit.HEARTS, CardValue.ACE)
        assertEquals(1, gameService.addScore(card1, card3), "Pair with an Ace score should be 1.")
    }

    @Test
    fun testSelectPair() {
        val player = rootService.currentGame?.player1 ?: fail("Player 1 should be initialized.")
        val card1 = Card(CardSuit.HEARTS, CardValue.FOUR)
        val card2 = Card(CardSuit.SPADES, CardValue.JACK)
        playerService.selectPair(player, card1, card2)
        // Assert that the pair was selected and the score was updated correctly
    }

    @Test
    fun testUseDrawPileWhenEmptyShouldThrowException() {
        // Arrange
        val player = rootService.currentGame?.player1 ?: fail("Player 1 should be initialized.")
        rootService.currentGame?.drawPile?.clear()  // Ensure the draw pile is empty

        // Act & Assert
        val exception = assertFailsWith<Exception> {
            playerService.useDrawPile(player)
        }
        assertEquals("there are no cards to draw, choose another action.", exception.message, "The expected exception was not thrown.")
    }

    @Test
    fun testPass() {
        val player = rootService.currentGame?.player1 ?: fail("Player 1 should be initialized.")
        playerService.pass(player)
        // Assert that the pass counter was incremented and the turn was switched
    }

}

