//package com.talgham.demo;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import com.talgham.demo.service.AuthenticationService;
//
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//	
//	@Autowired
//	private AuthenticationService authenticationService;
//
//	
//    private AuthenticationProvider authenticationProvider;
//
//    @Autowired
//    @Qualifier("daoAuthenticationProvider")
//    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
//        this.authenticationProvider = authenticationProvider;
//    }
//
// 
//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider(){
//
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
//        daoAuthenticationProvider.setUserDetailsService(authenticationService);
//        return daoAuthenticationProvider;
//    }
//
//    @Autowired
//    public void configureAuthManager(AuthenticationManagerBuilder authenticationManagerBuilder){
//        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
//    }
//    
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
////		http
////			.authorizeRequests()
////				.anyRequest().permitAll()
////				.and()
////			.formLogin().loginPage("/login")
////				.usernameParameter("username").passwordParameter("password");
//		
//		
////		http
////        .authorizeRequests().anyRequest().permitAll();
////        .anyRequest().authenticated()
////        .and()
////        .formLogin().loginPage("/login").permitAll()
////        .and()
////        .logout().permitAll();
////
////			
////				.anyRequest().authenticated()
////				.and()
////			.formLogin()
////				.loginPage("/login")
////				.usernameParameter("username").passwordParameter("password")
////				.permitAll();;
//	}
//
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web
//			.ignoring()
//			.antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
//	}
//
////	@Autowired
////	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////		auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
////	}
////	
////	@Autowired
////	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
////		auth.userDetailsService(authenticationService).passwordEncoder(passwordEncoder);
////	}
//	
//	
//}
