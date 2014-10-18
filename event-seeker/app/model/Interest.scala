package model

/**
 * An Interest is the combination of an event type and an event category, i.e. music-hardrock
 * @author George Georgovassilis
 */
class Interest(val eventType: EventTypes.EventType, val category: String) {

  override def hashCode = category.hashCode()

  override def equals(other: Any) = other match {
    case that: Interest => this.eventType == that.eventType && this.category == that.category
    case _ => false
  }
}