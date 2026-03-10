import java.util.*;




//___________AHMED_________________
public class Pathfinder {
    private static final int GRID_SIZE = 3;
    private static final int GRID_WIDTH = 1400;
    private static final int GRID_HEIGHT = 1000;
    private static final int MAX_ITERATIONS = 1000000000; // Limite le nombre d'itérations

    private static class Node implements Comparable<Node> {
        int x, y;
        Node parent;
        double g, h, f;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.f, other.f);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Node) {
                Node other = (Node) obj;
                return this.x == other.x && this.y == other.y;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static List<int[]> findPath(int startX, int startY, int endX, int endY,
                                       List<Bar> tables, List<Client> clients, List<Barman> barmans) {
        // Vérification des limites
        if (startX < 0 || startX >= GRID_WIDTH * GRID_SIZE ||
                startY < 0 || startY >= GRID_HEIGHT * GRID_SIZE ||
                endX < 0 || endX >= GRID_WIDTH * GRID_SIZE ||
                endY < 0 || endY >= GRID_HEIGHT * GRID_SIZE) {
            return null;
        }

        int startGridX = startX / GRID_SIZE;
        int startGridY = startY / GRID_SIZE;
        int endGridX = endX / GRID_SIZE;
        int endGridY = endY / GRID_SIZE;

        // Si le point de départ ou d'arrivée n'est pas valide, retourner null
        if (!isValidPosition(startGridX, startGridY, tables, clients, barmans) ||
                !isValidPosition(endGridX, endGridY, tables, clients, barmans)) {
            return null;
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>(100);
        Map<String, Node> closedSet = new HashMap<>();
        int iterations = 0;

        Node startNode = new Node(startGridX, startGridY);
        startNode.g = 0;
        startNode.h = heuristic(startGridX, startGridY, endGridX, endGridY);
        startNode.f = startNode.g + startNode.h;

        openSet.add(startNode);

        while (!openSet.isEmpty() && iterations < MAX_ITERATIONS) {
            iterations++;
            Node current = openSet.poll();
            String key = current.x + "," + current.y;

            if (current.x == endGridX && current.y == endGridY) {
                return reconstructPath(current);
            }

            closedSet.put(key, current);

            // Optimisation : ne considérer que les 4 directions principales
            int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];
                String newKey = newX + "," + newY;

                if (closedSet.containsKey(newKey)) continue;
                if (!isValidPosition(newX, newY, tables, clients, barmans)) continue;

                double tentativeG = current.g + 1.0;

                Node neighbor = new Node(newX, newY);
                neighbor.g = tentativeG;
                neighbor.h = heuristic(newX, newY, endGridX, endGridY);
                neighbor.f = neighbor.g + neighbor.h;
                neighbor.parent = current;

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }
            }
        }

        // Si aucun chemin n'est trouvé ou limite d'itérations atteinte, retourner null
        return null;
    }

    private static double heuristic(int x1, int y1, int x2, int y2) {
        // Distance de Manhattan
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }

    private static boolean isValidPosition(int x, int y, List<Bar> tables, List<Client> clients, List<Barman> barmans) {
        if (x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT) {
            return false;
        }

        // Optimisation : utiliser un buffer pour éviter les collisions trop proches
        int buffer = 2;

        for (Bar table : tables) {
            int tableX = 500 / GRID_SIZE;
            int tableY = 450 / GRID_SIZE;
            if (Math.abs(x - tableX) < buffer && Math.abs(y - tableY) < buffer) {
                return false;
            }
        }

        // Réduire la vérification des collisions avec les entités mobiles
        for (Client client : clients) {
            int clientX = client.getX() / GRID_SIZE;
            int clientY = client.getY() / GRID_SIZE;
            if (Math.abs(x - clientX) < buffer && Math.abs(y - clientY) < buffer) {
                return false;
            }
        }

        for (Barman barman : barmans) {
            int barmanX = barman.getX() / GRID_SIZE;
            int barmanY = barman.getY() / GRID_SIZE;
            if (Math.abs(x - barmanX) < buffer && Math.abs(y - barmanY) < buffer) {
                return false;
            }
        }

        return true;
    }

    private static List<int[]> reconstructPath(Node endNode) {
        List<int[]> path = new ArrayList<>();
        Node current = endNode;

        while (current != null) {
            path.add(0, new int[]{current.x * GRID_SIZE, current.y * GRID_SIZE});
            current = current.parent;
        }

        // Optimisation : limiter la taille du chemin
        if (path.size() > 50) {
            return path.subList(0, 50);
        }

        return path;
    }
}