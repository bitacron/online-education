package com.example.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel  {
    //写操作
     public static void main(String[] args) {
        //实现excel写操作
        //1.设置写入文件地址和excel文件名称
        //String filename="E:\\write.xlsx";
        //2.调用easyExcel里面的方法实现写操作
        //EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());
        //读操作
        String filename = "E:\\write.xlsx";
        EasyExcel.read(filename, DemoData.class, new ExcelListener()).sheet().doRead();
    }


    private static List<DemoData> getData() {
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setSno(i + 1);
            data.setSname("Lucy" + i);
            list.add(data);
        }
        return list;
    }

}
