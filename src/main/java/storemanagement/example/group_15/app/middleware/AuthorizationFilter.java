// package storemanagement.example.group_15.filters;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import io.jsonwebtoken.Claims;
// import jakarta.servlet.*;
// import jakarta.servlet.annotation.WebFilter;
// import jakarta.servlet.http.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.stereotype.Component;
// import storemanagement.example.group_15.app.dto.response.common.ApiResponse;
// import
// storemanagement.example.group_15.domain.permissions.entity.PermissionEntity;
// import
// storemanagement.example.group_15.domain.permissions.repository.PermissionRepository;
// import storemanagement.example.group_15.domain.rules.entity.RuleEntity;
// import storemanagement.example.group_15.domain.users.entity.AuthEntity;
// import
// storemanagement.example.group_15.domain.users.repository.AuthRepository;

// import java.io.IOException;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Optional;
// import java.util.Set;

// @Component
// @WebFilter(urlPatterns = {"/admin/*", "/user/*"})
// public class AuthorizationFilter implements Filter {
// @Autowired
// private ObjectMapper objectMapper;
// @Autowired
// private PermissionRepository permissionRepository;
// @Autowired
// private AuthRepository authRepository;
// @Override
// public void doFilter(ServletRequest request, ServletResponse response,
// FilterChain chain)
// throws IOException, ServletException {
// HttpServletRequest httpRequest = (HttpServletRequest) request;
// HttpServletResponse httpResponse = (HttpServletResponse) response;

// Claims claims = (Claims) request.getAttribute("claims");
// String sub = (String) claims.get("sub");
// Optional<AuthEntity> user =
// this.authRepository.findById(Long.parseLong(sub));
// if (user.isEmpty()){
// this.sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED.value(),
// "UNAUTHORIZED");
// return;
// }
// // Optional<RuleEntity> rule = this.

// chain.doFilter(request, response);
// }

// private boolean isProtectedUrl(String requestUri, List<PermissionEntity>
// allPermissions) {
// for (PermissionEntity permission : allPermissions) {
// if (permission.getUrlPattern() != null &&
// requestUri.matches(permission.getUrlPattern())) {
// return true;
// }
// }
// return false;
// }

// @Override
// public void init(FilterConfig filterConfig) throws ServletException {}
// @Override
// public void destroy() {}
// private void sendErrorResponse(HttpServletResponse response, int status,
// String message) throws IOException {
// response.setStatus(status);
// response.setContentType("application/json");
// response.setCharacterEncoding("UTF-8");
// ApiResponse<?> apiResponse = ApiResponse.error(message, status);
// objectMapper.writeValue(response.getOutputStream(), apiResponse);
// }
// }
