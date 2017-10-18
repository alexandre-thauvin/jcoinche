package server;

public class Card {

    public String suite;
    public String number;
    public int    power;

    Card(int suite, int number) {

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
                this.number = "sept";
                break;
            case 1:
                this.number = "huit";
                break;
            case 2:
                this.number = "neuf";
                break;
            case 3:
                this.number = "dix";
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
            default:
                this.number = "";
                break;
        }
    }
}