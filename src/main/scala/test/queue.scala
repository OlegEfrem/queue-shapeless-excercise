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
  def empty[T]: Queue[T] = ???
}
