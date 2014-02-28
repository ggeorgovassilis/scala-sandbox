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

abstract class BaseScalaWebserviceTest extends BaseScalaTest {

  val mapper = new ObjectMapper()
  var dispatcherServlet: DispatcherServlet = null

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

  def fromJson[T](response: MockHttpServletResponse, c: TypeReference[T]):T = {
    mapper.readValue(response.getContentAsString(), c)
  }

  def get[T](url: String, c:Class[T]):Option[T] = {
    val request = new MockHttpServletRequest(dispatcherServlet.getServletContext(), "GET", url);
    val response = new MockHttpServletResponse();
    invoke(request, response);
    val result = fromJson(response, c)
    if (result.isDefined) result
    else None
  }

  def get[T](url:String, c:TypeReference[T]):T={
    val request = new MockHttpServletRequest(dispatcherServlet.getServletContext(), "GET", url);
    val response = new MockHttpServletResponse();
    invoke(request, response);
    fromJson(response, c)
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