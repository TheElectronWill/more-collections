package com.electronwill.collection

import scala.collection.mutable
import scala.reflect.ClassTag

/**
 * A fast, simple `Map[Int, V]` based on a plain `Array[V]`.
 * Useful when the keys start at 0 and follow each other.
 *
 * @author TheElectronWill
 */
final class ArrayMap[@specialized(Int) V: ClassTag](
  initialCapacity: Int,
  private[this] val nullValue: V
) extends mutable.Map[Int, V] {

  /** Contains the elements of the ArrayMap. */
  private[this] var elements = new Array[V](initialCapacity)

  /** The number of (non-null) elements. */
  private[this] var elementCount = 0

  override def size: Int = elementCount
  override def default(key: Int): V = nullValue

  override def +=(kv: (Int, V)): this.type = {
    update(kv._1, kv._2)
    this
  }

  override def put(key: Int, value: V): Option[V] = {
    val previousValue = apply(key)
    update(key, value)
    if (nullValue == previousValue) None else Some(previousValue)
  }

  override def update(key: Int, value: V): Unit = {
    elementCount += 1
    if (key >= elements.length) {
      elements = growAmortize(elements, key + 1)
    }
    elements(key) = value
  }

  override def -=(key: Int): this.type = {
    if (key < elements.length) {
      doRemove(key)
    }
    this
  }

  override def remove(key: Int): Option[V] = {
    val previousValue = apply(key)
    if (previousValue == nullValue) {
      None
    } else {
      doRemove(key)
      Some(previousValue)
    }
  }

  def -=(key: Int, expectedValue: Int): this.type = {
    remove(key, expectedValue)
    this
  }

  def remove(key: Int, expectedValue: Int): Boolean = {
    val previousValue = apply(key)
    if (previousValue == expectedValue) {
      doRemove(key)
      true
    } else {
      false
    }
  }

  private def doRemove(key: Int): Unit = {
    elements(key) = nullValue
    elementCount -= 1
  }

  override def clear(): Unit = {
    var i = 0
    while (i < elements.length) {
      elements(i) = nullValue
      i += 1
    }
    elementCount = 0
  }

  override def get(key: Int): Option[V] = {
    if (key >= elements.length) {
      None
    } else {
      val v = elements(key)
      if (nullValue == v) None else Some(v)
    }
  }

  override def apply(key: Int): V = {
    if (key >= elements.length) nullValue else elements(key)
  }

  override def iterator: Iterator[(Int, V)] = new Iterator[(Int, V)] {
    // Iterates over (key,elem)
    private[this] var id = 0
    private[this] var nextElement: V = nullValue

    private def findNext(): Unit = {
      // Finds the next non-null element
      while (id < elements.length && (nullValue == nextElement)) {
        val v = elements(id)
        if (nullValue != v) {
          nextElement = v.asInstanceOf[V]
        }
        id += 1
      }
    }

    override def hasNext: Boolean = {
      if (nullValue == nextElement) {
        findNext()
      }
      nullValue == nextElement
    }

    override def next(): (Int, V) = {
      if (nullValue == nextElement) {
        findNext()
      }
      val e = nextElement
      nextElement = nullValue
      (id - 1, e)
    }

    override def size: Int = elementCount
  }
}
