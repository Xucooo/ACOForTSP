import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by coco on 17-11-2.
 * 以第一行序列为基础进行统计，即FirstLine[i]和FirstLine[i+1]在全部的序列中一共出现的频次,得到的结果是CountAllFreq中得到的结果的子集
 */
public class CountFreq {
    static int[] Firstline = new int[48];//第一行
    static String stringBuffer ;//当前读入的行
    static String[] charBuffer = new String[48];//stringBuffer被分割后的字符串
    static BufferedReader data;
    static int[] AfterSortIndex;//按照频数排序完成后,AfterSortIndex[i]：出现频次为第i高的点在Count中的索引，
//    如：AfterSortIndex = {7,8,1,4},意思是按频数至多到少为： Count[7] > Count[8] > Count[1] > Count[4]
//    关于Count[]的注释，详见getFreq()方法

//    读入第一行
    public  static void getFirstline() throws Exception
    {
        //读入文件
        data = new BufferedReader(new InputStreamReader(new FileInputStream( "/home/coco/Downloads/result.txt")));
        // 将第一行保存在Firstline中
        stringBuffer = data.readLine();
        charBuffer = stringBuffer.split(" ");
        for (int i = 0; i < 48; i++)
        {
            Firstline[i] = Integer.valueOf(charBuffer[i]);
        }
    }


//    获取频率
    public static int[] getFreq() throws Exception
    {
        int[] count = new int[47];//count[i]对应第一行的路段(FirstLine[i], FirstLine[i+1])
        int value;
        for (int j = 0; j <47 ; j++) {
            count[j] = 1;
        }

//        for (int i = 0; i < 39; i++)
        for (int i = 0; i < 399; i++) {
            ArrayList<Integer> temp = ReadAppointedLine();
            for (int j = 0; j < 47; j++)
            {
                value = temp.indexOf(Firstline[j]);
                if ( value != -1 && value != 47 && (Firstline[j+1]==temp.get(value+1)))
                {
                    count[j]++;
                }
            }
        }
        return count;
    }



//    对频率排序
    public static int[] SortForCount(int[] count)
    {
        int i,index = 0 ;
        int[] CopyCount = count.clone();
        int max = 0;
        AfterSortIndex = new int[CopyCount.length];

        for (int j = 0; j <CopyCount.length ; j++)
        {
            for (i = 0; i < CopyCount.length; i++)
            {
                if (max < CopyCount[i])
                {
                    max = CopyCount[i];
                    index = i;
                }
            }
            AfterSortIndex[j] = index;
            CopyCount[index] = 0;
            max = 0;
        }

        for (int j = 0; j <AfterSortIndex.length ; j++)
        {
            System.out.print("(" + Firstline[AfterSortIndex[j]] + ", " + Firstline[AfterSortIndex[j]+1] + "):"+count[AfterSortIndex[j]] + " ");
        }
        return AfterSortIndex;
    }

//    每一次读入一行

    public static ArrayList<Integer> ReadAppointedLine() throws Exception
    {

        ArrayList<Integer>  tour = new ArrayList<Integer>();
        stringBuffer = data.readLine();
        charBuffer = stringBuffer.split(" ");
        tour.clear();
        for (int i = 0; i < 48; i++)
        {
            tour.add(Integer.valueOf(charBuffer[i]));
        }

        return tour;

    }

    public static void main(String[] args) throws Exception
    {
        getFirstline();
        int[] re = getFreq();
//        for (int i = 0; i < re.length; i++)
//        {
//            System.out.println("(" + Firstline[i] + ", " + Firstline[i+1]+ ")" + ": "+ re[i]);
//        }
        System.out.println("Sort by Freq: ");
        SortForCount(re);


    }

}

