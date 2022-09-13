package com.jinwoo.sns.configuration.filter;

import com.jinwoo.sns.model.User;
import com.jinwoo.sns.service.UserService;
import com.jinwoo.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get header
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println(header);
        if(header == null || !header.startsWith("Bearer ")){
            log.error("Error occurs while getting header not Bearer {}",request.getRequestURL());
            filterChain.doFilter(request,response);
            return;
        }
        try {
            final String token  = header.split(" ")[1].trim();
            // check token is valid
            if(JwtTokenUtils.isExpired(token,key)){
                log.error("Key is expired-----");
                filterChain.doFilter(request,response);
                return;
            }
            //get userName
            String userName = JwtTokenUtils.getUserName(token,key);

            // check user is valid
            User user = userService.loadUserByUserName(userName);


            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user,null,user.getAuthorities()

            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }catch (RuntimeException e){
            log.error("Error occurs validating. {}",e.toString());

            filterChain.doFilter(request,response);
            return;
        }
        filterChain.doFilter(request,response);
    }
}
