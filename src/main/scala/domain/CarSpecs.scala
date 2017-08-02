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

object CarSpecs{
  def toCarBody(carBody: String): CarBody = {
    carBody match{
      case "cabriolet" => CarBody.Cabriolet

      case _ =>
        println(s"toCarBody could not parse for string $carBody")
        CarBody.Other
    }
  }

  def toTransmission(transmission: String): Transmission = {
    transmission match{
      case "manual" => Transmission.Manual

      case _ =>
        println(s"toTransmission could not parse for string $transmission")
        Transmission.Other
    }
  }

  def toEngineType(engineType: String): EngineType = {
    engineType match {
      case "fuel engine" => EngineType.Petrol

      case _ =>
        println(s"toEngineType could not parse for string $engineType")
        EngineType.Other
    }
  }

  def toEnergyLevel(energyLevel: String): EnergyLevel = {
    energyLevel match {
      case "a" | "A" => EnergyLevel.A
      case "b" | "B" => EnergyLevel.B
      case "c" | "C" => EnergyLevel.C
      case "d" | "D" => EnergyLevel.D
      case "e" | "E" => EnergyLevel.E
      case "f" | "F" => EnergyLevel.F
      case _ =>
        println(s"toEnergyLevel could not parse for string $energyLevel")
        EnergyLevel.Unknown
    }
  }
}

object CarBody extends Enumeration {
  type CarBody = Value
  val SUV, Convertible, Cabriolet, Coupe, Hatchback, Sedan, PickUp, Wagon, MPV, Diesel, HybridElectric, Luxury, Other = Value
}

object Transmission extends Enumeration {
  type Transmission = Value
  val Manual, Automatic, Other = Value
}

object EngineType extends Enumeration {
  type EngineType = Value
  val Petrol, Diesel, Electric, Hybrid, Other = Value
}

object EnergyLevel extends Enumeration {
  type EnergyLevel = Value
  val A, B, C, D, E, F, Unknown = Value
}
