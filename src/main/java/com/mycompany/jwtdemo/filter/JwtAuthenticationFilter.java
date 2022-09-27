package com.mycompany.jwtdemo.filter;

import com.mycompany.jwtdemo.service.CustomUserDetailService;
import com.mycompany.jwtdemo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
// llamar a este filtro solo una vez por solicitud
//call this filter only once per request
@Component

public class JwtAuthenticationFilter  extends OncePerRequestFilter {
    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //get the jwt token from request header
        // validate that jwt token
        String bearerToken = request.getHeader("Authorization");
        String userName = null;
        String token = null;

        // check if el token exist of has bearer tyext
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){

            //extract jwt token from  bearertoken
            token = bearerToken.substring(7);

            try {
                //extract username from the token
                userName = jwtUtil.extractUsername(token);

                //get userDetails for this user
               UserDetails user = customUserDetailService.loadUserByUsername(userName);

               //Security checks
                if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities());
                    upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(upat);
                }else{
                    System.out.println("Invalid Token !!");
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else{
            System.out.println("Invalid Bearer Token Format!!");
        }
        // si todo está bien, reenvía la solicitud de filtro al punto final de la solicitud
    // if all if well forward the filter request to the resquest endpoint
        filterChain.doFilter(request, response);
    }
}
