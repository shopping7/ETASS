package cn.shopping.ETASS.web.servlet;



import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.CloudServer;
import cn.shopping.ETASS.service.CommonService;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;
import cn.shopping.ETASS.service.impl.CloudServerImpl;
import cn.shopping.ETASS.service.impl.CommonServiceImpl;
import it.unisa.dia.gas.jpbc.Element;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@WebServlet("/getFile")
public class GetFileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User loginUser = (User)session.getAttribute("loginUser");
        if(loginUser == null){
            response.sendRedirect("/web/login.html");
        }
        String id = loginUser.getUser_id();

        String[] KW1 = new String[0];
        String theta_id = null;
        SK sk = null;
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
                if (fileItem.isFormField()) {
//                    获取字段名称
                    String file_kw = fileItem.getString("utf-8");
                    System.out.println(file_kw);
                    KW1 = file_kw.split(",");

                } else {
                    String name = fileItem.getFieldName();
                    if (name.equals("sk")) {

//                        String filepath = "/upload/upload_sk.txt";
//                        File file = new File(filepath);
//                        file.getParentFile().mkdirs();
//                        file.createNewFile();
                        InputStream in = fileItem.getInputStream();
//                        FileOutputStream out=new FileOutputStream(file);
//                        byte[] bytes=new byte[1024];//每次读取一个字节
//                        int len;
//                        开始读取上传文件的字节，并将其输出到服务器端的上传文件输出流中
//                        while ((len=in.read(bytes))>0)
//                            out.write(bytes,0,len);
//                        in.close();
//                        out.close();

//                        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
                        ObjectInputStream is = new ObjectInputStream(in);
                        sk = (SK) is.readObject();
                        in.close();
//                        file.delete();
                        fileItem.delete();
                    } else if (name.equals("theta_id")) {
//                        String filepath = "/upload/upload_theta_id.txt";
//                        File file = new File(filepath);
//                        file.getParentFile().mkdirs();
//                        file.createNewFile();
                        InputStream in = fileItem.getInputStream();
//                        FileOutputStream out=new FileOutputStream(file);
//                        byte[] bytes=new byte[1024];//每次读取一个字节
//                        int len;
//                        开始读取上传文件的字节，并将其输出到服务器端的上传文件输出流中
//                        while ((len=in.read(bytes))>0)
//                            out.write(bytes,0,len);
//                        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
                        ObjectInputStream is = new ObjectInputStream(in);
                        theta_id = (String) is.readObject();
                        in.close();
//                        file.delete();
                        fileItem.delete();
                    }
                }
            }
        } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (FileUploadException e) {
                e.printStackTrace();
            }

        CommonService common = new CommonServiceImpl();
        PKAndSKAndID pkAndSKAndID = common.getPKAndSKAndID(id);
        SK sk_1 = pkAndSKAndID.getSk();
        boolean b1 = sk.equals(sk_1);
        System.out.println(b1);
        AlgorithmServiceImpl algorithmService = new AlgorithmServiceImpl();
        algorithmService.setup();
        Element Did = algorithmService.getDid(theta_id);

        TKW tkw = algorithmService.Trapdoor(sk,KW1);


        //3 云服务器寻找对应的文件
        CloudServer CS = new CloudServerImpl();
        CS.setup();
        String[] attrs = CS.getAttr(id);
        List<Encrypt_File> file_list = CS.getFile(KW1);

        String directory = "/upload/";
        File directoryFile = new File(directory);
        if(!directoryFile.isDirectory() && !directoryFile.exists()){
            directoryFile.mkdirs();
        }
        //设置最终输出zip文件的目录+文件名
        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy年MM月dd日");
        String zipFileName = formatter.format(new Date())+".zip";
        String strZipPath = directory+zipFileName;

        ZipOutputStream zipStream = null;
        FileInputStream zipSource = null;
        BufferedInputStream bufferStream = null;
        File zipFile = new File(strZipPath);
        try{
            zipStream = new ZipOutputStream(new FileOutputStream(zipFile));
            int i = 0;
            if(file_list != null){
                for(Encrypt_File file : file_list){
                    CT ct = file.getCt();
                    VKM vkm = file.getVkm();
                    LSSSMatrix lsss = file.getLsss();
                    LSSSMatrix lsssD1 = lsss.extract(attrs);
                    int lsssIndex[] = lsssD1.getIndex();
                    CTout ctout = CS.Transform(ct, tkw, Did,lsssD1,lsssIndex);
                    if(ctout != null) {
                        String filepath = "/upload/";
                        String filename = "decrypt"+i+".txt";
                        File file_temp = new File(filepath+filename);
                        if(file_temp.exists()){
                            algorithmService.Dec(ctout, sk, vkm,filepath+filename);
                            zipSource = new FileInputStream(file_temp);
                            ZipEntry zipEntry = new ZipEntry(filename);
                            zipStream.putNextEntry(zipEntry);
                            bufferStream = new BufferedInputStream(zipSource, 1024 * 10);
                            int read = 0;
                            byte[] buf = new byte[1024 * 10];
                            while((read = bufferStream.read(buf, 0, 1024 * 10)) != -1)
                            {
                                zipStream.write(buf, 0, read);
                            }
                            i++;
                        }
                        file_temp.delete();
                    }

                }
            }else{
                System.out.println("无匹配文件");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(null != bufferStream) bufferStream.close();
                if(null != zipStream){
                    zipStream.flush();
                    zipStream.close();
                }
                if(null != zipSource) zipSource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(zipFile.exists()){
            downFile(response,zipFileName,strZipPath);
            zipFile.delete();
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    public void downFile(HttpServletResponse response,String filename,String path ){
        if (filename != null) {
            FileInputStream is = null;
            BufferedInputStream bs = null;
            OutputStream os = null;
            try {
                File file = new File(path);
                if (file.exists()) {
                    //设置Headers
                    response.setHeader("Content-Type","application/octet-stream");
                    //设置下载的文件的名称-该方式已解决中文乱码问题
                    response.setHeader("Content-Disposition","attachment;filename=" +  new String( filename.getBytes("gb2312"), "ISO8859-1" ));
                    is = new FileInputStream(file);
                    bs =new BufferedInputStream(is);
                    os = response.getOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while((len = bs.read(buffer)) != -1){
                        os.write(buffer,0,len);
                    }
                }else{
//                    String error = Base64Util.encode("下载的文件资源不存在");
//                    response.sendRedirect("/imgUpload/imgList?error="+error);
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }finally {
                try{
                    if(is != null){
                        is.close();
                    }
                    if( bs != null ){
                        bs.close();
                    }
                    if( os != null){
                        os.flush();
                        os.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
