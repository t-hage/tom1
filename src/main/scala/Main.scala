import java.util.concurrent.TimeUnit

import akka.CarScrapeActor.ScrapeMakeOverview
import config.module.{DefaultAkkaModule, DefaultCarScrapeModule, SystemModule}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by Tom on 01-Jul-17.
  */
object Main extends App{
  implicit val systemModule: SystemModule = new SystemModule
      with DefaultAkkaModule with DefaultCarScrapeModule {}

  val carScrapeActor = systemModule.carScrapeActor

  val carLinks = systemModule.carScrapeService.getLinksFromAllCars(70)
  carLinks.foreach(carLink =>{carScrapeActor ! ScrapeMakeOverview(carLink) })


//  val pw = new PrintWriter(new File(systemModule.config.getString("link-file-name") ))
//  pw.close()

  Await.ready(systemModule.actorSystem.whenTerminated, Duration(1, TimeUnit.MINUTES))
}
