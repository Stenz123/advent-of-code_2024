package days.day24

import days.Day
import java.io.File
import java.lang.StringBuilder


class Day24: Day(false) {
    override fun partOne(): Any {
        val wires = readInput().takeWhile { it.isNotEmpty() }.map {
            val (name, value) = it.split(": ")
            Wire(name, value.toInt())
        }.toMutableList()

        val operations = readInput().takeLastWhile { it.isNotEmpty() }.map {
            val (wire1Name, operation, wire2Name, outPutName) = it.split(" -> ", " ")
            val (wire1, wire2, outPut) = listOf(wire1Name, wire2Name, outPutName).map { wireName ->
                wires.find { it.name == wireName } ?: Wire(wireName, null).also { wires.add(it) }
            }
            Operation(wire1, wire2, outPut, operation)
        }

        val resultWires = processWires(wires.toMutableList(), operations.toMutableList())
        val zValues = resultWires.filter { it.name.startsWith("z") }.sortedBy { it.name }.map { it.value!! }.joinToString("")
        println(zValues)


        return zValues.toLong(2)
    }

    override fun partTwo(): Any {
        System.setProperty("org.graphstream.ui", "swing")

        val wires = readInput().takeWhile { it.isNotEmpty() }.map {
            val (name, value) = it.split(": ")
            Wire(name, value.toInt())
        }.toMutableList()

        val operations = readInput().takeLastWhile { it.isNotEmpty() }.map {
            val (wire1Name, operation, wire2Name, outPutName) = it.split(" -> ", " ")
            val (wire1, wire2, outPut) = listOf(wire1Name, wire2Name, outPutName).map { wireName ->
                wires.find { it.name == wireName } ?: Wire(wireName, null).also { wires.add(it) }
            }
            Operation(wire1, wire2, outPut, operation)
        }

        processWires(wires, operations.toMutableList())


        fun getNeighbours(wire: Wire): MutableList<Wire> {
            val res = mutableListOf<Wire>()
            operations.forEach {
                when  {
                    it.wire1 == wire -> res.add(it.wire2)
                    it.wire2 == wire -> res.add(it.wire1)
                    it.outPut == wire -> res.add(it.outPut)
                }
            }
            return res
        }

        val countGroups = wires.map {
            it to getNeighbours(it).groupingBy { neighbour ->
            neighbour.toIdentifier(operations)
        }.eachCount()}

        val groups = wires.groupingBy {
            getNeighbours(it).groupingBy { neighbour ->
                neighbour.toIdentifier(operations)
            }.eachCount()
        }.eachCount().filter { it.value < 10 }

        val res =  countGroups.filter {
            it.second in groups.keys
        }.map { it.first.name }.toMutableList()
        res.removeAt(0)
        res.removeAt(0)

        res.add("z05")
        res.remove("qjc")
        res.remove("vqg")
        res.add("z11")
        res.remove("gmr")
        res.remove("chn")
        res.remove("chc")
        res.add("z35")
        visualize(wires, operations, res)

        return res.sorted().joinToString(",")

    }

    fun List<Wire>.getSum() = this.sortedBy { it.name }.map { it.value!! }.joinToString("").reversed()

    fun List<Operation>.swapOutputs(o1: String, o2: String) {
        swapOutPuts(this.first { it.outPut.name == o1 }, this.first { it.outPut.name == o2 })
    }
    fun swapOutPuts(o1: Operation, o2: Operation) {
        val temp = o1.outPut
        o1.outPut = o2.outPut
        o2.outPut = temp
    }

    fun parseInput(): Pair<MutableList<Wire>, List<Operation>> {
        val wires = readInput().takeWhile { it.isNotEmpty() }.map {
            val (name, value) = it.split(": ")
            Wire(name, value.toInt())
        }.toMutableList()

        val operations = readInput().takeLastWhile { it.isNotEmpty() }.map {
            val (wire1Name, operation, wire2Name, outPutName) = it.split(" -> ", " ")
            val (wire1, wire2, outPut) = listOf(wire1Name, wire2Name, outPutName).map { wireName ->
                wires.find { it.name == wireName } ?: Wire(wireName, null).also { wires.add(it) }
            }
            Operation(wire1, wire2, outPut, operation)
        }
        return wires to operations
    }

    fun processWires(wires: MutableList<Wire>, operations: MutableList<Operation>): List<Wire>{
        while(operations.isNotEmpty()){
            operations.filter { it.canExecute() }.forEach { operation ->
                operation.execute()
                operations.remove(operation)
            }
        }
        return wires
    }

    data class Wire(val name: String, var value: Int?){
        override fun toString(): String {
            return "$name: $value"
        }

        fun toIdentifier(operations: List<Operation>) = when {
            name.startsWith("z") -> "z"
            name.startsWith("y") -> "y"
            name.startsWith("x") -> "x"
            else -> operations.find { it.outPut == this }!!.operataion
        }
    }
    class Operation(val wire1: Wire, val wire2: Wire, var outPut: Wire, val operataion: String){
        fun canExecute() = wire1.value != null && wire2.value != null
        fun execute() = when (operataion){
                "AND" -> outPut.value = wire1.value!! and wire2.value!!
                "OR" -> outPut.value = wire1.value!! or wire2.value!!
                "XOR" -> outPut.value = wire1.value!! xor wire2.value!!
                 else -> {throw Exception("Invalid operation")}
            }


    }



    fun visualize(wires: List<Wire>, operations: List<Operation>, colored: List<String>) {
        val stringBuilder = StringBuilder()

        stringBuilder.appendLine(
            """
        digraph G {
            subgraph {
               node [style=filled,color=red]
                ${colored.joinToString(" ") { it }}
            }
            subgraph {
               node [style=filled,color=green]
                ${wires.filter{it.name.startsWith("z")}.joinToString(" ") { it.name }}
            }
            subgraph {
                node [style=filled,color=gray]
                ${wires.filter{it.name.startsWith("y")}.joinToString(" ") { it.name }}
            }
            subgraph {
                node [style=filled,color=gray]
                ${wires.filter{it.name.startsWith("x")}.joinToString(" ") { it.name }}
            }
            subgraph {
                node [style=filled,color=pink]
                ${operations.filter { op -> op.operataion == "AND" }.joinToString(" ") { op -> op.outPut.name }}
            }
            subgraph {
                node [style=filled,color=yellow];
                ${operations.filter { op -> op.operataion == "OR" }.joinToString(" ") { op -> op.outPut.name }}
            }
            subgraph {
                node [style=filled,color=lightblue];
                ${operations.filter { op -> op.operataion == "XOR" }.joinToString(" ") { op -> op.outPut.name }}
            }
            """.trimIndent()
        )
        operations.forEach {
            stringBuilder.appendLine("    ${it.wire1} -> ${it.outPut}")
            stringBuilder.appendLine("    ${it.wire2} -> ${it.outPut}")
        }
        stringBuilder.appendLine("}")

        val path = "src/main/kotlin/days/day24/"
        File("${path}graph.dot").writeText(stringBuilder.toString())
    }
}

