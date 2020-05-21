package cn.shopping.ETASS.web.servlet;




import cn.shopping.ETASS.domain.ResultInfo;
import cn.shopping.ETASS.domain.lsss.LSSSEngine;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.service.AlgorithmService;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.List;


@WebServlet("/uploadFile")
public class UploadFileServlet extends HttpServlet {
    private AlgorithmService algorithmService = new AlgorithmServiceImpl();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("utf-8");

//        System.out.println("123");
//        HttpSession session = request.getSession();
//
        String user_id="Alice";

//
//        String policy_1=request.getParameter("policy");
        String policy = null;
        String upload_kw = null;
        ResultInfo info = new ResultInfo();
        try{
            response.setContentType("text/html;charset=utf-8");
//            创建DiskFileItemFactory工厂对象
            DiskFileItemFactory factory=new DiskFileItemFactory();
//            设置文件缓存目录，如果该文件夹不存在则创建一个
            File f=new File("C:\\Users\\shopping\\Documents\\test");
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
                        filename= Math.random()+"_"+filename;
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
                                writer.print("上传成功");
                            }else{
                                System.out.println("policy为空");
                            }

                        }
                    }
                }


            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
