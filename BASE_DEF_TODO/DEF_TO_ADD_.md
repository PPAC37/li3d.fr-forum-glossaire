
### "G-Code de fin" Ultimaker Cura
Extrait de 
https://www.lesimprimantes3d.fr/forum/topic/50965-compr%C3%A9hension-du-g-code-r%C3%A9solution-dun-probl%C3%A8me-de-fin-dimpression-for%C3%A7age-de-position-artillery-genius-v1/#comment-526981

exemple d'une capture d'ecran des "Paramètres de la machine" Ultimaker Cura v4.13.1? pour une "Artillery Genius" onglet "Imprimante"

https://www.lesimprimantes3d.fr/forum/uploads/monthly_2023_01/907588142_Capturedcran2023-01-02223127.png.ad54c6931fe23b2aeac7f0f0fc63c09c.png

"G-Code de fin"
~~~
G91; relative positioning
 G1 Z1.0 F3000 ; move z up little to prevent scratching of print
 G90; absolute positioning
 G1 X0 Y200 F1000 ; prepare for part removal
 M104 S0; turn off extruder
 M140 S0 ; turn off bed
 ;G1 X0 Y300 F1000 ; prepare for part removal
 M84 ; disable motors
 M106 S0 ; turn off fan
~~~

N.B. Ici ligne 7 mise en commentaire car demande un mouvement hors du plateau 
 firmware marlin g-code https://marlinfw.org/meta/gcode/

patterns de remplacement ( utilisable dans les g-code de début et de fin ) de Ultimaker Cura http://files.fieldofview.com/cura/Replacement_Patterns.html ( attention pas les même ni la même syntaxe avec PrusaSilcer/SuperSlicer )
~~~
{machine_width}	Machine Width	The width (X-direction) of the printable area.
{machine_depth}	Machine Depth	The depth (Y-direction) of the printable area.
{machine_height}	Machine Height	The height (Z-direction) of the printable area.
~~~

### G-code PrusaSlicer / SuperSlicer

SuperSlicer est un "fork" de PrusaSlicer

####
https://help.prusa3d.com/fr/article/informations-generales_1910

liste de variables disponibles dans le langage macro G-Code personnalisé dans PrusaSlicer.  https://help.prusa3d.com/fr/article/liste-des-variables_205643

Le langage macro est décrit en détail sur une page séparée. http://help.prusa3d.com/fr/article/macros_1775
####
https://github.com/5axes/SuperSlicer-FRDocumentation/blob/main/src/superslicer.md

les "G-Code personnalisé"
https://github.com/5axes/SuperSlicer-FRDocumentation/blob/main/src/filament_settings/filament_settings.md#g-code-personnalis%C3%A9

