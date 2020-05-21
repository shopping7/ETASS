package cn.shopping.ETASS.web.servlet;


import cn.shopping.ETASS.domain.ResultInfo;
import cn.shopping.ETASS.domain.User;
import cn.shopping.ETASS.domain.lsss.LSSSMatrix;
import cn.shopping.ETASS.domain.pv.*;
import cn.shopping.ETASS.service.CloudServer;
import cn.shopping.ETASS.service.CommonService;
import cn.shopping.ETASS.service.GetFile;
import cn.shopping.ETASS.service.impl.AlgorithmServiceImpl;
import cn.shopping.ETASS.service.impl.CloudServerImpl;
import cn.shopping.ETASS.service.impl.CommonServiceImpl;
import cn.shopping.ETASS.service.impl.GetFileImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisa.dia.gas.jpbc.Element;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;


@WebServlet("/getFile")
public class GetFileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                    String filename = "C:\\Users\\shopping\\Documents\\test\\decrypt"+i+".txt";
                    i++;
                    algorithmService.Dec(ctout, sk, vkm,filename);

                    response.setCharacterEncoding("UTF-8"); //设置编码字符
                    response.setContentType("application/octet-stream;charset=UTF-8"); //设置下载内容类型
                    response.setHeader("Content-disposition", "attachment;filename="+"decrypt.txt");//设置下载的文件名称
                    OutputStream out = response.getOutputStream();   //创建页面返回方式为输出流，会自动弹出下载框


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
            System.out.println("无匹配文件");
        }





//        File file = new File(path);
//        // 取得文件名。
//        String filename = file.getName();
//
//        // 以流的形式下载文件。
//        InputStream fis = new BufferedInputStream(new FileInputStream(path));
//        byte[] buffer = new byte[fis.available()];
//        fis.read(buffer);
//        fis.close();
//        // 清空response
//        response.reset();
//        // 设置response的Header
//        response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
//        response.addHeader("Content-Length", "" + file.length());
//        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
//        response.setContentType("application/octet-stream");
//        toClient.write(buffer);
//        toClient.flush();
//        toClient.close();



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
