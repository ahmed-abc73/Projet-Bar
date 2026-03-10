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

    

    private void setBesoinExp() {
        switch (grade) {
            case "MAX" -> this.besoinExp = 99999;
            case "Expert" -> this.besoinExp = 50;
            case "Intermediaire" -> this.besoinExp = 50;
            case "Debutant" -> this.besoinExp = 50;
        }
    }

    

    // Méthode pour augmenter l'expérience et gérer les changements de grade
    public void plusExp(int xp) {
        this.exp += xp + (xp * adapt);
        this.customColor = blanc;
        if (grade.equals("Debutant")){
            if (exp >= besoinExp){
                this.grade = "Intermediaire";
                setBesoinExp();
                this.exp = 1;
                this.customColor = Color.white;}
        }
        else if (grade.equals("Intermediaire")) {
            if (exp >= besoinExp){
                setBesoinExp();
                this.grade = "Expert";
                this.exp = 1;
                this.customColor = yellw;}
        } else if (grade.equals("Expert")){
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

    public synchronized void service(){

        

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

    

    

    public synchronized void move(List<Client> clients, List<Barman> autresBarmans, List<Bar> tables) {
        
        for (Client client : this.client) {
            if (client.getSoif() < 390) {
                if (this.grade.equals("Debutant"))
                    setCustomColor(blanc);
                else if (this.grade.equals("Intermediaire"))
                    setCustomColor(yellw);
                else if (this.grade.equals("Expert"))
                    setCustomColor(orange);
                else if (this.grade.equals("MAX"))
                    setCustomColor(rouge);

            }
        }
    }

    @Override
    public void run() {
        while (running) {
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
