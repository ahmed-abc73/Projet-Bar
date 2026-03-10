import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

//_______________LEANDRE_________________

public class Animation extends JPanel implements ActionListener {
    private List<Client> clients;
    private List<Barman> barmans;
    private List<Bar> bar;
    private Timer timer;
    private Image clientImage;
    private Image barmanImage;
    private Image barImage;
    private Image backgroundImage;

    public Animation(List<Client> clients, List<Barman> barmans, List<Bar> bars) {
        this.clients = clients;
        this.barmans = barmans;
        this.bar = bars;
        timer = new Timer(20, this); // Intervalle de temps ajusté pour fluidité
        timer.start();

        try {
            backgroundImage = ImageIO.read(getClass().getResource("solBar.png"));
            barmanImage = ImageIO.read(getClass().getResource("barmanBar.png"));
            clientImage = ImageIO.read(getClass().getResource("clientBar.png"));
            barImage = ImageIO.read(getClass().getResource("tableBar.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Client client : clients) {
            new Thread(client).start();
        }
        for (Barman barman : barmans) {
            new Thread(barman).start();
        }


    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, 1500, getHeight(), this);

        // Dessiner chaque client à sa position
        for (Client client : clients) {
            g.setColor(client.getCustomColor());
            g.fillRect(client.getX(), client.getY(), client.getTaille(), client.getTaille() + 10);
            g.drawImage(clientImage, client.getX(), client.getY(), 50, 50, this);

            //FAIRE EN SORTE QUE QUAND LA SATISFACTION == 0, LE CLIENT DISPARAIT
        }

        for (Barman barman : barmans) {
            g.setColor(barman.getCustomColor());
            g.fillRect(barman.getX(), barman.getY(), barman.getTaille(), barman.getTaille() + 10);

            g.drawImage(barmanImage, barman.getX(), barman.getY(), barman.getTaille(), barman.getTaille() + 10, this);
        }

        for (Bar bar : bar) {
            g.drawImage(barImage, bar.getX(), bar.getY(), 100, 100, this);
        }
    }
    private void drawBarman(Graphics g, int x, int y, int taille, Color couleur){
        g.fillRect(x, y, taille, taille + 10);
        g.setColor(couleur);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
                // Déplacement et gestion des collisions pour les clients
        for (Client client : clients) {
            client.move(clients, barmans, bar);
        }

        // Déplacement et gestion des collisions pour les barmans
        for (Barman barman : barmans) {
            barman.move(clients,barmans, bar);
        }





        repaint();

    }

   /* @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(20); // Délai pour réguler la vitesse d'animation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            actionPerformed(null); // Déclenche le cycle d'animation
        }
    }*/

    public void removeClient(Client client) {
        clients.remove(client);
    }
}
