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

package io.github.andremion.slidingpuzzle.domain.search

import io.github.andremion.slidingpuzzle.domain.collections.Heap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

class AstarSearch<Data>(
    private val heuristics: (data: Data) -> Int,
    private val successors: (data: Data) -> List<Data>
) {

    data class Node<Data>(
        val data: Data,
        val parent: Node<Data>?,
        val g: Int,
        val h: Int,
    ) {
        val f: Int = g + h

        override fun equals(other: Any?): Boolean =
            data == (other as? Node<*>)?.data

        override fun hashCode(): Int =
            data.hashCode()
    }

    // Frontier of the search tree
    private val frontierHeap = Heap(compareBy(Node<Data>::f))

    // Already expanded nodes
    private val exploredSet = mutableSetOf<Node<Data>>()
//    private val exploredSet = HashMap<Data, Node<Data>>()

    @Throws(CancellationException::class, IllegalStateException::class)
    suspend fun performSearch(
        start: Data,
        goal: Data,
    ): List<Data> = withContext(Dispatchers.Default) {
        frontierHeap.clear()
        exploredSet.clear()

        val root = Node(
            data = start,
            parent = null,
            g = 0,
            h = heuristics(start)
        )
        frontierHeap.add(root)

        var iterations = 0

        var node: Node<Data>? = null
        while (frontierHeap.isNotEmpty()) {
            ensureActive()
            println("iteration: ${++iterations}")

            node = getNextNode()

            if (node?.data == goal) {
                break
            } else {
                node?.expand()
            }
        }

        if (node == null) {
            error("No solution found")
        }

        val data = node.backtrackPath()
            .map(Node<Data>::data)
        println("path: ${data.mapIndexed { index, state -> "$state => g: $index" }}")

        data
    }

    private fun getNextNode(): Node<Data>? {
        val node = frontierHeap.poll()
        if (node != null) exploredSet.add(node)
//        if (node != null) exploredSet[node.data] = node
        return node
    }

    private fun Node<Data>.expand() {
        val successors = successors(data)
            .map { successorData ->
                Node(
                    data = successorData,
                    parent = this,
                    g = g + 1,
                    h = heuristics(successorData)
                )
            }.filter { successorNode ->
                successorNode !in exploredSet
//                val value = exploredSet[successorNode.data]
//                if (value != null) {
//                    if (successorNode.f < value.f) {
//                        exploredSet.remove(successorNode.data)
//                        exploredSet[successorNode.data] = successorNode
//                        true
//                    } else {
//                        false
//                    }
//                } else {
//                    true
//                }
            }
        frontierHeap.addAll(successors)
    }
}

/**
 * Backtrack path from the current node to the start node.
 */
private fun <Data> AstarSearch.Node<Data>.backtrackPath(): List<AstarSearch.Node<Data>> {
    var current: AstarSearch.Node<Data>? = this
    val path = ArrayDeque<AstarSearch.Node<Data>>()
    while (current != null) {
        path.addFirst(current)
        current = current.parent
    }
    return path
}
