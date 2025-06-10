package fileprocessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//每个文件的扫描和处理任务 实现callable接口
public class FileScanTask {
    //扫描指定目录,返回所有文件
    public List<File> scan(String rootPath){
        File root = new File(rootPath);//根据路径创建File对象
        List<File> result = new ArrayList<>();//创建结果列表

        if (root.exists() && root.isDirectory()){//判断文件是否存在,如果File对象不是文件夹,则没必要继续处理
            collectFiles(root, result);
        }

        return result;
    }

    //
    private void collectFiles(File dir, List<File> result) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    result.add(file);
                } else if (file.isDirectory()) {
                    collectFiles(file, result);
                }
            }
        }
    }
}
