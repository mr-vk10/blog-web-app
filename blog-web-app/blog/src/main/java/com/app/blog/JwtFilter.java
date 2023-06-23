package com.app.blog;

import com.app.blog.exceptions.IncorrectJwtException;
import com.app.blog.models.Users;
import com.app.blog.util.Constants;
import com.app.blog.util.JWTUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class JwtFilter extends GenericFilterBean {
	
	@Autowired
	private JWTUtils jwtUtils;
	
    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
    	
    	// get jwt token
    	
    	HttpServletRequest request = (HttpServletRequest) sr;
    	HttpServletResponse response = (HttpServletResponse) sr1;
 		String requestTokenHeader = request.getHeader("authorization");
 		
 		Users user = null;
		String username = null;
		String jwtToken = null;
    	
		if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer ")) {
			
			jwtToken = requestTokenHeader.substring(7);
			try {

				// username = jwtUtil.extractUsername(jwtToken);

				user = jwtUtils.getUser(jwtToken);

				if (user != null) {
					username = user.getUsername();
				}

			} catch (Exception e) {
				// throw new IncorrectJwtException(e.getMessage());
				// throw new Exception(e.getMessage());
				

			    // Set the desired status code
			    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			    response.getWriter().write(e.getMessage());
			}

			// validate the token

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						user, null, user.getAuthorities());

				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			} else {
				throw new IncorrectJwtException("Unable to read JSON value");
				
			}
		} 
		// now this request can be forwarded
		fc.doFilter(request, sr1);
			
		
    	// To change body of generated methods, choose
        // Tools | Templates.
    	// throw new UnsupportedOperationException("Not supported yet."); 
        
        
    }

	

}
