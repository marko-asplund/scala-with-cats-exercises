package com.practicingtechie.s_w_cats


package ch2_1 {

  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }
  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }
  object Monoid {
    def apply[A](implicit monoid: Monoid[A]) = monoid
  }


  object BoolMonoid {
    object BoolMonoid1 extends Monoid[Boolean] {
      override def combine(x: Boolean, y: Boolean): Boolean = x || y
      override def empty: Boolean = false
    }

    // (x || y) || z == x || (y || z) ==> associative

    object BoolMonoid2 extends Monoid[Boolean] {
      override def combine(x: Boolean, y: Boolean): Boolean = x && y
      override def empty: Boolean = true
    }

    // (x || y) || z == x || (y || z) ==> associative

  }

  object SetMonoid {
    class SetMonoid1[A] extends Monoid[Set[A]] {
      override def combine(x: Set[A], y: Set[A]): Set[A] = x | y
      override def empty: Set[A] = Set.empty
    }
    class SetMonoid2[A] extends Monoid[Set[A]] {
      override def combine(x: Set[A], y: Set[A]): Set[A] = x & y
      override def empty: Set[A] = ??? // the whole set
    }
  }

  object Main {
    def main(args: Array[String]): Unit = {
    }
  }
}

package ch2_2 {
  object SuperAdder {
    import cats.Monoid
    import cats.instances.int._    // for Monoid
    import cats.instances.string._
    import cats.instances.option._

    val intMonoid = Monoid[Int]
    val optIntMonoid = Monoid[Option[Int]]

    case class Order(totalCost: Double, quantity: Double)

    implicit object orderMonoid extends Monoid[Order] {
      override def empty: Order = Order(0, 0)
      override def combine(x: Order, y: Order): Order = Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
    }

    add(List(1,2,3))

    add(List(Some(1), Some(55), None))

    add(List.empty[Order])


    def add[A](items: List[A])(implicit m: Monoid[A]): A = items.reduce((e1, e2) => m.combine(e1, e2))
  }

}
