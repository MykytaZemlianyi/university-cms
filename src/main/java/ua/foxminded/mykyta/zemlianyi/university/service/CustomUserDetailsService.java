package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.Constants;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static Logger logger = LogManager.getLogger(CustomUserDetailsService.class.getName());

    private JdbcTemplate jdbcTemplate;

    public CustomUserDetailsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetails> users = jdbcTemplate.query(Constants.LOAD_USER_BY_USERNAME, ps -> {
            ps.setString(1, username);
            ps.setString(2, username);
            ps.setString(3, username);
        }, (rs, rowNum) -> User.withUsername(rs.getString("email")).password(rs.getString("password"))
                .authorities(rs.getString("role")).build());

        return users.stream().findFirst()
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USER_NOT_FOUND_ERROR));
    }

}
