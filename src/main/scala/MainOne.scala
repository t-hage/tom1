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



//    val document: Document = Jsoup.connect("http://www.cars-data.com/en/bentley-flying-spur-v8-specs/70206").get
//    val specificationElements: Elements = document.select(".row.box")
//
//    println(s"${specificationElements.size()} is the size")
//
//    specificationElements.forEach(element =>{
//
//      val subElements = element.getElementsByClass("col-6")
//
//      (0 until subElements.size()).foreach(index=>{
//        if(index%2 == 0 && index+1 < subElements.size()){
//
//          val title = subElements.get(index).text()
//          val info = subElements.get(index+1).text()
//
//          println(s"Gotten info about $title $info")
//        }
//      })
//
//    })
  }
}
