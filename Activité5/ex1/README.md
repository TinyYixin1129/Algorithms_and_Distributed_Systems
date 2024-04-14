## Sommer

Un programme Go qui réalise la somme en parallèle d'un tableau de nombres

## Architecture

1. Une fonction sommer qui reçoit un tableau de Int et un chanal, elle calcule la somme de tableau et envoie le résultat via le chanal.
2. Dans la fonction main, recevoir un argument de nombre de la ligne de commande, c'est le nombre de taille du tableau. Puis créer le tableau avec les nombres entiers consécutifs.
3. Récupérer le nombre de processeurs disponibles et calculer la taille du slice pour chaque goroutine. Par exemple, si on a 2 CPU: CPU1 et CPU2, et on a 7 nombre, 7/2=3..1, si chaque CPU récupère 3 nombre, on a besoin de 1 plus. Donc s'il n'est pas divisible, on ajoute un nombre sur chaque CPU
    ```
    slice_size := length / nbcpu
	if length%nbcpu != 0 {
		slice_size++
	}
    ```
4. Lancer la fonction dans un certain nombre de goroutine selon le nombre de processeurs disponibles et la taille de slice calculé.
5. Attendre tous les fonctions sont terminées et sommer les résultats.

## 
## Démarrage du programme
Taille du table est 100 par défaut
```
    go build ex1.go
    ./ex1 -n 1000
```

## Parallélisme
Nous laissons autant de processeurs que possible calculer simultanément et répartissons les nombres aussi uniformément que possible. Donc parallélisme implémenté