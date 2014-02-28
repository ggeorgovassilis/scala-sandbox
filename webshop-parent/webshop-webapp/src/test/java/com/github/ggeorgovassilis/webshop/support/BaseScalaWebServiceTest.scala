package com.github.ggeorgovassilis.webshop.support
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.core.`type`.TypeReference
import org.springframework.mock.web.MockServletConfig
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import com.github.ggeorgovassilis.webshop.model.ValidationErrorsDTO
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.databind.JsonMappingException
import com.github.ggeorgovassilis.webshop.model.ValidationErrorsDTO
import org.springframework.http.HttpStatus

abstract class BaseScalaWebserviceTest extends BaseScalaTest {

  val mapper = new ObjectMapper()
  var dispatcherServlet: DispatcherServlet = null
  
  val STATUS_OK = HttpStatus.OK.value();
  val STATUS_CREATED = HttpStatus.CREATED.value();
  val STATUS_NOT_FOUND = HttpStatus.NOT_FOUND.value();
  val STATUS_BAD = HttpStatus.BAD_REQUEST.value();

  def toJson(obj: Any) = mapper.writeValueAsBytes(obj)

  def fromJson[T](response: MockHttpServletResponse, c: java.lang.Class[T]):Option[T] = {
    try {
      return Some(mapper.readValue(response.getContentAsString(), c))
    } catch {
      case e: JsonMappingException => return None
      case e: UnrecognizedPropertyException => return None
    }
    return None
  }

  def fromJson[T](response: MockHttpServletResponse, c: TypeReference[T]):Option[T] = {
    try {
      return Some(mapper.readValue(response.getContentAsString(), c))
    } catch {
      case e: JsonMappingException => return None
      case e: UnrecognizedPropertyException => return None
    }
    return None
  }

  def invoke(request: MockHttpServletRequest, response: MockHttpServletResponse) {
    dispatcherServlet.service(request, response);
  }

  def post[T >: Null](url: String, obj: T): InvocationResult[T] = {
    val request = new MockHttpServletRequest(dispatcherServlet.getServletContext(), "POST", url);
    val content = toJson(obj)
    request.setContent(content)
    request.setContentType("application/json");
    val response = new MockHttpServletResponse();
    invoke(request, response);
    val returnedObject = fromJson(response, obj.getClass)
    val errors = fromJson(response, classOf[ValidationErrorsDTO])
    new InvocationResult(response.getStatus(), returnedObject, errors)
  }


  def get[T](url: String, c:Class[T]):InvocationResult[T] = {
    val request = new MockHttpServletRequest(dispatcherServlet.getServletContext(), "GET", url);
    val response = new MockHttpServletResponse();
    invoke(request, response);
    val returnedObject = fromJson(response, c)
    val errors = fromJson(response, classOf[ValidationErrorsDTO])
    new InvocationResult(response.getStatus(), returnedObject, errors)
  }

  def get[T](url:String, c:TypeReference[T]):InvocationResult[T]={
    val request = new MockHttpServletRequest(dispatcherServlet.getServletContext(), "GET", url);
    val response = new MockHttpServletResponse();
    invoke(request, response);
    val returnedObject = fromJson(response, c)
    val errors = fromJson(response, classOf[ValidationErrorsDTO])
    new InvocationResult(response.getStatus(), returnedObject, errors)
  }

  before {
    val config = new MockServletConfig("spring");
    config.addInitParameter(
      "contextConfigLocation",
      "classpath:webshop/environment-context.xml, classpath:webshop/application-context.xml");
    dispatcherServlet = new DispatcherServlet();
    dispatcherServlet.init(config);

    get("/api/reset",classOf[Object]);
  }

  after {
    dispatcherServlet.destroy();
  }

}