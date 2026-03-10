import java.awt.*;
import java.util.List;
import java.util.Random;

//_______________FLORIENT_________________

public class Barman implements Runnable {


    private volatile boolean running = true;
    private int motiv, besoinExp;
    private String grade;
    private double adapt, exp;
    private List<Client> client;
    private int initialX, x, initialY, y;
    private int speedX = 5, speedY = 5;
    private int taille;
    private Object commandePrete;
    private final Random random = new Random();
    private double directionX = 0, directionY = 0; // Directions initiales
    private final int windowWidth = 400, windowHeight = 1000;
    private final double visionAngle = Math.toRadians(70);
    private final int visionDist = 30;
    private List<int[]> path;
    private int pathIndex = 0;
    private Color orange = new Color(150, 110, 1);
    private Color blanc = Color.white;
    private Color vert = new Color(50, 200, 50);
    private Color yellw = new Color(255, 200, 1);
    private Color rouge = new Color(120, 40, 40);
    private Color customColor;



    private boolean disponible = false;


    public Barman(double adapt, String grade, int motiv, int initialX, int initialY) {
        this.exp = 1;
        this.adapt = adapt;
        this.motiv = motiv;
        this.grade = grade;

        setBesoinExp(); // Définit le besoinExp en fonction du grade

        this.initialX = initialX;
        this.initialY = initialY;
        this.x = initialX;
        this.y = initialY;
        this.client = client;

        this.taille = 50;

        this.customColor = blanc;

    }

    // Méthode pour définir `besoinExp` en fonction du grade actuel

    /*
    private void setBesoinExp() {
        switch (grade) {
            case "MAX" -> this.besoinExp = 99999;
            case "Expert" -> this.besoinExp = 3000;
            case "Intermediaire" -> this.besoinExp = 2000;
            case "Debutant" -> this.besoinExp = 1000;
        }
    }
    */


    private void setBesoinExp() {
        switch (grade) {
            case "MAX" -> this.besoinExp = 99999;
            case "Expert" -> this.besoinExp = 50;
            case "Intermediaire" -> this.besoinExp = 50;
            case "Debutant" -> this.besoinExp = 50;
        }
    }


    /*
    private void setBesoinExp() {
        switch (grade) {
            case "MAX" -> this.besoinExp = 99999;
            case "Expert" -> this.besoinExp = 3000;
            case "Intermediaire" -> this.besoinExp = 2000;
            case "Debutant" -> this.besoinExp = 1000;
        }
    }
    */

    // Méthode pour augmenter l'expérience et gérer les changements de grade
    public void plusExp(int xp) {
        this.exp += xp + (xp * adapt);
        this.customColor = blanc;
        if (grade == "Debutant"){
            if (exp >= besoinExp){
                this.grade = "Intermediaire";
                setBesoinExp();
                this.exp = 1;
                this.customColor = Color.white;}
        }
        else if (grade == "Intermediaire") {
            if (exp >= besoinExp){
                setBesoinExp();
                this.grade = "Expert";
                this.exp = 1;
                this.customColor = yellw;}
        } else if (grade == "Expert"){
            if (exp >= besoinExp){
                setBesoinExp();
                this.grade = "MAX";
                this.exp = 1;
                this.customColor = orange;}
        }
    }

    public void moinsExp(int xp) {
        this.exp -= xp;

        if (exp < 0) {
            switch (grade) {
                case "MAX" -> grade = "Expert";
                case "Expert" -> grade = "Intermediaire";
                case "Intermediaire" -> grade = "Debutant";
            }
            setBesoinExp();
            exp = besoinExp - 1;
            setTaille(taille - 2);
        }
    }
/*
    private void setRandomDirection() {
        directionX += random.nextGaussian() * 0.3;
        directionY += random.nextGaussian() * 0.3;
        double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
        directionX /= magnitude;
        directionY /= magnitude;
    }*/

    public synchronized void service(){

        /*
        int chance = random.nextInt(2); //1 chance sur 2 qu'il y ait erreur mais faudra changer
        if (chance == 1) {
            moinsExp(150);
            moinsMotiv();
        }
        else {
        */

        for (Client client : this.client){
            if (client.getSoif() == 100 && client.isServi() && (!client.isAttService())) {
                client.alcoolPlus();
                plusMotiv();
            }}

        //notifyAll();
        this.customColor = vert;

        //}
    }
    public void plusMotiv(){
        motiv ++;
    }
    public void moinsMotiv(){
        motiv -= 5;
    }

    /*
    public void quitte(List<Barman> listObjBarmans) {
        if (this.motiv <= 0) {
            running = false;
            // L'enlever de la liste, le faire quitter le bar
        }
        synchronized (listObjBarmans) {
            listObjBarmans.remove(this); // Retire le barman de la liste partagée
        }
    }
    */

    

    public synchronized void move(List<Client> clients, List<Barman> autresBarmans, List<Bar> tables) {
        /*
        //setCustomColor(noir);
        // Nouvelle logique de pathfinding
        if (path == null || pathIndex >= path.size()) {
            int newTargetX = 300;
            int newTargetY = 300;
            path = Pathfinder.findPath(x, y, newTargetX, newTargetY, tables, clients, autresBarmans);
            pathIndex = 0;
        }

        if (path != null && pathIndex < path.size()) {
            int[] nextPoint = path.get(pathIndex);
            int dx = nextPoint[0] - x;
            int dy = nextPoint[1] - y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < speedX) {
                x = nextPoint[0];
                y = nextPoint[1];
                pathIndex++;
            } else {
                double ratio = speedX / distance;
                x += (int)(dx * ratio);
                y += (int)(dy * ratio);}}

        else {
            // Ancienne logique de mouvement si le pathfinding échoue

            setRandomDirection();
            x += directionX * speedX;
            y += directionY * speedY;
        }

        if (x <= 0 || x >= windowWidth) {
            directionX = -directionX;}
        if (y <= 0 || y >= windowHeight) {
            directionY = -directionY;}

        for (Barman autre : autresBarmans) {
            if (this != autre && detectCollision(autre.getX(), autre.getY(), 25)) {
                directionX = -directionX;
                directionY = -directionY;
                break;}}

        for (Client client : clients) {
            if (detectCollision(client.getX(), client.getY(), 150)) {
                directionX = -directionX;
                directionY = -directionY;
                break;}}

        for (Bar table : tables) {
            if (detectCollision(table.getX(), table.getY(), 10)) {
                directionX = -directionX;
                directionY = -directionY;
                break;}}
        */
        for (Client client : this.client) {
            if (client.getSoif() < 390) {
                if (this.grade == "Debutant")
                    setCustomColor(blanc);
                else if (this.grade == "Intermediaire")
                    setCustomColor(yellw);
                else if (this.grade == "Expert")
                    setCustomColor(orange);
                else if (this.grade == "MAX")
                    setCustomColor(rouge);


            }
        }
    }

/*
    public void move2(List<Client> clients, List<Barman> autresBarmans, List<Bar> tables){


    }
        private boolean detectCollision(int autreX, int autreY, int minDistance) {
        double distance = Math.sqrt(Math.pow(autreX - x, 2) + Math.pow(autreY - y, 2));
        return distance < minDistance;
    }

 */

    @Override
    public void run() {
        while (running) {
            move(client, List.of(), List.of());

            System.out.println(exp + "/" + besoinExp + "  GRADE = " + grade + "    Adapt = " + adapt + "COULEUR " + customColor);

            try {
                Thread.sleep(50); // Pause pour ajuster la vitesse du mouvement
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Getters et Setters


    public Color getCustomColor() {
        return customColor;
    }

    public void setCustomColor(Color customColor) {
        this.customColor = customColor;
    }


    public double getExp() { return exp; }
    public void setExp(double exp) { this.exp = exp; }
    public double getAdapt() { return adapt; }
    public void setAdapt(double adapt) { this.adapt = adapt; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public int getMotiv() { return motiv; }
    public void setMotiv(int motiv) { this.motiv = motiv; }
    public List<Client> getClient() { return client; }
    public void setClient(List<Client> client) { this.client = client; }
    public int getInitialX() { return initialX; }
    public void setInitialX(int initialX) { this.initialX = initialX; }
    public int getInitialY() { return initialY; }
    public void setInitialY(int initialY) { this.initialY = initialY; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getTaille() { return taille; }
    public void setTaille(int taille) { this.taille = taille; }
}
