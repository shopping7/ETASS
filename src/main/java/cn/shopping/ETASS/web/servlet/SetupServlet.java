package cn.shopping.ETASS.web.servlet;

import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.AlgorithmService;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/SetupServlet")
public class SetupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.设置编码
        request.setCharacterEncoding("utf-8");

        HttpSession session = request.getSession();
        //5.调用Service查询
//        AlgorithmService service = new AlgorithmServiceImpl();
//        PPAndMSK ppandmsk = service.setup();
//        MSK msk = ppandmsk.getMsk();
//        PP pp = ppandmsk.getPp();




    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
