package com.paddypower.hackday

import groovy.json.JsonBuilder

class RestController  {
    static responseFormats = ['json']

    def hackdayService = new HackdayService()

    def getSlotsSetup = {
        println(params.dateFrom)
        println(params.eventType)
        def res = hackdayService.availableOptions
        render(text: new JsonBuilder(res).toString(), contentType: "application/json", encoding: "UTF-8")
    }

}
