package net.dunotech.venus.system.service.oss.cloud;

import net.coobird.thumbnailator.Thumbnails;
import net.dunotech.venus.system.config.Constants;
import net.dunotech.venus.system.dto.common.UploadFileInfoDto;
import net.dunotech.venus.system.mapper.oss.OssMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * oss文件上传处理Service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OssHandleService {
    @Resource
    protected OssMapper ossMapper;

    private static final String TMP_FILE_PATH = "/tmp/files";

    /**
     * 文件上传oss,并存入本地数据库
     * @param request
     * @param isCompress  是否压缩
     * @return
     * @throws IOException
     */
    public List<UploadFileInfoDto> upload(MultipartHttpServletRequest request,String isCompress) throws IOException{
        // 构造文件名迭代器
        Iterator<String> itr = request.getFileNames();
        // 初始化MultipartFile
        MultipartFile file = null;
        List<UploadFileInfoDto> uploadFileInfoDtos = new ArrayList<>();
        while (itr.hasNext()){
            // 获取下一个文件
            file = request.getFile(itr.next());
            if (file.isEmpty()) {
                throw new Error("上传文件不能为空");
            }
            //上传文件
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            //获得文件后缀名
            String url;
            if(Constants.YES.equals(isCompress) && checkPicture(file)){
                //压缩图片
                FileInputStream fis = new FileInputStream(zipPictureFile(file));
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
                byte[] b = new byte[1024];
                int len = -1;
                while((len = fis.read(b)) != -1) {
                    bos.write(b, 0, len);
                }
                byte[] fileByte = bos.toByteArray();
                bos.close();
                url = OSSFactory.build().uploadSuffix(fileByte, suffix);
            }else{
                url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);
            }

            UploadFileInfoDto uploadFileInfoDto = new UploadFileInfoDto(file.getOriginalFilename(),url.substring(url.lastIndexOf("/")+1),suffix,url);
            uploadFileInfoDtos.add(uploadFileInfoDto);
        }
        return uploadFileInfoDtos;
    }

    /**
     * 判断是否为图片
     * @param file
     * @return
     */
    private boolean checkPicture(MultipartFile file){
        try {
            Image image = ImageIO.read(file.getInputStream());
            return image != null;
        } catch(IOException ex) {
            return false;
        }
    }

    /**
     * 图片压缩
     * @param multipartFile
     * @return
     * @throws Exception
     */
    private File zipPictureFile(MultipartFile multipartFile)throws IOException{
        initDirectory(TMP_FILE_PATH);
        File file = new File(TMP_FILE_PATH+multipartFile.getOriginalFilename());
        Thumbnails.of(multipartFile.getInputStream()).scale(1f).outputQuality(0.5f).toFile(file);
        return file;
    }

    /**
     * 初始化目录
     */
    private boolean initDirectory(String path) {

        File dir = new File(path);

        if (dir.exists()) {
            return false;
        }

        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }

        // 创建目录
        if (dir.mkdirs()) {
            return true;
        } else {
            return false;
        }
    }
}
