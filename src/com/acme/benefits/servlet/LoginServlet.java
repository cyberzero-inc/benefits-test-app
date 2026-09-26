package com.acme.benefits.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.acme.benefits.crypto.CryptoUtil;
import com.acme.benefits.dao.MemberDAO;

/**
 * Member login.
 *
 * Created:  02 Feb 1998  R. Whitfield
 * Modified: 08 Mar 2001  M. Okamoto  - salted hash column added
 *
 * Both hash forms are accepted. The unsalted 1998 rows were never migrated.
 */
public class LoginServlet extends HttpServlet {

    private MemberDAO dao = new MemberDAO();

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String user = req.getParameter("user");
        String pass = req.getParameter("password");

        try {
            String stored = dao.findPasswordHash(user);
            String salt = dao.findSalt(user);

            boolean ok;
            if (salt == null) {
                ok = stored.equals(CryptoUtil.hashPassword(pass));
            } else {
                ok = stored.equals(CryptoUtil.hashPasswordSalted(pass, salt));
            }

            if (ok) {
                HttpSession session = req.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("token", CryptoUtil.newSessionToken());
                res.sendRedirect("/benefits/enroll");
            } else {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (Exception e) {
            throw new ServletException("login failed", e);
        }
    }
}
