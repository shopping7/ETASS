package cn.shopping.ETASS.web.servlet;

import cn.shopping.ETASS.domain.ResultInfo;
import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.domain.lsss.LSSSEngine;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.pv.PK;
import cn.shopping.ETASS.domain.pv.PKAndSKAndID;
import cn.shopping.ETASS.domain.pv.SK;
import cn.shopping.ETASS.service.AlgorithmService;
import cn.shopping.ETASS.service.CommonService;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;
import cn.shopping.ETASS.service.impl.CommonServiceImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private AlgorithmService algorithmService = new AlgorithmServiceImpl();
    private CommonService common = new CommonServiceImpl();

    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        HttpSession session = request.getSession();

        Map<String, String[]> map = request.getParameterMap();
        //3 封装user对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        User loginUser = algorithmService.login(user);
        ResultInfo info = new ResultInfo();
        //6.判断是否登录成功
        if(loginUser != null){
            //登录成功
            //将用户存入session
            session.setAttribute("loginUser",loginUser);
            info.setFlag(true);

        }else{
            //登录失败
            info.setFlag(false);
            //提示信息
            info.setErrorMsg("用户名或密码错误！");

        }
        writeValue(response,info);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        HttpSession session = request.getSession();
        session.removeAttribute("loginUser");
        response.sendRedirect("/web/login.html");
    }

    public void getPKAndSK(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        User loginUser = (User)session.getAttribute("loginUser");
        if(loginUser == null){
            response.sendRedirect("/web/login.html");
        }

        String user_id= loginUser.getUser_id();
        PKAndSKAndID pkAndSKAndID = common.getPKAndSKAndID(user_id);
        PK pk = pkAndSKAndID.getPk();
        SK sk = pkAndSKAndID.getSk();
        String theta_id = pkAndSKAndID.getTheta_id();
        session.setAttribute("pk",pk);
        session.setAttribute("sk",sk);
        session.setAttribute("theta_id",theta_id);
    }

    public void getPK(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        PK pk = (PK)session.getAttribute("pk");
        String filepath = "/upload/pk.txt";
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filepath));
            os.writeObject(pk);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        downFile(response,"pk.txt",filepath);
    }

    public void getSK(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        SK sk = (SK)session.getAttribute("sk");
        String filepath = "/upload/sk.txt";
//        String filepath = "C:\\Users\\shopping\\Documents\\test\\sk.txt";
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filepath));
            os.writeObject(sk);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        downFile(response,"sk.txt",filepath);
    }

    public void getTheta_id(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        String theta_id = (String)session.getAttribute("theta_id");
        String filepath = "/upload/theta_id.txt";
//        String filepath = "C:\\Users\\shopping\\Documents\\test\\theta_id.txt";
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filepath));
            os.writeObject(theta_id);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        downFile(response,"theta_id.txt",filepath);
    }


    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        User loginUser = (User)session.getAttribute("loginUser");
        if(loginUser == null){
            response.sendRedirect("/web/login.html");
        }

        String user_id= loginUser.getUser_id();
        String policy = null;
        String upload_kw = null;
        try{

//            创建DiskFileItemFactory工厂对象
            DiskFileItemFactory factory=new DiskFileItemFactory();
//            设置文件缓存目录，如果该文件夹不存在则创建一个
            File f=new File("/upload/");
            if (!f.exists()){
                f.mkdirs();
            }
            factory.setRepository(f);
//            创建ServletFileUpload对象
            ServletFileUpload fileUpload=new ServletFileUpload(factory);
//            设置字符编码
            fileUpload.setHeaderEncoding("utf-8");
//            解析request，将form表单的各个字段封装为FileItem对象
            List<FileItem> fileItems = fileUpload.parseRequest(request);
//            遍历List集合
            for (FileItem fileItem:fileItems) {
//            判断是否为普通字段
                if (fileItem.isFormField()){
//                    获取字段名称
                    String name = fileItem.getFieldName();
                    if(name.equals("policy")){
//                        如果字段值不为空
//                        if (!fileItem.getString().equals("")){
                        policy=fileItem.getString("utf-8");
//                            writer.print("上传者："+value+"<br />");
                        System.out.println(policy);
//                            String policy = "hospital&doctor&(heart|(flu&headache))";
//                        }
                    }else if(name.equals("upload_kw")){
                        if (!fileItem.getString().equals("")){
                            upload_kw=fileItem.getString("utf-8");
//                            writer.print("上传者："+value+"<br />");
                            System.out.println(upload_kw);
                        }
                    }
                }
                else {
                    //获取上传的文件名
                    String filename=fileItem.getName();
//                    处理上传文件
                    if(filename!=null&&filename!=""){
//                        writer.print("上传的文件名称是："+filename+"<br />");
//                        保持文件名唯一
//                        filename= UUID.randomUUID().toString()+"_"+filename;
//                        filename= Math.random()+"_"+filename;
                        String webpath="/upload/";
//                        创建文件路径
                        String filepath=getServletContext().getRealPath(webpath+filename);
                        //创建File对象
                        File file=new File(filepath);
                        //创建文件夹
                        file.getParentFile().mkdirs();
                        //创建文件
                        file.createNewFile();
                        //获取上传文件流
                        InputStream in=fileItem.getInputStream();
//                        使用 FileOutputStream打开服务器端的上传文件
                        FileOutputStream out=new FileOutputStream(file);
//                        流的对拷
                        byte[] bytes=new byte[1024];//每次读取一个字节
                        int len;
//                        开始读取上传文件的字节，并将其输出到服务器端的上传文件输出流中
                        while ((len=in.read(bytes))>0)
                            out.write(bytes,0,len);
                        in.close();
                        out.close();
                        fileItem.delete();

                        if(file != null) {

                            //2 用户设置访问文件策略生成lsss矩阵
                            LSSSEngine engine = new LSSSEngine();
                            if(policy != null) {
                                LSSSMatrix lsss = engine.genMatrix(policy);
                                //3 加密上传
                                AlgorithmServiceImpl algorithmService = new AlgorithmServiceImpl();
                                String[] kw = upload_kw.split(",");
                                algorithmService.setup();
                                algorithmService.Enc(user_id,file, kw, lsss);
                                file.delete();
                                response.getOutputStream().write("success".getBytes());
                            }else{
                                System.out.println("policy为空");
                                response.getOutputStream().write("fail".getBytes());
                            }

                        }
                    }
                }


            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void getUserAttr(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");


        HttpSession session = request.getSession();
        User loginUser = (User)session.getAttribute("loginUser");
        if(loginUser == null){
            response.sendRedirect("/web/login.html");
        }
        String id = loginUser.getUser_id();
        String username = common.getUsername(id);
        List<String> userAttr = common.getUserAttr(id);
        User user = new User();
        user.setUsername(username);
        user.setAttrs(userAttr.toString());


        writeValue(response,user);
    }

    public void userEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User loginUser = (User)session.getAttribute("loginUser");
        if(loginUser == null){
            response.sendRedirect("/web/login.html");
        }
        String id = loginUser.getUser_id();
        String username = request.getParameter("username");
        System.out.println(username);

        common.editUsername(id, username);
        response.getOutputStream().write("修改成功".getBytes());
    }


}


