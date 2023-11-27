package service

import entity.*
import view.Refreshable
import kotlin.test.*

/**
 * Class that provides tests for [GameService] and [PlayerService] (both at the same time,
 * as their functionality is not easily separable) by basically playing through some sample games.
 * [TestRefreshable] is used to validate correct refreshing behavior even though no GUI
 * is present.
 */
class ServiceTest {

    private val cards = listOf(

        //pyramid cards
        Card(CardSuit.CLUBS, CardValue.THREE),      //0
        Card(CardSuit.DIAMONDS, CardValue.ACE),     //1
        Card(CardSuit.HEARTS, CardValue.SIX),       //2
        Card(CardSuit.CLUBS, CardValue.TEN),        //3
        Card(CardSuit.HEARTS, CardValue.FIVE),      //4
        Card(CardSuit.HEARTS, CardValue.TWO),       //5
        Card(CardSuit.SPADES, CardValue.JACK),      //6
        Card(CardSuit.CLUBS, CardValue.JACK),       //7
        Card(CardSuit.DIAMONDS, CardValue.FOUR),    //8
        Card(CardSuit.HEARTS, CardValue.KING),      //9
        Card(CardSuit.HEARTS, CardValue.FOUR),      //10
        Card(CardSuit.SPADES, CardValue.FIVE),      //11
        Card(CardSuit.DIAMONDS, CardValue.TEN),     //12
        Card(CardSuit.SPADES, CardValue.KING),      //13
        Card(CardSuit.HEARTS, CardValue.SEVEN),     //14
        Card(CardSuit.SPADES, CardValue.SEVEN),     //15
        Card(CardSuit.SPADES, CardValue.NINE),      //16
        Card(CardSuit.SPADES, CardValue.ACE),       //17
        Card(CardSuit.CLUBS, CardValue.SIX),        //18
        Card(CardSuit.DIAMONDS, CardValue.NINE),    //19
        Card(CardSuit.CLUBS, CardValue.FOUR),       //20
        Card(CardSuit.CLUBS, CardValue.TWO),        //21
        Card(CardSuit.DIAMONDS, CardValue.JACK),    //22
        Card(CardSuit.SPADES, CardValue.SIX),       //23
        Card(CardSuit.CLUBS, CardValue.FIVE),       //24
        Card(CardSuit.HEARTS, CardValue.TEN),       //25
        Card(CardSuit.SPADES, CardValue.TWO),       //26
        Card(CardSuit.CLUBS, CardValue.ACE),        //27

        //draw stack cards
        Card(CardSuit.CLUBS, CardValue.QUEEN),      //28
        Card(CardSuit.CLUBS, CardValue.KING),       //29
        Card(CardSuit.HEARTS, CardValue.THREE),     //30
        Card(CardSuit.CLUBS, CardValue.EIGHT),      //31
        Card(CardSuit.SPADES, CardValue.EIGHT),     //32
        Card(CardSuit.HEARTS, CardValue.EIGHT),     //33
        Card(CardSuit.DIAMONDS, CardValue.TWO),     //34
        Card(CardSuit.DIAMONDS, CardValue.SEVEN),   //35
        Card(CardSuit.DIAMONDS, CardValue.FIVE),    //36
        Card(CardSuit.CLUBS, CardValue.SEVEN),      //37
        Card(CardSuit.HEARTS, CardValue.ACE),       //38
        Card(CardSuit.SPADES, CardValue.TEN),       //39
        Card(CardSuit.HEARTS, CardValue.JACK),      //40
        Card(CardSuit.SPADES, CardValue.QUEEN),     //41
        Card(CardSuit.DIAMONDS, CardValue.EIGHT),   //42
        Card(CardSuit.SPADES, CardValue.THREE),     //43
        Card(CardSuit.DIAMONDS, CardValue.KING),    //44
        Card(CardSuit.DIAMONDS, CardValue.SIX),     //45
        Card(CardSuit.CLUBS, CardValue.NINE),       //46
        Card(CardSuit.DIAMONDS, CardValue.QUEEN),   //47
        Card(CardSuit.SPADES, CardValue.FOUR),      //48
        Card(CardSuit.HEARTS, CardValue.NINE),      //49
        Card(CardSuit.HEARTS, CardValue.QUEEN),     //50
        Card(CardSuit.DIAMONDS, CardValue.THREE)    //51
    )

    /**
     * starts a game with a static order of cards that can be used
     * in other tests to deterministically validate the outcome
     * of turns.
     *
     * The cards of the resulting game are (top-of-stack cards first):
     *
     * - pyramid:    ♣3, ♦A, ♥6, ♣10, ♥5, ♥2, ♠J, ♣J, ♦4, ♥K, ♥4, ♠5,
     *               ♦10, ♠K, ♥7, ♠7, ♠9, ♠A, ♣6, ♦9, ♣4, ♣2, ♦J, ♠6,
     *               ♣5, ♥10, ♠2, ♣A
     *
     * - draw stack: ♣Q, ♣K, ♥3, ♣8, ♠8, ♥8, ♦2, ♦7, ♦5, ♣7, ♥A, ♠10,
     *               ♥J, ♠Q, ♦8, ♠3, ♦K, ♦6, ♣9, ♦Q, ♠4, ♥9, ♥Q, ♦3
     *
     *
     * @param refreshables refreshables to be added to the root service
     * right after its instantiation (so that, e.g., start new game will already
     * be observable)
     *
     * @return the root service holding the started game as [RootService.currentGame]
     */
    private fun setUpGame(vararg refreshables: Refreshable): RootService {
        val mc = RootService()
        refreshables.forEach { mc.addRefreshable(it) }

        mc.gameService.startNewGame("Bob", "Alice")


        mc.currentGame!!.drawPile.clear()
        mc.currentGame!!.drawPile.pushAll(cards.subList(27, 52))

        mc.currentGame!!.pyramid.clear()
        mc.gameService.createPyramid(cards.subList(0, 28))

        var index = 0
        for(level in 0..6){
            for(cardIndex in 0.. level){
                if(cardIndex == 0 || cardIndex == level) {
                    cards[index].flipped = true
                }
                assertEquals(cards[index++], mc.currentGame!!.pyramid[level][cardIndex])
            }
        }

        println(mc.currentGame)
        return mc
    }

    /**
     * Tests the default case of starting a game: instantiate a [RootService] and then run
     * startNewGame on its [RootService.gameService].
     */
    @Test
    fun testStartNewGame() {
        val testRefreshable = TestRefreshable()
        val mc = RootService()
        mc.addRefreshable(testRefreshable)

        assertFalse(testRefreshable.refreshAfterStartNewGameCalled)
        assertNull(mc.currentGame)
        mc.gameService.startNewGame("Bob", "Alice")
        assertTrue(testRefreshable.refreshAfterStartNewGameCalled)
        assertNotNull(mc.currentGame)

        assertEquals(24, mc.currentGame!!.drawPile.size, "Draw pile should have correct number of cards")
        assertEquals(28, mc.currentGame!!.pyramid.flatten().size, "Pyramid should have correct number of cards")
        assertTrue(mc.currentGame!!.storagePile.isEmpty(), "storagePile should be initialized as an empty Stack")
        assertTrue(mc.currentGame!!.collectedStoragePile.isEmpty(), "collectedStoragePile should be initialized as an empty Stack")
    }

    /**
     * Uses [cards] as the source for deterministic card stacks and tests whether the pyramid is correctly initialized.
     */
    @Test
    fun testCreatePyramid(){
        val testRefreshable = TestRefreshable()
        val mc = setUpGame(testRefreshable)

        assertNotNull(mc.currentGame)

        var index = 0
        for(level in 0..6){
            for(cardIndex in 0.. level){
                assertEquals(cards[index++], mc.currentGame!!.pyramid[level][cardIndex])
            }
        }
    }

    /**
     * This test method uses [setUpGame] with a predetermined set of cards [cards] to create a deterministic game environment.
     * It tests several critical actions and scenarios in the game's flow, including:
     *
     * - Selecting and removing valid pairs of cards by players, verifying that the correct cards are moved to the collected storage pile
     *   and that the appropriate refresh action is called. It checks this for multiple pairs and both players.
     *
     * - Using the draw pile to add a card to the storage pile, ensuring the top card of the draw pile is moved correctly,
     *   and the corresponding refresh action is invoked.
     *
     * - Selecting a pair that includes a card from the storage pile and a card from the pyramid, confirming that the pair is
     *   correctly removed and added to the collected storage pile.
     *
     * - Attempting to select an invalid pair of cards, which should fail and not trigger a refresh action.
     *
     * - A player passing their turn, verifying that the pass action triggers the appropriate refresh.
     *
     * This method simulates a sequence of turns in the game, testing player interactions, game logic, and the integration
     * between the game state and the refresh system.
     */
    @Test
    fun testPlayerAction(){
        val testRefreshable = TestRefreshable()
        val mc = setUpGame(testRefreshable)

        assertNotNull(mc.currentGame)
        val p1 = mc.currentGame!!.player1
        val p2 = mc.currentGame!!.player2

        mc.playerActionService.selectPair(p1, cards[5], cards[9])
        assertEquals(cards[9], mc.currentGame!!.collectedStoragePile.peek())
        assertTrue { testRefreshable.refreshAfterRemovePairCalled }

        mc.playerActionService.selectPair(p2, cards[3], cards[4])
        assertEquals(cards[4], mc.currentGame!!.collectedStoragePile.peek())
        assertTrue { testRefreshable.refreshAfterRemovePairCalled }

        mc.playerActionService.useDrawPile(p1)
        assertEquals(Card(CardSuit.DIAMONDS, CardValue.THREE), mc.currentGame!!.storagePile.peek())
        assertTrue { testRefreshable.refreshAfterRevealCardCalled }

        mc.playerActionService.useDrawPile(p2)
        assertEquals(Card(CardSuit.HEARTS, CardValue.QUEEN), mc.currentGame!!.storagePile.peek())
        assertTrue { testRefreshable.refreshAfterRevealCardCalled }

        mc.playerActionService.selectPair(p1, Card(CardSuit.HEARTS, CardValue.QUEEN), cards[0])
        assertEquals(Card(CardSuit.HEARTS, CardValue.QUEEN), mc.currentGame!!.collectedStoragePile.peek())
        assertTrue { testRefreshable.refreshAfterRemovePairCalled }

        mc.playerActionService.selectPair(p2, Card(CardSuit.DIAMONDS, CardValue.THREE), cards[1])
        assertEquals(Card(CardSuit.DIAMONDS, CardValue.THREE), mc.currentGame!!.collectedStoragePile.peek())
        assertTrue { testRefreshable.refreshAfterRemovePairCalled }
        testRefreshable.reset()

        assertFails { mc.playerActionService.selectPair(p1, cards[2], cards[6]) }
        assertFalse { testRefreshable.refreshAfterRemovePairCalled }

        mc.playerActionService.pass(p2)
        assertTrue(testRefreshable.refreshAfterPassCalled)

    }

    /**
     * Test a complete playthrough to test the proper game ending.
     */
    @Test
    fun testGameEnd() {
        val testRefreshable = TestRefreshable()
        val mc = setUpGame(testRefreshable)

        assertNotNull(mc.currentGame)
        val p1 = mc.currentGame!!.player1
        val p2 = mc.currentGame!!.player2

        mc.playerActionService.selectPair(p1, cards[5], cards[9])
        assertEquals(cards[9], mc.currentGame!!.collectedStoragePile.peek())
        assertTrue { testRefreshable.refreshAfterRemovePairCalled }

        mc.playerActionService.selectPair(p2, cards[3], cards[4])
        assertEquals(cards[4], mc.currentGame!!.collectedStoragePile.peek())
        assertTrue { testRefreshable.refreshAfterRemovePairCalled }

        mc.playerActionService.useDrawPile(p1)
        assertEquals(Card(CardSuit.DIAMONDS, CardValue.THREE), mc.currentGame!!.storagePile.peek())
        assertTrue { testRefreshable.refreshAfterRevealCardCalled }

        mc.playerActionService.selectPair(p2, Card(CardSuit.DIAMONDS, CardValue.THREE), cards[1])
        assertEquals(Card(CardSuit.DIAMONDS, CardValue.THREE), mc.currentGame!!.collectedStoragePile.peek())
        assertTrue { testRefreshable.refreshAfterRemovePairCalled }

        mc.playerActionService.pass(p1)
        assertTrue(testRefreshable.refreshAfterPassCalled)

        mc.playerActionService.pass(p2)

        assertTrue { testRefreshable.refreshAfterGameEndCalled }

    }

}