package io.github.styxiner.complyx_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl uds) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = uds;
    }

    // Esto es de documentación vieja. En futuras versiones lo quitaran
    /*public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }*/
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtUtil, userDetailsService);
        
        // Que conste que con lambda esto es mucho más fácil
        // Pero lo puedo pasar a clases anónimas
                
        // Esto está deprecated
        //http.csrf().disable();	

        http.csrf(new Customizer<CsrfConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CsrfConfigurer<HttpSecurity> c) {
                c.disable();
            }
        });
        
        // Esto también esta obsoleto
        //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http.sessionManagement(new Customizer<SessionManagementConfigurer<HttpSecurity>>() {
            @Override
            public void customize(SessionManagementConfigurer<HttpSecurity> s) {
                s.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            }
        });
        
        //Deprecated tmb
        //http.authenticationProvider(authenticationProvider());
        
        http.authenticationProvider(authenticationProvider());
        
        // deprecated
        // http.authorizeRequests()

        /*
        // endpoints publicos
        	.requestMatchers("/api/auth/login").permitAll()
        	.requestMatchers("/api/auth/refresh").permitAll()
        	.requestMatchers("/v3/api-docs/**").permitAll()
        	.requestMatchers("/swagger-ui/**").permitAll()
        	.requestMatchers("/swagger-ui.html").permitAll()
        	
        	// enpoints que necesitan autenticación
        	.requestMatchers("/api/auth/logout").authenticated()
        	
        	// endpoints que necesitan roles concretos
        	.requestMatchers("/api/users/**").hasRole("ADMIN")
        	.requestMatchers("/api/agents/**").hasAnyRole("ADMIN", "TECNICO")
        	
        	// filtro general independiente de los roles
        	.anyRequest().authenticated();
        */
        
        http.authorizeHttpRequests(new Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
                auth.requestMatchers("/api/auth/login").permitAll();
                auth.requestMatchers("/api/auth/refresh").permitAll();
                auth.requestMatchers("/v3/api-docs/**").permitAll();
                auth.requestMatchers("/swagger-ui/**").permitAll();
                auth.requestMatchers("/swagger-ui.html").permitAll();
                
                auth.requestMatchers("/api/auth/logout").authenticated();
                
                auth.requestMatchers("/api/users/**").hasRole("ADMIN");
                auth.requestMatchers("/api/agents/**").hasAnyRole("ADMIN", "TECNICO");
                
                auth.anyRequest().authenticated();
            }
        });
        
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
