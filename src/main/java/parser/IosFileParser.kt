package parser

import java.io.File
import java.io.InputStream


class IosFileParser(private val file: String) {

    fun parse(): HashMap<String, String> {

        val inputStream: InputStream = File("src/main/resources/$file").inputStream()
        val lineList = mutableListOf<String>()

        inputStream.bufferedReader().useLines { lines ->
            lines.filter { !it.startsWith("//") }
                    .filter { !it.isBlank() }
                    .forEach {
                        lineList.add(it)
                    }
        }
        val map = hashMapOf<String, String>()
        lineList.forEachIndexed { index, str ->
            val split = str.split("=")
            val key = split[0].trim().drop(1).dropLast(1)
            val value = split[1].trim().drop(1).dropLast(2)
//            println("> $index:\n$key\n$value")
            map[key] = value
        }

        return map

    }

}