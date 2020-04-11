package controllers

import javax.inject._
import models.User
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import scala.collection.mutable.ListBuffer

@Singleton
class FormController @Inject()(messagesAction: MessagesActionBuilder, cc: ControllerComponents) extends AbstractController(cc) {

  val userForm = Form(
    mapping("name" -> text,
      "email" -> email,
      "age" -> number
    )(User.apply)(User.unapply)
  )

  val users = new ListBuffer[User]()

  def index() = messagesAction { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.index(userForm,users.toList))
  }
  def create() = messagesAction { implicit request: MessagesRequest[AnyContent] =>
    userForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.index(formWithErrors, users.toList)),
      userData => {
          users.addOne(userData)
          Redirect(routes.FormController.index())
      }
    )
  }
}
