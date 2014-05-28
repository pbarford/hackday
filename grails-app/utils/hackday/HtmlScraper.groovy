package hackday

class HtmlScraper {
    def prefix = "/home/paulo/code/github/hackday/src/groovy/"

    def readFile(file) {
        new File(file).text
    }

    def processMarket(eventName, marketName) {
        "{\"id\" : \"" + generateId() + "\", \"name\" : \"" + marketName +"\", " + generateSelections(prefix + eventName + "-" + marketName + ".html") + "}"
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
                json += "{ \"id\" : \"" + generateId() +"\", \"name\" : \"" + selectionName +"\", \"price\" : " + price + "}"
                first = false
            }
        }
        json += "]"
    }

    def generateId() {
        UUID.randomUUID().toString()
    }

    def processEvent(eventName, epochStartTime) {
        def json = "{ \"id\" : \"" + generateId() + "\", \"name\" : \"" + eventName + "\", \"time\" : " + epochStartTime + ", \"markets\" : ["
        json += processMarket(eventName, "win-draw-win") + ","
        json += processMarket(eventName, "first-goalscorer") + ","
        json += processMarket(eventName, "correct-score")
        json += "]}"
    }

    def processEvents() {
        def json = "{ \"events\" : ["
        //json += processEvent("brazil-croatia", 1402606800000) + ","
        json += processEvent("mexico-cameroon", 1402678800000)+ ","
        json += processEvent("spain-holland", 1402689600000) + ","
        json += processEvent("chile-australia", 1402700400000)
        json += "]}"
    }

}
