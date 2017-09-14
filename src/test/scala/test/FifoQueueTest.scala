package test

import org.scalatest.{ FreeSpec, Matchers, OptionValues }
import scala.util.Random

class FifoQueueTest extends FreeSpec with Matchers with OptionValues {
  private type T = Long
  private val queueSize = 1000000 //queue will be double size of this number
  private val emptyQueue = Queue.empty[T]
  private val bigList: List[T] = List.fill(queueSize)(Random.nextLong)
  private val bigQueue = new FifoQueue[T](bigList, bigList.reverse) //same elements twice, to have big test data for both insert and remove

  "Time complexity tests" - {
    import scala.math._
    "isEmpty should have Q(1) in milliseconds" in {
      val emptyExecutionTime = executionTimeFor(emptyQueue.isEmpty)
      val bigExecutionTime = executionTimeFor(bigQueue.isEmpty)
      abs(emptyExecutionTime - bigExecutionTime) should be <= 1L //time difference is one millisecond or less
    }

    "insert should have Q(1) in milliseconds" in {
      val emptyExecutionTime = executionTimeFor(emptyQueue.insert(1))
      val bigExecutionTime = executionTimeFor(bigQueue.insert(1))
      abs(emptyExecutionTime - bigExecutionTime) should be <= 1L
    }

    "remove should have amortized Q(1) = less than 2 milliseconds" in {
      val emptyExecutionTime = executionTimeFor(emptyQueue.remove)
      val bigExecutionTime = executionTimeFor(bigQueue.remove)
      abs(emptyExecutionTime - bigExecutionTime) should be <= 2L //might take slightly longer because it has to reverse in list into out, when latter is empty
    }

    "insert should not be constant in nanoseconds" in {
      val emptyExecutionTime = executionTimeFor(emptyQueue.insert(1), System.nanoTime())
      val bigExecutionTime = executionTimeFor(bigQueue.insert(1), System.nanoTime())
      emptyExecutionTime shouldNot be(bigExecutionTime)
    }

    def executionTimeFor[R](function: => R, nowFunction: => Long = System.currentTimeMillis()): Long = {
      val start = nowFunction
      val res = function
      val end = nowFunction
      end - start
    }
  }

  "Functional tests" - {
    "isEmpty should" - {
      "return true for empty Queue" in {
        emptyQueue.isEmpty shouldBe true
      }

      "return false for non empty Queue" in {
        bigQueue.isEmpty shouldBe false
      }
    }

    "insert should" - {
      "return a new queue with the element prepended" in {
        val q1 = emptyQueue.insert(1)
        val q2 = q1.insert(2)
        q2.asInstanceOf[FifoQueue[T]].all should contain theSameElementsInOrderAs List(2, 1)
      }

      "not mutate the original queue" in {
        emptyQueue.insert(1)
        emptyQueue.isEmpty shouldBe true
      }
    }

    "remove should" - {
      val removeQueueElements: List[T] = List(1, 2, 3)
      val removeQueue = new FifoQueue[T](removeQueueElements)
      "return a new queue with the first inserted element removed" in {
        val result = removeQueue.remove._2
        result.asInstanceOf[FifoQueue[T]].all should contain theSameElementsInOrderAs removeQueueElements.init
      }

      "return removed element" in {
        val result = removeQueue.remove._1.value
        result shouldBe removeQueueElements.last
      }

      "not mutate the original queue" in {
        removeQueue.remove
        removeQueue.all should contain theSameElementsInOrderAs removeQueueElements
      }
    }

    "toString should" - {
      "return all values for a non empty queue" in {
        val queue = new FifoQueue[Int](List(1, 2, 3))
        queue.toString shouldBe "1,2,3"
        queue.toString shouldNot be("1,2,3,4")
      }

      "return empty string for an empty queue" in {
        emptyQueue.toString shouldBe ""
      }
    }
  }
}
