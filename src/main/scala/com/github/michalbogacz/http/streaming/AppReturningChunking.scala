package com.github.michalbogacz.http.streaming

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.mongodb.reactivestreams.client.{MongoClients, MongoCollection}
import org.bson.Document

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object AppReturningChunking extends Directives {
  
  implicit val system = ActorSystem()
  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
  implicit val mat = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    val mongoClient = MongoClients.create()
    val coll = mongoClient.getDatabase("test").getCollection("resources")

    val route =
      path("resources") {
        get {
          complete(HttpEntity(ContentTypes.`application/json`, getData(coll)))
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

  }

  def getData(coll: MongoCollection[Document]): Source[ByteString, NotUsed] = {
    Source.fromPublisher(coll.find())
      .map(_.toJson)
      .map(ByteString(_))
      .intersperse(ByteString("["), ByteString(","), ByteString("]"))
  }

}
