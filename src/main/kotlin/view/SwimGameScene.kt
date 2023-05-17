package view

import entity.Card
import entity.Game
import entity.Player
import service.CardImageLoader
import service.RootService
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color


/**
 * This is the main thing for the Swim game. The scene shows the complete table at once.
 * Player 1 "sits" is on the bottom half of the screen, other players sit around
 * Each player has four activities: Knock, pass, swap one card and swap all cards
 * It has a small board to show who is playing and how much score he has
 */
class SwimGameScene (private val rootService: RootService) : BoardGameScene(1920, 1080), Refreshable {
    private val backSide = ImageVisual(CardImageLoader().backImage)
    private val buttonColor = ColorVisual(255,255,49)
    private val cardImageLoader = CardImageLoader()

    private val p1Label = Label(
        width = 600, height = 50,
        posX = 500, posY = 625,
        text = "current player and his cards:",
        font = Font(size = 40, color = Color.WHITE),
    )
    private val p2Label = Label(
        width = 400, height = 50,
        posX = 50, posY = 550,
        text = "actions of player:",
        font = Font(size = 40, color = Color.WHITE),
    )
    private val p3Label = Label(
        width = 400, height = 50,
        posX = 10, posY = 225,
        text = "one has knocked",
        font = Font(size = 40, color = Color.WHITE),
    )

    //Text field to show the turn of player and score
    private val showTurn = Label(
        height = 200, width = 300,
        posX = 50, posY = 0, font = Font(size = 50, color = Color.WHITE)
    )
    private val showNumberOfCard = Label(
        height = 200, width = 350,
        posX = 1190, posY = 300, font = Font(size = 50, color = Color.WHITE)
    )

    // show Someone knocked
    private val showKnock = Label(
        height = 200, width = 300,
        posX = 50, posY = 200, font = Font(size = 40, color = Color.RED)
    )

    // currents (bottom of screen) cards
    private val playerCards = LinearLayout<CardView>(
        height = 150, width = 600,
        posX = 600, posY = 700, spacing = 150,
    )

    //cards in middle
    private val cardsInMid = LinearLayout<CardView>(
        height = 150, width = 600,
        posX = 650, posY = 350, spacing = 50
    )

    // another cards to present another players
    // cards on top screen
    private val p1LeftCard = CardView(posX = 600, posY = 70, front = backSide)
    private val p1MidCard = CardView(posX = 700, posY = 70, front = backSide)
    private val p1RightCard = CardView(posX = 800, posY = 70, front = backSide)

    // cards on the left side
    private val p2LeftCard = CardView(posX = 1500, posY = 70, front = backSide)
    private val p2MidCard = CardView(posX = 1600, posY = 70, front = backSide)
    private val p2RightCard = CardView(posX = 1700, posY = 70, front = backSide)

    // cards on the right side
    private val p3LeftCard = CardView(posX = 1500, posY = 500, front = backSide)
    private val p3MidCard = CardView(posX = 1600, posY = 500, front = backSide)
    private val p3RightCard = CardView(posX = 1700, posY = 500, front = backSide)

    //stack of card
    private val midCardsPresent = CardView(posX = 1300, posY = 300, front = backSide)

    //Knock Button
    private val knockButton = Button(
        width = 200, height = 50,
        posX = 50, posY = 625,
        text = "Knock", font = Font(32),
        visual = buttonColor
    ).apply {
        onMouseClicked = {
            rootService.currentGame?.let {
                rootService.playerService.knock()
            }
            isVisible = false
        }
    }

    //Pass Button
    private val passButton = Button(
        width = 200, height = 50,
        posX = 50, posY = 775,
        text = "Pass",
        font = Font(32),
        visual = buttonColor
    ).apply {
        onMouseClicked = {
            rootService.currentGame?.let {
                rootService.playerService.pass()
            }
        }
    }

    //Swap One Button
    private val swapButton = Button(
        width = 200, height = 50,
        posX = 250, posY = 700,
        text = "Swap",
        font = Font(32),
        visual = buttonColor
    ).apply {
        onMouseClicked = {
            rootService.currentGame?.let {
                try {
                    rootService.playerService.swapOne()
                } catch (err: Exception) {
                    throw Exception("Please choose a card on your hand and a card on the table")
                }

            }
        }
    }

    //Swap All Button
    private val swapAllButton = Button(
        width = 200, height = 50,
        posX = 250, posY = 850,
        text = "Swap All",
        font = Font(32),
        visual = buttonColor
    ).apply {
        onMouseClicked = {
            rootService.currentGame?.let {
                rootService.playerService.swapAll()
                updateShowBoard(rootService.currentGame!!)
            }
        }

    }

    private val flipCardButton = Button(
        width = 200, height = 50,
        posX = 50, posY = 925,
        text = "Flip card",
        font = Font(32),
        visual = buttonColor
    ).apply {
        onMouseClicked = {
            playerCards.forEach {
                cardView -> when(cardView.currentSide){
                CardView.CardSide.BACK -> cardView.showFront()
                CardView.CardSide.FRONT -> cardView.showBack()
                }
            }
        }
    }

    /**
     * structure to hold pairs of (card, cardView) that can be used
     *
     * 1. to find the corresponding view for a card passed on by a refresh method (forward lookup)
     *
     * 2. to find the corresponding card to pass to a service method on the occurrence of
     * ui events on views (backward lookup).
     */
    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()


    init {
        background = ImageVisual("MuLogo.png")
        addComponents(
            playerCards,
            p1LeftCard, p1MidCard, p1RightCard,
            p2LeftCard, p2MidCard, p2RightCard,
            p3LeftCard, p3MidCard, p3RightCard,
            midCardsPresent,
            cardsInMid,
            knockButton, passButton, swapButton, swapAllButton,flipCardButton,
            p1Label, p2Label,p3Label,
            showNumberOfCard, showTurn, showKnock
        )
    }

    override fun refreshAfterStartGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }
        cardMap.clear()
        updateCardsInMiddle(game,cardImageLoader)
        updateShowBoard(game)
        updateCards(game,cardImageLoader)
    }

    override fun refreshAfterChangeTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        val delay = DelayAnimation(500)
        delay.onFinished = {
            updateShowBoard(game)
            updateCards(game,cardImageLoader)
        }
        playAnimation(delay)
    }

    override fun refreshAfterEndGame() {
        knockButton.isVisible = true
    }

    override fun refreshAfterSwapCard(player: Player) {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }
        val cardImageLoader = CardImageLoader()
        updateCards(game,cardImageLoader)
        updateCardsInMiddle(game,cardImageLoader)
        println("has refreshed after swap card")
    }
    /**
     * initialize Cards in  middle
     */
    private fun updateCardsInMiddle(game:Game, cardImageLoader: CardImageLoader){
        cardsInMid.clear()
        var chosenCardIndex = -1
        game.middleCards.forEach {
            val cardView = CardView(
                height = 250,
                width = 170,
                front = ImageVisual(cardImageLoader.frontImageFor(it.suit, it.value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
            cardView.showFront()
            cardsInMid.add(cardView)
            cardMap.add(it to cardView)
        }
        cardsInMid.forEach {
            ++chosenCardIndex
            addTableCardViewClickListener(it,chosenCardIndex,cardsInMid)
        }

    }
    /**
     * Update the card of current Player
     */
    private fun updateCards(game:Game, cardImageLoader: CardImageLoader){
        playerCards.clear()
        var cardIndex = -1
        game.players[game.currentPlayer].playerCards.forEach{
            val cardView = CardView(
                height = 250,
                width = 170,
                front = backSide,
                back = ImageVisual(cardImageLoader.frontImageFor(it.suit, it.value))
            )
            cardView.showFront()
            playerCards.add(cardView)
            cardMap.add(it to cardView)
        }
        playerCards.forEach {
            cardIndex++
            addPlayerCardViewClickListener(it,cardIndex,playerCards)
        }
    }
    /**
     * Update the score and the name of current player
     */
    private fun updateShowBoard(game:Game){
        showTurn.text = "${game.players[game.currentPlayer].name}'s turn"
        val numberOfCard = rootService.currentGame!!.drawCards.size
        showNumberOfCard.text = "$numberOfCard"
        if (game.hasKnocked != false){
            showKnock.text = "Yes"
        } else showKnock.text = "No"

    }

    private fun addTableCardViewClickListener(cardView: CardView, cardIndex: Int, tableCardViews: LinearLayout<CardView>) {
        cardView.apply {
            onMouseClicked = { selectTableCardAction(cardIndex, tableCardViews) }
        }
    }

    private fun selectTableCardAction(chosenCardIndex: Int, tableCardViews: LinearLayout<CardView>) {
        resizeCardViewsToDefault(tableCardViews)

        rootService.currentGame?.indexChosenCard = chosenCardIndex
        var cardIndex = -1
        tableCardViews.forEach {
            ++cardIndex
            if (cardIndex == chosenCardIndex) it.resize(width = 169, height=260)
        }
    }


    private fun addPlayerCardViewClickListener(cardView: CardView, cardIndex: Int, playerCardViews: LinearLayout<CardView>) {
        cardView.apply {
            onMouseClicked = { selectPlayerCardAction(cardIndex, playerCardViews) }
        }
    }

    private fun selectPlayerCardAction(chosenCardIndex: Int, playerCardViews: LinearLayout<CardView>) {
        val game = rootService.currentGame
        checkNotNull(game)
        resizeCardViewsToDefault(playerCardViews)
        val indexCurrentPlayer = game.currentPlayer
        game.players[indexCurrentPlayer].indexChosenCard = chosenCardIndex
        var cardIndex = -1
        playerCardViews.forEach {
            ++cardIndex
            if (cardIndex == chosenCardIndex) it.resize(width = 169, height=260)
        }
    }

    private fun resizeCardViewsToDefault(cardViews: LinearLayout<CardView>) {
        cardViews.forEach {
            it.resize(width = 130, height=200)
        }
    }
}