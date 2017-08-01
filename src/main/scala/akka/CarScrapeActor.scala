package akka

import akka.CarScrapeActor.{ScrapeCarDetails, ScrapeMakeOverview}
import akka.actor.{Actor, Props}
import config.module.SystemModule
import domain.CarSpecs

/**
  * Created by Tom on 28-Jun-17.
  */
object CarScrapeActor {
  val name = "CarScrapeActor"

  case class ScrapeCarDetails(urlPath: String)
  case class ScrapeMakeOverview(urlPath: String)

  def props(implicit systemModule: SystemModule): Props = Props(new CarScrapeActor)
}

class CarScrapeActor(implicit val systemModule: SystemModule) extends Actor{

  override def receive: Receive = {
    case ScrapeMakeOverview(urlPath) =>
      val carDetailPagesList = systemModule.carScrapeService.getCarDetailPages(urlPath)
      carDetailPagesList.foreach(self ! ScrapeCarDetails(_))

    case ScrapeCarDetails(urlPath) =>
      val carSpecs: CarSpecs = systemModule.carScrapeService.getCarSpecs(urlPath)
      //TODO: save to DB here
      println(carSpecs)

  }
}
