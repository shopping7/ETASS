package cn.shopping.ETASS.service;

import java.io.File;

public interface UploadFile {
    void uploadFile(String user_id, String policy, File file, String[] kw);

}
