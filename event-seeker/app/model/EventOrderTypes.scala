package model

/**
 * Specifies sorting order on an entity attribute
 * @author George Georgovassilis
 * 
 */
object EventOrderTypes extends Enumeration {
  type Order = Value
  val Price=Value("price")
  val Date=Value("date")
  val None=Value("")
}