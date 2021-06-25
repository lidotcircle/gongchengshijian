package six.daoyun.config;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import six.daoyun.service.UserService;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;

    @Value("${daoyun.supersuper.enable}")
    private boolean superEnable;
    @Value("${daoyun.supersuper.name}")
    private String supername;

    private static final ArrayList<String> bypassURIs;
    static {
        bypassURIs = new ArrayList<String>();
        bypassURIs.add("/apis/auth/user");
        bypassURIs.add("/apis/auth/refresh-token");
        bypassURIs.add("/apis/auth/jwt");
        bypassURIs.add("/apis/auth/password");
        bypassURIs.add("/apis/message");
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JwtRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        log.info("filtering HTTP Request for: " + request.getMethod() + " " + request.getRequestURI());

        boolean bypass = !request.getRequestURI().startsWith("/apis/");
        for(String uri: bypassURIs) {
            if(request.getRequestURI().startsWith(uri)) {
                bypass = true;
                break;
            }
        }

        String isAdmin = request.getHeader("X-IM-ADMIN");
        if(this.superEnable && supername.equals(isAdmin)) {
            request.setAttribute("username", "admin");
            bypass = true;
        }

        if(request.getMethod().equalsIgnoreCase("OPTIONS")) {
            bypass = true;
        }


        if(!bypass) {
            final String jwtToken = request.getHeader("Authorization");

            String username = null;
            if (jwtToken != null) {
                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken);

                    if(username == null) {
                        System.out.println("bad username");
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    } else {
                        request.setAttribute("username", username);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Get a bad JWT Token");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                } catch (ExpiredJwtException e) {
                    System.out.println("JWT Token has expired");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } else {
                response.setHeader("X-REASON", "REQUIRE JWT");
                System.out.println("JWT Token is null");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "require JWT");
                return;
            }

            var uri = request.getRequestURI();
            if(uri.endsWith("/")) uri = uri.substring(0, uri.length() - 1);
            final var method = request.getMethod().toUpperCase();
            if (!this.userService.hasPermission(username, uri, method)) {
                log.info("Deny {} to Access {} with {} method", username, uri, method);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No Permission");
            }
        }

        chain.doFilter(request, response);
    }

}

