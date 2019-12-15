package com.electronwill.collection

import org.scalatest.flatspec._

class BagSpec extends AnyFlatSpec {
  def fill(bag: Bag[String]) = {
    bag += "a"
    bag += "b"
    bag += "abc"
    bag
  }

  def emptyBag(implicit bag: Bag[String]): Unit = {
    it should "be empty" in {
      assert(bag.isEmpty)
      assert(bag.size == 0)
      assert(bag.headOption == None)
      assert(bag.lastOption == None)
      assert(bag.indexOf("abc") == -1)
      assert(!bag.contains("abc"))
    }
  }

  def anyBag(implicit bag: Bag[String]): Unit = {
    it should "support adding elements" in {
      bag += "zzz"
      assert(bag.contains("zzz"))
      assert(bag.last == "zzz")

      bag += "zz2"
      assert(bag.contains("zz2"))
      assert(bag.last == "zz2")
    }

    it should "have a correct iterator" in {
      var idx = 0
      val it = bag.iterator
      assert(it.size == bag.size)
      while (it.hasNext) {
        assert(it.next() == bag(idx))
        idx += 1
      }
    }

    it should "be clearable" in {
      bag.clear()
      assert(bag.isEmpty)
    }
  }

  def nonEmptyBag(implicit bag: Bag[String]): Unit = {
    it should "not be empty" in {
      assert(bag.nonEmpty)
      assert(bag.size > 0)
      assert(bag.headOption.nonEmpty)
      assert(bag.lastOption.nonEmpty)
      assert(bag.indexOf("abc") > 0)
      assert(bag.contains("abc"))
      assert(bag(bag.indexOf("abc")) == "abc")
    }

    it should "support removing elements" in {
      val initialSize = bag.size
      bag.remove(0)
      assert(bag.size == initialSize-1)
    }
  }

  behavior of "A SimpleBag (when empty)"
  implicit var bag: Bag[String] = new SimpleBag[String]
  it should behave like emptyBag
  it should behave like anyBag

  behavior of "A SimpleBag (when filled)"
  bag = fill(new SimpleBag[String])
  it should behave like nonEmptyBag
  it should behave like anyBag

  behavior of "A ConcurrentBag (when empty)"
  bag = new ConcurrentBag[String]
  it should behave like emptyBag
  it should behave like anyBag

  behavior of "A ConcurrentBag (when filled)"
  bag = fill(new ConcurrentBag[String])
  it should behave like nonEmptyBag
  it should behave like anyBag
}
