package domain

import domain.CarBody.CarBody
import domain.EnergyLevel.EnergyLevel
import domain.EngineType.EngineType
import domain.Transmission.Transmission

/**
  * Created by Tom on 02-Jul-17.
  */
case class CarSpecs(fullName: String,
                    model: String,
                    brand: String,
                    price: Long,
                    carBody: CarBody,
                    doorsNr: Int,
                    transmission: Transmission,
                    seating: Int,
                    year: Int,
                    engineType: EngineType,
                    maxPowerKw: Int,
                    topSpeed: Float,
                    acc0_100: Float,
                    avConsumption: Float,
                    energyLevel: EnergyLevel,
                    length: Float,
                    specsMap: Map[String, String]) {

}

object CarBody extends Enumeration {
  type CarBody = Value
  val SUV, Convertible, Coupe, Hatchback, Sedan, PickUp, Wagon, MPV, Diesel, HybridElectric, Luxury, Other = Value
}

object Transmission extends Enumeration {
  type Transmission = Value
  val Manual, Automatic = Value
}

object EngineType extends Enumeration {
  type EngineType = Value
  val Petrol, Diesel, Electric, Hybrid = Value
}

object EnergyLevel extends Enumeration {
  type EnergyLevel = Value
  val A, B, C, D, E, F = Value
}
