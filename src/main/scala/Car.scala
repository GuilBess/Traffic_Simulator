
//Case class describing the properties of each car
case class Car(var pos: Double, var speed: Double, accel: Double, maxSpeed: Double, braking: Double, var leftToBrake: Int = 0,var waitToStart: Int = 0, var slowest: Boolean = false)

//leftToBreak is a variable that lets the car brake for a little longer than necessary
//waitToStart is a variable that lets the car wait a bit before starting again from a stop