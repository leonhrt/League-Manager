La base de dades utilitzada durant aquest projecte ha estat firebase en comptes de SQL ja que ho hem vist més senzill per com ho voliem plantejar. 
Firebase no funciona per PK's ni FK's tot i així hem volgut similar algo semblant a SQL, el que fem és que per cada entitat tindrem el que vol 
semblar una PK que es representarà com un UUID, el que farem serà relacionar, en les relacions en les que hem cregut, com es faria amb una 
relació N:M, és a dir ens guardem mutuament el identificador del altre. 

Un inconvenient QUE CAL REMARCAR es que firebase ens obliga a tenir constructors buits i setters de tots els atributs que volem serialitzar
com també getters de tots aquests atributs que volem guardar a la base. Sabem que això TRENCA el principi d'encapsulament "TELL DON'T ASK" però
no hi havia manera de fer-ho d'altre forma. 
Un altre inconvenient que hi ha hagut és la representació de certs objectes en la base de dades els quals hem hagut de crear entitats exclusivament 
per a poder guardar-nos la informació al núvol. 
Com a últim s'ha hagut de definir alguns atributs com a Transient (atributs que no volem deserialitzar) ja que ens guardavem objectes que FireBase
no coneix i per tant no ens deixava.  

Les taules que hem cregut necessàries en aquest projecte són les següents: 

	- USER (representar els jugadors a la base)
	- TEAM (representar els equips a la base)
	- LEAGUE (representar les lligues a la base)
	- MATCH (representar un partit a la base) 
	- CALENDAR (representar una sèrie de partits a la base)
	- JOURNEY (representar una jornada a la base)
	- RANKING (representar les estadistiques d'un equip en una lliga a la base)

Esmentar que algunes de les taules tenen més pes que d'altres en la lògica ja que com hem esmentat

Com a tal, per a poder accedir-hi desde un projecte caldrà fer la lectura d'un fitxer .json el qual contindrà tota la informació necessària per
a poder establir la connexió. 

No s'afageix el .json ja que com a tal es troba dins del projecte. Adjuntariem link per a que es pogués visualitzar millor la base de dades, però
cal donar permissos. 


