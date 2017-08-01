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
    //
    //    val carLinks: Array[String] = new Array(carLinkElements.size())
    //
    //
    //    (0 until carLinkElements.size()).foreach((index)=>{
    //      val fullLink = carLinkElements.get(index).select("a").attr("href")
    //      val carPath = fullLink.replace(s"$host$subPath", "")
    //      carLinks(index) = carPath
    //    })
    //
    //    carLinks

    carLinkElements.asScala.map(_.select("a").attr("href").replace(s"$host$subPath", "")).toList
  }

  def getCarDetailPages(path: String): List[String] = {
    val document: Document = Jsoup.connect(s"$host$subPath$path").get
    val carDetailLinkElements: Elements = document.select(".types .col-6")

    carDetailLinkElements.asScala.map(_.select("a").attr("href").replace(s"$host$subPath", "")).toList

    //    val carDetailLinks: Array[String] = new Array(carDetailLinkElements.size())
    //
    //    (0 until carDetailLinkElements.size()).foreach((index)=>{
    //      val fullLink = carDetailLinkElements.get(index).select("a").attr("href")
    //      val carPath = fullLink.replace(s"$host$subPath", "")
    //      carDetailLinks(index) = carPath
    //    })
    //
    //    carDetailLinks.toList
  }

  def getCarSpecs(path: String): CarSpecs = {
    val document: Document = Jsoup.connect(s"$host$subPath$path").get

    //TODO: breadCrumbElements and all specmaps -> Carspecs in a converter


    val breadCrumbElements: Elements = document.select("#breadcrumb a span[itemprop='title']")

    val fullName = breadCrumbElements.get(3).text()
    val brand = breadCrumbElements.get(1).text()
    val model = breadCrumbElements.get(2).text().replaceAll(brand, "").trim

    val finalSpecsMap = getSpecTableByHeading(document, TableHeading.GeneralSpecifications)


    val carYear = finalSpecsMap("Car production year:").toInt

    val price = finalSpecsMap("Last new price:").replaceAll("[^\\d]", "").toInt
    val engineType = finalSpecsMap("Engine type:")

    val doorsNr = finalSpecsMap("Doors number:").toInt

    CarSpecs(fullName, model, brand, price, CarBody.SUV, doorsNr, Transmission.Manual, 5, carYear, EngineType.Petrol, 69, 120f, 6.3f, 0.2f, EnergyLevel.D, 4022f, finalSpecsMap)
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
//
//    for (i <- childElements.indices if i % 2 == 0) {
//      val title = childElements(i).text()
//      val info = childElements(i + 1).text()
//
//      carSpecsMap += title -> info
//    }
//
//
//    val carSpecsMap: collection.mutable.Map[String, String] = collection.mutable.Map()
//
//    specificationElements.forEach(element => {
//
//      val subElements = element.getElementsByClass("col-6")
//
//      (0 until subElements.size()).foreach(index => {
//        if (index % 2 == 0 && index + 1 < subElements.size()) {
//
//          val title = subElements.get(index).text()
//          val info = subElements.get(index + 1).text()
//
//          carSpecsMap += title -> info
//        }
//      })
//
//    })
//    val finalSpecsMap = carSpecsMap.toMap
  }

  //  TableHeading.GeneralSpecifications.toString


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
