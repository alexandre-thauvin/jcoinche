package server;

public class Card {

    String  suite;
    String  number;
    int     power;

    public Card(int suite, int number) {

        switch (suite)
        {
            case 0:
                this.suite = "Spadet";

                break;
            case 1:
                this.suite = "Heart";
                    break;
            case 2:
                this.suite = "Diamond";
                    break;
            case 3:
                this.suite = "Club";
                    break;
            default:
                this.suite = "";
                    break;

        }
        switch (number)
        {
            case 0:
                this.number = "7";
                break;
            case 1:
                this.number = "8";
                break;
            case 2:
                this.number = "9";
                break;
            case 3:
                this.number = "10";
            case 4:
                this.number = "Jack";
                break;
            case 5:
                this.number = "Queen";
                break;
            case 6:
                this.number = "King";
                break;
            case 7:
                this.number = "Ace";
                break;
            default:
                this.number = "";
                break;
        }
    }
}