## Cribe

Un programme Go qui recherche les nombres premiers en utilisant le crible de Hoare

## Architecture

1. Une fonction **filter** qui commence par un Prime, elle reçoit les nombre à filtre dans un canal, wg pour synchronisation, et envoie le Prime suivant vers un canal de résultat
2. Dans cette fonction, elle vérifie si les nombres reçu sont un multiple du nombre premier de départ. Sinon, le premier sera le nombre premier de départ de filter prochain, et envoyer les reste au filter prochain pour les filtrer. Si oui, elle ne fait rien comme les supprime. Ensemble Des nombre premier de départ sera le résultat.
3. Dans la fonction main, recevoir un argument de nombre de la ligne de commande, c'est le nombre jusqu'à lequel on va filtrer.
4. wg est pour attendre jusqu'à le filtre prochain sera terminé.
5. On envoie les nombre de 3 jusqu'à le nombre saisi dans le canal pour que le premier filter(2) peut les filtrer.
6. Après les filtres sont terminé, on affiche les résultats.

##

## Démarrage du programme

Le nombre est 100 par défaut

```
    go build ex2.go
    ./ex2 -n 1000
```

## Parallélisme

Différent que ex1, on ne crée pas certain nombre de goroutine, mais on en crée un lorsque on trouve un nombre de premier. C'est un algorithme récursif qui est dynamique et vérifie parallélisme.
