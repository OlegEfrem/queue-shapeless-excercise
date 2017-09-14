package test

/*
  Implement the following api for a FIFO (first-in, first-out) Queue with the indicated complexities

  This should be an *immutable* queue
*/
trait Queue[+T] {
  // O(1)
  def isEmpty: Boolean

  // O(1)
  def insert[G >: T](g: G): Queue[G]

  // O(1) amortised
  def remove: (Option[T], Queue[T])
}

object Queue {
  def empty[T]: Queue[T] = new FifoQueue[T]
}

/**
 * In order to have operations with constant time complexity - O(1) their implementation need to use only Q(1) downstream operations.
 * Of the existing collections [[List]] has head, tail, prepend and isEmpty complexity as Q(1)
 *
 * @see http://docs.scala-lang.org/overviews/collections/performance-characteristics.html
 * @param in  input elements used to add elements to the head.
 * @param out output elements (reverse of in) to remove elements from the head.
 */
class FifoQueue[+T](private val in: List[T] = Nil, private val out: List[T] = Nil) extends Queue[T] {

  /**
   * Q(1) complexity since only using isEmpty on the Nil and :: which return straight away
   */
  override def isEmpty: Boolean = in.isEmpty && out.isEmpty

  /**
   * Q(1) complexity since it uses only [[List]].:: (prepend).
   */
  override def insert[G >: T](g: G): Queue[G] = new FifoQueue(g :: in)

  /**
   * In average Q(1) since it needs to revers in list when out gets empty.
   */
  override def remove: (Option[T], Queue[T]) = out match {
    case h :: t => (Some(h), new FifoQueue(in, t))
    case Nil =>
      in.reverse match {
        case h :: t => Some(h) -> new FifoQueue(Nil, t)
        case Nil => (None, this)
      }
  }

  override def toString: String = all.mkString(",")

  /**
   * Utility method to return all elements of the queue
   */
  def all: List[T] = in ::: out.reverse
}
