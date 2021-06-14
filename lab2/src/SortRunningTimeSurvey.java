import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Random;

/**
 * @author Shilong Li (Lori)
 * @project ADALab
 * @filename SortRunningTimeSurvey
 * @date 2021/1/20 10:26
 */
// java2
public class SortRunningTimeSurvey {
    //                                  Task Name           Function Name      run times upper
    static String[][] taskList = {{"InsertionSortTest", "insertionSortTime", "100000"},
            {"BubbleSortTest", "bubbleSortTime", "100000"},
            {"SelectionSortTest", "selectionSortTime", "100000"},
            {"QuickSortTest", "quickSortTime", "100000"},
            {"MergeSortTest", "mergeSortTime", "100000"},
            {"HeapSortTest", "heapSortTime", "100000"}};

    static String[] dataList = {"AscendingSequence", "DescendingSequence", "RandomSequence", "OutOfOrderSequence"};
    static int[] data = new int[100000001];

    public static void main(String[] args) {
        String osName = System.getProperty("os.name");
        System.out.println(osName);
        try {
            File xlsFile = new File("RunningTimeSurvey.xls");
            // Create a workbook
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(xlsFile);
            Class<?> me = Class.forName(Thread.currentThread().getStackTrace()[1].getClassName());

            int maxExp = getMaxExp(taskList);

            for (int s = 0; s < dataList.length; s++) {
                // Create a worksheet
                WritableSheet sheet = workbook.createSheet("RunningTime_" + dataList[s], s);
                // the first row
                for (int j = 1, n = 1; j <= maxExp; j++) {
                    n = 10 * n;
                    sheet.addCell(new Label(j + 1, 0, "n = " + n));
                }
                for (int i = 0; i < taskList.length; i++) {
                    // col row data
                    sheet.addCell(new Label(0, i + 1, taskList[i][0]));
                    sheet.addCell(new Label(1, i + 1, taskList[i][1]));
                }
                for (int i = 1; i <= maxExp; i++) {
                    Method methodGenSeq = me.getMethod(dataList[s], int.class);
                    methodGenSeq.invoke(null, (int) Math.pow(10, i));
                    for (int j = 0; j < taskList.length; j++) {
                        String[] taskInfo = taskList[j];
                        Method method = me.getMethod(taskInfo[1], int.class);
                        int upper = Integer.parseInt(taskInfo[2]);
                        int n = (int) Math.pow(10, i);
                        if (n > upper) {
                            continue;
                        }

                        // run a sort program, get the run time
                        Long time = (Long) method.invoke(null, n);
                        // add the run time to the sheet
                        // col row data
                        sheet.addCell(new Label(i + 1, j + 1, time.toString()));
                    }

                }
            }
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getMaxExp(String[][] taskList) {
        int maxExp = 0;
        for (int i = 0; i < taskList.length; i++) {
            int temp = (int) Math.ceil(Math.log10(Integer.parseInt(taskList[i][2])));
            if (maxExp < temp) {
                maxExp = temp;
            }
        }
        return maxExp;
    }

    public static void AscendingSequence(int n) {
        for (int i = 0; i < n; i++) {
            data[i] = i;
        }
        System.out.println("AscendingSequence " + n + "elements");
    }

    public static void DescendingSequence(int n) {
        for (int i = 0; i < n; i++) {
            data[i] = n - i;
        }
        System.out.println("DescendingSequence " + n + "elements");

    }

    public static void RandomSequence(int n) {
        for (int i = 0; i < n; i++) {
            data[i] = i;
        }
        // shuffle
        Random rnd = new Random();
        for (int i = n; i > 1; i--) {
            int j = rnd.nextInt(i);
            int temp = data[j];
            data[j] = data[i - 1];
            data[i - 1] = temp;
        }
        System.out.println("RandomSequence " + n + " elements");

    }

    public static void OutOfOrderSequence(int n) {
        for (int i = 0; i < n; i++) {
            data[i] = i;
        }
        // shuffle
        Random rnd = new Random();
        for (int t = 0; t < n * 0.1; t++) {
            int i = rnd.nextInt(n);
            int j = rnd.nextInt(n);
            int temp = data[j];
            data[j] = data[i];
            data[i] = temp;
        }
        System.out.println("OutOfOrderSequence " + n + "elements");

    }

    public static long insertionSortTime(int n) {
        int[] list = new int[n];
        System.arraycopy(data, 0, list, 0, n);
        long timeStart = System.currentTimeMillis();
        insertionSort(n, list);
        long timeEnd = System.currentTimeMillis();
        long timeCost = timeEnd - timeStart;
        return timeCost;
    }

    public static void insertionSort(int n, int[] list) {
        for (int i = 0; i < n; i++) {
            for (int j = i; j >= 1; j--) {
                if (list[j - 1] > list[j]) {
                    int temp = list[j - 1];
                    list[j - 1] = list[j];
                    list[j] = temp;
                }
            }
        }
    }

    public static long bubbleSortTime(int n) {
        int[] list = new int[n];
        System.arraycopy(data, 0, list, 0, n);
        long timeStart = System.currentTimeMillis();
        bubbleSort(n, list);
        long timeEnd = System.currentTimeMillis();
        long timeCost = timeEnd - timeStart;
        return timeCost;
    }

    public static void bubbleSort(int n, int[] list) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list[j] > list[j + 1]) {
                    // swap
                    int tmp = list[j + 1];
                    list[j + 1] = list[j];
                    list[j] = tmp;
                }
            }
        }
    }

    public static long selectionSortTime(int n) {
        int[] list = new int[n];
        System.arraycopy(data, 0, list, 0, n);
        long timeStart = System.currentTimeMillis();
        selectionSort(n, list);
        long timeEnd = System.currentTimeMillis();
        long timeCost = timeEnd - timeStart;
        return timeCost;
    }

    public static void selectionSort(int n, int[] list) {
        // TODO :add your code here
        // reference: http://math.hws.edu/eck/cs124/javanotes8/c7/s4.html 7.4.4
        for (int i = 0; i < n - 1; i++) {
            int index = i;
            for (int j = i + 1; j < n; j++) {
                if (list[j] < list[index]) {
                    index = j;
                }
            }
            int temp = list[index];
            list[index] = list[i];
            list[i] = temp;
        }
    }

    public static long quickSortTime(int n) {
        int[] list = new int[n];
        System.arraycopy(data, 0, list, 0, n);
        long timeStart = System.currentTimeMillis();
        quickSort(list, 0, n - 1);
        long timeEnd = System.currentTimeMillis();
        return timeEnd - timeStart;
    }

//    public static void quickSort(int n, int[] list, int lo) {
////         TODO :add your code here
//        // Adam's ppt
//
//    }

    public static void quickSort(int[] arr, int lo, int hi) {
        if (lo < hi) {
            int p = partition(arr, lo, hi);
            quickSort(arr, lo, p - 1);
            quickSort(arr, p + 1, hi);
        }
    }

    public static int partition(int[] arr, int lo, int hi) {
        Random r = new Random();
        int[] arrC = new int[arr.length];
        int p = r.nextInt(hi - lo + 1) + lo;
        int pivot = arr[p];
        int L = lo;
        int R = hi;
        for (int i = lo; i <= hi; i++) {
            if (arr[i] < pivot) {
                arrC[L++] = arr[i];
            } else if (arr[i] >= pivot && i != p) {
                arrC[R--] = arr[i];
            }
        }
        arrC[R] = pivot;
        for (int i = lo; i <= hi; i++) {
            arr[i] = arrC[i];
        }
        return R;
    }


    public static long mergeSortTime(int n) {
        int[] list = new int[n];
        System.arraycopy(data, 0, list, 0, n);
        long timeStart = System.currentTimeMillis();
        mergeSort(list, 0, n - 1);
        long timeEnd = System.currentTimeMillis();
        long timeCost = timeEnd - timeStart;
        return timeCost;
    }

//    public static void mergeSort(int n, int[] list) {
//        // TODO :add your code here
//        // https://introcs.cs.princeton.edu/java/42sort/ Mergesort
//
//    }

    public static void mergeSort(int[] arr, int L, int R) {
        if (L < R) {
            int m = (L + R) / 2;
            mergeSort(arr, L, m);
            mergeSort(arr, m + 1, R);
            merge(arr, L, m, R);
        }
    }

    public static void merge(int[] arr, int L, int m, int R) {
        int[] temp = new int[R - L + 1];
        int i = 0;
        int p1 = L;
        int p2 = m + 1;

        while (p1 <= m && p2 <= R) {
            if (arr[p1] < arr[p2]) {
                temp[i++] = arr[p1++];
            } else {
                temp[i++] = arr[p2++];
            }
        }

        while (p1 <= m) {
            temp[i++] = arr[p1++];
        }
        while (p2 <= R) {
            temp[i++] = arr[p2++];
        }
        for (i = 0; i < R - L + 1; i++) {
            arr[L + i] = temp[i];
        }
    }


    public static long heapSortTime(int n) {
        int[] list = new int[n];
        System.arraycopy(data, 0, list, 0, n);
        long timeStart = System.currentTimeMillis();
        heapSort(n, list);
        long timeEnd = System.currentTimeMillis();
        long timeCost = timeEnd - timeStart;
        return timeCost;
    }

    public static void heapSort(int n, int[] list) {
        // TODO :add your code here
        // optional
        for (int i = (n - 1) / 2; i >= 0; i--) {
            int parent = i;
            int child = 2 * i + 1;
            while (child <= n - 1) {
                if (child + 1 <= n - 1 && list[child] > list[child + 1]) {
                    child++;
                }
                if (list[parent] > list[child]) {
                    int temp = list[parent];
                    list[parent] = list[child];
                    list[child] = temp;
//                    swap(list, parent, child);
                } else {
                    break;
                }
                parent = child;
                child = 2 * parent + 1;
            }
        }

    }

}
