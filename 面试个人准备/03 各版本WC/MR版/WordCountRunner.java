package wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * @Description
 * @Author cqh <caoqingghai@1000phone.com>
 * @Version V1.0
 * @Since 1.0
 * @Date 2019/4/12 17：23
 */
public class WordCountRunner {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();



            //conf.set("TOPN",args[0]);
            //conf.addResource();

            //conf.set("mapreduce.framework.name","local");
            //conf.set("fs.defaultFS","file:///");
            //设置行分隔符，可能 读取多行数据比如我们的配置文件</property>
            //conf.set("textinputformat.record.delimiter","</doc>");
            //获取job并携带参数
            Job job = Job.getInstance(conf,"wordCount");
            //可以用job对象封装一些信息
            //首先程序是一个jar包，就要指定jar包的位置
            //将jar包放在root目录下
            //可以将这个程序打包为pv.jar,上传到linux机器上
            //使用命令运行
            //hadoop jar /root/pv.jar pvcount.PvCountRunner /data/pvcount /out/pvcount
            //job.setJar("d:/word.jar");
            /**
             * 一个jar包中可能有多个job,所以我们要指明job使用的是哪儿一个map类
             * 哪儿一个reduce类
             */
            job.setMapperClass(WordCountMapper.class);
            job.setReducerClass(WordCountReduce.class);

            /**
             * map端输出的数据要进行序列化，所以我们要告诉框架map端输出的数据类型
             */
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);
            /**
             * reduce端要输出，所有也要指定数据类型
             */
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
            /**
             * 告诉框架用什么组件去读数据，普通的文本文件，就用TextInputFormat
             * 导入长包
             */
            job.setInputFormatClass(TextInputFormat.class);
            /**
             * 告诉这个组件去哪儿读数据
             * TextInputFormat有个父类FileInputFormat
             * 用父类去指定到哪儿去读数据
             * 输入路径是一个目录，该目录下如果有子目录需要进行设置递归遍历，否则会报错
             */
            FileInputFormat.addInputPath(job,new Path(args[0]));

            /**
             * 干预分片的大小
             */
            //FileInputFormat.setMinInputSplitSize(job,1000);
            //FileInputFormat.setMaxInputSplitSize(job,1000000);
            //设置写出的组件
            job.setOutputFormatClass(TextOutputFormat.class);
            //设置写出的路径
            FileOutputFormat.setOutputPath(job,new Path(args[1]));

            job.setCombinerClass(WordCountReduce.class);

            //job.setGroupingComparatorClass();

            // FileOutputFormat.
            //job.setNumReduceTasks(2);
            //执行
            /**
             * 信息设置完成，可以调用方法向yarn去提交job
             * waitForCompletion方法就会将jar包提交给RM
             * 然后rm可以将jar包分发，其他机器就执行
             */
            //传入一个boolean类型的参数，如果是true,程序执行会返回true/flase
            //如果参数传入true,集群在运行时会有进度，这个进度会在客户端打印
            boolean res = job.waitForCompletion(true);
            /**
             * 客户端退出后会返回一个状态码，这个状态码我们可以写shell脚本的时候使用
             * 根据返回的状态码的不同区执行不同的逻辑
             */
            System.exit(res? 0:1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}