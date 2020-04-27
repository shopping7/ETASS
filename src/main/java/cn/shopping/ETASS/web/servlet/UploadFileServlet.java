package cn.shopping.ETASS.web.servlet;

import cn.shopping.ETASS.domain.ResultInfo;
import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.service.GetFile;
import cn.shopping.ETASS.service.UploadFile;
import cn.shopping.ETASS.service.impl.GetFileImpl;
import cn.shopping.ETASS.service.impl.UploadFileImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/uploadFileServlet")
public class UploadFileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String kw_1 = request.getParameter("kw_1");
        String kw_2 = request.getParameter("kw_2");
        String kw_3 = request.getParameter("kw_3");
        String[] kw = new String[]{kw_1,kw_2,kw_3};
//        String[] kw = {"oncology department","Raffles hospital","doctor"};
//        String msg = "hello";
        String msg = request.getParameter("msg");
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        String user_id = user.getUser_id();
        System.out.println(user_id);
        ResultInfo info = new ResultInfo();
        UploadFile uf = new UploadFileImpl();
        uf.uploadFile(user_id,msg,kw);

        info.setErrorMsg("上传成功");
        //将info对象序列化为json
        ObjectMapper mapper = new ObjectMapper();

        //将json数据写回客户端
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
