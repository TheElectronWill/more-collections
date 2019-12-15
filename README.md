# ElectronWill's collections

[![Build Status](https://img.shields.io/travis/com/TheElectronWill/more-collections?logo=travis)](https://travis-ci.com/TheElectronWill/more-collections)
[![Maven Central](https://img.shields.io/maven-central/v/com.electronwill/more-collections_2.13?logo=apache)](https://search.maven.org/search?q=g:com.electronwill%20AND%20a:more-collections*)
![Scala Versions](https://img.shields.io/badge/scala%20versions-2.12%20%7C%202.13-%23C22D40?logo=scala)

Some optimized Scala collections that I use in several projects.

## Bag
A Bag is a collection optimized for removal. Removing an element in the middle of a Bag moves the last element to "fill the gap" and costs O(1).

Implementations: `SimpleBag`, `ConcurrentBag`  
These implementations do not support value types, ie no primitives.

## Index
An Index associates integer IDs to the elements you add in it.

Implementations: `RecyclingIndex`, `ConcurrentRecyclingIndex`  
For those implementations, get, add, update and remove are O(1).  
Recycling indexes try to keep the maximum ID as small as possible by reusing the IDs of the elements that have been removed.

## CompactStorage
A CompactStorage is an array of unsigned values of arbitrary bit size. Each storage defines how many bits a value takes, and stores them in a contiguous way. For instance, if you create a storage with 4 bits per value, 2 values will take exactly 1 byte.