import java.util.ArrayList;

/**
 * Created by coco on 17-12-13.
 */
public class SortForDist {

    ArrayList <Integer> SortDistline = new ArrayList<Integer>();
    ArrayList <Integer> SortDistColum = new ArrayList<Integer>();
    int[][] SortDistIndex = new int[48][48];

    public int SortDist(int[][] distance,int i){
        int max = 0;
        int LineTemp =0 ,Columtemp = 0;


        int[][] copyDist = new int[48][48];

        for (int j = 0; j < 48; j++) {
            copyDist[j] = distance[j].clone();
        }

        for (int k = 0; k < i; k++) {

            for (int j =0 ; j < 48; j++) {
                for (int l = 0; l < 48; l++) {
                    if (max < copyDist[j][l]){
                        max = copyDist[j][l];
                        LineTemp = j;
                        Columtemp = l;
                    }
                }
            }
            SortDistline.add(LineTemp);
            SortDistColum.add(Columtemp);
            copyDist[LineTemp][Columtemp] = 0;
            copyDist[Columtemp][LineTemp] = 0;
            max = 0;

        }

        return 0;
    }

    public int SortDist(int[][] distance,int i,int j){
        int max = 0,a = 0;
        int Index =0;
        int[][] res =new int[48][48];

        int[][] copyDist = new int[48][48];

        for (int k = 0; k < 48; k++) {
            for (int l = 0; l < 48; l++) {
                if (max < copyDist[k][l]) max = copyDist[k][l];
                Index = l;
            }
            res[k][a] = Index;
            

        }
        return 0;
    }
}

class test{
    public static void main(String[] args)throws Exception {
        SortForDist sort = new SortForDist();
        ACO acooo = new ACO(48, 2, 10, 1.f, 5.f, 0.4f);
        acooo.init("data.txt");
        sort.SortDist(acooo.distance,40);
        for (int i = 0; i < 40; i++) {
            System.out.println(i +":(" + sort.SortDistline.get(i) + " ," + sort.SortDistColum.get(i) +") :"
                    + acooo.distance[sort.SortDistline.get(i)][sort.SortDistColum.get(i)]);
        }

        acooo.init("data.txt",40,11,40,sort.SortDistline,sort.SortDistColum);


        System.out.println();

        for (int i = 0; i < 40; i++) {
            System.out.println(i +":(" + sort.SortDistline.get(i) + " ," + sort.SortDistColum.get(i) +") :"
                    + acooo.distance[sort.SortDistline.get(i)][sort.SortDistColum.get(i)]);
        }
    }
}