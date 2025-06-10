package fileprocessor;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /*
         * 为什么先写Main.java
         * 1.并不是为了让它立刻成功,而是让它先运行起来,方便查看结果
         * 先设计好程序的调用结构(面向接口思维)
         * 明确我们要写什么类,写什么方法
         * 为接下来的开发 立下“蓝图”
         *
         * 这是Top-Down 自顶而下的设计法
         * */
        Scanner input = new Scanner(System.in);
        System.out.println("请输入要处理的文件路径:");

        String rootPath = input.nextLine();
        FileProcessor processor = new FileProcessor(rootPath);
        processor.processFiles();

    }

    private static void listFileTest(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                System.out.println(f.getName());
            } else if (f.isDirectory()) {
                listFileTest(f.getAbsolutePath());
            }
        }

        System.out.println(files[0].getAbsoluteFile());
        System.out.println(files[0].getAbsolutePath());
    }
}
