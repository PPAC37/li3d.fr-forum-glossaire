# li3d.fr-forum-glossaire

Essais de gestion du glossaire https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/ et de solution pour l'ajout / la rédaction / mise a jours, de définitions de termes propre a l'impression 3D.

Objectif / 
* Permetre un travail collaboratif  ( cf GitHub les utilisateurs peuvent proposer de Push Request de modification )
* Historique des versions des définition ( cf le principe de l'historique de version d'un depot GIT )
* Automatisation de la generation d'un sommaire pour le sujet du forum
* Automatisation de la generation d'une version en un document HTML et ou PDF ( avec navigation interne ... )

TODO
* [ ] Vérifier si la licence définie pour se dépot et adapté. ( Actuellement il serai Domaine public )
* [ ] Créer une action git pour regénérer le sommaire depuis l'outil de Yo' ( cf https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/?do=findComment&comment=478057 ) 
* [ ] Demander a Yo' si il veux bien faire un depot github de son outil ( les sources et une version release pour voir si l'on peut en faire une version sans GUI pour généré le sommaire via une Action github (dans un environnement ... win ? (mais je préfére Ubuntu ;) ) ) )
* [ ] Analyse des besoins

On stock 

les définitions importé depuis le sujet du forum dans ? li3d.fr_forum_cache ( des wget ou curl ou ? des pages du sujet avec les log des wget ? ) 


On travaille dans le répertoire "glossaire_definition"

? Pour une définition, un répertoire qui contien un README.md Source.md images/

? Quand on a des alias pour une même définition les README.md des alias ne contien que 
~~~
voir <lien vers le fichier README.MD du terme principal pour cette alias>
~~~

...

* [ ] Outils d'import des definition existante de https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/ ( un outil ( un genre de parseur ) qui transforme les commentaires du sujet en fichier(s) .md )




LES DEFINITIONS A FAIRE

