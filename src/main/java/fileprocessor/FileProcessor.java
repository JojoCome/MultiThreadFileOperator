package fileprocessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

//控制线程池,管理任务提交的主逻辑类
/*
 * 目标:
 * 1.使用线程池 ExecutorService并发处理文件
 * 2.遍历指定目录,找到所有文件
 * 3.为每个文件提交一个Callable任务
 * 4.使用Future收集任务执行情况 (暂时只打印)
 * */
public class FileProcessor {
    private final String rootPath;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private StatsCollector  statsCollector = new StatsCollector(executor);
    public FileProcessor(String rootPath) {
        /*根据给定的路径字符串rootPath,创建一个File对象root,代表一个目录或文件
         * 不会立刻访问磁盘文件是否存在,只是创建一个抽象表示
         *
         * 后面的判断是:路径合法性检查,确保我们输入的路径是一个存在的目录,否则就终止程序运行
         * */
        this.rootPath = rootPath;
    }

    public void processFiles() {
        FileScanTask fileScanTask = new FileScanTask();
        List<File> files = fileScanTask.scan(rootPath);
        statsCollector.processFiles(files);
        statsCollector.printStats();
        executor.shutdown();
    }
}
