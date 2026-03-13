# Projet-Bar

Simulation d un bar avec des clients, des barmans et un pathfinding A*.
Projet realise dans le cadre du cours de L3 MIASHS.

## Description

Le programme simule un bar dans lequel :
- Des **clients** se deplacent librement, puis vont commander au bar quand ils ont soif
- Des **barmans** les servent et gagnent de l experience
- Le deplacement utilise l algorithme **A*** pour trouver le chemin optimal
- L ensemble tourne avec des **threads Java** (un par client, un par barman)

## Structure du projet

```
src/
├── Main.java          # Point d entree
├── TestThread.java    # Creation des objets et lancement de la simulation
├── Animation.java     # Interface graphique (Swing)
├── Client.java        # Thread client : deplacement, soif, alcooemie
├── Barman.java        # Thread barman : grades, experience, service
├── Bar.java           # Tables du bar
└── Pathfinder.java    # Algorithme A* de recherche de chemin
```

## Fonctionnalites

- Pathfinding **A*** pour le deplacement des clients vers le barman et vers leur table
- Systeme de **grades** pour les barmans (Debutant -> Intermediaire -> Expert -> MAX)
- Gestion de l alcooemie et de la satisfaction des clients
- Animation graphique avec images personnalisees
- Gestion des collisions entre entites

## Lancer le projet

### Avec IntelliJ IDEA
Ouvrir le projet et lancer Main.java ou TestThread.java.

### En ligne de commande
```bash
javac -d out/ src/*.java
java -cp out/ Main
```

## Auteurs

- **Ahmed Bekakria**
- **Florient**
- **Leandre**
