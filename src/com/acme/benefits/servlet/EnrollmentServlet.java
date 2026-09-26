package com.acme.benefits.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.acme.benefits.crypto.CryptoUtil;
import com.acme.benefits.dao.MemberDAO;

/**
 * Benefits enrollment.
 *
 * Created:  02 Feb 1998  R. Whitfield
 * Modified: 19 Nov 2003  M. Okamoto  - added dependent coverage
 */
public class EnrollmentServlet extends HttpServlet {

    private MemberDAO dao = new MemberDAO();

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String memberId = req.getParameter("memberId");
        String ssn = req.getParameter("ssn");
        String plan = req.getParameter("planCode");

        if (memberId == null || ssn == null) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            String encryptedSsn = CryptoUtil.encryptSsn(ssn);

            Hashtable record = new Hashtable();
            record.put("memberId", memberId);
            record.put("ssn", encryptedSsn);
            record.put("planCode", plan);

            dao.saveEnrollment(record);

            HttpSession session = req.getSession(true);
            session.setAttribute("token", CryptoUtil.newSessionToken());

            PrintWriter out = res.getWriter();
            out.println("<html><body>");
            out.println("<h2>Enrollment received for " + memberId + "</h2>");
            out.println("</body></html>");
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("enrollment failed", e);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Vector plans = dao.listPlans();
        PrintWriter out = res.getWriter();
        out.println("<html><body><form method=\"post\">");
        for (int i = 0; i < plans.size(); i++) {
            out.println("<option>" + plans.elementAt(i) + "</option>");
        }
        out.println("</form></body></html>");
        out.close();
    }
}
