import java.io.*;
import java.util.ArrayList;
import java.util.Random;
class FA {
    public static int items[],num,cap,sortedlist[];
    public FA(int n,int cp){
        items = new int[n];
        num = n;
        cap = cp;
        sortedlist = new int[n];
    }
    public int implementFirefly(){
        sortList();
        int max=100; int currBin,min = assignBins(items);
        int shuffle[] = new int[num];
        ArrayList<Integer> numbers = new ArrayList<>();
        Random rand = new Random();
        for(int i=0;i<max;i++){
            while(numbers.size()<num){
                int ran = rand.nextInt(num);
                if(!numbers.contains(ran))
                    numbers.add(ran);
            }
            for(int j=0;j<num;j++)
                shuffle[j] = items[numbers.get(j)];
            currBin = assignBins(shuffle);
            if(currBin<min)
                min = currBin;
            numbers.clear();
        }
        return min;
    }
    public int assignBins(int arr[]){
        int favor,bins=0,max_favor,max_favor_pos;
        int Bin[] = new int[1000]; boolean insert;
        for(int i=0;i<num;i++){
            insert = false;
            for(int k=0;k<=bins;k++) {
                if(sortedlist[i] <= (cap - Bin[k])) {
                    Bin[k]+= sortedlist[i];
                    insert = true;
                    break;
                }
            }
            if(!insert) {
                bins++;
                Bin[bins]+=sortedlist[i];
            }
            max_favor = 0;
            insert = false;
            max_favor_pos=i;
            for(int j=0;j<num;j++){
                if(arr[j]==0 || sortedlist[i]==arr[j])
                    continue;
                favor = attractiveness(sortedlist[i],arr[j]) - distance(sortedlist[i],j);
                if(favor>max_favor){
                    max_favor = favor;
                    max_favor_pos = j;
                }
            }
            if(max_favor_pos!=i) {
                for(int k=0;k<=bins;k++) {
                    if(arr[max_favor_pos] <= (cap - Bin[k])) {
                        Bin[k]+= arr[max_favor_pos];
                        insert = true;
                        break;
                    }
                }
                if(!insert) {
                    bins++;
                    Bin[bins]+=arr[max_favor_pos];
                }
            }
            arr[max_favor_pos]=0;
        }
        return bins+1;
    }
    public int distance(int a,int b){
        int i;
        for(i=0;i<num;i++)
            if(items[i]==a)
                break;
        return Math.abs(i-b);
    }
    public int attractiveness(int x, int y){
        return cap-x+y;
    }
    public void sortList(){
        int i,key,j;
        for (i=1; i<num; ++i){
            key = items[i];
            j = i-1;
            while (j>=0 && items[j] > key){
                items[j+1] = items[j];
                j = j-1;
            }
            items[j+1] = key;
        }
    }
    public static void main(String []args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the number of test cases : ");
        int tst = Integer.parseInt(in.readLine());int binsa;
        int bins[] = new int[tst];
        long timings[] = new long[tst];
        PrintWriter binfile = new PrintWriter(new BufferedWriter(new FileWriter("C:\\Users\\Super\\Desktop\\bins.txt",true)));
        PrintWriter timefile = new PrintWriter(new BufferedWriter(new FileWriter("C:\\Users\\Super\\Desktop\\timings.txt",true)));
        for(int k=0;k<tst;k++) {
            int n = Integer.parseInt(in.readLine());
            int cp = Integer.parseInt(in.readLine());
            FA obj = new FA(n,cp);
            for(int i=0;i<num;i++) {
                items[i] = Integer.parseInt(in.readLine());
                sortedlist[i] = items[i];
                if (items[i] > cap) {
                    System.out.println("WEIGHT_i > CAPACITY_bin ERROR");
                    System.exit(0);
                }
                if (items[i] == 0) {
                    System.out.println("ZERO WEIGHT ERROR");
                    System.exit(0);
                }
            }
            long startTime = System.nanoTime();
            binsa = obj.implementFirefly();
            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            binfile.println(binsa);
            timefile.println(totalTime/1000000);
            bins[k] = binsa;
            timings[k] = totalTime/1000000;
        }
        System.out.println("==========================================");
        System.out.println("Case\tBins\tTime");
        for(int i=0;i<tst;i++)
            System.out.println((i+1)+"\t\t"+bins[i]+"\t\t"+timings[i]);
        binfile.close();
        timefile.close();
    }
}