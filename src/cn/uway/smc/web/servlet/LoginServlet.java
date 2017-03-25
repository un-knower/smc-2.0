package cn.uway.smc.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uway.smc.db.dao.SmcUserDAO;
import cn.uway.smc.db.pojo.SMCUser;

public class LoginServlet extends HttpServlet {

	private final static Logger LOG = LoggerFactory
			.getLogger(LoginServlet.class);

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String exit = req.getParameter("exit");
		if (exit == null) {
			String userName = req.getParameter("userName");
			String password = req.getParameter("password");
			SMCUser u = new SMCUser();
			u.setUserName(userName);
			u.setPassWord(password);
			int userId = 0;
			boolean b = false;
			try {
				b = new SmcUserDAO().checkAccount(u);
				userId = u.getId();
			} catch (Exception e1) {
				LOG.error("username or password is error ,cause:{}", e1);
			}
			if (b) {

				req.getSession().setAttribute("login", true);
				req.getSession().setAttribute("userId", String.valueOf(userId));
				resp.sendRedirect("/page/main.jsp");
				req.getSession().setMaxInactiveInterval(1200);
			} else {
				resp.sendRedirect("/page/index.jsp");
			}
		} else {
			// 注销
			req.getSession().removeAttribute("login");
			req.getSession().invalidate();
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().println(
					"<script>window.parent.location.replace('/');</script>");
			resp.getWriter().flush();
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	public static void main(String[] args) {

	}

}
