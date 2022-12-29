# li3d.fr-forum-glossaire

Essais de gestion du glossaire https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/ et de solution pour l'ajout / la rédaction / mise a jours, de définitions de termes propre a l'impression 3D.

## Objectif 
* Permetre un travail collaboratif  ( cf un utilisateur github, peut faire un fork du dépot pour y faire des modifications via des commit qui pouront etre proposé en Pull Request (PR) de modification vers le dépot d'origine )
* Historique des versions des définitions ( cf le principe de l'historique de version d'un depot GIT )
* [ ] Automatisation de la generation d'un sommaire pour le sujet du forum
* [ ] Automatisation de la generation d'une version en un document HTML et ou PDF ( avec navigation interne ... )

## TODO
* [ ] Vérifier si la licence définie pour se dépot et adapté. ( Actuellement il serai Domaine public )
* [ ] Créer une action git pour regénérer le sommaire depuis l'outil de Yo' ( cf https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/?do=findComment&comment=478057 ) 
* [ ] Demander a Yo' si il veux bien faire un depot github de son outil ( les sources et une version release pour voir si l'on peut en faire une version sans GUI pour généré le sommaire via une Action github (dans un environnement ... win ? (mais je préfére Ubuntu ;) ) ) )
* [ ] Analyse des besoins

On stock 

les définitions importé depuis le sujet du forum dans ? li3d.fr_forum_gloassaire_cache ( via un script qui fait des wget "brut / non connecté comme un utilisateur du forum" des pages du sujet. voir https://github.com/PPAC37/li3d.fr-forum-glossaire/blob/main/bash_sh/getForumSujetGlossairePages.sh ) 


On travaille dans le répertoire "glossaire_definition"

? Pour une définition, 
* un répertoire ayant comme nom le terme pour la définition et qui contien 
  * un README.md (la définition en elle même), 
  * Source.md (Si il y a des partie ou image d'autre provenance) 
  * et eventuellement un répertoire images/

? Quand on a des alias pour une même définition les README.md d'un alias ne contien que 
~~~
# <l'alias>
voir [<terme principal pour cette alias>](lien vers le fichier README.MD du terme principal pour cette alias)
~~~

...

* [ ] Outils d'import des definitions existante de https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/ ( un outil ( un genre de parseur "html to md") qui transforme les commentaires du sujet en arborécences repertoires/fichier(s) .html et ou .md )

* [ ] Un script bash linux qui automatise la "mise en cache" de la version du forum
* [ ] html to md ( voir pour le faire en java ou si posible d'utiliser des service en ligne de html to md )

* [ ] https://github.com/marketplace/actions/create-issue-from-file avec un script qui compare la version en "cache" du forum sur se dépot avec la version en ligne et créé un fichier des différences, dont le contenue sera remonté comme une issue en cas de différences ?

## L'outil de création de Sommaire de Yo'
Message masqué uniquement visible pour les daministrateurs / modérateurs du forum https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/?do=findComment&comment=478057

~~~
2) Générer le sommaire (tuto en cours de création)

A) Créer un dossier 'de travail' sur votre PC (peu importe le nom et le chemin)

B) Enregistrer toutes les pages du sujet dans le dossier de travail.

C) Télécharger et lancer le programme "sommaire.exe" Sommaire.exe(en cours de développement)

D) Un premier fichier code.txt est créé (qui n'est plus vraiment utile) , contenant tous les noms définis (est considéré comme nom un groupe de lettre en gras situé après une puce) classés par ordre alphabétique.

Un second fichier codeHTML.txt est créé. Il contient le code pour mettre en forme le message.
~~~

## LES DEFINITIONS


### Liste des définition faite
le sommaire du sujet 

le repertoire de travaille du github

### Les définitions a revoir ( 


### Les définition en cours de modification / création sur le forum 


### [Liste des definition a faire](DEF_TODO.md) 
