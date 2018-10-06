package com.practicingtechie.s_w_cats


package ch3_1 {
  import cats.Functor
  import cats.syntax.functor._

  sealed trait Tree[+A]
  final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
  final case class Leaf[A](value: A) extends Tree[A]

  object Ch3_1 {
    implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
      override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
        case Branch(l, r) => Branch(map(l)(f), map(r)(f))
        case Leaf(v) => Leaf(f(v))
      }
    }

    def t1() = {
      val t: Tree[Int] = Branch(Branch(Leaf(5), Leaf(8)), Branch(Leaf(2), Leaf(9)))
      t.map(_ + 9)
    }
  }

}

package ch3_2 {

  object Ch3_2 {

    trait Printable[A] {
      self =>

      def format(value: A): String

      def contramap[B](func: B => A): Printable[B] = new Printable[B] {
        def format(value: B): String = self.format(func(value))
      }
    }

    def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)

    implicit val stringPrintable: Printable[String] = new Printable[String] {
      def format(value: String): String = s""""$value""""
    }

    implicit val booleanPrintable: Printable[Boolean] = new Printable[Boolean] {
      def format(value: Boolean): String = if (value) "yes" else "no"
    }

    /*
    implicit def boxPrintable[A](implicit p: Printable[A]): Printable[Box[A]] = new Printable[Box[A]] {
      def format(b: Box[A]): String = p.format(b.value)
    }
    */
    implicit def boxPrintable[A](implicit p: Printable[A]): Printable[Box[A]] =
      p.contramap[Box[A]](ba => ba.value)

    final case class Box[A](value: A)

    format(Box("hello world"))
  }

}

package ch3_3 {

  object Ch3_3 {
    trait Codec[A] {
      self =>
      def encode(value: A): String
      def decode(value: String): A
      def imap[B](dec: A => B, enc: B => A): Codec[B] = new Codec[B] {
        override def encode(value: B): String = self.encode(enc(value))
        override def decode(value: String): B = dec(self.decode(value))
      }
    }
    def encode[A](value: A)(implicit c: Codec[A]): String = c.encode(value)
    def decode[A](value: String)(implicit c: Codec[A]): A = c.decode(value)

    implicit val stringCodec: Codec[String] =
      new Codec[String] {
        def encode(value: String): String = value
        def decode(value: String): String = value
      }

    implicit val intCodec: Codec[Int] = stringCodec.imap(_.toInt, _.toString)
    implicit val booleanCodec: Codec[Boolean] = stringCodec.imap(_.toBoolean, _.toString)
    implicit val doubleCodec: Codec[Double] = stringCodec.imap(_.toDouble, _.toString)


    case class Box[A](value: A)
    implicit def boxCodec[A](implicit c: Codec[A]): Codec[Box[A]] = stringCodec.imap(s => Box(c.decode(s)), _.value.toString)
  }

}

