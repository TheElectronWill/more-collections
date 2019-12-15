package com.electronwill.collection

import org.scalatest.flatspec.AnyFlatSpec

trait StorageBehavior {
  this: AnyFlatSpec =>
  def testInit(sto: CompactStorage, bpv: Int, size: Int, byteSize: Int): Unit = {
    assert(sto.bitsPerValue == bpv)
    assert(sto.size == size)
    assert(sto.byteSize == byteSize)
  }

  def ensureZeroes(sto: CompactStorage): Unit = {
    assert(sto.bytes.forall(_ == 0))
    assert(sto(0) == 0)
    assert(sto(1) == 0)
    assert(sto(2) == 0)
  }

  def fill(sto: CompactStorage): Unit = {
    sto.fill(1)
    assert(sto(0) == 1)
    assert(sto(1) == 1)
    assert(sto(2) == 1)
    assert(sto.bytes.exists(_ != 0))

    if (sto.maxValue >= 5) {
      sto.fill(5)
      assert(sto(0) == 5)
      assert(sto(1) == 5)
      assert(sto(2) == 5)
      assert(sto.bytes.exists(_ != 0))
    }

    sto.fill(0)
    assert(sto(0) == 0)
    assert(sto(1) == 0)
    assert(sto(2) == 0)
    assert(sto.bytes.forall(_ == 0))
  }

  def setOne(sto: CompactStorage): Unit = {
    sto(1) = 1
    assert(sto(0) == 0)
    assert(sto(1) == 1)
    assert(sto(2) == 0)

    sto(0) = 1
    assert(sto(0) == 1)
    assert(sto(1) == 1)
    assert(sto(2) == 0)

    sto(2) = 1
    assert(sto(0) == 1)
    assert(sto(1) == 1)
    assert(sto(2) == 1)
  }

  def setZero(sto: CompactStorage): Unit = {
    sto(1) = 0
    assert(sto(0) == 1)
    assert(sto(1) == 0)
    assert(sto(2) == 1)

    sto(0) = 0
    assert(sto(0) == 0)
    assert(sto(1) == 0)
    assert(sto(2) == 1)

    sto(2) = 0
    assert(sto(0) == 0)
    assert(sto(1) == 0)
    assert(sto(2) == 0)
  }

  def setMax(sto: CompactStorage): Unit = {
    val maxValue = sto.maxValue
    sto(1) = maxValue
    assert(sto(0) == 0)
    assert(sto(1) == maxValue)
    assert(sto(2) == 0)

    sto(2) = maxValue
    assert(sto(0) == 0)
    assert(sto(1) == maxValue)
    assert(sto(2) == maxValue)

    sto(0) = maxValue
    assert(sto(0) == maxValue)
    assert(sto(1) == maxValue)
    assert(sto(2) == maxValue)
  }

  def compactStorage(storage: CompactStorage, bpv: Int, size: Int, byteSize: Int): Unit = {
    it should "know itself properly" in testInit(storage, bpv, size, byteSize)
    it should "be zero when created" in ensureZeroes(storage)
    it should "fill correctly" in fill(storage)
    it should "support .set(1)" in setOne(storage)
    it should "support .set(0)" in setZero(storage)
    it should "support .set(maxValue)" in setMax(storage)
  }
}
