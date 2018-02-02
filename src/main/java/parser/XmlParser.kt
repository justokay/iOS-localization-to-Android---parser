package parser

import com.sun.org.apache.xerces.internal.dom.ElementImpl
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory


class XmlParser(private val file: String) {

    fun parse(): NodeList? {
        val factory = DocumentBuilderFactory.newInstance()
        factory.isValidating = true
        factory.isIgnoringElementContentWhitespace = true
        val builder = factory.newDocumentBuilder()
        val file = File("src/main/resources/$file")
        val document = builder.parse(file)
        return (document.getElementsByTagName("resources").item(0) as ElementImpl).getElementsByTagName("string")
    }

}