
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



   
   

