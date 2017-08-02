import akka.CarScrapeActor.ScrapeCarDetails
import config.module.{DefaultAkkaModule, DefaultCarScrapeModule, SystemModule}

import scala.collection.mutable

/**
  * Created by Tom on 17-Jun-17.
  */
class MainOne {

}

object MainOne {
  def main(args: Array[String]): Unit = {
    println("Hello World lets do this")
    implicit val systemModule: SystemModule = new SystemModule
      with DefaultAkkaModule with DefaultCarScrapeModule {}

    val carScrapeActor = systemModule.carScrapeActor

    carScrapeActor ! ScrapeCarDetails("/kia-sportage-1.6-gdi-economyline-specs/70616")

  }
}
