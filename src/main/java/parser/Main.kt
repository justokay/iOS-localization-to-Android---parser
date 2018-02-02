package parser

import com.sun.org.apache.xerces.internal.dom.DeferredAttrImpl
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


fun main(args: Array<String>) {
    val localizedMap = parseXml(IosFileParser("Localizable_spain.strings").parse())

    try {
        createTranslatedXml(localizedMap)
    } catch (e: Exception) {
        println(e)
    }

}

fun createTranslatedXml(localizedMap: HashMap<String, String>) {
    val docFactory = DocumentBuilderFactory.newInstance()
    val docBuilder = docFactory.newDocumentBuilder()
    val doc = docBuilder.newDocument()
    val rootElement = doc.createElement("resources")

    doc.appendChild(rootElement)
    localizedMap.forEach { key, value ->
        val element = doc.createElement("string")
        val attr = doc.createAttribute("name")
        attr.value = key
        element.setAttributeNode(attr)
        element.appendChild(doc.createTextNode(value))
        rootElement.appendChild(element)
    }

    val transformerFactory = TransformerFactory.newInstance()
    val transformer = transformerFactory.newTransformer()
    val source = DOMSource(doc)
    val result = StreamResult(File("src/main/resources/strings_.xml"))
    transformer.transform(source, result)
}

private fun parseXml(transtale: HashMap<String, String>): HashMap<String, String> {

    val map: HashMap<String, String> = hashMapOf()
    var count: Int = 0
    XmlParser("strings.xml").parse()?.let { nodeList ->
        (0 until nodeList.length)
                .filter {
                    nodeList.item(it).attributes.getNamedItem("translatable")?.takeIf { it.nodeValue == "true" } == null
                }
                .forEach {
                    val item = nodeList.item(it)
                    transtale[item.textContent]?.let {
                        //                        println("> ${++count}:\n$it")
                        map[(item.attributes.getNamedItem("name") as DeferredAttrImpl).value] = it
                    }
                }
    }

    return map

}