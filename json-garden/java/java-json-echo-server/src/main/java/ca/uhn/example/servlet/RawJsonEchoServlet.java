import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.IResourceProvider;
import jakarta.servlet.http.HttpServlet;
import ca.uhn.fhir.context.FhirContext;
import java.util.ArrayList;
import java.util.List;
import ca.uhn.fhir.parser.IParser;
import jakarta.servlet.ServletException;
import ca.uhn.fhir.parser.JsonParser;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.commons.io.IOUtils;
import com.google.common.base.Charsets;
import ca.uhn.fhir.parser.json.jackson.JacksonStructure;
import ca.uhn.fhir.rest.server.RestfulServer;
import java.io.StringReader;
import ca.uhn.fhir.parser.json.JsonLikeStructure;
import ca.uhn.fhir.parser.json.BaseJsonLikeObject;
//import ca.uhn.fhir.model.api.IResource;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Parameters;
import java.io.StringWriter;
import ca.uhn.fhir.parser.json.BaseJsonLikeWriter;
import ca.uhn.fhir.parser.IParserErrorHandler;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.parser.StrictErrorHandler;
import org.hl7.fhir.instance.model.api.IBaseResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.util.List;

@WebServlet("/hapiecho")
public class RawJsonEchoServlet extends RestfulServer {
    private static final long serialVersionUID = 1L;
    private volatile IParserErrorHandler myParserErrorHandler = new LenientErrorHandler();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String body = IOUtils.toString(request.getInputStream(), Charsets.UTF_8);
	try {

	    Map<String, Object> map = new ObjectMapper().readValue(body, Map.class);
	    //eresponse.getWriter().write(new ObjectMapper().writeValueAsString(map));
	    response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(map));
	} catch (MismatchedInputException e) {
	    List<Object> array = new ObjectMapper().readValue(body, List.class);
	    //response.getWriter().write(new ObjectMapper().writeValueAsString(array));
	    response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(array));
	}
	response.getOutputStream().close();
	response.setContentType("text/plain; charset=UTF-8");
	//response.getWriter().close();
    }
}
