package model

import java.util.Date
import play.api.libs.json._

/**
 * The event entity
 * @author George Georgovassilis
 * 
 */
case class Event(var id:Long, val eventType:EventTypes.EventType, val title:String, val performers:Array[String], val city:String, val categories:Array[String], val start:Date, val end:Date, val price:Long) {


}

