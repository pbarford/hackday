package hackday.hackday

import com.paddypower.hackday.HackdayService

/**
 * Created by paulo on 28/05/14.
 */
class JsonSlurper {

    def prefix = "/home/paulo/code/github/hackday/src/groovy/"

    def readFile(file) {
        new File(file).text
    }

    def processMarket(eventName, marketName) {
        "{ \"name\" : \"" + marketName +"\", " + generateSelections(prefix + eventName + "-" + marketName + ".html") + "}"
    }

    def generateSelections(selectionFile) {
        def html = readFile(selectionFile)
        def table = new XmlSlurper().parseText(html)
        def json = "\"selections\" : ["
        def first = true;
        table.tr.each { row ->
            def selectionName = ""
            def price = ""
            row.td.each { td ->
                if(td.@id.text().contains("_name")) selectionName = td
                if(td.@id.text().contains("_PP")) price = td
            }
            if(price!="") {
                if(!first) json+=","
                json += "{ \"name\" : \"" + selectionName +"\", \"price\" : " + price + "}"
                first = false
            }
        }
        json += "]"
    }

    def processEvent(eventName, epochStartTime) {
        def json = "{ \"name\" : \"" + eventName + "\", \"time\" : " + epochStartTime + ", \"markets\" : ["
        json += processMarket(eventName, "win-draw-win") + ","
        json += processMarket(eventName, "first-goalscorer") + ","
        json += processMarket(eventName, "correct-score")
        json += "]}"
    }

    def processEvents() {
        def json = "{ \"events\" : ["
        json += processEvent("brazil-croatia", 1402606800000) + ","
        json += processEvent("mexico-cameroon", 1402678800000)+ ","
        json += processEvent("spain-holland", 1402689600000) + ","
        json += processEvent("chile-australia", 1402700400000)
        json += "]}"
    }

    public static void main(String[] args) {
        //def slurper = new JsonSlurper();
        //println(slurper.processEvents())
        def s  = new HackdayService()
        def ops = s.availableOptions
        println(ops.slots[0].eventId)
        println(ops.slots[0].eventName)
    }

}
