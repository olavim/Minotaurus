**Tärkeä huomio ohjelman rakenteesta**

Ohjelma on pilkottu siten, että pääohjelma sisältää ainoastaan käyttöliittymäkoodia, rajapintoja ja *plugin loaderin*.
Plugin loader koostuu `com.github.tilastokeskus.minotaurus.plugin` pakkauksen luokista. Sen tehtävä on ladata ulkoisia moduuleita
`plugins` kansiosta. Pluginit ovat **.jar** tiedostoja, jotka sisältävät yhden luokan, joka toteuttaa `Plugin` rajapinnan.

Esimerkki `plugins` kansiosta:

```
\---plugins
    +---myMazeGen.jar
    |       MyMazeGen.class
    |
    \---myRunner.jar
            MyRunner.class
```

Tämä GitHub repo sisältää kaksi `plugins` kansiota.

Ensimmäinen on päähakemistossa; se sisältää kaikkien pluginien lähdekoodit. Jokainen tämän kansion alikansio on erillinen netbeans projektinsa.

Toinen on `Minotaurus/target/plugins` kansiossa, joka sisältää käännetyt pluginit ja jotka pääohjelma lataa käynnistyksen
yhteydessä.
