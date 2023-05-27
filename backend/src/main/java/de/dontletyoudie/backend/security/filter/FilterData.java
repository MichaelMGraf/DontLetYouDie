package de.dontletyoudie.backend.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.istack.NotNull;
import lombok.NonNull;
import lombok.ToString;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@ToString
public class FilterData {

    private final FilterDataInternal data;


    private FilterData(FilterDataInternal data) {
        this.data = data;
    }

    public DecodedJWT getToken() {
        return data.token;
    }

    public HttpServletRequest getRequest() {
        return data.request;
    }

    public Map<String, Method> getFilterMethods() {
        return data.filterMethods;
    }

    public FilterChain getFilterChain() {
        return data.filterChain;
    }

    public HttpServletResponse getResponse() {
        return data.response;
    }

    public String getBody() throws IOException {
        StringBuilder builder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (data.request.getInputStream(), StandardCharsets.UTF_8))) {
            int c;
            while ((c = reader.read()) != -1) {
                builder.append((char) c);
            }
        }
        return builder.toString();
    }

    private static class FilterDataInternal {
        private HttpServletRequest request;
        private DecodedJWT token;
        private Map<String, Method> filterMethods;
        private FilterChain filterChain;
        private HttpServletResponse response;
    }

    public static class Builder {
        private final FilterDataInternal data;

        public Builder() {
            this.data = new FilterDataInternal();
        }

        public Builder withFilterMethods(@NonNull Map<String, Method> filterMethods) {
            data.filterMethods = filterMethods;
            return this;
        }

        public Builder withRequest(@NotNull HttpServletRequest request) {
            data.request = request;
            return this;
        }

        public Builder withResponse(@NonNull HttpServletResponse response) {
            data.response = response;
            return this;
        }

        public Builder withFilterChain(@NotNull FilterChain filterChain) {
            data.filterChain = filterChain;
            return this;
        }

        public Builder withToken(DecodedJWT token) {
            data.token = token;
            return this;
        }

        public FilterData build() {
            return new FilterData(data);
        }
    }
}
