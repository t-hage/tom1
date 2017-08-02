package service

import config.module.SystemModule
import domain._
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements
import service.TableHeading.TableHeading

import scala.collection.JavaConverters._

/**
  * Created by Tom on 02-Jul-17.
  */
class CarScrapeService(implicit systemModule: SystemModule) {

  lazy val host: String = systemModule.carScrapeConfig.getString("host")
  lazy val subPath: String = systemModule.carScrapeConfig.getString("subpath")

  def getLinksFromAllCars(page: Int): List[String] = {
    val pageConstant = "/page"
    val allCarsPath = "/all-cars"
    val extension = ".html"

    val path = allCarsPath + pageConstant + page + extension

    val document: Document = Jsoup.connect(s"$host$subPath$path").get
    val carLinkElements: Elements = document.select(".models .col-4")
    carLinkElements.asScala.map(_.select("a").attr("href").replace(s"$host$subPath", "")).toList
  }

  def getCarDetailPages(path: String): List[String] = {
    val document: Document = Jsoup.connect(s"$host$subPath$path").get
    val carDetailLinkElements: Elements = document.select(".types .col-6")
    carDetailLinkElements.asScala.map(_.select("a").attr("href").replace(s"$host$subPath", "")).toList
  }

  def getCarSpecs(path: String): CarSpecs = {
    val document: Document = Jsoup.connect(s"$host$subPath$path").get

    val breadCrumbElements: Elements = document.select("#breadcrumb a span[itemprop='title']")

    val generalSpecificationsMap = getSpecTableByHeading(document, TableHeading.GeneralSpecifications).withDefaultValue("Unknown")
    val driveMap = getSpecTableByHeading(document, TableHeading.Drive).withDefaultValue("Unknown")
    val fuelEngineMap = getSpecTableByHeading(document, TableHeading.FuelEngine).withDefaultValue("Unknown")
    val electricEngineMap = getSpecTableByHeading(document, TableHeading.ElectricEngine).withDefaultValue("Unknown")
    val performanceMap = getSpecTableByHeading(document, TableHeading.Performance).withDefaultValue("Unknown")
//    val chassisMap = getSpecTableByHeading(document, TableHeading.Chassis).withDefaultValue("Unknown")
//    val transmissionMap = getSpecTableByHeading(document, TableHeading.Transmission).withDefaultValue("Unknown")
//    val securityMap = getSpecTableByHeading(document, TableHeading.Security).withDefaultValue("Unknown")
//    val interiorMap = getSpecTableByHeading(document, TableHeading.Interior).withDefaultValue("Unknown")
//    val exteriorMap = getSpecTableByHeading(document, TableHeading.Exterior).withDefaultValue("Unknown")
//    val weightsMap = getSpecTableByHeading(document, TableHeading.Weights).withDefaultValue("Unknown")
//    val baggageCargoMap = getSpecTableByHeading(document, TableHeading.BaggageCargo).withDefaultValue("Unknown")
    val exteriorSizesMap = getSpecTableByHeading(document, TableHeading.ExteriorSizes).withDefaultValue("Unknown")
//    val interiorSizesMap = getSpecTableByHeading(document, TableHeading.InteriorSizes).withDefaultValue("Unknown")
//    val comfortMap = getSpecTableByHeading(document, TableHeading.Comfort).withDefaultValue("Unknown")
//    val serviceWarranty = getSpecTableByHeading(document, TableHeading.ServiceWarranty).withDefaultValue("Unknown")

    val fullName = breadCrumbElements.get(3).text()
    val brand = breadCrumbElements.get(1).text()
    val model = breadCrumbElements.get(2).text().replaceAll(brand, "").trim

    val price = tryParseFloat(generalSpecificationsMap("Last new price:")).toInt
    val carBody = CarSpecs.toCarBody(generalSpecificationsMap("Carbody:"))
    val doorsNr = generalSpecificationsMap("Doors number:").replaceAll("[^\\d]", "").toInt
    val transmission = CarSpecs.toTransmission(generalSpecificationsMap("Transmission type:"))
    val carYear = generalSpecificationsMap("Car production year:").replaceAll("[^\\d]", "").toInt

    val engineType = CarSpecs.toEngineType(driveMap("Engine type:"))

    val maxPowerKw = "^\\d+".r.findFirstIn(if(fuelEngineMap("Max power:") == "Unknown") electricEngineMap("Total max power:") else fuelEngineMap("Max power:")).getOrElse("0").toInt
    val topSpeed = tryParseFloat(performanceMap("Top speed:"))
    val acc0_100 = tryParseFloat(performanceMap("Acceleration 0-100 km/h:"))
    val avConsumption = tryParseFloat(performanceMap("Average consumption:"))
    val energyLevel = CarSpecs.toEnergyLevel(performanceMap("Energy level:"))

    val length = tryParseFloat(exteriorSizesMap("Length:"))

    CarSpecs(fullName, model, brand, price, carBody, doorsNr, transmission, 5, carYear, engineType, maxPowerKw,
      topSpeed, acc0_100, avConsumption, energyLevel, length, generalSpecificationsMap)
  }

  def getSpecTableByHeading(document: Document, tableHeading: TableHeading): Map[String, String] = {
    val specificationElements: Elements = document.select(".row.box")

    specificationElements.asScala.find(_.child(0).text == tableHeading.toString)
      .map(element => element.getElementsByClass("col-6").asScala.toList)
      .getOrElse(List.empty)
      .grouped(2)
      .map((list: Seq[Element]) => {
        list.head.text() -> list(1).text()
      }).toMap
  }

  private def tryParseFloat(floatString: String): Float = {
    "[\\d,]+".r.findFirstIn(floatString.replaceAll("\\.", "")).getOrElse("0").replace(",", ".").toFloat
  }

}

object TableHeading extends Enumeration {
  type TableHeading = Value
  val GeneralSpecifications = Value("General Specifications")
  val Drive = Value("Drive")
  val FuelEngine = Value("Fuel engine")
  val ElectricEngine = Value("Electric engine")
  val Performance = Value("Performance")
  val Chassis = Value("Chassis")
  val Transmission = Value("Transmission - Gear ratio")
  val Security = Value("Security")
  val Interior = Value("Interior")
  val Exterior = Value("Exterior")
  val Weights = Value("Weights")
  val BaggageCargo = Value("Baggage cargo")
  val ExteriorSizes = Value("Exterior sizes")
  val InteriorSizes = Value("Interior sizes")
  val Comfort = Value("Comfort")
  val ServiceWarranty = Value("Service warranty")
}
