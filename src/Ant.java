/**
 * Created by coco on 17-10-20.
 * 蚂蚁类，进行路径搜索的载体
 */


import java.util.Random;
import java.util.Vector;

public class Ant implements Runnable {

    private Vector<Integer> tabu; // 禁忌表
    private Vector<Integer> allowedCities; // 允许搜索的城市
    private float[][] delta; // 信息数变化矩阵
    private int[][] distance; // 距离矩阵
    private float alpha;
    private float beta;

    private int tourLength; // 路径长度
    private int cityNum; // 城市数量
    private int firstCity; // 起始城市
    private int lastCity;//终点城市
    private int currentCity; // 当前城市

    public Ant() {
        cityNum = 30;
        tourLength = 0;
    }

    /**
     * Constructor of Ant
     *
     * @param num
     *            城市数量
     */
    public Ant(int num) {
        cityNum = num;
        tourLength = 0;
    }

    /**
     * 初始化蚂蚁，随机选择起始位置
     *
     * @param distance
     *            距离矩阵
     * @param a
     *            alpha
     * @param b
     *            beta
     */

    public void init(int[][] distance, float a, float b) {
        alpha = a;
        beta = b;
        // 初始允许搜索的城市集合
        allowedCities = new Vector<Integer>();
        // 初始禁忌表
        tabu = new Vector<Integer>();
        // 初始距离矩阵
        this.distance = distance;
        // 初始信息数变化矩阵为0
        delta = new float[cityNum][cityNum];
        for (int i = 0; i < cityNum; i++) {
            Integer integer = i;
            allowedCities.add(integer);
            for (int j = 0; j < cityNum; j++) {
                delta[i][j] = 0.f;
            }
        }
        // 随机挑选一个城市作为起始城市
        Random random = new Random(System.currentTimeMillis());
        firstCity = random.nextInt(cityNum);
        lastCity = firstCity;
        // 允许搜索的城市集合中移除起始城市
        for (Integer i : allowedCities) {
            if (i.intValue() == firstCity) {
                allowedCities.remove(i);
                break;
            }
        }
        // 将起始城市添加至禁忌表
        tabu.add(Integer.valueOf(firstCity));
        // 当前城市为起始城市
        currentCity = firstCity;
    }

    /**
     * 初始化蚂蚁，采用固定起始位置和终点位置
     * @param distance
     *        距离矩阵
     * @param a
     * @param b
     *        更新信息素矩阵参数
     * @param firstCity
     *        起始城市
     * @param lastCity
     *        终点城市
     */
    public void init(int[][] distance, float a, float b,int firstCity,int lastCity) {
        alpha = a;
        beta = b;
        // 初始允许搜索的城市集合
        allowedCities = new Vector<Integer>();
        // 初始禁忌表
        tabu = new Vector<Integer>();
        // 初始距离矩阵
        this.distance = distance;
        // 初始信息素变化矩阵为0
        delta = new float[cityNum][cityNum];
        for (int i = 0; i < cityNum; i++) {
            Integer integer = i;
            allowedCities.add(integer);
            for (int j = 0; j < cityNum; j++) {
                delta[i][j] = 0.f;
            }
        }
        // 设置起始城市和终点城市
        this.firstCity = firstCity;
        this.lastCity = lastCity;

        // 在允许搜索的城市集合中查找并移除起始城市
        for (Integer i : allowedCities) {
            if (i.intValue() == firstCity) {
                allowedCities.remove(i);
                break;
            }
        }
        // 将起始城市添加至禁忌表
        tabu.add(Integer.valueOf(firstCity));
        // 起始城市为当前城市
        currentCity = firstCity;
    }

    /**
     * 选择下一个城市
     * @author damimao
     * @param pheromone 信息素矩阵
     *
     */
    public void selectNextCity(float[][] pheromone) {
//        float[] p = new float[cityNum];
        double[] p = new double[cityNum];
//        float sum = 0.0f;
        double sum = 0.0f;
        // 计算分母部分

        for (int i = 0;i < allowedCities.size();i++) {
            int city = allowedCities.get(i);
            sum += Math.pow(pheromone[currentCity][city], alpha)
                    * Math.pow(1.0 / distance[currentCity][city], beta);
        }
        // 计算概率矩阵
        for (int i = 0; i < cityNum; i++) {
            boolean flag = false;
            for (int j = 0;j < allowedCities.size();j++) {
                if (i == allowedCities.get(j)) {
//                    p[i] = (float) (Math.pow(pheromone[currentCity][i], alpha) * Math
//                            .pow(1.0 / distance[currentCity][i], beta)) / sum;
                    p[i] = (Math.pow(pheromone[currentCity][i], alpha) * Math
                            .pow(1.0 / distance[currentCity][i], beta)) / sum;
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                p[i] = 0.f;
            }
        }

        // 轮盘赌选择下一个城市
        Random random = new Random(System.currentTimeMillis());
//        float selectP ;
        double selectP ;
        int selectCity = 0;

        if (allowedCities.size() == 1) {
            if(allowedCities.get(0) == lastCity) {
                selectCity = lastCity;
            }
            else {
                selectCity = allowedCities.get(0);
            }
        }
        else
        {
//            selectP = random.nextFloat()*(1 - p[lastCity]);
            selectP = random.nextDouble()*(1 - p[lastCity]);
            double sum1 = 0;
            for (int i = 0; i < cityNum; i++)
            {
                if (i == lastCity)
                {
                    selectP = selectP + p[lastCity];
                    sum1 += p[i];
                }
                else
                {
                    sum1 += p[i];
                }

                if (sum1 >= selectP)
                {
                    selectCity = i;
                    break;
                }

            }

        }
//            System.out.println("selectCity:" + selectCity +"\tlastCity:"+lastCity);


//        int selectCity = 0;
//        do {
//            selectCity = allowedCities.get((int) Math.floor(Math.random() * allowedCities.size()));
//        }while((allowedCities.size() > 1)&&(selectCity == lastCity));



        // 从允许选择的城市中去除select city
        for (int i = 0;i < allowedCities.size();i++) {
            if (allowedCities.get(i) == selectCity) {
                allowedCities.remove(i);
                break;
            }
        }
        // 在禁忌表中添加select city
        tabu.add((selectCity));
        // 将当前城市改为选择的城市
        currentCity = selectCity;
    }


    public void run()
    {
        for (int j = 1; j < cityNum; j++) {
            selectNextCity(ACO.pheromone);
        }

        if(firstCity == lastCity){
            getTabu().add(lastCity);
        }
//        if (getTourLength() < ACO.bestLength) {
//            // 比当前优秀则拷贝优秀TSP路径
//            ACO.bestLength = getTourLength();
//
//            ACO.bestTour.clear();
//            for (int k = 0; k < getTabu().size() ; k++) {
//                ACO.bestTour.add(getTabu().get(k));
//            }//---- lupeng & damimao
//        }


        for (int j = 0; j < getTabu().size() - 1; j++) {
            getDelta()[getTabu().get(j)][getTabu().get(j + 1)] = (float) (1. / getTourLength());

            getDelta()[getTabu().get(j + 1)][getTabu().get(j)] = (float) (1. / getTourLength());
        }//---- lupeng

        ACO.UpdateSync();
    }

    /**
     * 计算路径长度
     *
     * @return 路径长度
     */
    private int calculateTourLength() {
        int len = 0;
        //禁忌表tabu最终形式：起始城市,城市1,城市2...城市n,起始城市
//        for (int i = 0; i < cityNum; i++) {
//            len += distance[this.tabu.get(i).intValue()][this.tabu.get(i + 1)
//                    .intValue()];
//        }
        for (int i = 0; i < this.tabu.size()-1; i++) {
            len += distance[this.tabu.get(i)][this.tabu.get(i + 1)];
        }//--- lupeng
        return len;
    }

    public Vector<Integer> getAllowedCities() {
        return allowedCities;
    }

    public void setAllowedCities(Vector<Integer> allowedCities) {
        this.allowedCities = allowedCities;
    }

    public int getTourLength() {
        tourLength = calculateTourLength();
        return tourLength;
    }

    public void setTourLength(int tourLength) {
        this.tourLength = tourLength;
    }

    public int getCityNum() {
        return cityNum;
    }

    public void setCityNum(int cityNum) {
        this.cityNum = cityNum;
    }

    public Vector<Integer> getTabu() {
        return tabu;
    }

    public void setTabu(Vector<Integer> tabu) {
        this.tabu = tabu;
    }

    public float[][] getDelta() {
        return delta;
    }

    public void setDelta(float[][] delta) {
        this.delta = delta;
    }

    public int getFirstCity() {
        return firstCity;
    }

    public void setFirstCity(int firstCity) {
        this.firstCity = firstCity;
    }

    public int getLastCity() {
        return lastCity;
    }
}
