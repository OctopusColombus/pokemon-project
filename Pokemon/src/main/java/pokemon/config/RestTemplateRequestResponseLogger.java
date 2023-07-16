package pokemon.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RestTemplateRequestResponseLogger implements ClientHttpRequestInterceptor {
    private final Logger log = LoggerFactory.getLogger(RestTemplateRequestResponseLogger.class);

    public RestTemplateRequestResponseLogger() {
    }

    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        this.logRequest(httpRequest, bytes);
        ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, bytes);
        this.logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) {
        if (this.log.isDebugEnabled()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n').append("=======================request begin================================================").append('\n');
            stringBuilder.append("URI         : {").append(request.getURI()).append("}\n");
            stringBuilder.append("Method      : {").append(request.getMethod()).append("}\n");
            stringBuilder.append("Headers     : {").append(request.getHeaders()).append("}\n");
            stringBuilder.append("Request body: {").append(new String(body, StandardCharsets.UTF_8)).append("}\n");
            stringBuilder.append("=======================request end==================================================");
            this.log.debug(stringBuilder.toString());
        }

    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        if (this.log.isDebugEnabled()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n').append("=======================response begin===============================================").append('\n');
            stringBuilder.append("Status code  : {").append(response.getStatusCode()).append("}\n");
            stringBuilder.append("Status text  : {").append(response.getStatusText()).append("}\n");
            stringBuilder.append("Headers      : {").append(response.getHeaders()).append("}\n");
            if (response.getHeaders().getContentLength() > 0L) {
                MediaType contentType = response.getHeaders().getContentType();
                if (MediaType.APPLICATION_JSON.equals(contentType) || MediaType.APPLICATION_JSON_UTF8.equals(contentType) || MediaType.APPLICATION_XML.equals(contentType)) {
                    String s = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
                    if (s.length() > 2000) {
                        s = s.substring(0,2000);
                    }
                    stringBuilder.append("Response body: {").append(s).append("}\n");
                }
            }
            stringBuilder.append("=======================response end=================================================");
            this.log.debug(stringBuilder.toString());
        }

    }
}
