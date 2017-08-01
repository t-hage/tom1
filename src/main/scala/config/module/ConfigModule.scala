package config.module

import akka.actor.ActorSystem
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by Tom on 01-Jul-17.
  */
trait ConfigModule {
  def config: Config
  def actorSystem: ActorSystem
}

trait DefaultAkkaModule extends ConfigModule { this: SystemModule =>
  lazy val config = ConfigFactory.load()
  lazy val actorSystem: ActorSystem = ActorSystem("main-actor-system", config)
}
