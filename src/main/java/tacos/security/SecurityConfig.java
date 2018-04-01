package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetails u1 = User.withDefaultPasswordEncoder().username("buzz").password("infinity").roles("USER").build();
        UserDetails u2 = User.withDefaultPasswordEncoder().username("woody").password("bullseye").roles("USER").build();
        auth.inMemoryAuthentication().withUser(u1).withUser(u2);
    }
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests().antMatchers("/h2-console/**").permitAll();
        httpSecurity.authorizeRequests().antMatchers("/design", "/orders/**").access("hasRole('ROLE_USER')").and().formLogin();
 
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }
}
