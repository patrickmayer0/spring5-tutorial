package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Bean
    public PasswordEncoder encoder() {
        // Using a better password encoder.
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // open door for /h2-console and /register page.
        httpSecurity.authorizeRequests().antMatchers("/h2-console/**", "/register").permitAll();
        
        // authorize authenticated user on /design (design.html) and /orders (orders.html) page.
        httpSecurity.authorizeRequests().antMatchers("/design", "/orders/**").access("hasRole('ROLE_USER')")
        
        /* set /login as login page (mapped to login.html) : go on to /design when logged in. */
            .and().formLogin().loginPage("/login").defaultSuccessUrl("/design", true)
            
        /* display login page upon logout. */
            .and().logout().logoutSuccessUrl("/login");

        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }
 
}
