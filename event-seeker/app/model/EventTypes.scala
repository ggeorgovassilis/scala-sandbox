package model

/**
 * Models various types of events
 * @author George Georgovassilis
 */
object EventTypes extends Enumeration {
  type EventType = Value
  val Music = Value("music")
  val Conference = Value("conference")
  val Sports = Value("sports")
}