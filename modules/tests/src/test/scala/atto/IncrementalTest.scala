package atto
import Atto._

import org.scalacheck._

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements", "org.wartremover.warts.Any"))
object IncrementalTest extends Properties("Incremental") {
  import Prop._

  // list of ints chunked arbitrarily
  property("incremental/1") = forAll { (n: Int, ns0: List[Int], c: Char, s: String) =>
    val sep = s + s + s + c.toString
    !sep.exists(_.isDigit) ==> {
      val ns = n :: ns0 ++ ns0 ++ ns0
      val p = sepBy(int, string(sep))
      val s = ns.mkString(sep)
      val c = s.grouped(1 max (n % s.length).abs).toList
      c.foldLeft(p.parse(""))(_ feed _).done match {
        case ParseResult.Done("", `ns`) => true
        case _ => false
      }
    }
  }

}
