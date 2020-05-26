package cn.shopping.ETASS.web.servlet;

import cn.shopping.ETASS.domain.KGCUser;
import cn.shopping.ETASS.domain.ResultInfo;
import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.AlgorithmService;
import cn.shopping.ETASS.service.CommonService;
import cn.shopping.ETASS.service.KGC;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;
import cn.shopping.ETASS.service.impl.CommonServiceImpl;
import cn.shopping.ETASS.service.impl.KGCImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@WebServlet("/kgc/KGC/*")
public class KGCServlet extends BaseServlet {
    private KGC kgc = new KGCImpl();
    private CommonService common = new CommonServiceImpl();

    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        HttpSession session = request.getSession();

        Map<String, String[]> map = request.getParameterMap();
        //3 封装user对象
        KGCUser user = new KGCUser();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        KGCUser loginUser = kgc.login(user);
        ResultInfo info = new ResultInfo();
        //6.判断是否登录成功
        if(loginUser != null){
            //登录成功
            //将用户存入session
            session.setAttribute("kgc_user",loginUser);
            info.setFlag(true);

        }else{
            //登录失败
            info.setFlag(false);
            //提示信息
            info.setErrorMsg("用户名或密码错误！");
//            request.setAttribute("login_msg","用户名或密码错误！");
            //跳转登录页面
//            request.getRequestDispatcher("/login.jsp").forward(request,response);

        }
        writeValue(response,info);
    }

    public void getAllAttr(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Attr> attrs = common.getAllAttr();
        writeValue(response,attrs);
    }

    public void deleteAttr(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        System.out.println(id);
        common.deleteAttr(id);
        response.sendRedirect("/web/kgc/attr.html");
    }

    public void addAttr(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        String attr_name = request.getParameter("attr_name");
        common.addAttr(attr_name);
        response.getOutputStream().write("success".getBytes());
    }

    public void getAllUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = common.getAllUser();
        writeValue(response,users);
    }

    public void alterUserInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user_id = request.getParameter("user_id");
        UserAlter userAlter = new UserAlter();
        User user = common.getOneUser(user_id);
        List<String> userAttr = common.getUserAttr(user_id);
        List<Attr> allAttr = common.getAllAttr();
        user.setAttribute(userAttr);
        userAlter.setUser(user);
        userAlter.setAttrs(allAttr);
        writeValue(response,userAlter);

    }

    public void alterUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        String user_id = request.getParameter("user_id");
        String username = request.getParameter("username");
        String[] attr_update = request.getParameterValues("user_attr");
        common.updateUser(user_id,username,attr_update);
        response.getOutputStream().write("修改成功".getBytes());
    }

    public void addUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String user_id = request.getParameter("user_id");
        String[] attribute=request.getParameterValues("attribute");
        System.out.println(username);
        System.out.println(user_id);
        System.out.println(attribute);

        //还没设置mysql事物
        KGC kgc = new KGCImpl();
        kgc.getSetup();
        common.addUser(user_id,username);

        for (int i = 0; i < attribute.length; i++) {
            common.addUserAttrById(user_id,attribute[i]);
        }

        kgc.KeyGen(user_id);

        //添加ul可上传文件时做
        CommonService commonService = new CommonServiceImpl();
        PKAndSKAndID pkAndSKAndID = commonService.getPKAndSKAndID(user_id);
        PK pk = pkAndSKAndID.getPk();
//
        AlgorithmService algorithmService = new AlgorithmServiceImpl();
        algorithmService.setup();
        algorithmService.CreateUL(user_id,pk);

        response.getOutputStream().write("添加成功".getBytes());
    }

    public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String user_id = request.getParameter("user_id");
        System.out.println(user_id);
        common.deleteUser(user_id);
        response.sendRedirect("/web/kgc/users.html");
    }


    public void updateSystem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        kgc.updateSystem();
        response.getOutputStream().write("success".getBytes());
    }

    public void trance(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        List<FileItem> fileItems = getFormItem(request);
        for (FileItem fileItem : fileItems) {
//            判断是否为文件字段
            if (!fileItem.isFormField()) {
                String name = fileItem.getFieldName();
                if (name.equals("sk_revo")) {
                    //获取上传的文件名
                    InputStream in = fileItem.getInputStream();
                    ObjectInputStream is = new ObjectInputStream(in);
                    try {
                        kgc.getSetup();
                        SK sk = (SK) is.readObject();

                        TranceID trance = kgc.Trance(sk);
                        System.out.println(trance.getId());
                        String id = trance.getId();
//                        writeValue(response,id);
                        response.getOutputStream().write(id.getBytes());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    in.close();
                    fileItem.delete();
                }
            }
        }
    }
}
