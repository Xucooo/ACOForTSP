import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by coco on 17-11-11.
 * 对所有对点之间可能出现的频率进行统计 City[i]和City[j]在全部的序列中一共出现的频次
 */
public class CountAllFreq {

    static int[][] CountFreq = new int[48][48];//统计矩阵，记录48个点两两之间出现的频次
    static BufferedReader data;
    static String stringBuffer;//记录当前读入的行
    static int[] AfterSortLineIndex;
    static int[] AfterSortColumIndex;
    static int[] AfterSortFreq;
    static int FreqNum = 60;

    public static void ReadIn() throws Exception{
        data = new BufferedReader(new InputStreamReader(new FileInputStream( "result.txt")));
    }

    //对每一行进行数对出现的频率统计，保存在CountFreq[][]中
    public static int[][] count() throws Exception{

        String[] charBuffer ;
        ArrayList<Integer> line = new ArrayList<Integer>();

        //读入文件
//        data = new BufferedReader(new InputStreamReader(new FileInputStream( "/home/coco/Downloads/result.txt")));
        stringBuffer = data.readLine();
        charBuffer = stringBuffer.split(" ");
        for (int i = 0; i <charBuffer.length ; i++) {
            line.add(Integer.valueOf(charBuffer[i]));
        }
        //对每一行出现的数对计数
        for (int i = 0; i <47 ; i++) {

            CountFreq[line.get(i)][line.get(i+1)] = CountFreq[line.get(i)][line.get(i+1)]+1;

//            CountFreq[][]
        }
        return CountFreq;
    }


    //对获取的频率排序，并将各对点序号及出现频率保存
    public static int[] SortForCount(int[][] CountFreq){

        int LineIndex =0 ,ColumIndex = 0 ;
        int[][] CopyCount = new int[48][48] ;
        int max = 0;
//        AfterSortLineIndex = new int[CopyCount.length]; //按出现频率降序排序后，CountFreq[][]的行号
//        AfterSortColumIndex = new int[CopyCount.length];//按出现频率降序排序后，CountFreq[][]的列号
        AfterSortLineIndex = new int[FreqNum];
        AfterSortColumIndex = new int[FreqNum];

//        AfterSortFreq = new int[40];//保存出现频率最高的前40对点的频率
        AfterSortFreq = new int[FreqNum];
        for (int i = 0; i < 48; i++) {
            CopyCount[i] = CountFreq[i].clone();
        }

        for (int k = 0 ; k < FreqNum ; k ++ ) {//只找出频率最高的前40对点
            for (int i = 0; i < 48; i++) {
                for (int j = 0; j < 48; j++) {
                    if (max < CopyCount[i][j]) {
                        max = CopyCount[i][j];
                        ColumIndex = j;
                        LineIndex = i;
                    }
                }
            }

            CopyCount[LineIndex][ColumIndex] = 0;
            max = 0;
            AfterSortLineIndex[k] = LineIndex;
            AfterSortColumIndex[k] = ColumIndex;
            AfterSortFreq[k] = CountFreq[LineIndex][ColumIndex];
        }

//        for (int i = 0; i < 40; i++) {
//
//            System.out.print("(" + AfterSortLineIndex[i] + ", " + AfterSortColumIndex[i] + ")" + ":" + AfterSortFreq[i] + " ");
//        }


        return AfterSortFreq;
    }

}
