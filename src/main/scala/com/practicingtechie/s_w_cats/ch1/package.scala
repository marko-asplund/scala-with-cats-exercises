package com.practicingtechie.s_w_cats


package ch1 {

  trait Printable[A] {
    def format(value: A): String
  }

  final case class Cat(name: String, age: Int, color: String)


  object PrintableInstances {
    implicit val printableString: Printable[String] = new Printable[String] {
      def format(value: String) = value
    }

    implicit val printableInt: Printable[Int] = new Printable[Int] {
      def format(value: Int) = value.toString
    }

    implicit val printableCat: Printable[Cat] = new Printable[Cat] {
      def format(c: Cat) = s"${c.name} is a ${c.age} year-old ${c.color} cat."
    }
  }

  object Printable {
    def format[A](value: A)(implicit printable: Printable[A]) = printable.format(value)
    def print[A](value: A)(implicit printable: Printable[A]): Unit = println(format(value))
  }

  object PrintableSyntax {
    implicit class PrintableOps[A](a: A) {
      def format(implicit p: Printable[A]): String = p.format(a)
      def print(implicit p: Printable[A]): Unit = println(format)
    }
  }

  object Main {

    val cat = Cat("willy", 2, "white")

    def ex1() = {
      import PrintableInstances._

      Printable.print(cat)
    }

    def ex2() = {
      import PrintableInstances._
      import PrintableSyntax._

      cat.print
    }

    def ex3() = {
      import cats._
      import cats.implicits._

      implicit val intShow = Show.fromToString[Int]
      implicit val stringShow = Show.show[String](s => s)
      implicit val catShow = Show.show[Cat](c => s"${c.name} is a ${c.age} year-old ${c.color} cat.")

      println(cat.show)
    }

    def ex4() = {
      import cats.Eq
      import cats.syntax.eq._
      import cats.instances.option._
      import cats.instances.int._
      import cats.instances.string._

      implicit val catEq: Eq[Cat] = Eq.instance[Cat] { (c1, c2) =>
        c1.name === c2.name && c1.age === c2.age && c1.color === c2.color
      }

      val cat1 = Cat("Garfield",   38, "orange and black")
      val cat2 = Cat("Heathcliff", 33, "orange and black")

      println(cat1 === cat2)

      val optionCat1 = Option(cat1)
      val optionCat2 = Option.empty[Cat]
      println(optionCat1 === optionCat2)

      println(cat1 === cat1)
    }

    def main(args: Array[String]): Unit = {
      println("ex1")
      ex1()
      println("ex2")
      ex2()
      println("ex3")
      ex3()
      println("ex4")
      ex4()
    }
  }

}
