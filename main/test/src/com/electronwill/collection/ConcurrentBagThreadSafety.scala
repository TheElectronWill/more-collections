package com.electronwill.collection

import java.util.concurrent.atomic.AtomicReference

import org.scalatest.flatspec._

class ConcurrentBagThreadSafety extends AnyFlatSpec {

  val bag = new ConcurrentBag[String]
  val threadNames = Seq("a", "b", "c")
  val count = 200

  "A ConcurrentBag" should "support insertion from multiple threads" in {
    val insertionThreads = threadNames.map(n => insertionThread(n, count))
    runAll(insertionThreads)
    assert(bag.nonEmpty)
    assert(bag.size == threadNames.size * count)
    checkInsertion(threadNames)
  }

  it should "support removal from multiple threads" in {
    val removalThreads = threadNames.map(_ => removalThread(count))
    runAll(removalThreads)
    assert(bag.isEmpty)
  }

  it should "support insertion again (after removal)" in {
    val insertionThreads = threadNames.map(n => insertionThread(n, count))
    runAll(insertionThreads)
    assert(bag.nonEmpty)
    assert(bag.size == threadNames.size * count)
    checkInsertion(threadNames)
  }

  it should "support concurrent insertions, removals and reads" in {
    val lastRead = new AtomicReference[String]("none")
    val removeThread = new Thread(() => {
      val it = bag.iterator
      while (it.hasNext) {
        val next = it.next()
        assert(next ne null)
        //println(s"remove $next")
        it.remove()
      }
    })
    val readThread = new Thread(() => {
      val it = bag.iterator
      while (it.hasNext) {
        val next = it.next()
        assert(next ne null)
        lastRead.set(next)
        //println(s"read $next, size ${bag.size}")
      }
    })
    val addThread = insertionThread("i", 5000)
    val miscThreads = Seq(addThread, removeThread, readThread)
    runAll(miscThreads)
  }

  it should "be clearable" in {
    bag.clear()
    assert(bag.isEmpty)
  }

  private def runAll(threads: Seq[Thread]): Unit = {
    threads.foreach(_.start)
    threads.foreach(_.join)
  }

  private def removalThread(count: Int) = {
    new Thread(() => {
      var i = 0
      while (i < count) {
        bag.remove(0)
        i += 1
      }
    })
  }

  private def insertionThread(threadName: String, count: Int) = {
    new Thread(() => {
      var i = 0
      while (i < count) {
        val str = s"$threadName$i"
        //println("add " + str)
        bag += str
        i += 1
      }
    })
  }

  private def checkInsertion(names: Seq[String]): Unit = {
    for (n <- names) {
      var i = 0
      while (i < count) {
        assert(bag.contains(s"$n$i"))
        i += 1
      }
    }
  }
}
