//package com.example.springsecurityclient.cofig;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends AbstractHttpConfigurer<WebSecurityConfig , HttpSecurity> {
//
//    private static final String[] WHITE_LIST_URLS ={
//            "/register"
//    };
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder(11);
//    }
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors().and().csrf().disable() // Disable CORS and CSRF protection
//                .authorizeRequests()
//                .anyRequest().permitAll(); // Allow all requests to be accessed without authentication
//        return http.build();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain2 (HttpSecurity http) throws Exception{
//        return http.
//                csrf().disable()
//                .authorizeHttpRequests()
//                .requestMatchers("").permitAll().and()
//                .authorizeHttpRequests().requestMatchers("").authenticated()
//                .and().formLogin()
//                .and().build();
//    }
//
//
//}
