###............Conception de jeu de PACMAN............###########
Ce projet a consisté en la réalisation dans le langage JAVA du jeu dit du « Pacman ».
Un personnage Pacman se deplace dans un labyrinthe afin de lui faire manger toutes les pacgommes...
Quatre fantommes s'y deplace pour devorer pacman.
Si un des fantomes touche le pacman alors le pacman perd une des ses trois vies. En plus des pacgommes classique (bleus), il existe aussi quatre pacgommes bonus (autre couleurs). Ces bonus ont un effet sur le pacman et/ou les fantˆomes et/ou le labyrinthe.

Environnement de Conception:
Pour le developpement du Pacman, l'Environnement utilisé est eclipse et conformement aux consignes, l'interface utilisé est le swing et l'application se repose sur le pattern state.....

Execution en ligne de commande:

Apres avoir telechargé le dossier zip , ouvrir un terminal et se mettre dans la racine du dossier , en tapant les  commandes suivantes :

javac -d classes src/com/pacman/*.java

java -cp classes com.pacman.Application