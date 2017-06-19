package com.github.michalbogacz.http.streaming

import akka.http.scaladsl.server.{Directive1, Directives}

trait ServiceDirectives extends Directives {

  val pageParams: Directive1[PageParams] = parameters(('pageNumber.as[Int].?, 'pageSize.as[Int].?)).as(PageParams)
}
