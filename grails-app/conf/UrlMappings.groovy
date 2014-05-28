class UrlMappings {

	static mappings = {

        "/slots/setup"(controller: "rest", action: "getSlotsSetup")

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
