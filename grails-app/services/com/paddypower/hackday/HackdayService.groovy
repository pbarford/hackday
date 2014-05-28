package com.paddypower.hackday

import com.paddypower.hackday.domain.AvailableOptions
import com.paddypower.hackday.domain.Option
import com.paddypower.hackday.domain.Slot
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import hackday.HtmlScraper


class HackdayService {

    def jsonEvents = new HtmlScraper().processEvents()

    def getAvailableOptions() {
        def availableOptions = new AvailableOptions()
        availableOptions.eventDescription = "2014 World Cup"
        availableOptions.slots = getSlotsFrom()
        availableOptions
    }

    def getSlotsFrom() {
        def jsonSlurper = new JsonSlurper()
        def json = jsonSlurper.parseText(jsonEvents)
        def slots = []
        json.events.each{ ev ->
            slots << generateSlot(ev)
        }
        return slots
    }

    def generateSlot(ev) {
        def slot = new Slot()
        slot.eventId = ev.id
        slot.eventName = ev.name.replace('-', ' v ')

        def options = []
        ev.markets.each { market ->
            if(market.name.equalsIgnoreCase("win-draw-win")) {
                options << generateOption(market.id, "football_" + ev.name.split("-")[0])
                options << generateOption(market.id, "football_" + ev.name.split("-")[1])
                options << generateOption(market.id, "football_draw")
            } else {
                options << generateOption(market.id, "football_" + market.name)
            }
        }
        slot.options = options
        return slot
    }

    def generateOption(id, description) {
        def option = new Option()
        option.id = id
        option.description = description
        println(option.id + ": " + option.description)
        return option
    }

    public static void main(String[] args) {
        def s  = new HackdayService()
        AvailableOptions ops = s.availableOptions

        println(ops.eventDescription)
        ops.slots.each { slot ->
            println ("--> eventId:" + slot.eventId + ", name: " + slot.eventName)
            slot.options.each { option ->
                println ("-->--> optionId:" + option.id + ", name: " + option.description)
            }
        }
        println(new JsonBuilder(ops.slots[0].options[0]).toString())
    }
}
