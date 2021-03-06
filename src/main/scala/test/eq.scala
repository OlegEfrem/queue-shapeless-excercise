package test

import shapeless._

/*
  We want to automatically generate instances of a particular typeclass using the shapeless library.
  The typeclass we are interested in is the Eq typeclass which determines whether two given instances of a type are equal

  Part 1)
  Implement implicit Eq instances for the scala primitives Boolean, Double, Int, and String.

  Part 2)
  Given Eq instances of all types of an HList, generate an Eq instance for that HList.
  Note, the function signatures are incomplete. Additionally, you may need additional functions to make this work

  Part 3)
  Given Eq instances of all types of a Coproduct, generate an Eq instance for that Coproduct

  Part 4)
  Generate Eq instances of all ADTs (represented as case classes and sealed traits)

  Your code should compile and successfully run the code found in EqTest
  You may wish to add additional tests
 */

sealed trait Eq[T] {
  def eqv(t1: T, t2: T): Boolean

  def neqv(t1: T, t2: T): Boolean = !eqv(t1, t2)
}

object Eq {
  def apply[T](implicit ev: Eq[T]): Eq[T] = ev

  def instance[T](f: (T, T) => Boolean): Eq[T] = new Eq[T] {
    def eqv(t1: T, t2: T): Boolean = f(t1, t2)
  }

  // Part 1
  implicit val intEq: Eq[Int] = Eq.instance((x: Int, y: Int) => x == y)
  implicit val doubleEq: Eq[Double] = Eq.instance((x: Double, y: Double) => x == y)
  implicit val booleanEq: Eq[Boolean] = Eq.instance((x: Boolean, y: Boolean) => x == y)
  implicit val stringEq: Eq[String] = Eq.instance((x: String, y: String) => x == y)

  // Part 2
  implicit val eqHNil: Eq[HNil] = new Eq[HNil] {
    def eqv(x: HNil, y: HNil): Boolean = true
  }

  implicit def hconsEq[H, T <: HList](implicit eqH: Lazy[Eq[H]], eqT: Lazy[Eq[T]]): Eq[H :: T] =
    new Eq[H :: T] {
      def eqv(x: H :: T, y: H :: T): Boolean =
        eqH.value.eqv(x.head, y.head) && eqT.value.eqv(x.tail, y.tail)
    }

  // Part 3
  // $COVERAGE-OFF$
  implicit val eqCNil: Eq[CNil] = new Eq[CNil] {
    def eqv(x: CNil, y: CNil): Boolean = true
  }
  // $COVERAGE-ON$

  implicit def cconsEq[L, R <: Coproduct](implicit eqH: Lazy[Eq[L]], eqT: Lazy[Eq[R]]): Eq[L :+: R] =
    new Eq[L :+: R] {
      def eqv(x: L :+: R, y: L :+: R): Boolean =
        (x, y) match {
          case (Inl(xh), Inl(yh)) => eqH.value.eqv(xh, yh)
          case (Inr(xt), Inr(yt)) => eqT.value.eqv(xt, yt)
          case _ => false
        }
    }

  // Part 4
  implicit def genericEq[T, Repr](implicit gen: Generic.Aux[T, Repr], eqRepr: Lazy[Eq[Repr]]): Eq[T] =
    new Eq[T] {
      def eqv(x: T, y: T): Boolean =
        eqRepr.value.eqv(gen.to(x), gen.to(y))
    }

}

object ImplicitOps {

  implicit class EqOps[T](x: T)(implicit ev: Eq[T]) {
    def ====(y: T): Boolean = ev.eqv(x, y) // scalastyle:ignore

    def !===(y: T): Boolean = ev.neqv(x, y) // scalastyle:ignore
  }

}

