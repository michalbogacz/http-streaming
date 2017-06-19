package com.github.michalbogacz.http.streaming

case class PageParams(pageNumber: Option[Int], pageSize: Option[Int]) {
  require(pageNumber.forall(size ⇒ size > 0), "page number must be greater then 0")
  require(pageSize.forall(size ⇒ size > 0 && size < 1000), "page size must be greater then 0 and less then 1000")

  private val DefaultPageNumber = 1
  private val DefaultPageSize = 16

  val limit: Int = pageSize.getOrElse(DefaultPageSize)
  val skip: Int = (pageNumber.getOrElse(DefaultPageNumber) - 1) * limit
}
