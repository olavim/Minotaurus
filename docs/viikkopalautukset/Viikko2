Viikko on kulunut "plugin loaderin" parissa, eli siis koneiston osan, joka lataa ohjelman ulkoiset osaset paikoilleen.
Käytännössä siis eri tekoälyt, labyrinttigeneraattorit, ym. koodataan omiin Jar-tiedostoihinsa, jotka pääohjelma etsii
tietystä kansiosta ja lataa muistiin. Näin kuka tahansa voi laajentaa ohjelmaa kääntämättä sitä uudestaan ja uudestaan.

Tällainen ulkopuolisten luokkien lataaminen on itselleni jotain uutta, josta johtuen siihen kului suhteellisen paljon aikaa.
Vaikein on kuitenkin jo takana, joten mitä koodin määrään tulee, työtahti kiristynee.

Yksikkötestaus on toistaiseksi vähäistä. Se johtuu siitä, että tähän asti lähes kaikki luokat ovat joko rajapintoja,
käyttöliittymäluokkia tai niin pieniä, ettei niitä kannata testata (esim. `Pair` luokka, jolla on pelkästään get- ja set-metodit).
Aivan, plugin loader kannattaisi testata verille, mutta en tiedä miten se kannattaisi, tai voisi, tehdä. Palaan asiaan myöhemmin.

Nyt kun plugin loader on näennäisesti saatu tehtyä, jää seuraavalle viikolle ainakin ladattujen osien listaaminen käyttäjälle,
sekä ensimmäisen labyrintin generointi.

Kaikki oleellisimmat algoritmit tulevat olemaan erillisissä tiedostoissa. Nämä sijaitsevat `plugins` kansiossa päähakemiston
alla. Jokainen algoritmi ja tekoäly tulevat siis käytännössä olemaan oma projektinsa, mutta se todennäköisesti vain helpottaa
tarkastajien ja vertaisarvioijien työtä.

Käytin noin 16 tuntia kuluneella viikolla.
