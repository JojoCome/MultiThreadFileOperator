package fileprocessor;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

//统计器类,统计文件数量,总大小,每种扩展名的数量和大小
public class StatsCollector {
    private final ExecutorService executor;

    //线程安全的统计结构
    private AtomicInteger totalFileCount = new AtomicInteger(0);
    private AtomicLong totalFileSize = new AtomicLong(0);
    private ConcurrentHashMap<String, AtomicInteger> extensionCountMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, AtomicLong> extensionSizeMap = new ConcurrentHashMap<>();

    public StatsCollector(ExecutorService executor) {
        this.executor = executor;
    }


    public void processFiles(Iterable<File> files) {
        for (File file : files) {
            executor.submit(() -> {
                // 累加总数和总大小
                totalFileCount.incrementAndGet();
                totalFileSize.addAndGet(file.length());

                // 统计扩展名
                String ext = getExtension(file.getName());
                extensionCountMap.computeIfAbsent(ext, k -> new AtomicInteger(0)).incrementAndGet();
                extensionSizeMap.computeIfAbsent(ext, k -> new AtomicLong(0)).addAndGet(file.length());
            });
        }
    }

    public void printStats() {
        System.out.println("📊 统计结果:");
        System.out.println("总文件数: " + totalFileCount.get());
        System.out.println("总文件大小: " + totalFileSize.get() + " 字节");

        System.out.println("按扩展名分类：");
        for (String ext : extensionCountMap.keySet()) {
            int count = extensionCountMap.get(ext).get();
            long size = extensionSizeMap.get(ext).get();
            System.out.printf(" - %s: %d 个文件, 总大小: %d 字节%n", ext, count, size);
        }
    }

    // 提取扩展名的方法
    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }
        return "unknown";
    }
}
