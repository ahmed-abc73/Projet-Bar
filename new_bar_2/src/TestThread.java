import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



//_______________LEANDRE_________________

public class TestThread {

    public static void main(String[] args) {


        Random random = new Random();

        //__________________________PARTIE CREATION DES OBJETS ET THREADS________________________________


        int nombreClients = 30;
        int nombreBarmans = 2;


        // ET LE NOMBRE DE TABLES...


        List<Client> listObjClients = new ArrayList<>();
        List<Barman> listObjBarmans = new ArrayList<>();
        List<Bar> listObjTables = new ArrayList<>();


        int x = 150;
        int y = 200;
        int nb = 350;

        for (int i = 0; i < 6; i++) {
            if ((x + nb) < 1500) {
                x += nb;
            }
            else {
                x = 500;
                y += 450;
            }

            Bar tabl = new Bar(x, y, nombreClients, 0); // Adapte le constructeur

            listObjTables.add(tabl);


        }


        //Barmans
        for (int i = 0; i < nombreBarmans; i++) {
            double adapt = random.nextDouble() * 1;
            int motiv = random.nextInt(100);
            String grade = "Debutant";
            int initialX = 300;
            int initialY = random.nextInt(1000);

            Barman barman = new Barman(adapt, grade, motiv,  initialX, initialY); // Adapte le constructeur
            barman.setX(initialX);
            barman.setY(initialY);
            listObjBarmans.add(barman);

        }
        /*System.out.println("12345678 Position Y barman 0"+listObjBarmans.get(0).getInitialY());
        System.out.println("12345678 Position Y barman 1"+listObjBarmans.get(1).getInitialY());
        System.out.println("12345678 Position Y barman 2"+listObjBarmans.get(2).getInitialY());
        System.out.println("12345678 Position Y barman 3"+listObjBarmans.get(3).getInitialY());*/
// L'ASSOCIATION C'EST ICI ______________________________________________________

        //Clients
        if (nombreClients >= nombreBarmans) {
            int id = 0;
            for (int i = 0; i < nombreClients; i++) {
                int satisfaction = 0;
                double tolerance = random.nextDouble(); // Entre 0 et 10
                double alcool = 50;
                int soif = 200 + (int)(Math.random() * 301);
                String type = random.nextBoolean() ? "occasionnel" : "habitué";
                String genre = random.nextBoolean() ? "homme" : "femme";
                int initialX = random.nextInt(1400);
                int initialY = random.nextInt(1000);

                if (id >= listObjBarmans.size()) {
                    id = 0;
                }
                List<Barman> barmanL = new ArrayList<>();
                barmanL.add(listObjBarmans.get(id));

               // int targetY = barmanL.get(0).getInitialY(); //utilisationpour déplacement client vers barman

                Bar bar = listObjTables.get(random.nextInt(listObjTables.size()));

                Client client = new Client(satisfaction, tolerance, alcool, type, genre, barmanL, bar, initialX, initialY);
                client.setSoif(soif);

                listObjClients.add(client);



                Barman barmanL2 = barmanL.get(0);
                if (barmanL2.getClient() == null) {
                    // Si aucun client n'est associé, initialiser la liste des clients
                    barmanL2.setClient(new ArrayList<>());
                }

                // Ajouter le client à la liste des clients du barman
                barmanL2.getClient().add(client);

                id++;



            }
        }
        else{
            int satisfaction = 0;
            double tolerance = random.nextDouble() * 10; // Entre 0 et 10
            double alcool = 50;
            String type = random.nextBoolean() ? "occasionnel" : "habitué";
            String genre = random.nextBoolean() ? "homme" : "femme";
            int initialX = random.nextInt(1400);
            int initialY = random.nextInt(1000);
            int soif = 200 + (int)(Math.random() * 301);

            List<Barman> barmanL = new ArrayList<>();

            // Ajouter tous les barmans à la liste des barmans pour ce client
            for (int i = 0; i < nombreBarmans; i++) {
                barmanL.add(listObjBarmans.get(i));
            }
            //int targetY = barmanL.get(0).getInitialY(); //utilisation pour déplacement client vers barman

            Bar bar = listObjTables.get(random.nextInt(listObjTables.size()));

            Client client = new Client(satisfaction, tolerance, alcool, type, genre, barmanL, bar, initialX, initialY);
            client.setSoif(soif);

            listObjClients.add(client);

            // Associer ce client à tous les barmans
            for (Barman barman : barmanL) {
                if (barman.getClient() == null) {
                    barman.setClient(new ArrayList<>());
                }
                barman.getClient().add(client);
            }

        }

//__________________________________________________________________________________________

        Animation animation = new Animation(listObjClients, listObjBarmans, listObjTables);

        //_______LANCEMENT DES THREAD____________

        for (Client client : listObjClients)
        {
            Thread clientThread = new Thread(client);
            clientThread.start();
        }

        for (Barman barman : listObjBarmans)
        {
            Thread barmanThread = new Thread(barman);
            barmanThread.start();
        }

        //________________________________________________________________________________

//_________________CONFIGURATION DE LA MAP________________________

        JFrame frame = new JFrame("BAR");
        frame.add(animation);
        frame.setSize(2000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

//________________________________________________________________


/*
        // Joindre les threads
        try {
            for (Client client : listObjClients) {
                Thread clientThread = new Thread(client);
                clientThread.join();  // Joindre chaque thread client
            }
            for (Barman barman : listObjBarmans) {
                Thread barmanThread = new Thread(barman);
                barmanThread.join();  // Joindre chaque thread barman
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
