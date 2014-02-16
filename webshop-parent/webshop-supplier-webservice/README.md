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



# Assumptions / Known issues
- All animals are assumed to produce milk and wool. In practice there might be categories of animals that produce one and not the other.
- The calculation of the wool output is iterative and thus inefficient for dates far in the future
- The internal data representation has been normalized to days
