S'ha usat Intelij com a IDE com s'ha ensenyat a l'assignatura ja que ens facilita el poder treballar amb programació orientada a objectes. 
En quant a la base de dades s'ha fet servir FireStore en comptes de SQL, en el readme de la base de dades, s'entra més en detall sobre aquesta.  

Per baixar-se el projecte simplement es baixa com si fos un projecte normal d'Intelij, la base de dades es crearà ja al llegir del JSON que tenim dins del projecte el qual conté la informació necesària per a establir la conexió. 

Només executar el programa ens demanarà que iniciem sessió, per a poder fer servir els serveis d'administrador caldrà ficar admin com a email o dni i LeagueManagerC11 com a contrasenya, aquesta es podrà canviar sempre que es volgui desde el config.json. Si es vol accedir com a usuari caldrà ficar el dni o email junt amb la contrasenya d'alguns dels usuaris creats a la base de dades. Qualsevol usuari pot recuperar la contrasenya també desde la pantalla principal. 

El admin podrà executar les funcions de crear team, crear lliga i eliminar tant teams com lligues, també serà capaç de poder veure tots els equips i lligues del sistema, en canvi el player només podrà veure partits i lligues a les quals els equips on juga juguen aquelles lligues. En la creació de les lligues caldrà posar com a mínim dos equips, i en la creació d'un equip caldrà seleccionar un fitxer .json desde els fitxers del ordinador. 

Per cada lliga es podrà veure les estadístiques d'aquesta junt amb els detalls de cada equip els quals hi participen. A part també es podrà veure un gràfic que representa els punts de cada equip en aquesta. Tant la taula d'estadístiques com el gràfic s'actualitzen a temps real cada vegada que s'acaba un partit. 

En quant a la simulació dels partits, aquests s'actualitzen a temps reals i si cliques sobre ells et permeten veure actualitzacions sobre aquests també en temps real. 

Tant l'admin com l'usuari com podrà sortir del sistema fent el logout, en fer-lo el sistema tornarà a la pantalla principal on es demana l'inici de sessió. 
 