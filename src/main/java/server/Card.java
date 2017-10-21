package server;

public class Card {

    String suite;
    String number;

    public Card(int suite, int number) {

        switch (suite)
        {
            case 0:
                this.suite = "pic";

                break;
            case 1:
                this.suite = "coeur";
                    break;
            case 2:
                this.suite = "carreau";
                    break;
            case 3:
                this.suite = "trefle";
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
                this.number = "valet";
                break;
            case 5:
                this.number = "dame";
                break;
            case 6:
                this.number = "roi";
                break;
            case 7:
                this.number = "as";
                break;
            default:
                this.number = "";
                break;
        }
    }
}