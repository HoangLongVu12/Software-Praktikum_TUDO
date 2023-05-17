package entity
/**
 * Data class for the single typ of game elements that the game "Swim" knows: cards.
 *
 * It is characterized by a [CardSuit] and a [CardValue]
 */
data class Card(val suit: CardSuit, val value: CardValue) {

    override fun toString() = "$suit$value"

    /**
     * generates card value to score
     *
     * @return generated score
     */
    fun toDoubleValue() : Double {
        var doubleValue = when(value.toString()){
            "2" -> 2.0
            "3" -> 3.0
            "4" -> 4.0
            "5" -> 5.0
            "6" -> 6.0
            "7" -> 7.0
            "8" -> 8.0
            "9" -> 9.0
            "A" -> 11.0
            else -> 10.0
        }
        return doubleValue
    }
    /**
     * compares two [Card]s according to the [Enum.ordinal] value of their [CardSuit]
     * (i.e., the order in which the suits are declared in the enum class)
     */
    operator fun compareTo(other: Card) = this.value.ordinal - other.value.ordinal

    /**
     * compares if 2 cards are the same
     *
     * @param other second card to compare
     * @return true if 2 cards are the same, false if not
     */
    fun equals(other: Card): Boolean {
        return this.value.ordinal == other.value.ordinal && this.suit.ordinal == other.suit.ordinal
    }

    /**
     * compares if values of 2 cards are the same
     *
     * @param other second card to compare
     * @return true if values of 2 cards are the same, false if not
     */
    fun equalsValue(other: Card): Boolean {
        return this.value.ordinal == other.value.ordinal
    }

    /**
     * compares if suits of 2 cards are the same
     *
     * @param other second card to compare
     * @return true if suits of 2 cards are the same, false if not
     */
    fun equalsSuit(other: Card): Boolean {
        return this.suit.ordinal == other.suit.ordinal
    }



}






