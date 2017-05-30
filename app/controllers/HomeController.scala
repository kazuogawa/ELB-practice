package controllers

import javax.inject._

import play.api._
import play.api.mvc._
import org.joda.time.DateTime
import scalikejdbc._

@Singleton
class HomeController @Inject() extends Controller {
  implicit val session = AutoSession

  def index = Action {
    models.Accesscount.create(new DateTime)
    Ok(views.html.index("Your aws1 is ready."))
  }
}
