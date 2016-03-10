Viikolla toteutin ArrayListin ja PriorityQueuen minimikeolla. PriorityQueue olisi yhtä hyvin voinut olla maksimikeko,
sillä luokalle voi antaa itsemäärittelemänsä Comparator-olion, jonka vuoksi PriorityQueuea voi kohdella kumpana tahansa
(minimi- tai maksimikekona).

ArrayListin ja PriorityQueuen lisäksi toteutin yhden Runnerin, joka etsii lyhimmän polun lähimpään maaliin vähän muunneltua
A* algoritmia käyttäen. "Vähän muunnellulla" tarkoitan, että algoritmi ei etsi lyhintä polkua ennalta tunnettuun päämäärään,
vaan lopullinen päämäärä saattaa muuttua kesken polun etsinnän. Pelialueella saattaa olla useampi maali, jolloin algoritmin
täytyy ottaa ne kaikki huomioon. Vaikka jokin maali olisi lähempänä kuin muut (jonkin heurestiikan mukaan), ei se välttämättä
tarkoita, etteikö johonkin toiseen maaliin olisi todellisuudessa lyhyempi matka.

Simulaation voi nyt myös aloittaa päävalikosta. Simulaatio ei ala, ellei valitun Scenarion joitain sääntöjä ole toteutettu.

Seuraavalla viikolla teen lisää Runnereita. Teen varmaan myös toisen Scenarion. Koitan saada javadocia myös parempaan kuosiin.
