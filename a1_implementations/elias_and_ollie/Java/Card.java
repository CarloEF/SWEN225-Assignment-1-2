package Java;

import java.util.stream.Stream;

public interface Card {

    /**
     * All the possible PlayerCard values
     */
    enum PlayerCard implements Card {
        COLONEL_MUSTARD("Colonel Mustard"),
        PROFESSOR_PLUM("Professor Plum"),
        REVEREND_GREEN("Reverend Green"),
        MRS_PEACOCK("Mrs Peacock"),
        MISS_SCARLET("Miss Scarlet"),
        MRS_WHITE("Mrs White");

        public String name;
        PlayerCard(String name) {
            this.name = name;
        }
        public String toString() {return this.name; }
    }

    /**
     * All the possible WeaponCard values
     */
    enum WeaponCard implements Card {

        DAGGER("Dagger"),
        CANDLESTICK("Candlestick"),
        REVOLVER("Revolver"),
        ROPE("Rope"),
        LEAD_PIPE("Lead Pipe"),
        SPANNER("Spanner");

        public String name;
        WeaponCard(String name) {
            this.name = name;
        }
        public String toString() {return this.name; }
    }

    /**
     * All the possible RoomCard values
     */
    enum RoomCard implements Card {

        KITCHEN("Kitchen"),
        BALLROOM("Ballroom"),
        CONSERVATORY("Conservatory"),
        DINING_ROOM("Dining Room"),
        BILLIARD_ROOM("Billiard Room"),
        LIBRARY("Library"),
        LOUNGE("Lounge"),
        HALL("Hall"),
        STUDY("Study");

        public String name;
        RoomCard(String name) {
            this.name = name;
        }
        public String toString() {return this.name; }
    }

    /**
     * @return an array of all of the Card values of all types (all PlayerCards, RoomCards, and WeaponCards).
     */
    public static Card[] values() {
        return Stream.of(PlayerCard.values(), WeaponCard.values(), RoomCard.values())
                .flatMap(Stream::of)
                .toArray(Card[]::new);
    }
}