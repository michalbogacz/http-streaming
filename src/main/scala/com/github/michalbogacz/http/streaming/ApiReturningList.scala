package com.github.michalbogacz.http.streaming

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import com.mongodb.reactivestreams.client.{MongoClients, MongoCollection}
import org.bson.Document

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn

object ApiReturningList extends ServiceDirectives {

  implicit val system = ActorSystem()
  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
  implicit val mat = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    val mongoClient = MongoClients.create()
    val coll = mongoClient.getDatabase("test").getCollection("resources")

    val route =
      path("resources") {
        pageParams { pageParams =>
          get {
            complete(getData(coll, pageParams).map(HttpEntity(ContentTypes.`application/json`, _)))
          }
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

  }

  def getData(coll: MongoCollection[Document], pageParams: PageParams): Future[String] =
    Source.fromPublisher(coll.find().skip(pageParams.skip).limit(pageParams.limit))
      .map(_.toJson)
      .intersperse("[", ",", "]")
      .runFold("")((acc, e) ⇒ acc + e)


}
