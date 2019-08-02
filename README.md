# AmazinKart

Amazin Kart is simple java project for applying applying discounts to the prducts on runtime.

## Getting started

* Fork this project
* ```mvn clean install```
* ```mvn exec:java -Dexec.mainClass=com.forbes.amazinkart.AmazinKart -Dexec.args="prmotionsSetA"```
* ```mvn exec:java -Dexec.mainClass=com.forbes.amazinkart.AmazinKart -Dexec.args="prmotionsSetB"```

## Applying new promotions

* All the existing promotions are defined in file `promotion.json` under resources folder in the specific json format.
* For intorducing new promotions just add entry of promotion in the above file.

## Output files

* Output file is by default written to `/target/classes/output.json`
* This file can be overwritten at `src/main/resources/config.properties` under `outputFilePath` property