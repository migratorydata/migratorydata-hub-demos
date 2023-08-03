<h2>Project description</h2>

This project contains multiple application that publish live data from a source to a migratory data subject.

First to use the application you need set the **migratory-data-server** and **migratory-data-entitlement-token** from **resources/config.properties**.

Below will be listed all the applications with a short description

<h2>Application 1: Amsterdam parkings</h2>

**Source site:** https://www.europeandataportal.eu/data/datasets/9orkef6t-au29g?locale=en \
https://creativecommons.org/licenses/by/4.0/deed.en

**Main app:** com.real.time.publisher.amsterdam.parkings.App

**Run command:** gradlew amsterdamparkings

<h4>Short description</h4>
This application brings data about amsterdam parkings on a migratory data subject(data as, free spaces, total spaces,
parking name, latitude, longitude etc).
The matches info will be published as a list of jsons.
The requests are made every 10 seconds and are published every 10 seconds on the migratory data subject.

<h4>Configuration guide</h4>

To use this application you need to actualize config file value **amsterdam-parkings-subject**.

<h2>Application 2: Zurich parkings</h2>

**Source site:** https://www.europeandataportal.eu/data/datasets/parkleitsystem-stadt-zurich?locale=en

**Main app:** com.real.time.publisher.zurich.parkings.App

**Run command:** gradlew zurichparkings

<h4>Short description</h4>
This application brings data about zurich parkings on a migratory data subject.
The matches info will be published as a list of jsons.
The requests are made every 10 seconds and are published every 10 seconds on the migratory data subject.

<h4>Configuration guide</h4>

To use this application you need to actualize config file value **zurich-parkings-subject**.

<h2>Application 3: JCDecaux bikes stations</h2>

**Source site:** https://developer.jcdecaux.com/#/home

**Main app:** com.real.time.publisher.jcdecaux.bikes.App

**Run command:** gradlew jcdecauxbikes

<h4>Short description</h4>
This application uses https://developer.jcdecaux.com/#/home about bikes stations disponibility from 
11 countries and 24 different regions or cities. For each city or region the stations informations
will pe published on a migratory data subject every minute(for more details about the regions for which
the bike stations information are given look in the configuration file).
All the stations informations for a region will be published as a json list.
The requests are made every 10 seconds to check if any of the matches information was updated.

<h4>Configuration guide</h4>

To use this application you need to create a account on https://developer.jcdecaux.com/#/home and after that get the API key that was given to
you and put it **jcdecaux-api-key** in **resources/config.properties**. Also, you need to configure the properties file with the migratory data subject to publish
the messages to.

<h2>Application 4: Brussels traffic</h2>

**Source site:** https://www.europeandataportal.eu/data/datasets/9a047e86-3947-424a-84e8-a192f18a735a?locale=en \
https://datastore.brussels/web/data/dataset/9a047e86-3947-424a-84e8-a192f18a735a

**Main app:** com.real.time.publisher.brussels.traffic.App

**Run command:** gradlew brusselstraffic

<h4>Short description</h4>
This application brings data about brussels traffic on a migratory data subject.
The matches info will be published as a list of jsons.
The requests are made every 10 seconds and are published every 10 seconds on the migratory data subject.

<h4>Configuration guide</h4>

To use this application you need to actualize config file value **brussels-traffic-subject**.

<h2>Application 5: Cryptocurrency rates</h2>

**Source site:** https://alternative.me/crypto/api/

**Main app:** com.real.time.publisher.cryptocurrency.App

**Run command:** gradlew cryptocurrency

<h4>Short description</h4>
This application brings data about 15 of the top cryptocurrencies(the name and the prices in USD and EUR).
The info will be published as a list of jsons.
Every one of the cryptocurrency is updated at 5 minutes.
The requests are made every 60 seconds and are published when there is any new data on the migratory data subject.

<h4>Configuration guide</h4>

To use this application you need to actualize config file value **cryptocurrency-subject**.
