**Scotiabank Scala Interview Questions**

(c) Scotiabank 2017

A selection of interview problems for developer candidates.

There are 2 exercises to complete.

- **Exercise #1** is a data structure question in src/main/scala/test/queue.scala
- **Exercise #2** is a question on automatic typeclass derivation using shapeless in src/main/scala/test/eq.scala

You should complete both questions.

**Implementation considerations**
- **Exercise #1** [Immutable FIFO Queue](https://github.com/OlegEfrem/queue-shapeless-excercise/blob/master/src/main/scala/test/queue.scala#L29) 
borrows from the default scala immutable Queue implementation described [here](http://www.scala-lang.org/api/2.12.0/scala/collection/immutable/Queue.html)
- **Exercise #2** [Type Class Derivation](https://github.com/OlegEfrem/queue-shapeless-excercise/blob/master/src/main/scala/test/eq.scala#L33) with shapeless 
borrows from Miles Sabin (shapeless creator) on [here](https://github.com/milessabin/shapeless-type-class-derivation-2015-demo/blob/master/src/main/scala/derivation/derivation.scala#L89)
* Both solutions are thoroughly tested:
  * [scoverage](https://github.com/scoverage/sbt-scoverage) report result is:
      ```
        [info] Statement coverage.: 100.00%
        [info] Branch coverage....: 100.00%
        [info] Coverage reports completed
        [info] 100% Coverage !
      ```       
  * FifoQueue is tested [here](https://github.com/OlegEfrem/queue-shapeless-excercise/blob/master/src/test/scala/test/FifoQueueTest.scala), with the test output: 
      ```
      [info] FifoQueueTest:
      [info] Time complexity tests
      [info] - isEmpty should have Q(1) in milliseconds
      [info] - insert should have Q(1) in milliseconds
      [info] - remove should have amortized Q(1) = less than 2 milliseconds
      [info] - insert should not be constant in nanoseconds
      [info] Functional tests
      [info]   isEmpty should
      [info]   - return true for empty Queue
      [info]   - return false for non empty Queue
      [info]   insert should
      [info]   - return a new queue with the element prepended
      [info]   - not mutate the original queue
      [info]   remove should
      [info]   - return a new queue with the first inserted element removed
      [info]   - return removed element
      [info]   - not mutate the original queue
      [info]   toString should
      [info]   - return all values for a non empty queue
      [info]   - return empty string for an empty queue
      ``` 
  * Type class derivation is tested [here](https://github.com/OlegEfrem/queue-shapeless-excercise/blob/master/src/test/scala/test/EqTest.scala), with the test output:
      ```
      [info] EqTest:
      [info] Equivalence test
      [info]   product should
      [info]   - be equal to itself
      [info]   - be equal to another instance with same type and values
      [info]   - be different by values
      [info]   coproduct should
      [info]   - be equal to itself
      [info]   - be equal to another instance with same type and values
      [info]   - be different by values
      [info]   custom type should
      [info]   - be equal to itself
      [info]   - be equal to another instance with same type and values
      [info]   - be different by values
      [info]   - be different by types
      ```
* Code is auto formatted using [scalariform](https://github.com/sbt/sbt-scalariform);
* Coding style is enforced with [scalastyle](http://www.scalastyle.org/) and [wartremover](http://www.wartremover.org/);