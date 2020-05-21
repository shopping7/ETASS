package cn.shopping.ETASS.web.servlet;

import cn.shopping.ETASS.domain.KGCUser;
import cn.shopping.ETASS.domain.ResultInfo;
import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.domain.lsss.LSSSEngine;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.AlgorithmService;
import cn.shopping.ETASS.service.CloudServer;
import cn.shopping.ETASS.service.CommonService;
import cn.shopping.ETASS.service.UserService;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;
import cn.shopping.ETASS.service.impl.CloudServerImpl;
import cn.shopping.ETASS.service.impl.CommonServiceImpl;
import cn.shopping.ETASS.service.impl.UserServiceImpl;
import it.unisa.dia.gas.jpbc.Element;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
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

        UserService us = new UserServiceImpl();
        User loginUser = us.login(user);
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


    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        String user_id="Alice";

        String policy = null;
        String upload_kw = null;
        ResultInfo info = new ResultInfo();
        try{
            response.setContentType("text/html;charset=utf-8");
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
//            获取字符流
            PrintWriter writer=response.getWriter();
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
                                info.setFlag(true);
                                info.setSuccessMsg("上传成功");
                            }else{
                                System.out.println("policy为空");
                                info.setFlag(false);
                                info.setErrorMsg("上传失败");
                            }

                        }
                    }
                }


            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        writeValue(response,info);
    }

    public void getFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = "Alice";

        AlgorithmServiceImpl algorithmService = new AlgorithmServiceImpl();
        CommonService commonService = new CommonServiceImpl();
        algorithmService.setup();
        PKAndSKAndID pkAndsk = commonService.getPKAndSKAndID(id);
        SK sk = pkAndsk.getSk();
        String theta_id = pkAndsk.getTheta_id();
        Element Did = algorithmService.getDid(theta_id);

        //2 根据用户输入的关键字生成陷门
        String file_kw = request.getParameter("file_kw");
        System.out.println(file_kw);
        String[] KW1 = file_kw.split(",");

        TKW tkw = algorithmService.Trapdoor(sk,KW1);


        //3 云服务器寻找对应的文件
        CloudServer CS = new CloudServerImpl();
        CS.setup();
        String[] attrs = CS.getAttr(id);
        List<Encrypt_File> file_list = CS.getFile(KW1);
        int i = 0;
        response.setCharacterEncoding("UTF-8"); //设置编码字符
        response.setContentType("application/octet-stream;charset=UTF-8"); //设置下载内容类型
        response.setHeader("Content-disposition", "attachment;filename="+"decrypt.txt");//设置下载的文件名称
        OutputStream out = response.getOutputStream();
        if(file_list != null){
            for (Encrypt_File file : file_list) {
                CT ct = file.getCt();
                VKM vkm = file.getVkm();
                LSSSMatrix lsss = file.getLsss();

                LSSSMatrix lsssD1 = lsss.extract(attrs);
                int lsssIndex[] = lsssD1.getIndex();
                CTout ctout = CS.Transform(ct, tkw, Did,lsssD1,lsssIndex);

                //4 云服务传送ctout给用户，用户解密
                if(ctout != null){
                    String filename = "/upload/decrypt"+i+".txt";
                    i++;
                    algorithmService.Dec(ctout, sk, vkm,filename);

                       //创建页面返回方式为输出流，会自动弹出下载框


                    //方法1-1：IO字节流下载，用于小文件
                    System.out.println("字节流下载");
                    InputStream is = new FileInputStream(filename);  //创建文件输入流
                    byte[] Buffer = new byte[2048];  //设置每次读取数据大小，即缓存大小
                    int size = 0;  //用于计算缓存数据是否已经读取完毕，如果数据已经读取完了，则会返回-1
                    while((size=is.read(Buffer)) != -1){  //循环读取数据，如果数据读取完毕则返回-1
                        out.write(Buffer, 0, size); //将每次读取到的数据写入客户端
                    }
                    is.close();
                }
            }
        }else{
//
            System.out.println("无匹配文件");
        }




    }

    public void getUserAttr(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");


        HttpSession session = request.getSession();

        String id = "Alice";
        String username = "Alice";
        List<String> userAttr = common.getUserAttr(id);
        User user = new User();
        user.setUsername(username);
        user.setAttrs(userAttr.toString());


        writeValue(response,user);
    }

    public void userEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        String id = "Alice";
        String username = request.getParameter("username");
        System.out.println(username);

        common.editUsername(id, username);
        ResultInfo info = new ResultInfo();
        info.setFlag(true);
        info.setSuccessMsg("修改成功");
        writeValue(response,info);
    }


}


