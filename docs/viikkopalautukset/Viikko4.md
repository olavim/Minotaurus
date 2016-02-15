Muutin `Stack`in toteutusta taulukkoon perustuvasta linkitettyyn listaan perustuvaksi.
Tietorakenteilla ei ole tällä hetkellä paljoa käyttöä, mutta nyt kun olen saanut `MazeGenerator`, `Scenario`, ja `Runner` luokkien perustukset tehtyä, voin hyvillä mielin siirtyä merkityksellisempiin asioihin. 

Plugin loaderia on tullut myös siistittyä ja turhia asioita poisteltua, ja sain sille myös testit kirjoitettua.
Tämä vaati testien resources-kansioon parin Jarin lisäystä jonka jälkeen testaaminen olikin suoraviivaista.

`SimulationHandler`ia jatkoin sen verran, että sillä pystyy juuri ja juuri testailemaan `MazeGenerator`ia, `Scenario`ta ja `Runner`ia, tosin toteutus on vasta alullaan ja toimii huonosti. Samalla tapaa käyttöliittymäluokkiin on tullut muutoksia jotta simulaatioita pystyy testaamaan edes jotenkin.

Seuraava viikko kulunee olemassaolevien ongelmien korjailuun, testien kirjoitteluun ja erilaisten `Scenario`iden ja `Runner`eiden
luomiseen. Riippuen minkälaisia `Runner`eita teen, toteutan tarvittavia tietorakenteita. A*/dijkstra on ennenpitkää edessä syystä tai toisesta, jota varten ainakin `HashSet` ja `PriorityQueue` ovat toteutettavien rakenteiden listalla. `HashSet`tiä edeltää `HashMap` toteutus, sillä `HashSet` on tavattoman helppo toteuttaa `HashMap`in avulla.

Viikkoon on kulunut noin 15 tuntia.
