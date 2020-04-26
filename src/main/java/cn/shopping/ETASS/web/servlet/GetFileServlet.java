package cn.shopping.ETASS.web.servlet;


import cn.shopping.ETASS.domain.ResultInfo;
import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.service.GetFile;
import cn.shopping.ETASS.service.impl.GetFileImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/getFileServlet")
public class GetFileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String kw_1 = request.getParameter("kw_trapdoor_1");
        String kw_2 = request.getParameter("kw_trapdoor_2");
        String kw_3 = request.getParameter("kw_trapdoor_3");
        String[] kw_trapdoor = new String[]{kw_1,kw_2,kw_3};
//        String[] kw_trapdoor = {"oncology department","Raffles hospital","doctor"};
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        String user_id = user.getUser_id();
        System.out.println(user_id);
        ResultInfo info = new ResultInfo();
        GetFile gf = new GetFileImpl();
        String str = gf.getFile(user_id, kw_trapdoor);
        System.out.println(str);
        if(str != null){
            info.setFlag(true);
            info.setErrorMsg(str);
        }else{
            info.setFlag(false);
            info.setErrorMsg("未找到符合的信息");
        }
        //将info对象序列化为json
        ObjectMapper mapper = new ObjectMapper();

        //将json数据写回客户端
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
