/**
 * Created by coco on 17-10-20.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.lang.*;

        import static java.util.logging.Logger.getLogger;

public class ACO {

    private Ant[] ants; // 蚂蚁
    private int antNum; // 蚂蚁数量
    private int cityNum; // 城市数量
    private int MAX_GEN; // 运行代数
    public static float[][] pheromone; // 信息素矩阵
//    private float[][] pheromone;
    private int[][] distance; // 距离矩阵
//    private int bestLength; // 最佳长度
//    private ArrayList<Integer> bestTour; // 最佳路径
    public static int bestLength;
    public static ArrayList<Integer> bestTour;
    public static int sum;//所有（起点，终点）经过的路径总长度
    public static int Ave;//40对（起点，终点）的平均路径长度

    public static int thrd_sync;

    static File of;
    static FileWriter fw;

    // 三个参数
    private float alpha;
    private float beta;
    private float rho;

    public ACO() {

    }

    /**
     * constructor of ACO
     *
     * @param n
     *            城市数量
     * @param m
     *            蚂蚁数量
     * @param g
     *            运行代数
     * @param a
     *            alpha
     * @param b
     *            beta
     * @param r
     *            rho
     *
     **/
    public ACO(int n, int m, int g, float a, float b, float r) {
        cityNum = n;
        antNum = m;
        ants = new Ant[antNum];
        MAX_GEN = g;
        alpha = a;
        beta = b;
        rho = r;
    }

    // 给编译器一条指令，告诉它对被批注的代码元素内部的某些警告保持静默
    @SuppressWarnings("resource")
    /**
     * 初始化ACO算法类
     * @param filename 数据文件名，该文件存储所有城市节点坐标数据
     * @throws IOException
     */
    private void init(String filename) throws IOException {
        // 读取数据
        int[] x;
        int[] y;
        String strbuff;
        BufferedReader data = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename)));
        distance = new int[cityNum][cityNum];
        x = new int[cityNum];
        y = new int[cityNum];
        for (int i = 0; i < cityNum; i++) {
            // 读取一行数据，数据格式1 6734 1453
            strbuff = data.readLine();
            // 字符分割
            String[] strcol = strbuff.split(" ");
            x[i] = Integer.valueOf(strcol[0]);// x坐标
            y[i] = Integer.valueOf(strcol[1]);// y坐标
        }
        // 计算距离矩阵
        // 针对具体问题，距离计算方法也不一样，此处用的是att48作为案例，它有48个城市，距离计算方法为伪欧氏距离，最优值为10628
        for (int i = 0; i < cityNum - 1; i++) {
            distance[i][i] = 0; // 对角线为0
            for (int j = i + 1; j < cityNum; j++) {
                double rij = Math
                        .sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j])
                                * (y[i] - y[j])) / 10.0);
                // 四舍五入，取整
                int tij = (int) Math.round(rij);
                if (tij < rij) {
                    distance[i][j] = tij + 1;
                    distance[j][i] = distance[i][j];
                } else {
                    distance[i][j] = tij;
                    distance[j][i] = distance[i][j];
                }
            }
        }
        distance[cityNum - 1][cityNum - 1] = 0;
        // 初始化信息素矩阵
        pheromone = new float[cityNum][cityNum];
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                pheromone[i][j] = 0.1f; // 初始化为0.1
            }
        }
        bestLength = Integer.MAX_VALUE;
        bestTour = new ArrayList<Integer>();
        // 随机放置蚂蚁

        for (int i = 0; i < antNum; i++) {
            ants[i] = new Ant(cityNum);
            ants[i].init(distance, alpha, beta);
        }
    }


    /**
     *
     * @param filename
     * @param firstCity
     * @param lastCity
     * @throws IOException
     */
    private void init(String filename,int firstCity,int lastCity) throws IOException
    {
        // 读取数据
        int[] x;
        int[] y;
        String strbuff;
        BufferedReader data = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename)));
        distance = new int[cityNum][cityNum];
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
        // 计算距离矩阵（对称矩阵）
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

        // 初始化信息素矩阵
        pheromone = new float[cityNum][cityNum];
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                pheromone[i][j] = 0.1f; // 初始化为0.1
            }
        }
        bestLength = Integer.MAX_VALUE;
        bestTour = new ArrayList<Integer>();

        // 随机放置蚂蚁
        for (int i = 0; i < antNum; i++) {
            ants[i] = new Ant(cityNum);
            ants[i].init(distance, alpha, beta,firstCity,lastCity);
        }
    }


    private void init(String filename,int firstCity,int lastCity,int k) throws IOException {
        String strbuff;
        BufferedReader data = new BufferedReader(new InputStreamReader(new FileInputStream("Distance.txt")));

        for (int i = 0; i < cityNum; i++)
        {
            strbuff = data.readLine();
            String[] strcol = strbuff.split("\t");
            for (int j = 0; j < cityNum ; j++)
            {
                // 字符分割
                distance[i][j] = Integer.valueOf(strcol[j]);
            }
        }


        for (int i = 0; i <= k ; i++) {
           distance[CountAllFreq.AfterSortLineIndex[i]][CountAllFreq.AfterSortColumIndex[i]] =
                    distance[CountAllFreq.AfterSortLineIndex[i]][CountAllFreq.AfterSortColumIndex[i]]/2;
           distance[CountAllFreq.AfterSortColumIndex[i]][CountAllFreq.AfterSortLineIndex[i]] =
                    distance[CountAllFreq.AfterSortLineIndex[i]][CountAllFreq.AfterSortColumIndex[i]];
        }

        // 初始化信息素矩阵
        pheromone = new float[cityNum][cityNum];
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                pheromone[i][j] = 0.1f; // 初始化为0.1
            }
        }
        bestLength = Integer.MAX_VALUE;
        bestTour = new ArrayList<Integer>();

        // 随机放置蚂蚁
        for (int i = 0; i < antNum; i++) {
            ants[i] = new Ant(cityNum);
            ants[i].init(distance, alpha, beta,firstCity,lastCity);
        }
    }



    public void solve(int firstCity,int lastCity) {
        // 迭代MAX_GEN次
        for (int g = 0; g < MAX_GEN; g++) {
            thrd_sync = 0;
//            if(g%100 == 0)
//                System.out.println("正在进行第 "+g+" 次迭代……");
            // antNum只蚂蚁
            for (int i = 0; i < antNum; i++) {
                // i这只蚂蚁走cityNum步，完整一个TSP
//                for (int j = 1; j < cityNum; j++) {
//                    ants[i].selectNextCity(pheromone);
//                }
                new Thread(ants[i]).start();

//                if(firstCity == lastCity){
//                    ants[i].getTabu().add(lastCity);
//                }
////                for (int j = 1; j < cityNum; j++) {
////                    ants[i].selectNextCity(pheromone);
////                }//---- lupeng
//
//                // 把这只蚂蚁起始城市加入其禁忌表中
//                // 禁忌表最终形式：起始城市,城市1,城市2...城市n,起始城市
//                //ants[i].getTabu().add(ants[i].getFirstCity());
//                // 查看这只蚂蚁行走路径距离是否比当前距离优秀
//                if (ants[i].getTourLength() < bestLength) {
//                    // 比当前优秀则拷贝优秀TSP路径
//                    bestLength = ants[i].getTourLength();
////                    for (int k = 0; k < cityNum + 1; k++) {
////                        bestTour[k] = ants[i].getTabu().get(k).intValue();
////                    }
//
//                    bestTour.clear();
//                    for (int k = 0; k < ants[i].getTabu().size() ; k++) {
//                        bestTour.add(ants[i].getTabu().get(k).intValue());
//                    }//---- lupeng & damimao
//                }
//                // 更新这只蚂蚁的信息素变化矩阵，对称矩阵
////                for (int j = 0; j < cityNum; j++) {
////                    ants[i].getDelta()[ants[i].getTabu().get(j).intValue()][ants[i]
////                            .getTabu().get(j + 1).intValue()] = (float) (1. / ants[i]
////                            .getTourLength());
////                    ants[i].getDelta()[ants[i].getTabu().get(j + 1).intValue()][ants[i]
////                            .getTabu().get(j).intValue()] = (float) (1. / ants[i]
////                            .getTourLength());
////                }
//                for (int j = 0; j < ants[i].getTabu().size() - 1; j++) {
//                    ants[i].getDelta()[ants[i].getTabu().get(j).intValue()][ants[i].getTabu().get(j + 1).intValue()] = (float) (1. / ants[i].getTourLength());
//
//                    ants[i].getDelta()[ants[i].getTabu().get(j + 1).intValue()][ants[i].getTabu().get(j).intValue()] = (float) (1. / ants[i].getTourLength());
//                }//---- lupeng
            }

            while(thrd_sync<antNum){
                System.out.print("");
            }

            for (int i = 0; i <antNum ; i++) {
                if (ants[i].getTourLength() < ACO.bestLength) {
                    // 比当前优秀则拷贝优秀TSP路径
                    bestLength =ants[i].getTourLength();

                    bestTour.clear();
                    for (int k = 0; k < ants[i].getTabu().size() ; k++) {
                        bestTour.add(ants[i].getTabu().get(k));
                    }//---- lupeng & damimao
                }

            }

            // 更新信息素
            updatePheromone();
            // 重新初始化蚂蚁
//            for (int i = 0; i < antNum; i++) {
//                ants[i].init(distance, alpha, beta);
//            }
            for (int i = 0; i < antNum; i++) {
                ants[i].init(distance, alpha, beta,ants[i].getFirstCity(),ants[i].getLastCity());
            }//--- lupeng
        }

        //            计算路径长度总值和平均值
        sum = bestLength + sum;

        // 打印最佳结果
        printOptimal();
        writeToFile();
//        System.out.flush();
    }

    // 更新信息素
    private void updatePheromone() {
        // 信息素挥发
        for (int i = 0; i < cityNum; i++)
            for (int j = 0; j < cityNum; j++)
                pheromone[i][j] = pheromone[i][j] * (1 - rho);
//                pheromone[i][j] = pheromone[i][j] * rho ;
        // 信息素更新
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                for (int k = 0; k < antNum; k++) {
                    pheromone[i][j] += ants[k].getDelta()[i][j];
                }
            }
        }
    }

    public static synchronized void UpdateSync(){
        thrd_sync ++ ;
    }

    private void printOptimal() {
        System.out.println("The optimal length is: " + bestLength);
        System.out.println("The optimal tour is: ");
        for (int i = 0; i < bestTour.size() ; i++) {
            System.out.print(bestTour.get(i)+" ");
        }
        System.out.println(" ");
    }


//    输入到文件
    private void writeToFile()
    {
        try
        {
//            FileWriter fw = new FileWriter(of);

//            fw.append("The optimal length is: " + bestLength + "\r\n");
//            fw.append("The optimal tour is:");
            for (int i = 0; i < bestTour.size(); i++)
            {
                fw.append(bestTour.get(i) + " ");
            }
            fw.append("\r\n");
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }



    /**
     * @param args
     * @throws IOException
     */
//    public static void main(String[] args) throws Exception {
//        System.out.println("Start....");
//        ACO aco = new ACO(48, 20, 1000, 1.f, 5.f, 0.4f);
//
//        for (int j = 0; j < 400; j++) {
//            int firstCity = (int) (Math.random() * 48);
//            int lastCity = (int) (Math.random() * 48);
//            System.out.println("第" + j + "对起点终点\tfirstCity:" + firstCity + "\tlastCity:" + lastCity);
//            aco.init("/home/coco/Downloads/data.txt", firstCity, lastCity);
//            aco.solve(firstCity, lastCity);
//        }
//
//        fw.close();
//
//        Ave = sum / 400;
//
//        System.out.println("The average tour length is : " + Ave);
//        CountAllFreq AllFreqNew = new CountAllFreq();
//        AllFreqNew.ReadIn();
//        for (int i = 0; i < 400; i++) {
//            AllFreqNew.count();
//        }
//        AllFreqNew.SortForCount(AllFreqNew.CountFreq);
//    }
//}
    public static void main(String[] args) throws Exception{
        ACO aco = new ACO(48, 100, 50, 1.f, 5.f, 0.4f);

        of = new File("result.txt");
        try{
            fw = new FileWriter(of);
        }catch (IOException ex){}

        //未优化
        for (int i = 0; i < 400; i++) {
            int firstCity = (int) (Math.random() * 48);
            int lastCity = (int) (Math.random() * 48);
//            System.out.println("第" + i + "对起点终点\tfirstCity:" + firstCity + "\tlastCity:" + lastCity);
            aco.init("data.txt", firstCity, lastCity);
            aco.solve(firstCity, lastCity);
        }
        fw.close();
        System.out.println("The average tour length is : " + sum/400);

        sum = 0;
        CountAllFreq AllFreq = new CountAllFreq();
        AllFreq.ReadIn();
        for (int i = 0; i < 400; i++) {
            AllFreq.count();
        }
        AllFreq.SortForCount(AllFreq.CountFreq);

        try{
            fw = new FileWriter(of);
        }catch (IOException ex){ex.printStackTrace();}

//        //优化
        for (int j = 0 ; j < 40 ; j++){
            for (int i = 0; i < 400 ; i++) {
                int firstCity = (int) (Math.random() * 48);
                int lastCity = (int) (Math.random() * 48);
//                System.out.println("第" + i + "对起点终点\tfirstCity:" + firstCity + "\tlastCity:" + lastCity);
                aco.init("data.txt", firstCity, lastCity,j);
                aco.solve(firstCity, lastCity);
            }
            System.out.println("优化"+j+": \t"+"The average tour length is: " + sum/400);
        sum = 0;
        }
    }
}
