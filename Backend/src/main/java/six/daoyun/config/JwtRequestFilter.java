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


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${daoyun.supersuper.enable}")
    private boolean superEnable;
    @Value("${daoyun.supersuper.name}")
    private String supername;

    private static final ArrayList<String> bypassURIs;
    static {
        bypassURIs = new ArrayList<String>();
        bypassURIs.add("/apis/auth/signup");
        bypassURIs.add("/apis/auth/login");
        bypassURIs.add("/apis/auth/jwt");
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JwtRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        log.info("filtering HTTP Request for: " + request.getMethod() + " " + request.getRequestURI());

        boolean bypass = false;
        for(String uri: bypassURIs) {
            if(request.getRequestURI().startsWith(uri)) {
                bypass = true;
                break;
            }
        }

        String isAdmin = request.getHeader("X-IM-ADMIN");
        if(this.superEnable && supername.equals(isAdmin)) {
            bypass = true;
        }

        if(request.getMethod() == "OPTIONS") {
            bypass = true;
        }


        if(!bypass) {
            final String jwtToken = request.getHeader("Authorization");

            String username = null;
            if (jwtToken != null) {
                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken);

                    if(username == null) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
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
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        chain.doFilter(request, response);
    }

}

