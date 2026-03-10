

//_______________AHMED_________________
public class Bar implements Runnable{
    private int nbTables=1, qualite, x, y;
    private Barman barman;
    private Client client;

    private int nbClients;

    private int expBar;
    /*
    public Bar(int nbTables) {
        this.nbTables = nbTables;
        this.qualite = qualite;
        this.barman = barman;
        this.client = client;
        this.capaTables = 4;
        this.x = x;
        this.y = y;
    }

     */

    public Bar(int x, int y, int nbClients, int expBar){
        this.x = x;
        this.y = y;
        this.nbClients = nbClients;
        this.expBar = expBar;
    }


    public void plusTables(){
        nbTables++;}

/*
    private void calculerCapaciteTotale() {
        for (int i = 0; i < nbTables; i++) {
            capaciteTotale += capacitesTables[i % capacitesTables.length];
        }}


 */

    public void plusExp() {
        expBar ++;
        lvlUp();}
    public void moinsExp() {
        expBar --;}

    public void lvlUp() {
        if (expBar >= 5) {
            expBar = 0;}}




    public synchronized void run(){
        while (true){

        }
    }


    public int getExpBar() {
        return expBar;
    }

    public void setExpBar(int expBar) {
        this.expBar = expBar;
    }

    public int getNbTables() {
        return nbTables;
    }

    public void setNbTables(int nbTables) {
        this.nbTables = nbTables;
    }


    public int getQualite() {
        return qualite;
    }

    public void setQualite(int qualite) {
        this.qualite = qualite;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Barman getBarman() {
        return barman;
    }

    public void setBarman(Barman barman) {
        this.barman = barman;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
