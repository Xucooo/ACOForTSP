import org.omg.PortableInterceptor.INACTIVE;

import java.io.*;

/**
 * Created by coco on 17-11-15.
 */
public class ComuteDist {
    static int cityNum = 48;//城市数量

    public static void main(String[] args) throws Exception {
        // 读人数据
        int[] x;
        int[] y;
        String strbuff;
        BufferedReader data = new BufferedReader(new InputStreamReader(
                new FileInputStream("/home/coco/Downloads/data.txt")));
        int[][] distance = new int[cityNum][cityNum];
        x = new int[cityNum];
        y = new int[cityNum];
        for (int i = 0; i < cityNum; i++)
        {
            // 读取一行数据，数据格式1 6734 1453
            strbuff = data.readLine();
            // 字符分割
            String[] strcol = strbuff.split(" ");
            x[i] = Integer.valueOf(strcol[0]);// x坐标
            y[i] = Integer.valueOf(strcol[1]);// y坐标
        }
        // 计算距离矩阵
        // 针对具体问题，距离计算方法也不一样，此处用的是att48作为案例，它有48个城市，距离计算方法为欧氏距离，最优值为10628
        for (int i = 0; i < cityNum - 1; i++)
        {
            distance[i][i] = 0; // 对角线为0
            for (int j = i + 1; j < cityNum; j++)
            {
                double rij = Math.sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j])) / 10.0);

                // 四舍五入，取整
                int tij = (int) Math.round(rij);
                if (tij < rij)
                {
                    distance[i][j] = tij + 1;
                    distance[j][i] = distance[i][j];
                }
                else
                {
                    distance[i][j] = tij;
                    distance[j][i] = distance[i][j];
                }
            }
        }
        distance[cityNum - 1][cityNum - 1] = 0;

        //输出距离矩阵到文件
        File IN = new File("/home/coco/Desktop/Distance.txt") ;
        FileWriter fw = new FileWriter(IN);

//        fw.write("d[" + "i" + "]" + ": " + "\t");
//        for (int i = 0; i <cityNum ; i++) {
//            fw.write(i + "\t");
//        }
//        fw.write("\n");
        for (int i = 0; i <cityNum ; i++) {
//            fw.write("d[" + i + "]" + ": " + "\t");
            for (int j = 0; j <cityNum; j++) {
                fw.write(distance[i][j] + "\t");
            }
//            fw.write("]");
            fw.write(" \n");

        }


        fw.close();

    }

}
