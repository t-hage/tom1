package config.module

import akka.CarScrapeActor
import akka.actor.ActorRef
import com.typesafe.config.Config
import service.CarScrapeService

/**
  * Created by Tom on 02-Jul-17.
  */
trait CarScrapeModule {
  val carScrapeActor: ActorRef
  def carScrapeService: CarScrapeService

  val carScrapeConfig: Config
}

trait DefaultCarScrapeModule extends CarScrapeModule { this: SystemModule =>
  lazy val carScrapeActor: ActorRef = actorSystem.actorOf(CarScrapeActor.props(this), CarScrapeActor.name)

  def carScrapeService: CarScrapeService = new CarScrapeService()(this)

  lazy val carScrapeConfig: Config = config.getConfig("car-scrape")
}
