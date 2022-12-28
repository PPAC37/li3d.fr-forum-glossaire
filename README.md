# li3d.fr-forum-glossaire

Essais de gestion du glossaire https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/ et de solution pour l'ajout / la rédaction / mise a jours, de définitions de termes propre a l'impression 3D.

## Objectif 
* Permetre un travail collaboratif  ( cf GitHub les utilisateurs peuvent proposer des Pull Request (PR) de modification )
* Historique des versions des définitions ( cf le principe de l'historique de version d'un depot GIT )
* Automatisation de la generation d'un sommaire pour le sujet du forum
* Automatisation de la generation d'une version en un document HTML et ou PDF ( avec navigation interne ... )

## TODO
* [ ] Vérifier si la licence définie pour se dépot et adapté. ( Actuellement il serai Domaine public )
* [ ] Créer une action git pour regénérer le sommaire depuis l'outil de Yo' ( cf https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/?do=findComment&comment=478057 ) 
* [ ] Demander a Yo' si il veux bien faire un depot github de son outil ( les sources et une version release pour voir si l'on peut en faire une version sans GUI pour généré le sommaire via une Action github (dans un environnement ... win ? (mais je préfére Ubuntu ;) ) ) )
* [ ] Analyse des besoins

On stock 

les définitions importé depuis le sujet du forum dans ? li3d.fr_forum_gloassaire_cache ( des wget "brut / non connecté comme un utilisateur du forum" des pages du sujet ) 
~~~
wget https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/ -O li3d.fr_forum_glossaire_page1.html

// TODO revoir si on peut pas faire plus simple pour obtenir le nombre de page du sujet."
grep -o -m 1 -e " rel=\"last\" data-page='[[:digit:]]'" li3d.fr_forum_glossaire_page1.html | grep -o -e "[[:digit:]]*"
3
// TODO dans une var pour un test dans un while de wget des pages ...
wget https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/page/2/ -O li3d.fr_forum_glossaire_page2.html
wget https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/page/3/ -O li3d.fr_forum_glossaire_page3.html
~~~


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

* [ ] Outils d'import des definitions existante de https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/ ( un outil ( un genre de parseur "html to md") qui transforme les commentaires du sujet en arborécences repertoires/fichier(s) .md )




## LES DEFINITIONS A FAIRE
( cf https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/?do=findComment&comment=478058 )
~~~
Propositions de membres

HeatBreak (Pas de def) (Bore, Metal, BiMetal)
Brise chaleur (Pas de def)
HeatBlock (Pas de def)
Bloc de chauffe (Pas de def) (E3DV6, Volcano, ...)
Nozzle (Pas de def)
Buse (Pas de def)
Radiateur ? (Pas de def)
PTFE (Pas de def)
FanDuct (Pas de def)
Conduit de ventilation (Pas de def)
4010/4020/5010/5020/... : (Pas de def)  dimension standardisée des ventilateur
Bed (Pas de def)
Plateau (Pas de def)
Thermistance (Pas de def)
MOSFET (Pas de def)
ADC (Pas de def)
DAC (Pas de def)
IO (Pas de def)
CPU (Types) (Pas de def)
Arduino (Pas de def)
VSCode (Pas de def)
PEI (Pas de def)
BuildTak (Pas de def)
UltraBase (Pas de def)
Rail Linéaire (Pas de def)
Pneufit (Pas de def)
EndStop (Pas de def)
Titan (Pas de def)
BMG (Pas de def)
BLTouch (3DTouch, ...) (Pas de def)
Capteur à induction (Pas de def)
Capteur Capacitif (Pas de def)
Touch Mi (Pas de def)
Courroies (Pas de def)
Belt (Pas de def)
Poulies (Pas de def)
Vis sans fin, vis à plomb, Lead (Pas de def)
Nema (14/17/23/...) (Pas de def)
LCD (Pas de def)
Tactile (Pas de def)
Marlin (Pas de def)
Klipper (Pas de def)
BackSlash (Pas de def)
Anti BackSlash (Pas de def)
Octoprint (Pas de def)
Fluidd (Pas de def)
MKS (Pas de def)
BTT (Pas de def)
Delta (Pas de def)
Cartesienne (Pas de def)
CoreXY (Pas de def)
CroXY (Pas de def)
Gates (Pas de def)
Délamination (Pas de def)
PT100 (Pas de def)
PT1000 (Pas de def)
NTC (Pas de def)
Capteurs de température (Pas de def) renvoi vers PT100/PT1000/NTC
Cartésienne (Pas de def)
Delta (Pas de def)
Cubique (Pas de def)
~~~
