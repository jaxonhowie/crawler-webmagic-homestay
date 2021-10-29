package us.codecraft.webmagic.processor.custom.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Hongyi Zheng
 * @date 2018/6/11
 */
public class FileMergeUtil {

    /**
     * 读取文件内容为String
     *
     * @param filePath 文件的绝对路径
     * @return 返回文本中的内容
     */
    public static String readFile(String filePath){
        File file = new File(filePath);
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineContent = null;
            while (null != (lineContent = bufferedReader.readLine())){
                 sb.append(lineContent);
            }
            bufferedReader.close();
            reader.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取文件夹下所有文件的绝对路径
     *
     * @param folderPath
     * @return
     */
    @SuppressWarnings("ConstantConditions")
    private static Set<String> getAllFilePath(String folderPath){
        Set<String> files = new HashSet<String>();
        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            files.add(file.getAbsolutePath());
        }
        return files;
    }


    private static void writeFile(String content, String filePath){
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(content);
            bw.append("\n");
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Set<String> files = getAllFilePath("D:\\web-magic\\www.zhihu.com");
        for (String file : files) {
            System.out.println("filename:"+file);
            writeFile(readFile(file),"D:\\web-magic\\www.zhihu.com\\zhihu.txt");
        }
    }


}
