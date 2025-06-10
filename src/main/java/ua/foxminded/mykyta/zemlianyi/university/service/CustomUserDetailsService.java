package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.Constants;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static Logger logger = LogManager.getLogger(CustomUserDetailsService.class.getName());

    private NamedParameterJdbcTemplate jdbcTemplate;

    public CustomUserDetailsService(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Map<String, Object> params = Map.of("username", username);

        List<UserDetails> users = jdbcTemplate.query(Constants.LOAD_USER_BY_USERNAME, params,
                (rs, rowNum) -> User.withUsername(rs.getString("email")).password(rs.getString("password"))
                        .authorities(rs.getString("role")).build());

        return users.stream().findFirst()
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USER_NOT_FOUND_ERROR));
    }

}
