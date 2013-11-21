package com.easytournament.webapp.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.easytournament.webapp.managedbean.AuthenticationBean;

@WebFilter("/app/*")
public class AuthenticationFilter implements Filter {

  @Inject
  private AuthenticationBean authenticationBean;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest)request;

    HttpSession session = req.getSession(false);
    String requestedPage = req.getRequestURI().toString();
    requestedPage = requestedPage.substring(requestedPage.indexOf("/", 1));
    requestedPage += "?" + req.getQueryString();

    if (session != null && authenticationBean != null
        && authenticationBean.isLoggedIn()) {
      authenticationBean.setRequestedPage(requestedPage);
      // User is logged in, so just continue request.
      chain.doFilter(request, response);
    }
    else {
      if (authenticationBean != null) {
        authenticationBean.setRequestedPage(requestedPage);
      }
      // User is not logged in, so redirect to index.
      HttpServletResponse res = (HttpServletResponse)response;
      res.sendRedirect(req.getContextPath() + "/login.jsf");
    }
  }

  @Override
  public void init(FilterConfig config) throws ServletException {
    // do nothing
  }

  @Override
  public void destroy() {
    // do nothing
  }
}
