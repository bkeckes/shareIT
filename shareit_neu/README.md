ShareIt Beispielimplementierung
===============================

Enthält einige Beispiele für Services der ShareIt Verleihplattform. 

* Package edu.hm.hafner.shareit:
  Enthält die Anwendungslogik, d.h. den Usecase Controller
* Package edu.hm.hafner.shareit.db: 
  Enthält die Datenhaltung mit einer NoSQL Datenbank (Mongo DB)
* Package edu.hm.hafner.shareit.model:
  Enthält die Entitäten
* Package edu.hm.hafner.shareit.util:
  Enthält eine Hilfsklasse zum Erzeugen und Nutzen der NoSQL Datenbank 

Das Projekt lässt sich nur in Eclipse 3.8/4.2 (Eclipse for Java Developers) importieren. 
Folgenden Eclipse Plug-ins werden benötigt:
- m2e
- checkstyle
- findbugs
- PMD

Diese Plugins können über die Datei Eclipse-Plugins.p2f in Eclipse installiert werden:
File->Import->Install->Software Items From File
