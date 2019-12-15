package com.electronwill.collection

import org.scalatest.flatspec.AnyFlatSpec

class RecyclingIndexSpec extends AnyFlatSpec {
  def addToEmpty(idx: Index[String]): Unit = {
    assert(idx.isEmpty)

    idx += "a"
    assert(idx.nonEmpty)
    assert(idx.size == 1)
    assert(idx.head == (0, "a"))

    val bId = idx += "b"
    val cId = idx += "c"
    assert(idx.nonEmpty)
    assert(idx.size == 3)
    assert(bId == 1)
    assert(cId == 2)
    assert(idx(bId) == "b")
    assert(idx(cId) == "c")
  }

  def makeAndFillGap(idx: Index[String]): Unit = {
    assert(idx.size == 3)

    idx.remove(1)
    assert(idx.size == 2)

    val dId = idx += "d"
    assert(idx.size == 3)
    assert(dId == 1)

    val eId = idx += "e"
    assert(idx.size == 4)
    assert(eId == 3)

    idx.remove(0)
    assert(idx.size == 3)
    assert((idx += "f") == 0)
    assert((idx += "g") == 4)
    assert(idx.size == 5)
  }

  def update(idx: Index[String]): Unit = {
    val s = idx.size

    idx(0) = "z"
    assert(idx(0) == "z")
    assert(idx.size == s)

    idx(idx.size-1) = "Z"
    assert(idx(idx.size-1) == "Z")
    assert(idx.size == s)
  }


  val index = new RecyclingIndex[String]()
  val concurrent = new ConcurrentRecyclingIndex[String]()

  "A RecyclingIndex" should "support adding elements" in addToEmpty(index)
  it should "reuse ids to fill gaps" in makeAndFillGap(index)
  it should "update correctly" in update(index)

  "A ConcurrentRecyclingIndex" should "support adding elements" in addToEmpty(concurrent)
  it should "reuse ids to fill gaps" in makeAndFillGap(concurrent)
  it should "update correctly" in update(concurrent)
}
