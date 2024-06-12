# Traffic Simulator

## Description

The goal of this project is to be a traffic simulator. We aim to analyse traffic
for different speed and acceleration values to see to what point the configuration changes traffic flow.
In term, we want to see different "types" of traffic by analysing the data and plotting it with a few distinct
starting configuration

## Getting Started

### Executing program

* Build using sbt.
* run Main with sbt or in an IDE.

## Modify the configuration

If you want to try for yourself with different setting, you can modify them in Main.Scala.

![code Screenshot](https://github.com/GuilBess/Traffic_Simulator/blob/master/media/modif.png?raw=true)

* Max speed, acceleration and braking should be changed carefully to avoid wierd cases.
* TIME_STEP is set up for a 60fps sim.
* ROAD_LENGTH and nbrCars will affect the car density. To modify the max number of cars, we can
* change the value in the assertion.
