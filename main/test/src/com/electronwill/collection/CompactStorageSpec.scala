package com.electronwill.collection

import org.scalatest.flatspec.AnyFlatSpec

class CompactStorageSpec extends AnyFlatSpec with StorageBehavior {

  "A storage with 4 bits per value" should "be specialized" in {
    assert(CompactStorage(4, 3).isInstanceOf[CompactStorage4])
  }
  it should behave like compactStorage(CompactStorage(4, 3), 4, 3, byteSize = 2)

  // ---
  "A storage with 8 bits per value" should "be specialized" in {
    assert(CompactStorage(8, 3).isInstanceOf[CompactStorage8])
  }
  it should behave like compactStorage(CompactStorage(8, 3), 8, 3, byteSize = 3)

  // ---
  "A storage with 16 bits per value" should "be specialized" in {
    assert(CompactStorage(16, 3).isInstanceOf[CompactStorage16])
  }
  it should behave like compactStorage(CompactStorage(16, 3), 16, 3, byteSize = 6)

  // ---
  "A storage with 1 bit per value" should "be general" in {
    assert(CompactStorage(1, 3).isInstanceOf[CompactStorageN])
  }
  it should behave like compactStorage(CompactStorage(1, 3), 1, 3, byteSize = 1)

  // ---
  "A storage with 3 bits per value" should "be general" in {
    assert(CompactStorage(3, 3).isInstanceOf[CompactStorageN])
  }
  it should behave like compactStorage(CompactStorage(3, 3), 3, 3, byteSize = 2)

  // ---
  "A storage with 11 bits per value" should "be general" in {
    assert(CompactStorage(11, 3).isInstanceOf[CompactStorageN])
  }
  it should behave like compactStorage(CompactStorage(11, 3), 11, 3, byteSize = 5)

  //
  "A storage with 12 bits per value" should "be general" in {
    assert(CompactStorage(12, 3).isInstanceOf[CompactStorageN])
  }
  it should behave like compactStorage(CompactStorage(12, 3), 12, 3, byteSize = 5)

  // ---
  "A storage with 32 bits per value" should "be general" in {
    assert(CompactStorage(32, 3).isInstanceOf[CompactStorageN])
  }
  it should behave like compactStorage(CompactStorage(32, 3), 32, 3, byteSize = 12)
}
