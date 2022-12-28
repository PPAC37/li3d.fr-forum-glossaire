#!/bin/bash
URL_SUJET_GLOSSAIRE=https://www.lesimprimantes3d.fr/forum/topic/45754-glossaire-de-limpression-3d/
DEST_FILES_PREFIX=li3d.fr_forum_glossaire_page
wget $URL_SUJET_GLOSSAIRE -O ${DEST_FILES_PREFIX}_1.html
# // TODO revoir si on peut pas faire plus simple pour obtenir le nombre de page du sujet.
TMP_CPT_MAX=`grep -o -m 1 -e " rel=\"last\" data-page='[[:digit:]]'" ${DEST_FILES_PREFIX}_1.html | grep -o -e "[[:digit:]]*"`
for (( TMP_CPT=2 ; TMP_CPT<=$TMP_CPT_MAX ; TMP_CPT++ )) ; do wget ${URL_SUJET_GLOSSAIRE}/page/$TMP_CPT/ -O ${DEST_FILES_PREFIX}_$TMP_CPT.html ; done

#wget ${URL_SUJET_GLOSSAIRE}/page/2/ -O ${DEST_FILES_PREFIX}_2.html

