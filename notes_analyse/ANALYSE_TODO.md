
## Analyse de l'existant 

Le lexique du blog "L'impression 3D pour les nuls (lexique)" https://www.lesimprimantes3d.fr/impression-3d-pour-les-nuls/  normalement repris / adapté dans le sujet du glossaire du forum ) 

Le sujet du Glossaire du forum https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/

L'index de la traduction fr de la docummentation de SuperSlicer sur le dépot de 5axes https://github.com/5axes/SuperSlicer-FRDocumentation/blob/main/src/glossary/glossary.md

Les divers autre glossaires que lon peut trouver sur internet


### Structure de données

#### Commentaire du sujet Glossaire du forum



   Un auteur du commentaire
   Une date de création du commentaire   
   Dans le coprs du ccommentaire 
      En début de commentaire ( en liste a puces )  le therme et posible alias ( en gras ... Pb si mal formate cf deux strong dans un li)
      La définition ( du HTML avec les limitations due a la saise / collage dans l'éditeur du forum ex: pas d'attribut id= pas de form ni de input ...)
   En fin du Commentaire 
      Une éventuel date de modification/édition du commentaire ( Nom de l'auteur de la modification et éventuel description de la modification )
      
      
  


#### 
 Si l'on se base sur le "Nom des pièces - Impression 3D.pdf" de www.youtube.com/Legueroloco  https://drive.google.com/file/d/1uKoNY9o2yDzYY7h8WyQHAW69CK6iIXCz/view
 Un tableau avec les colones
    
    Image
    Noms français
    Noms anglais
    Description sommaire
    
    
    
## Les alias et traduction
! (fr)
? (en)

    
    
## les acronymes
Faut t'il faire usage du tag HTML pour les acronymes ?

## les renvoies
Comment bien gérer les renvoies aux définition


## les contrainte de formatage pour l'outil de generation de sommaire depuis les commentaire du sujet Glossaire

! puces gras

voir https://www.lesimprimantes3d.fr/forum/topic/45962-cr%C3%A9ation-dun-glossaire-de-limpression-3d/?do=findComment&comment=479350


## Solutions (Disponibilité et contrainte)

De manière naive un service en ligne
 * sera la solution pour la collaboration
 * sera un problème quand on cherche a consulter / travailler hors connexion (il n'y a plus de connexion internt)
 * Dépand de la pérénité / légalité du service ( si un jours le service ferme, coupe l'accées, ou se trouve interdit )

Un dépot git hébergé sur github permet 
 * de profiter d'un service en ligne (github)
 * et donne la posibilité de travailler sur une copie du dépot git en ligne ou en local et de synchronisuer les modifications entre les dépots.
 * permetre des automatisation de traitement ( publication d'une page web, realease d'une documentation et ou d'une build d'un projet de programmation C/C++/Java/...) 
 * mais pas simple pour un débutants (énorme base de connaissance a aquérire). ( notion de versionning, de codage programmation langage html markdown ... spécificité de github, .... posibilitées de github , .... ) 


Un google doc 
  * en ligne 
  * avec posibilité de sauver une version en local
  * ? version
  * ? synchonisation
  * ? droits ( propriété intelectuel du format, légalité, ... )
  * ...

Un sujet sur un forum 
  * en ligne
  * collaboratif ( les utilisateurs qui peuve modifier / commenter ) 
  * suviie de version relatif au moteur du forum
  * limitation de format ( l'édition ... reformatage )
  * lourdeur du service ( on recharge une page compléte quand on passe d'une def a l'autre )
  * lourdeur de la maintenabilité ( un modérateur ...
  * lourdeur ( duréé et temps de travail ) pour y demander / faire une modification sur une définition ( citer(mais vérouillé) donc lien vers le commentaire de la def, proposer la modif a faire dans le sujet de Création du glossaire,... travalle collaboratif ... , un modérateur doit ensuite décider de tenir a jours le glossaire et les def d'aprés tout se travail (demande un suivie actif de l'auteur de la proposition des participants des modérateur ...). )  
  * ...
   
Un wiki
  * ? herbergement 
  * ? dificulté du maintien surtout si on passe au multilang ( tellement de version de moteur de wiki, est de plugin selon chacun, et pas forcement de truc facile pour migrer/sauver/restorer ... )
  * ? posiblilité des faire des export .pdf ou .html d'une page
  * ...

Wikidefinition ?
  * Compléxité ?

Un Blog 
  * ? reviens a un forum ?

Un site web ( LAMP / WAMP ou autre solution serveur web + sgbd )
  * ? cout et complexité de mise en place de la sécurisation et de maintien ...
  * 


---



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
le sommaire du sujet https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/#comment-478055

le repertoire de travaille du github

### Les définitions a revoir ou en cours de modification / création sur le forum 
https://www.lesimprimantes3d.fr/forum/topic/45962-cr%C3%A9ation-dun-glossaire-de-limpression-3d/?sortby=date#comments


### [Liste des definition a faire](./DEF_TODO.md) 


   
   

