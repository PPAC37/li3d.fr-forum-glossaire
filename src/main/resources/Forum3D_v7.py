#! /usr/bin/env python3.7

# import module
import urllib.request
from urllib.error import URLError, HTTPError
import os
from datetime import datetime
from datetime import date
import codecs
import webbrowser
import re

#-----------------------------------------
# DEFINITIONS / VARIABLES
#-----------------------------------------
ListeSsForum = []
ListeSujetSsReponse = []
ListeSsForumName = []
timeoutSpec = 10
NBJOUR = 10

#-----------------------------------------
# FUNCTION
#-----------------------------------------
def getDifference(then):
    
    now = date.today().strftime("%d/%m/%Y")
    duration = datetime.now() - then
    duration_in_s = duration.total_seconds() 
    
    #Date and Time constants
    day_ct = 24 * 60 * 60 			#86400

    return int(divmod(duration_in_s, day_ct)[0])

def CleanString(string):
    
    CleanedString = ''
    for car in string:
        if ((ord(car) < 255) and (ord(car) > 31)):
            CleanedString += car
            
    return CleanedString

#-----------------------------------------
# PROGRAMME PRINCIPAL
#-----------------------------------------

#-------------------------------------------------------------------------
# liste des sous-fora
#-------------------------------------------------------------------------
req = urllib.request.Request("https://www.lesimprimantes3d.fr/forum/")
sock1 = urllib.request.urlopen(req,timeout = timeoutSpec)
lines = sock1.readlines()
for line in lines :
    if (line.decode('utf-8').find('https://www.lesimprimantes3d.fr/forum/') != -1) and (line.decode('utf-8').find('dans') == -1):
        splitline1 = line.decode('utf-8').split('"')
        SsForumFound = False
        count = 0
        for string in splitline1 :
            count += 1
            #print("string:",string)
            if string.startswith('https://www.lesimprimantes3d.fr/forum/') and (len(string) > 38):
                #print("found string:",string," car suivant:",string[38])
                if ord(string[38]) > 47 and ord(string[38]) < 58:
                    SsForumFound = True
                    ListeSsForum.append(string)
                    splitline2 = line.decode('utf-8').split('>')
                    SsForumName = splitline2[1]
                    print("ss forum", string," Nom ssForum: ",SsForumName[:len(SsForumName)-3])
                    ListeSsForumName.append(SsForumName[:len(SsForumName)-3])
                    break
sock1.close()

#-------------------------------------------------------------------------
# recherche des sujets sans réponses dans les sous-fora
#-------------------------------------------------------------------------
LastSsForum = ''
SujetFound = ''
for index, SsForum in enumerate(ListeSsForum):
    SsForumAd = SsForum
    print('adresse ssForum: ',SsForumAd)
    req = urllib.request.Request(SsForumAd)
    try:
        sock2 = urllib.request.urlopen(req,timeout = timeoutSpec)
    except urllib.error.HTTPError as e:
        print("interdit")
        continue
    
    SsForumLines = sock2.readlines()
    
    # détermine le nombre de page du forum
    nbPageSsForum = 0
    for lines in SsForumLines:
        linesFormat = lines.decode('utf-8')
        if linesFormat.find('link rel="last"') != -1:
            SplitlinesFormat = linesFormat.replace('=',' ').replace('"',' ').split()
            try:
                nbPageSsForum = int(SplitlinesFormat[len(SplitlinesFormat)-2])
            except ValueError as ve:
                nbPageSsForum = 0
            break
    #print('nbPageSsForum = ', nbPageSsForum)
        
    # cherche les sujets sans réponse depuis 7 jours
    OldestDateNotReached = True
    CountPage = 0
    while (OldestDateNotReached):
        CountPage += 1
        #print("num Page = ",CountPage)
        TopicFound = False
        for lines in SsForumLines:
            linesFormat = lines.decode('utf-8')
            #print("linesFormat à analyser:",linesFormat)
            #print("Get TopicFound", TopicFound)
            if TopicFound == True:
                if linesFormat.find('datetime') != -1:
                    #print("datetime found")
                    StringSsForum = linesFormat.split()
                    for i, elem in enumerate(StringSsForum):
                        #print('element : ',elem,' indice=',i)
                        if 'title' in elem:
                            if getDifference(datetime.strptime(elem[7:], "%d/%m/%Y")) > NBJOUR:
                                TopicFound = False
                                OldestDateNotReached = False
            if TopicFound == True:            
                if linesFormat.find('ipsDataItem_stats_number') != -1:
                    #print("lines = ",lines)
                    TopicFound = False
                    StringSsForum = linesFormat.replace('>',' ').replace('<',' ').split()
                    #print("lines = ",StringSsForum)
                    for i, elem in enumerate(StringSsForum):
                        #print('element : ',elem,' indice=',i)
                        if 'ipsDataItem_stats_number' in elem:
                            break
                    nbreponse = StringSsForum[i + 1]
                    if int(nbreponse[0]) == 0:
                        if LastSsForum != SsForum:
                            LastSsForum = SsForum
                            print('Sous forum: ', ListeSsForumName[index])
                            newline = 'F' + CleanString(ListeSsForumName[index])
                            ListeSujetSsReponse.append(newline)
                        print('sujet sans reponse: ' , SujetFound, " Nom du Sujet ss reponse: " , SujetFoundName)
                        newline = 'S' + SujetFound
                        ListeSujetSsReponse.append(newline)
                        newline = 'N' + CleanString(SujetFoundName)
                        ListeSujetSsReponse.append(newline)
            if (linesFormat.find('https://www.lesimprimantes3d.fr/forum/topic/') != -1) and (linesFormat.find('data-ipsHover') != -1):
                splitline = linesFormat.split("'")
                for string in splitline:
                    if string.startswith("https://www.lesimprimantes3d.fr/forum/topic/"):
                        SujetFound = string
                        #print("SujetFound = ",string)
                        break
                mots = linesFormat.split("'")
                #print('x splité avec "',mots)
                count = 0
                for mot in mots:
                    #print('mot',count,'= ',mot)
                    if mot.find("title") != -1:
                        #print('trouvé')
                        break        
                    count += 1
                count += 1
                #print('title: ',mots[count])
                SujetFoundName = mots[count]
                TopicFound = True
                #print("set TopicFound", TopicFound)
        sock2.close()
        if ((nbPageSsForum == 0) or (CountPage >= nbPageSsForum)):
            OldestDateNotReached = False
        if (OldestDateNotReached and CountPage < nbPageSsForum):
            SsForumAd = SsForum + '?page=' + str(CountPage + 1)
            print('adresse ssForum: ',SsForumAd)
            req = urllib.request.Request(SsForumAd)
            try:
                sock2 = urllib.request.urlopen(req,timeout = timeoutSpec)
                SsForumLines = sock2.readlines()
            except urllib.error.HTTPError as e:
                print("interdit")
                OldestDateNotReached = False
                #break

#-------------------------------------------------------------------------            
# Creation du fichier HTML
#-------------------------------------------------------------------------
f = open('ListeSujetSansReponse.html', 'w')
texte = '<html><head></p>LISTE des SUJETS sans REPONSE depuis '+ str(NBJOUR) +' JOURS<p></head><body>'
for i,adresse in enumerate(ListeSujetSsReponse):
    StrAdresse = ''.join(map(str, adresse))
    #print('ListeSujetSsReponse Ligne(',i,'): ',StrAdresse)
    if StrAdresse.startswith('F'):  #affichage du lien du sous-forum
        texte += '<p>' + StrAdresse[1:] +'</p>'
    if StrAdresse.startswith('S'):  #affichage du lien du sujet
        texte += '<p><a href=' + StrAdresse[1:] + '>'
    if StrAdresse.startswith('N'):  #affichage du nom du sujet
        texte += StrAdresse[1:] + '</a></p>'

texte += '</body></html>'
html_template = ''.join(texte)
f.write(html_template)
  
# close the file
f.close()

# open html file
webbrowser.open('ListeSujetSansReponse.html')

print("FINI")
