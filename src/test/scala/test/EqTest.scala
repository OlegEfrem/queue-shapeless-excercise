package test

import org.scalatest.FreeSpec
import shapeless.{:+:, ::, CNil, Coproduct, Generic, HNil}


class EqTest extends FreeSpec {

  import ImplicitOps._

  "Equivalence test" - {

    "product should" - {
      type Product = Int :: String :: Boolean :: Double :: HNil
      val productEq = Eq[Product]

      val product1: Product = 1 :: "abc" :: false :: 0.0 :: HNil
      val product2: Product = 2 :: "def" :: true :: 1.0 :: HNil
      val product3: Product = 2 :: "def" :: true :: 1.0 :: HNil

      "be equal to itself" in {
        assert(product1 ==== product1)
        assert(product2 ==== product2)
      }

      "be equal to another instance with same type and values" in {
        assert(product2 ==== product3)
      }

      "be different by values" in {
        assert(product1 !=== product2)
      }
    }

    "coproduct should" - {
      type CoProduct = Int :+: String :+: Boolean :+: Double :+: CNil
      val coProductEq = Eq[CoProduct]

      val coproduct1 = Coproduct[CoProduct](3.0)
      val coproduct2 = Coproduct[CoProduct](false)
      val coproduct3 = Coproduct[CoProduct](false)

      "be equal to itself" in {
        assert(coproduct1 ==== coproduct1)
        assert(coproduct2 ==== coproduct2)
      }

      "be equal to another instance with same type and values" in {
        assert(coproduct2 ==== coproduct3)
      }

      "be different by values" in {
        assert(coproduct1 !=== coproduct2)
      }
    }

    "custom type should" - {
      sealed trait FooBar
      case class Foo(i: Int, s: String) extends FooBar
      case class Foo1(i: Int, s: String) extends FooBar
      case class Bar(b: Boolean, d: Double) extends FooBar
      val fooEq = Eq[Foo]
      val foo1Eq = Eq[Foo1]
      val barEq = Eq[Bar]
      val fooBarEq = Eq[FooBar]

      val foo1 = Foo(1, "abc")
      val bar1 = Bar(false, 0.0)
      val bar2 = Bar(true, 1.0)
      val bar3 = Bar(true, 1.0)

      val fooBar1: FooBar = foo1
      val fooBar2: FooBar = bar1
      val fooBar3: Foo1 = Generic[Foo1].from(Generic[Foo].to(foo1))

      "be equal to itself" in {
        assert(foo1 ==== foo1)
      }

      "be equal to another instance with same type and values" in {
        assert(bar2 ==== bar3)
      }

      "be different by values" in {
        assert(bar1 !=== bar2)
      }

      "be different by types" in {
        assert(fooBar1 !=== fooBar3)
      }
    }

  }
}
