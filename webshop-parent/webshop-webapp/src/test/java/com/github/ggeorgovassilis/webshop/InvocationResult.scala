package com.github.ggeorgovassilis.webshop

import com.github.ggeorgovassilis.webshop.model.ValidationErrorsDTO

class InvocationResult[T](statusParam:Integer, obj:Option[T], errorsParam:Option[ValidationErrorsDTO]) {

  val status = statusParam
  val value = obj
  val errors = errorsParam

}