# Sample web service for the scala webshop

Simulates a milk and wool supplier

# Functional specs

This web service computes the future output of milk and wool of a herd of animals. 
- An animal year lasts 100 days.
- An animal dies the day it turns 10 years old. 
- Day T means that T-1 days have passed.
- An animal outputs 50-animal.ageInDays(T)*0.03 liters of milk on day T.
- An animal can be shorn every so many days: 8+animal.ageInDays(T)*0.01.
- An animal can be shorn only after it turns 1 year old.
- The simulation starts at day 0 where every eligible animal can be shorn and milked.

# Command line usage

java com.github.ggeorgovassilis.webshop.supplierwebservice.application.ProductionPrediction path_to_xml day

# Web service usage

Assuming that the WAR file is deployed under the standard application name webshop-supplier-webservice:

HTTP GET to /webshop-supplier-webservice/stock/T, where T is the number of days from now, returns an overview
of the available stock (assuming no consumption but factoring in retirements).

HTTP GET to /webshop-supplier-webservice/herd/T, where T is the number of days from now, returns a list
of every animal in the herd and its condition

HTTP POST to //webshop-supplier-webservice/herd/add?name=NAME&age=AGE&ageLastShorn=AGELASTSHORN updates the status
of an animal in the herd (or adds a new one)

# Assumptions / Known issues
- All animals are assumed to produce milk and wool. In practice there might be categories of animals that produce one and not the other.
- The calculation of the wool output is iterative and thus inefficient for dates far in the future
- The internal data representation has been normalized to days
- The root URL is defined by the deployment procedure of this WAR file
