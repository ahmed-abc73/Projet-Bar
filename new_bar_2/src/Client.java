import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//_______________FLORIENT_________________

public class Client implements Runnable {

    private volatile boolean running = true;

    private double tolerance, directionX = 0, directionY = 0;
    private volatile double alcool;
    private int satisfaction, soif, initialX, initialY, x, y, speedX = 3, speedY = 3, posX, posY, indiceBarman;
    private String type = "occasionnel", genre;
    private final Random rand = new Random();
    private final int windowWidth = 1400, windowHeight = 1000;
    private final double visionAngle = Math.toRadians(70);
    private final int visionDist = 30;
    private List<Barman> barman = new ArrayList<>();
    private Bar bar;
    private Animation animation;
    private List<int[]> path;
    private int pathIndex = 0;
    private int taille;
    private Color vert = new Color(0, 100, 0);
    private Color bleu = new Color(0, 100, 200);
    private Color gris = Color.WHITE;
    private Color rouge = new Color(100, 0, 0);
    private Color customColor;
    private int targetY;
    private boolean servi;
    private boolean attService;
    private boolean goTabl;
    private boolean fini;

    public Client(int satisfaction, double tolerance, double alcool, String type, String genre, List<Barman> barman, Bar bar, int initialX, int initialY) {
        this.satisfaction = satisfaction; // Entre 0 et 100
        this.tolerance = tolerance; // Entre 0 et 1, CHANGEMENT ; faire en sorte que ce soit la tolerance a l'alcool
        this.alcool = alcool; // Entre 0 et 100
        this.type = type; // Soit occasionnel et ou habitué
        this.genre = genre; // homme/femme
        this.soif = 200;
        this.taille = 30;

        this.bar = bar;
        this.barman = barman;
        this.initialX = initialX;
        this.initialY = initialY;

        this.x = initialX;
        this.y = initialY;

        this.posX = posX;
        this.posY = posY;

        this.animation = animation;
        this.customColor = gris;

        this.indiceBarman = 0;

        this.targetY=targetY;
        this.servi = false;
        this.attService = false;
        this.goTabl = true;
        this.fini = false;
    }

    public void retour(int idBarman){
        if (satisfaction >= 100) {
            barman.get(idBarman).plusExp(50);
            barman.get(idBarman).setCustomColor(rouge);}

        if (satisfaction >= 200) {
            bar.plusExp();}

        if (barman.get(indiceBarman).getGrade().equals("MAX")){
            setIndiceBarman(rand.nextInt(barman.size()));
        }

        satisfaction = 20;

    }
    public void alcoolPlus(){

        this.soif += 300;
        this.customColor = gris;
        if (this.alcool < 100) {
            if (this.alcool + 10 <= 100) {
                this.alcool += 20 - (10 * this.tolerance);
                this.satisfaction += 300;
                retour(this.indiceBarman);
                barman.get(indiceBarman).setCustomColor(vert);
                embrouille();
                }
            else
                this.alcool = 100;}}
    public void alcoolMoins(){
        if (this.alcool > 0)
            this.alcool -= 0.1;}

    public void embrouille(){
        if (this.alcool > 80){
            int chance = rand.nextInt((int) (20 - (100 - this.alcool)));
            if (chance == 1){
                barman.get(getIndiceBarman()).moinsMotiv();
                barman.get(getIndiceBarman()).setGrade("Debutant");
                this.alcool = 1;
            }
        }
    }

    public boolean estDansChampDeVision(int cibleX, int cibleY) {
        double distance = Math.sqrt(Math.pow(cibleX - x, 2) + Math.pow(cibleY - y, 2));
        if (distance > visionDist) return false;
        double angleVersCible = Math.atan2(cibleY - y, cibleX - x);
        double angleClient = Math.atan2(directionY, directionX);
        double diffAngles = Math.abs(angleClient - angleVersCible);
        return diffAngles <= visionAngle / 2;
    }
    private void setRandomDirection() {
        directionX += rand.nextGaussian() * 0.3;
        directionY += rand.nextGaussian() * 0.3;
        double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
        directionX /= magnitude;
        directionY /= magnitude;
    }
    private boolean detectCollision(int positionAutreObjX, int positionAutreObjY, int minDistance) {
        double distance = Math.sqrt(Math.pow(positionAutreObjX - x, 2) + Math.pow(positionAutreObjY - y, 2));
        return distance < minDistance;
    }

    public void move(List<Client> autreClients, List<Barman> barmans, List<Bar> tables) {
        if (goTabl) {
            this.customColor = Color.white;
            this.soif -= 1;
        }

        if (this.soif == 100) {
            this.targetY = barman.get(indiceBarman).getY();
            this.goTabl = false;
            this.attService = true;
            this.customColor = Color.yellow;
            if (path == null || pathIndex >= path.size()) {
                int newTargetX = 400;
                int newTargetY = targetY;
                path = Pathfinder.findPath(x, y, newTargetX, newTargetY, tables, autreClients, barmans);
                pathIndex = 0;
            }
        }

        //System.out.println("position x et y"+this.x+" "+this.y+"position cible"+(this.targetY)+"table cible x y"+this.bar.getX()+" "+this.bar.getY());

        // CONTACT AVEC BARMAN
        if (attService && !servi) {
            if ((this.x == 400) && Math.abs(this.y-this.targetY) <= 3) {

                this.servi = true;
                this.attService = false;
                barman.get(0).service();
                this.customColor = Color.green;

            }
        }

        //CHERCHE TABLE
        if (!goTabl && servi && !attService) {

            if (path == null || pathIndex >= path.size()) {
                int cibleTableX = this.bar.getX();
                int cibleTableY = this.bar.getY();
                this.path = Pathfinder.findPath(x, y, cibleTableX, cibleTableY, tables, autreClients, barmans);
                this.pathIndex = 0;
            }
        }

        //CONTACT TABLE
        if (!this.goTabl && this.servi && !this.attService) {
            if ((Math.abs(this.x-this.bar.getX())<=3) && Math.abs(this.y-this.bar.getY())<=3) {
                this.goTabl = true;
                this.servi = false;
                this.fini = true;
                this.customColor = bleu;
            }
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
                y += (int)(dy * ratio);
            }
        }

        if (!attService && goTabl) {
            setRandomDirection();
            x += directionX * speedX;
            y += directionY * speedY;
        }

        // Garder la logique existante pour les collisions et les bords de la fenêtre
        if (x <= 400) {
            x = 400;
            directionX = -directionX;}
        if (x >= windowWidth) {
            x = windowWidth;
            directionX = -directionX;}
        if (y <= 0 || y >= windowHeight - 50) {
            directionY = -directionY;}

        for (Bar table : tables) {
            if (detectCollision(table.getX(), table.getY(), 50)) {
                directionX = -directionX;
                directionY = -directionY;}}

        for (Client autre : autreClients) {
            if (detectCollision(autre.getX(), autre.getY(), 10)) {
                directionX = -directionX;
                directionY = -directionY;}

        if (alcool >= 80){
            setCustomColor(bleu);
        }
    }
    }

    // Méthode qui gère l'exécution du thread pour le mouvement et la gestion de la soif
    @Override
    public void run() {
        while (running) {

            //soifMoins();
            alcoolMoins();

            System.out.println("----------------------");
            System.out.println("Soif: " + soif);
            System.out.println("exp du bar: " + bar.getExpBar());
            System.out.println("satisfaction: " + satisfaction);
            System.out.println("alcoolemie: " + alcool);
            System.out.println("pos client"+initialX);
            System.out.println("position x du bar"+bar.getX());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Synchronisation des actions de mouvement
    public synchronized void move2() {
        // Logique de pathfinding ou d'autres mouvements spécifiques
    }

    // -------------------------- Getters et Setters --------------------------

    public Color getCustomColor() {
        return customColor;
    }

    public void setCustomColor(Color customColor) {
        this.customColor = customColor;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public double getAlcool() {
        return alcool;
    }

    public void setAlcool(double alcool) {
        this.alcool = alcool;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<Barman> getBarman() {
        return barman;
    }

    public void setBarman(List<Barman> barman) {
        this.barman = barman;
    }

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public int getInitialX() {
        return initialX;
    }

    public void setInitialX(int initialX) {
        this.initialX = initialX;
    }

    public int getInitialY() {
        return initialY;
    }

    public void setInitialY(int initialY) {
        this.initialY = initialY;
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

    public int getSoif() {
        return soif;
    }

    public void setSoif(int soif) {
        this.soif = soif;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getIndiceBarman() {
        return indiceBarman;
    }

    public void setIndiceBarman(int indiceBarman) {
        this.indiceBarman = indiceBarman;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isServi() {
        return servi;
    }

    public void setServi(boolean servi) {
        this.servi = servi;
    }

    public boolean isAttService() {
        return attService;
    }

    public void setAttService(boolean attService) {
        this.attService = attService;
    }
}
