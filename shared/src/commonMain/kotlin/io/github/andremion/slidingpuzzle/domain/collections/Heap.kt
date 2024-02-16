/*
 *    Copyright 2024. André Luiz Oliveira Rêgo
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.andremion.slidingpuzzle.domain.collections

class Heap<E>(
    private val comparator: Comparator<E>
) : AbstractMutableCollection<E>() {

    private val heap = mutableListOf<E>()

    override val size: Int
        get() = heap.size

    override fun iterator(): MutableIterator<E> =
        heap.iterator()

    override fun add(element: E): Boolean {
        val result = heap.add(element)
        heapifyUp(heap.lastIndex)
        return result
    }

    override fun addAll(elements: Collection<E>): Boolean {
        val result = heap.addAll(elements)
        for (index in heap.lastIndex downTo 0) {
            heapifyUp(index)
        }
        return result
    }

    override fun clear() {
        heap.clear()
    }

    override fun isEmpty(): Boolean =
        heap.isEmpty()

    fun poll(): E? {
        val lastIndex = heap.lastIndex
        if (lastIndex == -1) {
            return null
        }
        if (lastIndex == 0) {
            return heap.removeAt(lastIndex)
        }
        val element = heap[0]
        heap[0] = heap.removeAt(lastIndex)
        heapifyDown(0)
        return element
    }

    private fun heapifyUp(index: Int) {
        var currentIndex = index
        while (currentIndex > 0) {
            val parentIndex = (currentIndex - 1) / 2
            val a = heap[currentIndex]
            val b = heap[parentIndex]
            if (comparator.compare(a, b) >= 0) {
                break
            }
            swap(currentIndex, parentIndex)
            currentIndex = parentIndex
        }
    }

    private fun heapifyDown(index: Int) {
        var currentIndex = index
        while (true) {
            val leftIndex = currentIndex * 2 + 1
            val rightIndex = currentIndex * 2 + 2
            var smallestIndex = currentIndex
            if (leftIndex < heap.size && comparator.compare(heap[leftIndex], heap[smallestIndex]) < 0) {
                smallestIndex = leftIndex
            }
            if (rightIndex < heap.size && comparator.compare(heap[rightIndex], heap[smallestIndex]) < 0) {
                smallestIndex = rightIndex
            }
            if (smallestIndex == currentIndex) {
                break
            }
            swap(currentIndex, smallestIndex)
            currentIndex = smallestIndex
        }
    }

    private fun swap(index1: Int, index2: Int) {
        val temp = heap[index1]
        heap[index1] = heap[index2]
        heap[index2] = temp
    }
}
