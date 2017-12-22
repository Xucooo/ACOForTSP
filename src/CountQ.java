/**
 * Created by coco on 17-11-28.
 */
class Count {
    public int FreqMin, FreqMax , DistMin , DistMax;
    public double[][] Q = new double[48][48];
    public int OptNum = 40;
    int[] AfterLine = new int[48];
    int[] AfterColum = new int[48];

    //求最低频率
    public int GetFreqMin(){
        FreqMin = Integer.MAX_VALUE;
        for (int i = 0; i <CountAllFreq.CountFreq.length ; i++) {
            for (int j = 0; j < CountAllFreq.CountFreq[i].length; j++) {
                if ((CountAllFreq.CountFreq[i][j] !=0)&&(CountAllFreq.CountFreq[i][j] < FreqMin)){
                    FreqMin = CountAllFreq.CountFreq[i][j];
                }
            }
        }
        return FreqMin;
    }

    //求最高频率
    public int GetFreqMax(){
        FreqMax = 0;
        for (int i = 0; i <CountAllFreq.CountFreq.length ; i++) {
            for (int j = 0; j < CountAllFreq.CountFreq[i].length; j++) {
                if (CountAllFreq.CountFreq[i][j] > FreqMax){
                    FreqMax = CountAllFreq.CountFreq[i][j];
                }
            }
        }
        return FreqMax;
    }

    //求最短距离
    public int GetDistMin(int[][] distance){
        DistMin = Integer.MAX_VALUE;
        for (int i = 0; i < distance.length ; i++) {
            for (int j = 0; j < distance[i].length; j++) {
                if ((distance[i][j] != 0) && (distance[i][j] < DistMin)){
                    DistMin = distance[i][j];
                }
            }
        }
        return DistMin;
    }

    //求最长距离
    public int GetDistMax(int[][] distance){
        DistMax = 0;
        for (int i = 0; i < distance.length ; i++) {
            for (int j = 0; j < distance[i].length; j++) {
                if (distance[i][j] > DistMax){
                    DistMax = distance[i][j];
                }
            }
        }
        return DistMax;
    }


    //求最大Q值
    public double[][] CountQ(int min,int max,int[][] distance){
        for (int i = 0; i < CountAllFreq.CountFreq.length; i++) {
            for (int j = 0; j <CountAllFreq.CountFreq[i].length ; j++) {
                if (CountAllFreq.CountFreq[i][j] > 1 && (distance[i][j] != 0)) {
                    Q[i][j] = (CountAllFreq.CountFreq[i][j] - (double)FreqMin) / ((double) FreqMax - (double) FreqMin) *
                            ((double) distance[i][j] - (double) DistMin) / ((double) DistMax - (double) DistMin);
                }

                else Q[i][j] = 0;
            }
        }
        return Q;
    }


    //对Q排序，取最大的前OptNum个存储其在Q中的行号和列号在AfterLine和AfterColum中
    public int[][] QSort(double[][] Q){
        double max = 0;
        int line = 0, colum = 0;
        double[][] QCopy = new double[Q.length][Q.length];
        int[][] Index = new int[Q.length][2];

        for (int i = 0; i < Q.length; i++) {
            QCopy[i] = Q[i].clone();
        }

        for (int k = 0; k < OptNum ; k++) {
            for (int i = 0; i < Q.length; i++) {
                for (int j = 0; j < Q[i].length; j++) {
                    if (max < QCopy[i][j]) {
                        max = QCopy[i][j];
                        line = i;
                        colum = j;
                    }
                }
            }

            QCopy[line][colum] = 0;
            max = 0;
            AfterLine[k] = line;
            AfterColum[k] = colum;
        }

        for (int i = 0; i <OptNum ; i++) {
            Index[i][0] = AfterLine[i];
            Index[i][1] = AfterColum[i];
            System.out.println(Index[i][0] +"," + Index[i][1] + ":"  + Q[Index[i][0]][Index[i][1]]);

        }


        return Index;
    }

}

class CountQ{
    public static void main(String[] args) throws Exception {
        ACO aco1 = new ACO(48, 10, 10, 1.f, 5.f, 0.4f);


        for (int i = 0; i < 10; i++) {
            int firstCity = (int) (Math.random() * 48);
            int lastCity = (int) (Math.random() * 48);
//            System.out.println("第" + i + "对起点终点\tfirstCity:" + firstCity + "\tlastCity:" + lastCity);
            aco1.init("data.txt", firstCity, lastCity);
            aco1.solve(firstCity, lastCity);
        }


        CountAllFreq AllFreq = new CountAllFreq();
        AllFreq.ReadIn();
        for (int i = 0; i < 10; i++) {
            AllFreq.count();
        }
        AllFreq.SortForCount(AllFreq.CountFreq);

        Count coun = new Count();
        int mi = coun.GetFreqMin();
        int ma = coun.GetFreqMax();
        coun.GetDistMin(aco1.distance);
        coun.GetDistMax(aco1.distance);
        double[][] qvalue;
        qvalue = coun.CountQ(mi, ma, aco1.distance);
        coun.QSort(qvalue);


    }
}