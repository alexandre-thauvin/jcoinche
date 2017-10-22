package server;

public class Card {

    String  suite;
    String  number;
    int     power;
    int     who;

    public Card(int suite, int number) {

        switch (suite)
        {
            case 0:
                this.suite = "spade";

                break;
            case 1:
                this.suite = "heart";
                    break;
            case 2:
                this.suite = "diamond";
                    break;
            case 3:
                this.suite = "club";
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
                this.number = "jack";
                break;
            case 5:
                this.number = "queen";
                break;
            case 6:
                this.number = "king";
                break;
            case 7:
                this.number = "ace";
                break;
            default:
                this.number = "";
                break;
        }
    }
}