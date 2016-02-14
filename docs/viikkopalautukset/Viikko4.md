Muutin Stackin toteutusta taulukkoon perustuvasta linkitettyyn listaan perustuvaksi.
Tietorakenteilla ei ole tällä hetkellä paljoa käyttöä, mutta nyt kun olen saanut MazeGenerator-, Scenario-, ja Runner-luokkien
perustukset tehtyä, voin hyvillä mielin siirtyä merkityksellisempiin asioihin. 

Plugin loaderia on tullut myös siistittyä ja turhia asioita poisteltua, ja sain sille myös testit kirjoitettua.
Tämä vaati testien resources-kansioon parin Jarin lisäystä jonka jälkeen testaaminen olikin suoraviivaista.

SimulationHandleria jatkoin sen verran, että sillä pystyy juuri ja juuri testailemaan MazeGeneratoria, Scenariota ja Runneria,
tosin toteutus on vasta alullaan ja toimii huonosti. Samalla tapaa käyttöliittymäluokkiin on tullut muutoksia jotta simulaatioita
pystyy testaamaan edes jotenkin.

Seuraava viikko kulunee olemassaolevien ongelmien korjailuun, testien kirjoitteluun ja erilaisten Scenarioiden ja Runnereiden
luomiseen. Riippuen minkälaisia Runnereita teen, toteutan tarvittavia tietorakenteita. A* on ennenpitkää edessä syystä tai
toisesta, jota varten ainakin HashSet on toteutettavien rakenteiden listalla. HashSettiä edeltää HashMap toteutus, sillä HashSet
on helppo toteuttaa HashMapin avulla.
