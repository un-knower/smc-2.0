package cn.uway.smc.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.uway.commons.codec.CodecUtil;
import cn.uway.smc.db.dao.SmcCfgSourceDAO;
import cn.uway.smc.db.dao.SmcUserDAO;
import cn.uway.smc.db.pojo.SMCCfgSource;
import cn.uway.smc.db.pojo.SMCUser;

public class PasswordCheckServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String check = req.getParameter("action");
		if (check == null)
			return;
		try {
			if (check.equals("checkPassword")) {
				String id = req.getParameter("id");
				String passwd = req.getParameter("passwd");
				SmcCfgSourceDAO sourceDao = new SmcCfgSourceDAO();
				SMCCfgSource ids;

				ids = sourceDao.get(Integer.valueOf(id));

				if (!ids.getPwd().equals(CodecUtil.toMD5(passwd))) {
					try {
						resp.getWriter().write("password is wrong !");// 密码错误，请重新确认输入
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					resp.getWriter().write("password is right !");// 密码错误，请重新确认输入
				}

			} else if (check.equals("checkLoginPassword")) {
				String id = req.getParameter("id");
				String passwd = req.getParameter("passwd");
				SmcUserDAO sourceDao = new SmcUserDAO();
				SMCUser ids = sourceDao.get(Integer.valueOf(id));
				if (!ids.getPassWord().equals(passwd)) {
					try {
						resp.getWriter().write("password is wrong !");// 密码错误，请重新确认输入
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
