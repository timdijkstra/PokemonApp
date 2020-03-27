# Pokemon App

## TODO
* [ ] Waar nodig onzinnige comments verwijderen.
* [ ] Checken of er errors gegenereed worden.

## Rubric

### Knock-out criteria
* [x] De app is te runnen.
* [x] De app is zelf gemaakt.
* [x] Er zijn geen externe libraries gebruikt (zonder goedkeuring vooraf).
* [x] Er wordt gebruik gemaakt van een webservice. 

### Minimumeisen
* [x] Los van splash screens, inlogscherm e.d. moeten er minimaal 2 schermen in de app
      zitten met uiteraard navigatie daartussen.
* [x] De app moet gebruik maken van een webservice, d.w.z. dat de app de data haalt vanaf
      het internet. Hierbij wordt b.v. gebruik gemaakt van een JSON- of XML-service op het
      web.
* [x] Een van de schermen moet een recyclerview tonen met data die van het web komt. Als
      extra mag je de data ook in een lokale datastore cachen en daarna synchroniseren met
      de databron op het web.
* [x] De app is multithreaded geïmplementeerd. Langdurige acties worden asynchroon
      uitgevoerd zodat de UI te allen tijde responsive blijft.
* [x] In minstens 1 van de 2 schermen moet minstens 1 'bewerking' geïmplementeerd zijn,
      d.w.z. je kunt een actie uitvoeren op de lijst of een item daaruit die anders is dan enkel
      navigeren ( b.v. 'Wijzigen' of 'Bellen' of 'Tonen op kaart' of …).
* [ ] Er moet een resultaat (plaatje, contact, …) uit een andere app worden opgehaald d.m.v.
      implicit intents.
* [x] Er moet 'iets' (b.v. instellingen/preferences) bewaard worden als lokale data.
* [x] Er mogen geen externe libraries gebruikt worden, behalve als dit eerst is overlegd met
      en goedgekeurd door de docenten. Volley is onderdeel van de lesstof en mag natuurlijk
      wel worden gebruikt.
* [x] De UI moet zowel in portrait als landscape orientatie goed werken.
* [x] De schermen van een Activity moeten opgebouwd zijn m.b.v. Fragments
* [ ] Algemene eisen
    * [x] De aangeboden functionaliteit moet uiteraard werken
    * [ ] De app mag niet crashen of errors genereren
    * [x] De app moet draaien onder Android Studio en op Android 10
    * [x] Gebruik voor de app minimaal 1 dangerous permission waar je aan de gebruiker
          run-time toestemming voor moet vragen.
