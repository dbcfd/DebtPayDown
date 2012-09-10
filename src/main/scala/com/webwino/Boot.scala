package com.webwino

import akka.actor.{Props, ActorSystem}

import org.scalatra._
import scalate.ScalateSupport

class Boot(system:ActorSystem) extends ScalatraServlet with ScalateSupport {
  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }

  notFound {
    // Try to render a ScalateTemplate if no route matched
    findTemplate(requestPath) map { path =>
      contentType = "text/html"
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound() 
  }
  
  system.registerOnTermination {
    //put cleanup code here
    system.log.info("Application shut down")
  }
}